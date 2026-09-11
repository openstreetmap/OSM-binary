/** Copyright (c) 2026 Leonard Ehrenfried. <mail@leonard.io>

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

package crosby.binary.file;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * A drop-in, parallel replacement for {@link BlockInputStream}.
 *
 * Reading a fileblock's header and raw bytes must stay strictly sequential,
 * since it consumes an {@link InputStream}, but decompressing and parsing a
 * block's contents (the CPU-heavy step, dominated by zlib inflate for large
 * files) is independent per block. This class overlaps that work across a
 * thread pool while still delivering completed blocks to the adaptor one at
 * a time, in file order -- exactly the contract {@link BlockReaderAdapter}
 * documents -- so an existing {@link BlockReaderAdapter} (such as a
 * {@code crosby.binary.BinaryParser} subclass) needs no changes to benefit:
 * its {@code handleBlock}/{@code skipBlock}/{@code complete} calls all still
 * happen on a single thread, one at a time, in order.
 *
 * Memory use is bounded by keeping at most {@code pipelineDepth} decompressed
 * blocks in flight at once (queued or in progress); tune it down for memory
 * constrained environments, or up to keep more threads fed on files with many
 * small blocks.
 */
public class ParallelBlockInputStream implements Closeable {
    public ParallelBlockInputStream(InputStream input, BlockReaderAdapter adaptor) {
        this(input, adaptor, Runtime.getRuntime().availableProcessors());
    }

    public ParallelBlockInputStream(InputStream input, BlockReaderAdapter adaptor, int numThreads) {
        this(input, adaptor, Executors.newFixedThreadPool(numThreads), true, numThreads * 2);
    }

    /**
     * Use a caller-supplied executor, e.g. to share a thread pool across
     * several files. The executor is not shut down by {@link #close()}.
     */
    public ParallelBlockInputStream(InputStream input, BlockReaderAdapter adaptor,
            ExecutorService executor, int pipelineDepth) {
        this(input, adaptor, executor, false, pipelineDepth);
    }

    private ParallelBlockInputStream(InputStream input, BlockReaderAdapter adaptor,
            ExecutorService executor, boolean ownsExecutor, int pipelineDepth) {
        this.input = input;
        this.adaptor = adaptor;
        this.executor = executor;
        this.ownsExecutor = ownsExecutor;
        this.pipelineDepth = Math.max(1, pipelineDepth);
    }

    public void process() throws IOException {
        Queue<Future<FileBlock>> inflight = new ArrayDeque<>();
        try {
            while (true) {
                FileBlockHead head;
                try {
                    head = FileBlockHead.readHead(input);
                } catch (EOFException e) {
                    break;
                }
                if (adaptor.skipBlock(head)) {
                    head.skipContents(input);
                    continue;
                }

                byte[] buf = new byte[head.getDatasize()];
                new DataInputStream(input).readFully(buf);
                inflight.add(submit(head, buf));

                if (inflight.size() >= pipelineDepth) {
                    adaptor.handleBlock(take(inflight));
                }
            }
            while (!inflight.isEmpty()) {
                adaptor.handleBlock(take(inflight));
            }
            adaptor.complete();
        } finally {
            if (ownsExecutor) {
                executor.shutdown();
            }
        }
    }

    private Future<FileBlock> submit(final FileBlockHead head, final byte[] buf) {
        return executor.submit(new Callable<FileBlock>() {
            @Override
            public FileBlock call() throws IOException {
                return head.parseData(buf);
            }
        });
    }

    private static FileBlock take(Queue<Future<FileBlock>> inflight) throws IOException {
        Future<FileBlock> future = inflight.poll();
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IOException(cause);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            input.close();
        } finally {
            if (ownsExecutor) {
                executor.shutdownNow();
            }
        }
    }

    private final InputStream input;
    private final BlockReaderAdapter adaptor;
    private final ExecutorService executor;
    private final boolean ownsExecutor;
    private final int pipelineDepth;
}
