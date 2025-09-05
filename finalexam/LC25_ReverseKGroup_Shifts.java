package finalexam;
import java.util.*;
public class LC25_ReverseKGroup_Shifts {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }

    //反轉[start, end)區間，end為不包含的節點
    private static ListNode reverse(ListNode start, ListNode end) {
        ListNode prev = end;
        ListNode cur = start;
        while (cur != end) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev; //新的區段頭
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (k <= 1 || head == null) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            //找到結尾
            ListNode kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) {
                kth = kth.next;
            }
            if (kth == null) break; //不足k個，結束

            ListNode groupNext = kth.next;
            //反轉
            ListNode newGroupHead = reverse(groupPrev.next, groupNext);
            ListNode newGroupTail = groupPrev.next;

            //接回去
            groupPrev.next = newGroupHead;
            newGroupTail.next = groupNext;

            //移動到下一組前
            groupPrev = newGroupTail;
        }
        return dummy.next;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int k = sc.nextInt();

        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (sc.hasNextInt()) {
            cur.next = new ListNode(sc.nextInt());
            cur = cur.next;
        }
        sc.close();

        ListNode newHead = reverseKGroup(dummy.next, k);

        cur = newHead;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" ");
            cur = cur.next;
        }
    }
}
