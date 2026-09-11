package crosby.binary;

import crosby.binary.file.ParallelBlockInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Verifies that ParallelBlockInputStream still delivers blocks to the adaptor
 * one at a time, in file order, producing output identical to the sequential
 * BlockInputStream reader (see ReadFileTest).
 */
public class ParallelBlockInputStreamTest {

    @Test
    public void testParallel() throws Exception {
        try (InputStream input = ReadFileTest.class.getResourceAsStream("/sample.pbf");
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter);
             ParallelBlockInputStream blockInput = new ParallelBlockInputStream(
                     input, new ReadFileTest.TestBinaryParser(printWriter), 4)) {
            blockInput.process();
            Assert.assertEquals(ReadFileTest.EXPECTED, stringWriter.toString());
        }
    }
}
