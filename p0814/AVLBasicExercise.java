package p0814;
public class AVLBasicExercise {

    private static class Node {
        int key;
        int height;
        Node left, right;
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    public void insert(int key) {
        root = insert(root, key);
    }

    public boolean search(int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    public int height() {
        return computeHeight(root);
    }

    public boolean isValidAVL() {
        return validateAVL(root, Integer.MIN_VALUE, Integer.MAX_VALUE).ok;
    }

    private Node insert(Node node, int key) {
        // BST 
        if (node == null) return new Node(key);
        if (key < node.key) node.left = insert(node.left, key);
        else if (key > node.key) node.right = insert(node.right, key);
        else return node;

        updateHeight(node);

        int bf = balanceFactor(node);

        // LL
        if (bf > 1 && key < node.left.key) return rotateRight(node);

        // RR
        if (bf < -1 && key > node.right.key) return rotateLeft(node);

        // LR
        if (bf > 1 && key > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // RL
        if (bf < -1 && key < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private int computeHeight(Node n) {
        if (n == null) return 0;
        int lh = computeHeight(n.left);
        int rh = computeHeight(n.right);
        return 1 + Math.max(lh, rh);
    }

    private static class CheckRes {
        boolean ok;
        int height;
        CheckRes(boolean ok, int height) {
            this.ok = ok;
            this.height = height;
        }
    }

    private CheckRes validateAVL(Node n, int min, int max) {
        if (n == null) return new CheckRes(true, 0);

        if (n.key <= min || n.key >= max) return new CheckRes(false, 0);

        CheckRes L = validateAVL(n.left, min, n.key);
        if (!L.ok) return new CheckRes(false, 0);

        CheckRes R = validateAVL(n.right, n.key, max);
        if (!R.ok) return new CheckRes(false, 0);

        int bf = L.height - R.height;
        if (bf < -1 || bf > 1) return new CheckRes(false, 0);

        int h = 1 + Math.max(L.height, R.height);
        return new CheckRes(true, h);
    }

    public static void main(String[] args) {
        AVLBasicExercise tree = new AVLBasicExercise();

        int[] values = {10, 20, 30, 40, 50, 25};
        for (int v : values) tree.insert(v);

        System.out.println("搜尋 25: " + tree.search(25));
        System.out.println("搜尋 35: " + tree.search(35));
        System.out.println("樹高度: " + tree.height());
        System.out.println("是有效 AVL？ " + tree.isValidAVL());
    }
}
