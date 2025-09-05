package LeetCode;
import java.util.HashMap;
import java.util.Map;
public class LC03_NoRepeat_TaipeiMetroTap {
    public int lengthOfLongestSubstring(String s) {
        //Map
        Map<Character, Integer>lastIndexMap = new HashMap<>();
        int maxLen =0;
        int left=0;

        for(int right=0; right<s.length(); right++){
            char c = s.charAt(right);
        
        //若重複，左指針移到該字元原位置的下一格
        if(lastIndexMap.containsKey(c)){
            left=Math.max(left, lastIndexMap.get(c)+1);
        }
        //更新字元最後位置
        lastIndexMap.put(c, right);
    }
        //更新最大長度
        return maxLen;
    }
}
