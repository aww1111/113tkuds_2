public class AVLBasicExercise {
    // ====== 內部節點結構 ======
    private static class Node {
        int key;
        int height; // 以此節點為根的高度（葉子為 1）
        Node left, right;
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    // ====== 簡化版 AVL 樹 ======
    private Node root;

    // --- 對外 API ---

    // 插入節點（忽略重複鍵）
    public void insert(int key) {
        root = insert(root, key);
    }

    // 搜尋節點（存在回傳 true）
    public boolean search(int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    // 計算樹的高度（空樹為 0），使用遞迴
    public int height() {
        return computeHeight(root);
    }

    // 檢查是否為有效 AVL（同時滿足 BST 與平衡因子在 [-1, 1]）
    public boolean isValidAVL() {
        return validateAVL(root, Integer.MIN_VALUE, Integer.MAX_VALUE).ok;
    }

    private Node insert(Node node, int key) {
        // 1) 標準 BST 插入
        if (node == null) return new Node(key);
        if (key < node.key) node.left = insert(node.left, key);
        else if (key > node.key) node.right = insert(node.right, key);
        else return node; // 忽略重複

        // 2) 更新高度
        updateHeight(node);

        // 3) 平衡調整（根據平衡因子與插入方向選擇旋轉）
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

        return node; // 無需旋轉
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

    // ====== 內部輔助：遞迴高度 & 驗證 ======

    // 遞迴計算高度（空樹為 0）
    private int computeHeight(Node n) {
        if (n == null) return 0;
        int lh = computeHeight(n.left);
        int rh = computeHeight(n.right);
        return 1 + Math.max(lh, rh);
    }

    // 驗證：同時檢查 BST 範圍與 AVL 平衡，並用遞迴返回高度
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

        // BST 範圍約束（嚴格不允許重複）
        if (n.key <= min || n.key >= max) return new CheckRes(false, 0);

        CheckRes L = validateAVL(n.left, min, n.key);
        if (!L.ok) return new CheckRes(false, 0);

        CheckRes R = validateAVL(n.right, n.key, max);
        if (!R.ok) return new CheckRes(false, 0);

        // 檢查平衡因子在 [-1, 1]
        int bf = L.height - R.height;
        if (bf < -1 || bf > 1) return new CheckRes(false, 0);

        int h = 1 + Math.max(L.height, R.height);
        return new CheckRes(true, h);
    }

    // ====== Demo 主程式 ======
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
