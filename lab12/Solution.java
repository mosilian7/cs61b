public class Solution {
    public static void main(String[] args) {
        int[] nums1 = new int[]{1};
        int[] nums2 = new int[]{};
        Solution s = new Solution();
        System.out.println(s.findMedianSortedArrays(nums1, nums2));
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int odd = (m + n) % 2;
        int half = (m + n) / 2;
        if (m == 0) {
            if (odd == 0) {
                return ((double) (nums2[half]) + (double) (nums2[half - 1]))/2;
            } else {
                return nums2[half];
            }
        }
        if (n == 0) {
            if (odd == 0) {
                return ((double) (nums1[half]) + (double) (nums1[half - 1]))/2;
            } else {
                return nums1[half];
            }
        }

        int left = 0;
        int right = m;
        int pointer1 = m/2;
        int pointer2 = half - pointer1;
        double minPart;
        double maxPart;
        while (true) {
            if ((pointer1 == 0 || pointer2 == n || nums1[pointer1 - 1] <= nums2[pointer2])
                    && (pointer2 == 0 || pointer1 == m || nums2[pointer2 - 1] <= nums1[pointer1])) {

                if (pointer2 == n) {
                    minPart = nums1[pointer1];
                } else if (pointer1 == m) {
                    minPart = nums2[pointer2];
                } else {
                    minPart = min(nums1[pointer1], nums2[pointer2]);
                }

                if (pointer1 == 0) {
                    maxPart = nums2[pointer2 - 1];
                } else if (pointer2 == 0) {
                    maxPart = nums1[pointer1 - 1];
                } else {
                    maxPart = max(nums1[pointer1 - 1], nums2[pointer2 - 1]);
                }

                if (odd == 0) {
                    return (maxPart + minPart) / 2;
                } else {
                    return minPart;
                }
            }

            System.out.println(pointer2);
            if ((pointer1 != 0 && pointer2 != n && nums1[pointer1 - 1] > nums2[pointer2])) {
                right = pointer1;
                left = (int) max(left, half - n);
                pointer1 = (left + right) / 2;
                pointer2 = half - pointer1;
                continue;
            }

            if ((pointer2 != 0 && pointer1 != m && nums2[pointer2 - 1] > nums1[pointer1])) {
                left = pointer1;
                right = (int) min(right, half);
                pointer1 = (left + right + 1) / 2;
                pointer2 = half - pointer1;
                continue;
            }
        }


    }

    private double max(int a, int b) {
        if (a < b) {
            return b;
        }
        return a;
    }

    private double min(int a, int b) {
        if (a > b) {
            return b;
        }
        return a;
    }
}
