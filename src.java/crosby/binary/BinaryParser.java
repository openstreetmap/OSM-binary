/** Copyright (c) 2010 Scott A. Crosby. <scott@sacrosby.com>
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as 
   published by the Free Software Foundation, either version 3 of the 
   License, or (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package crosby.binary;

import java.util.Date;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlock;
import crosby.binary.file.FileBlockPosition;
import crosby.binary.file.FileFormatException;

/**
 * Abstract base class for parsing OpenStreetMap binary (PBF) data blocks.
 * Handles decoding of OSMHeader and OSMData blocks and delegates the
 * parsing of entities (nodes, ways, relations) to subclasses.
 */
public abstract class BinaryParser implements BlockReaderAdapter {

    protected int granularity;
    private long lat_offset;
    private long lon_offset;
    protected int date_granularity;
    private String[] strings;

    /** Constant representing an undefined date. */
    public static final Date NODATE = new Date(-1);

    /**
     * Convert an OSM Info protobuf timestamp to a Java {@link Date} object.
     *
     * @param info the OSM Info message containing timestamp
     * @return a Date object, or NODATE if no timestamp is available
     */
    protected Date getDate(Osmformat.Info info) {
        if (info.hasTimestamp()) {
            return new Date(date_granularity * info.getTimestamp());
        } else {
            return NODATE;
        }
    }

    /**
     * Retrieve a string from the string table using its ID.
     * Index 0 is reserved as a delimiter and should not be used.
     *
     * @param id index in the string table
     * @return the corresponding string, or null if out of range
     */
    protected String getStringById(int id) {
        if (id < 0 || id >= strings.length) {
            return null;
        }
        return strings[id];
    }

    /**
     * Handles a single block from the PBF file and delegates it to the appropriate parser.
     *
     * @param message the FileBlock message containing type and data
     */
    @Override
    public void handleBlock(FileBlock message) {
        try {
            if ("OSMHeader".equals(message.getType())) {
                Osmformat.HeaderBlock headerBlock =
                        Osmformat.HeaderBlock.parseFrom(message.getData());
                parse(headerBlock);
            } else if ("OSMData".equals(message.getType())) {
                Osmformat.PrimitiveBlock primBlock =
                        Osmformat.PrimitiveBlock.parseFrom(message.getData());
                parse(primBlock);
            }
        } catch (InvalidProtocolBufferException e) {
            throw new FileFormatException(e);
        }
    }

    /**
     * Determines whether to skip reading a block based on its type.
     *
     * @param block the block position and metadata
     * @return true if the block should be skipped
     */
    @Override
    public boolean skipBlock(FileBlockPosition block) {
        if ("OSMData".equals(block.getType())) return false;
        if ("OSMHeader".equals(block.getType())) return false;

        System.out.println("Skipped block of type: " + block.getType());
        return true;
    }

    /**
     * Convert a latitude value stored in the protobuf into a double
     * compensating for granularity and latitude offset.
     *
     * @param degree the stored latitude value
     * @return latitude in degrees
     */
    public double parseLat(long degree) {
        return (granularity * degree + lat_offset) * 1e-9;
    }

    /**
     * Convert a longitude value stored in the protobuf into a double
     * compensating for granularity and longitude offset.
     *
     * @param degree the stored longitude value
     * @return longitude in degrees
     */
    public double parseLon(long degree) {
        return (granularity * degree + lon_offset) * 1e-9;
    }

    /**
     * Parse a PrimitiveBlock (contains string table, metadata, and PrimitiveGroups)
     * and dispatch each group to the appropriate parser method.
     *
     * @param block the OSM PrimitiveBlock
     */
    public void parse(Osmformat.PrimitiveBlock block) {
        Osmformat.StringTable stringTable = block.getStringtable();
        strings = new String[stringTable.getSCount()];

        for (int i = 0; i < strings.length; i++) {
            strings[i] = stringTable.getS(i).toStringUtf8();
        }

        granularity = block.getGranularity();
        lat_offset = block.getLatOffset();
        lon_offset = block.getLonOffset();
        date_granularity = block.getDateGranularity();

        for (Osmformat.PrimitiveGroup group : block.getPrimitivegroupList()) {
            // Exactly one of these should trigger on each loop.
            parseNodes(group.getNodesList());
            parseWays(group.getWaysList());
            parseRelations(group.getRelationsList());
            if (group.hasDense()) {
                parseDense(group.getDense());
            }
        }
    }

    /** Parse a list of Relation protobufs and send the resulting relations to a sink. */
    protected abstract void parseRelations(List<Osmformat.Relation> rels);

    /** Parse a DenseNodes protobuf and send the resulting nodes to a sink. */
    protected abstract void parseDense(Osmformat.DenseNodes nodes);

    /** Parse a list of Node protobufs and send the resulting nodes to a sink. */
    protected abstract void parseNodes(List<Osmformat.Node> nodes);

    /** Parse a list of Way protobufs and send the resulting ways to a sink. */
    protected abstract void parseWays(List<Osmformat.Way> ways);

    /** Parse a HeaderBlock protobuf. */
    protected abstract void parse(Osmformat.HeaderBlock header);
}
