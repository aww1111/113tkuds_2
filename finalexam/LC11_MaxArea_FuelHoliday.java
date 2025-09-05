package finalexam;
import java.util.*;
public class LC11_MaxArea_FuelHoliday {
    public static long maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        long max = 0;
        while (left < right) {
            long h = Math.min(height[left], height[right]);
            long width = right - left;
            max = Math.max(max, h * width);
            //移動較短邊
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return max;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] heights = new int[n];
        for (int i = 0; i < n; i++) {
            heights[i] = sc.nextInt();
        }
        System.out.println(maxArea(heights));
        sc.close();
    }
}
