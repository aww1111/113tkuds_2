package finalexam;
import java.util.HashMap;
import java.util.Map;
public class LC01_TwoSum_THSRHoliday {
    public int[] twoSum(int[] nums, int target) {
        //HashMap<座位，班次>
       Map<Integer, Integer> map = new HashMap<>();

       for (int i = 0; i < nums.length; i++) {
           int complement = target - nums[i];
           if (map.containsKey(complement)) {
               // 找到符合條件的組合，回傳索引
               return new int[] { map.get(complement), i };
           }
           map.put(nums[i], i);
       }
       return new int[] {-1,-1};
    }
}
