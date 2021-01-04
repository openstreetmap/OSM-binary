// SPDX-License-Identifier: LGPL-3.0-or-later
package crosby.binary.file;

import java.io.IOException;

public class FileFormatException extends IOException {

  public FileFormatException(String string) {
    super(string);
  }

  public FileFormatException(Throwable cause) {
    super(cause);
  }

  /**
   * 
   */
  private static final long serialVersionUID = -8128010128748910923L;

}
