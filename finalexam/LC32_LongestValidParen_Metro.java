package finalexam;
import java.util.*;
public class LC32_LongestValidParen_Metro {
    public static int longestValidParentheses(String s) {
        int maxLen = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); //棧底基準

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else { // ')'
                stack.pop();
                if (stack.isEmpty()) {
                    //重置基準
                    stack.push(i);
                } else {
                    //當前合法長度
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().trim();
        System.out.println(longestValidParentheses(s));
        sc.close();
    }
}
