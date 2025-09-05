//題目:two sum
//給定一個整數陣列 nums 和一個目標值 target，請回傳兩個索引，使得 nums[i] + nums[j] == target。
package LeetCode;

import java.util.HashMap;
import java.util.Map;

public class lt_01_twosum {
    public int[] twoSum(int[] nums, int target) {
        //使用 HashMap 儲存數值與索引，加速查找
       Map<Integer, Integer> map = new HashMap<>();
       for (int i = 0; i < nums.length; i++) {
           int complement = target - nums[i];
           if (map.containsKey(complement)) {
               // 找到符合條件的組合，回傳索引
               return new int[] { map.get(complement), i };
           }
           map.put(nums[i], i);
       }
       return new int[] {}; // 沒找到則回傳空陣列
    }
}
/*
解題思路：
1. 題目要求找到兩個數字相加等於 target。
2. 使用 HashMap 儲存「數值 → 索引」，查找是否有另一個數值能與當前數字配對。
3. 時間複雜度 O(n)，只需一次迴圈即可完成。
*/