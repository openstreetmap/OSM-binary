package crosby.binary;


import java.util.Date;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

import crosby.binary.Osmformat;
import crosby.binary.file.BlockReaderAdapter;
import crosby.binary.file.FileBlock;
import crosby.binary.file.FileBlockPosition;

public abstract class BinaryParser implements BlockReaderAdapter {
    private int granularity;
    private long lat_offset;
    private long lon_offset;
    private int date_granularity;
    private String strings[];

    protected Date getDate(Osmformat.Info info) {
      if (info.hasTimestamp()) {
          return new Date(date_granularity * (long) info.getTimestamp());
      } else
          return NODATE;
    }
    public static final Date NODATE = new Date();

    /** Get a string based on the index used. 
     * 
     * Index 0 is reserved to use as a delimiter, therefore, index 1 corresponds to the first string in the table 
     * @param id
     * @return
     */
    protected String getStringById(int id) {
      return strings[id];
    }
    
    @Override
    public void handleBlock(FileBlock message) {
        // TODO Auto-generated method stub
        try {
            if (message.getType().equals("OSMHeader")) {
                Osmformat.HeaderBlock headerblock = Osmformat.HeaderBlock
                        .parseFrom(message.getData());
                parse(headerblock);
            } else if (message.getType().equals("OSMData")) {
                Osmformat.PrimitiveBlock primblock = Osmformat.PrimitiveBlock
                        .parseFrom(message.getData());
                parse(primblock);
            }
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Error("ParseError"); // TODO
        }

    }


    @Override
    public boolean skipBlock(FileBlockPosition block) {
        // System.out.println("Seeing block of type: "+block.getType());
        if (block.getType().equals("OSMData"))
            return false;
        if (block.getType().equals("OSMHeader"))
            return false;
        System.out.println("Skipped block of type: " + block.getType());
        return true;
    }
    
    
    
    public double parseLat(long degree) {
      // Support non-zero offsets. (We don't currently generate them)
      return (granularity * degree + lat_offset) * .000000001;
    }

    public double parseLon(long degree) {
      // Support non-zero offsets. (We don't currently generate them)
       return (granularity * degree + lon_offset) * .000000001;
    }
   
    
    public void parse(Osmformat.PrimitiveBlock block) {
        Osmformat.StringTable stablemessage = block.getStringtable();
        strings = new String[stablemessage.getSCount()];

        for (int i = 0; i < strings.length; i++) {
            strings[i] = stablemessage.getS(i).toStringUtf8();
        }

        granularity = block.getGranularity();
        lat_offset = block.getLatOffset();
        lon_offset = block.getLonOffset();
        date_granularity = block.getDateGranularity();

        for (Osmformat.PrimitiveGroup groupmessage : block
                .getPrimitivegroupList()) {
            // Exactly one of these should trigger on each loop.
            parseNodes(groupmessage.getNodesList());
            parseWays(groupmessage.getWaysList());
            parseRelations(groupmessage.getRelationsList());
            if (groupmessage.hasDense())
                parseDense(groupmessage.getDense());
        }
    }
    
    protected abstract void parseRelations(List<Osmformat.Relation> rels);
    protected abstract void parseDense(Osmformat.DenseNodes nodes);
    protected abstract void parseNodes(List<Osmformat.Node> nodes);
    protected abstract void parseWays(List<Osmformat.Way> ways);
    protected abstract void parse(Osmformat.HeaderBlock header);

}