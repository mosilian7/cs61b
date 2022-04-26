package hw3.hash;

import java.util.List;
import java.util.HashMap;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        HashMap<Integer,Integer> buckets = new HashMap<>();
        int N = oomages.size();

        for (int i=0;i<M;i+=1) {
            buckets.put(i,0);
        }

        for (Oomage o:oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets.put(bucketNum, buckets.get(bucketNum)+1);
        }
        for (int i=0;i<M;i+=1) {
            int bucketSize = buckets.get(i);
            if (bucketSize < N/50 || bucketSize > N/2.5) {
                return false;
            }
        }
        return true;
    }
}
