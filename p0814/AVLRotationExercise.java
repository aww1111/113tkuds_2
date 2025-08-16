package p0814;

public class AVLRotationExercise {
    static class Node {
        int key;
        int height;
        Node left, right;
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

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

    static Node rotateLeft(Node x) {
        if (x == null || x.right == null) return x;
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    static Node rotateRight(Node y) {
        if (y == null || y.left == null) return y;
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    static Node rotateLeftRight(Node node) {
        if (node == null) return null;
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }
    static Node rotateRightLeft(Node node) {
        if (node == null) return null;
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

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
