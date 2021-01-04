// SPDX-License-Identifier: LGPL-3.0-or-later
package crosby.binary.file;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class BlockInputStream implements Closeable {
    // TODO: Should be seekable input stream!
    public BlockInputStream(InputStream input, BlockReaderAdapter adaptor) {
        this.input = input;
        this.adaptor = adaptor;
    }

    public void process() throws IOException {
      try {
        while (true) {
          FileBlock.process(input, adaptor);
        }
      } catch (EOFException e) {
        adaptor.complete();
      }
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    InputStream input;
    BlockReaderAdapter adaptor;
}
