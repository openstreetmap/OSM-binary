package crosby.binary.file;

import com.google.protobuf.ByteString;

/**
 * Base class that contains the metadata about a fileblock.
 * 
 * Subclasses of this include additional fields, such as byte offsets that let a
 * fileblock be read in a random-access fashion, or the data itself.
 * 
 * @author crosby
 * 
 */
public class FileBlockBase {

    protected FileBlockBase(String type, ByteString indexdata) {
        this.type = type;
        this.indexdata = indexdata;
    }

    /** Identifies the type of the data within a block */
    protected final String type;
    /**
     * Block metadata, stored in the index block and as a prefix for every
     * block.
     */
    protected final ByteString indexdata;

    public String getType() {
        return type;
    }

    public ByteString getIndexData() {
        return indexdata;
    }
}
