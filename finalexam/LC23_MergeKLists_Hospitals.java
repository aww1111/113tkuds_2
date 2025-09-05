package finalexam;
import java.util.*;
public class LC23_MergeKLists_Hospitals {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int v) { val = v; }
    }
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));

        //每條非空串列的頭節點放入堆
        for (ListNode node : lists) {
            if (node != null) pq.offer(node);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        while (!pq.isEmpty()) {
            ListNode minNode = pq.poll();
            tail.next = minNode;
            tail = tail.next;
            if (minNode.next != null) {
                pq.offer(minNode.next);
            }
        }

        return dummy.next;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        ListNode[] lists = new ListNode[k];

        for (int i = 0; i < k; i++) {
            ListNode dummy = new ListNode(0);
            ListNode cur = dummy;
            while (true) {
                int val = sc.nextInt();
                if (val == -1) break;
                cur.next = new ListNode(val);
                cur = cur.next;
            }
            lists[i] = dummy.next;
        }
        ListNode merged = mergeKLists(lists);

        //輸出合併後序列
        ListNode cur = merged;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) System.out.print(" ");
            cur = cur.next;
        }
        sc.close();
    }
}
