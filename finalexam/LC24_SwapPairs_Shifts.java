package finalexam;
import java.util.*;
public class LC24_SwapPairs_Shifts {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }
    public static ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;

        while (prev.next != null && prev.next.next != null) {
            ListNode a = prev.next;
            ListNode b = a.next;

            //交換ab
            prev.next = b;
            a.next = b.next;
            b.next = a;

            //prev移到下一對前
            prev = a;
        }
        return dummy.next;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //讀取一行整數直到EOF
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (sc.hasNextInt()) {
            cur.next = new ListNode(sc.nextInt());
            cur = cur.next;
        }
        ListNode newHead = swapPairs(dummy.next);
        //輸出交換後序列
        cur = newHead;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" ");
            cur = cur.next;
        }
        sc.close();
    }
}
