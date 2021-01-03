package crosby.binary;

import crosby.binary.*;
import crosby.binary.Osmformat.*;
import crosby.binary.file.*;
import java.io.*;
import java.util.List;

/**
 * Demonstrates how to read a file. Reads sample.pbf from the resources folder
 * and prints details about it to the standard output.
 *
 * @author Michael Tandy
 */
public class ReadFileExample {

    public static void main(String[] args) throws Exception {
        InputStream input = ReadFileExample.class.getResourceAsStream("/sample.pbf");
        BlockReaderAdapter brad = new TestBinaryParser();
        new BlockInputStream(input, brad).process();
    }

    private static class TestBinaryParser extends BinaryParser {

        @Override
        protected void parseRelations(List<Relation> rels) {
            if (!rels.isEmpty())
                System.out.println("Got some relations to parse.");
            Relation r = null;
        }

        @Override
        protected void parseDense(DenseNodes nodes) {
            long lastId=0;
            long lastLat=0;
            long lastLon=0;

            for (int i=0 ; i<nodes.getIdCount() ; i++) {
                lastId += nodes.getId(i);
                lastLat += nodes.getLat(i);
                lastLon += nodes.getLon(i);
                System.out.printf("Dense node, ID %d @ %.6f,%.6f\n",
                        lastId,parseLat(lastLat),parseLon(lastLon));
            }
        }

        @Override
        protected void parseNodes(List<Node> nodes) {
            for (Node n : nodes) {
                System.out.printf("Regular node, ID %d @ %.6f,%.6f\n",
                        n.getId(),parseLat(n.getLat()),parseLon(n.getLon()));
            }
        }

        @Override
        protected void parseWays(List<Way> ways) {
            for (Way w : ways) {
                System.out.println("Way ID " + w.getId());
                StringBuilder sb = new StringBuilder();
                sb.append("  Nodes: ");
                long lastRef = 0;
                for (Long ref : w.getRefsList()) {
                    lastRef+= ref;
                    sb.append(lastRef).append(" ");
                }
                sb.append("\n  Key=value pairs: ");
                for (int i=0 ; i<w.getKeysCount() ; i++) {
                    sb.append(getStringById(w.getKeys(i))).append("=")
                            .append(getStringById(w.getVals(i))).append(" ");
                }
                System.out.println(sb.toString());
            }
        }

        @Override
        protected void parse(HeaderBlock header) {
            System.out.println("Got header block.");
        }

        public void complete() {
            System.out.println("Complete!");
        }

    }

}
