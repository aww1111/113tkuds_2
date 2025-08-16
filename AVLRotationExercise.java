
public class AVLRotationExercise {
    static class Node {
        int key;
        int height;      // 以此節點為根的高度（葉子節點高度為 1）
        Node left, right;
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    // ====== 工具方法 ======
    static int height(Node n) {
        return n == null ? 0 : n.height;
    }

    static void updateHeight(Node n) {
        if (n != null) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }
    }

    static int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // ====== 單旋 ======
    // 左旋：x 為根，將其右子 y 提升為新根
    static Node rotateLeft(Node x) {
        if (x == null || x.right == null) return x;
        Node y = x.right;
        Node t2 = y.left;

        // 執行旋轉
        y.left = x;
        x.right = t2;

        // 更新高度：先下沉節點 x，再新根 y
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    // 右旋：y 為根，將其左子 x 提升為新根
    static Node rotateRight(Node y) {
        if (y == null || y.left == null) return y;
        Node x = y.left;
        Node t2 = x.right;

        // 執行旋轉
        x.right = y;
        y.left = t2;

        // 更新高度：先下沉節點 y，再新根 x
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    // ====== 雙旋 ======
    // 左右旋（修正 LR）：先對左子做左旋，再對根做右旋
    static Node rotateLeftRight(Node node) {
        if (node == null) return null;
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    // 右左旋（修正 RL）：先對右子做右旋，再對根做左旋
    static Node rotateRightLeft(Node node) {
        if (node == null) return null;
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    // ====== 驗證與列印 ======
    // 側向列印整棵樹
    static void printSideways(Node node) {
        printSideways(node, "", true);
    }

    static void printSideways(Node node, String prefix, boolean isTail) {
        if (node == null) {
            System.out.println(prefix + "(空)");
            return;
        }
        if (node.right != null) {
            printSideways(node.right, prefix + (isTail ? "│   " : "    "), false);
        }
        System.out.println(prefix + (isTail ? "└── " : "┌── ")
                           + node.key + " (h=" + node.height + ")");
        if (node.left != null) {
            printSideways(node.left, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    // 驗證是否為有效 AVL 樹（BST 性質 + 平衡因子範圍 + 高度一致性）
    static boolean isValidAVL(Node n, int min, int max) {
        if (n == null) return true;
        if (n.key <= min || n.key >= max) return false;
        if (!isValidAVL(n.left, min, n.key)) return false;
        if (!isValidAVL(n.right, n.key, max)) return false;
        int bf = balanceFactor(n);
        if (bf < -1 || bf > 1) return false;
        int expected = 1 + Math.max(height(n.left), height(n.right));
        return n.height == expected;
    }

    // ====== 測試資料構造 ======
    // 範例樹直接連成鏈，測試各種不平衡形態
    static Node buildRR() {
        Node a = new Node(10);
        a.right = new Node(20);
        a.right.right = new Node(30);
        updateHeight(a.right);
        updateHeight(a);
        return a;
    }

    static Node buildLL() {
        Node a = new Node(30);
        a.left = new Node(20);
        a.left.left = new Node(10);
        updateHeight(a.left);
        updateHeight(a);
        return a;
    }

    static Node buildLR() {
        Node a = new Node(30);
        a.left = new Node(10);
        a.left.right = new Node(20);
        updateHeight(a.left);
        updateHeight(a);
        return a;
    }

    static Node buildRL() {
        Node a = new Node(10);
        a.right = new Node(30);
        a.right.left = new Node(20);
        updateHeight(a.right);
        updateHeight(a);
        return a;
    }

    // ====== 主程式：執行測試 ======
    public static void main(String[] args) {
        System.out.println("=== 空樹與單節點邊界測試 ===");
        Node empty = null;
        empty = rotateLeft(empty);
        empty = rotateRight(empty);
        Node single = new Node(42);
        updateHeight(single);
        rotateLeft(single);
        rotateRight(single);
        System.out.println("空與單節點旋轉無錯誤\n");

        System.out.println("=== RR （10→20→30） 左旋 ===");
        Node rr = buildRR();
        printSideways(rr);
        rr = rotateLeft(rr);
        printSideways(rr);
        System.out.println("有效 AVL? " + isValidAVL(rr, Integer.MIN_VALUE, Integer.MAX_VALUE) + "\n");

        System.out.println("=== LL （30←20←10） 右旋 ===");
        Node ll = buildLL();
        printSideways(ll);
        ll = rotateRight(ll);
        printSideways(ll);
        System.out.println("有效 AVL? " + isValidAVL(ll, Integer.MIN_VALUE, Integer.MAX_VALUE) + "\n");

        System.out.println("=== LR （30 ← 10 → 20） 左右旋 ===");
        Node lr = buildLR();
        printSideways(lr);
        lr = rotateLeftRight(lr);
        printSideways(lr);
        System.out.println("有效 AVL? " + isValidAVL(lr, Integer.MIN_VALUE, Integer.MAX_VALUE) + "\n");

        System.out.println("=== RL （10 → 30 ← 20） 右左旋 ===");
        Node rl = buildRL();
        printSideways(rl);
        rl = rotateRightLeft(rl);
        printSideways(rl);
        System.out.println("有效 AVL? " + isValidAVL(rl, Integer.MIN_VALUE, Integer.MAX_VALUE) + "\n");

        System.out.println("所有旋轉測試完成");
    }
}
