package finalexam;
import java.util.*;
public class LC20_ValidParentheses_AlertFormat {
    public static boolean isValid(String s) {
        // 若長度為奇數的情況
        if (s.length() % 2 == 1) return false;

        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (map.containsValue(c)) {
                //開括號
                stack.push(c);
            } else if (map.containsKey(c)) {
                //閉括號
                if (stack.isEmpty() || stack.peek() != map.get(c)) {
                    return false;
                }
                stack.pop();
            } else {
                //非法字元）
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().trim();
        System.out.println(isValid(s));
        sc.close();
    }
}
