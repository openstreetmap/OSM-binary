package crosby.binary;

import crosby.binary.file.BlockInputStream;
import crosby.binary.file.ParallelBlockInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    /**
     * sample.pbf only has 4 blocks, so the default pipelineDepth (2 *
     * numThreads) never fills up mid-stream: every block ends up delivered
     * from the final drain loop after EOF, never from the backpressure
     * branch inside the read loop. Force a small pipelineDepth so that
     * branch -- and the resulting handleBlock/skipBlock interleaving -- is
     * actually exercised.
     */
    @Test
    public void testParallelWithBackpressure() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try (InputStream input = ReadFileTest.class.getResourceAsStream("/sample.pbf");
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter);
             ParallelBlockInputStream blockInput = new ParallelBlockInputStream(
                     input, new ReadFileTest.TestBinaryParser(printWriter), executor, 2)) {
            blockInput.process();
            Assert.assertEquals(ReadFileTest.EXPECTED, stringWriter.toString());
        } finally {
            executor.shutdownNow();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    /**
     * A block body truncated mid-stream must be treated as a clean end of
     * input -- same as BlockInputStream -- rather than aborting process()
     * before complete() is called and dropping whatever was already
     * in flight. Compares against BlockInputStream on the exact same bytes
     * to pin the expected behavior instead of hard-coding it.
     */
    @Test
    public void testTruncatedInputMatchesSequentialReader() throws Exception {
        byte[] full = readAll(ReadFileTest.class.getResourceAsStream("/sample.pbf"));
        byte[] truncated = new byte[full.length / 2];
        System.arraycopy(full, 0, truncated, 0, truncated.length);

        String sequential = runSequential(truncated);
        String parallel = runParallel(truncated);

        Assert.assertTrue("sequential reader should still complete", sequential.endsWith("Complete!" + System.lineSeparator()));
        Assert.assertEquals(sequential, parallel);
    }

    private static String runSequential(byte[] bytes) throws IOException {
        try (InputStream input = new ByteArrayInputStream(bytes);
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter)) {
            new BlockInputStream(input, new ReadFileTest.TestBinaryParser(printWriter)).process();
            return stringWriter.toString();
        }
    }

    private static String runParallel(byte[] bytes) throws IOException {
        try (InputStream input = new ByteArrayInputStream(bytes);
             StringWriter stringWriter = new StringWriter();
             PrintWriter printWriter = new PrintWriter(stringWriter);
             ParallelBlockInputStream blockInput = new ParallelBlockInputStream(
                     input, new ReadFileTest.TestBinaryParser(printWriter), 2)) {
            blockInput.process();
            return stringWriter.toString();
        }
    }

    private static byte[] readAll(InputStream in) throws IOException {
        try (InputStream input = in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int n;
            while ((n = input.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
            return out.toByteArray();
        }
    }
}
