package finalexam;
import java.util.*;
public class LC34_SearchRange_DelaySpan {
    private static int lowerBound(int[] nums, int target) {
        int l = 0, r = nums.length; // [l, r)
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }
    public static int[] searchRange(int[] nums, int target) {
        int start = lowerBound(nums, target);
        if (start == nums.length || nums[start] != target) {
            return new int[]{-1, -1};
        }
        int end = lowerBound(nums, target + 1) - 1;
        return new int[]{start, end};
    }
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            int target = sc.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = sc.nextInt();
            }
            int[] ans = searchRange(nums, target);
            System.out.println(ans[0] + " " + ans[1]);
        }
    }

}
