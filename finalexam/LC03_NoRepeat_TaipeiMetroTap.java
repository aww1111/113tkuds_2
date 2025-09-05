package finalexam;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class LC03_NoRepeat_TaipeiMetroTap {
    public static int lengthOfLongestSubstring(String s) {
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
        maxLen = Math.max(maxLen, right - left + 1);
    }
        return maxLen;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().trim();
        System.out.println(lengthOfLongestSubstring(s));
        sc.close();
    }
}
