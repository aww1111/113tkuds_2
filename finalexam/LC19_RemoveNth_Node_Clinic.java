package finalexam;
import java.util.*;
public class LC19_RemoveNth_Node_Clinic {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        //fast走n+1步，slow停在待刪前一節點
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        //fast到尾時，slow剛好在待刪前一節點
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        //刪除節點
        slow.next = slow.next.next;

        return dummy.next;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int len = sc.nextInt();
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int i = 0; i < len; i++) {
            cur.next = new ListNode(sc.nextInt());
            cur = cur.next;
        }
        int k = sc.nextInt();
        ListNode newHead = removeNthFromEnd(dummy.next, k);
        //輸出刪除後序列
        cur = newHead;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" ");
            cur = cur.next;
        }
        sc.close();
    }
}
