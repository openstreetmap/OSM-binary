// SPDX-License-Identifier: LGPL-3.0-or-later
package crosby.binary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.google.protobuf.ByteString;

/**
 * Class for mapping a set of strings to integers, giving frequently occurring
 * strings small integers.
 */
public class StringTable {
    public StringTable() {
        clear();
    }

    private HashMap<String, Integer> counts;
    private HashMap<String, Integer> stringmap;
    private String[] set;

    /**
     * Increments the count of the given string
     * @param s the string
     */
    public void incr(String s) {
        counts.merge(s, 1, Integer::sum);
    }

    /** After the stringtable has been built, return the offset of a string in it.
     *
     * Note, value '0' is reserved for use as a delimiter and will not be returned.
     * @param s the string to lookup
     * @return the offset of the string
     */
    public int getIndex(String s) {
        return stringmap.get(s);
    }

    public void finish() {
        Comparator<String> comparator = (s1, s2) -> counts.get(s2) - counts.get(s1);

        /* Sort the stringtable */

        /*
        When a string is referenced, strings in the stringtable with indices:
               0                : Is reserved (used as a delimiter in tags
         A:  1 to 127          : Uses can be represented with 1 byte
         B: 128 to 128**2-1 : Uses can be represented with 2 bytes,
         C: 128*128  to X    : Uses can be represented with 3 bytes in the unlikely case we have >16k strings in a block. No block will contain enough strings that we'll need 4 bytes.

        There are goals that will improve compression:
          1. I want to use 1 bytes for the most frequently occurring strings, then 2 bytes, then 3 bytes.
          2. I want to use low integers as frequently as possible (for better
             entropy encoding out of deflate)
          3. I want the stringtable to compress as small as possible.

        Condition 1 is obvious. Condition 2 makes deflate compress stringtable references more effectively.
        When compressing entities, delta coding causes small positive integers to occur more frequently
        than larger integers. Even though a stringtable references to indices of 1 and 127 both use one
        byte in a decompressed file, the small integer bias causes deflate to use fewer bits to represent
        the smaller index when compressed. Condition 3 is most effective when adjacent strings in the
        stringtable have a lot of common substrings.

        So, when I decide on the master stringtable to use, I put the 127 most frequently occurring
        strings into A (accomplishing goal 1), and sort them by frequency (to accomplish goal 2), but
        for B and C, which contain the less progressively less frequently encountered strings, I sort
        them lexicographically, to maximize goal 3 and ignoring goal 2.

        Goal 1 is the most important. Goal 2 helped enough to be worth it, and goal 3 was pretty minor,
        but all should be re-benchmarked.


        */



        set = counts.keySet().toArray(new String[0]);
        if (set.length > 0) {
          // Sort based on the frequency.
          Arrays.sort(set, comparator);
          // Each group of keys that serializes to the same number of bytes is
          // sorted lexicographically.
          // to maximize deflate compression.

          // Don't sort the first array. There's not likely to be much benefit, and we want frequent values to be small.
          //Arrays.sort(set, Math.min(0, set.length-1), Math.min(1 << 7, set.length-1));

          Arrays.sort(set, Math.min(1 << 7, set.length-1), Math.min(1 << 14,
              set.length-1));
          Arrays.sort(set, Math.min(1 << 14, set.length-1), Math.min(1 << 21,
              set.length-1), comparator);
        }
        stringmap = new HashMap<>(2 * set.length);
        for (int i = 0; i < set.length; i++) {
            stringmap.put(set[i], i + 1); // Index 0 is reserved for use as a delimiter.
        }
        counts = null;
    }

    public void clear() {
        counts = new HashMap<>(100);
        stringmap = null;
        set = null;
    }

    public Osmformat.StringTable.Builder serialize() {
        Osmformat.StringTable.Builder builder = Osmformat.StringTable
                .newBuilder();
        builder.addS(ByteString.copyFromUtf8("")); // Add a unused string at offset 0 which is used as a delimiter.
        for (String s : set) {
            builder.addS(ByteString.copyFromUtf8(s));
        }
        return builder;
    }
}
