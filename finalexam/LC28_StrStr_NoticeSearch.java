package finalexam;
import java.util.*;
public class LC28_StrStr_NoticeSearch {
    public static int strStr(String haystack, String needle) {
        if (needle.length() == 0) return 0;
        if (needle.length() > haystack.length()) return -1;

        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
            int j = 0;
            while (j < needle.length() && haystack.charAt(i + j) == needle.charAt(j)) {
                j++;
            }
            if (j == needle.length()) return i;
        }
        return -1;
    }
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String haystack = sc.nextLine();
            String needle = sc.nextLine();
            System.out.println(strStr(haystack, needle));
        }
    }
}
