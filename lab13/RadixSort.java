/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLen = 0;
        for (String s: asciis) {
            maxLen = maxLen > s.length() ? maxLen : s.length();
        }

        String[] out = asciis;

        for (int i = maxLen - 1; i >= 0; i -= 1) {
            out = sortHelperLSD(out, i);
        }
        return out;
    }

    private static String[] padding(String[] inputs, int maxLen) {
        String[] out = new String[inputs.length];
        for (int i = 0; i < inputs.length; i += 1) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < maxLen - inputs[i].length(); j += 1) {
                sb.append((char) 0);
            }
            out[i] = inputs[i] + sb.toString();
        }
        return out;
    }

    private static String unpadString(String padded, int maxLen) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <  maxLen; i += 1) {
            char c = padded.charAt(i);
            if ((int)  c == 0) {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void unpadding(String[] padded, int maxLen) {
        for (int i = 0; i < padded.length; i += 1) {
            padded[i] = unpadString(padded[i], maxLen);
        }
    }
    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // gather all the counts for each value
        int[] counts = new int[256];
        for (String s : asciis) {
            int i = index >= s.length() ? 0 : s.charAt(index);
            counts[i]++;
        }
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];

        for (int i = 0; i < asciis.length; i += 1) {
            int item = index >= asciis[i].length() ? 0 : asciis[i].charAt(index);
            int place = starts[item];
            sorted[place] = asciis[i];
            starts[item] += 1;
        }

        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] s = new String[]{"Alice", "Akko", "Diana", "Honoka", "Umi", "Kotori", "Rin", "Kayo"};
        String[] sorted = sort(s);
        for (String num: sorted) {
            System.out.println(num);
        }
    }
}
