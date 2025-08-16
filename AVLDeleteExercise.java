public class AVLDeleteExercise {
    // ====== 節點結構 ======
    private static class Node {
        int key, height;
        Node left, right;
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    private Node root;

    // ====== 對外 API ======

    // 插入（為了建樹測試）
    public void insert(int key) {
        root = insert(root, key);
    }

    // 刪除
    public void delete(int key) {
        root = delete(root, key);
    }

    // 搜尋
    public boolean search(int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    // 列印整棵樹（側向）
    public void printTree() {
        if (root == null) {
            System.out.println("(空)");
        } else {
            printSideways(root, "", true);
        }
    }

    // ====== 內部輔助：BST 插入 + AVL 平衡 ======

    private Node insert(Node node, int key) {
        if (node == null) return new Node(key);
        if (key < node.key) node.left = insert(node.left, key);
        else if (key > node.key) node.right = insert(node.right, key);
        else return node; // 忽略重複

        updateHeight(node);
        return rebalance(node, key);
    }

    // 刪除節點並回傳新的子樹根
    private Node delete(Node node, int key) {
        if (node == null) return null;

        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            // 找到要刪除的節點
            if (node.left == null && node.right == null) {
                // 葉子
                return null;
            } else if (node.left == null) {
                // 只有右子
                return node.right;
            } else if (node.right == null) {
                // 只有左子
                return node.left;
            } else {
                // 兩個子節點：用後繼 (min in right subtree) 替代
                Node succ = minValueNode(node.right);
                node.key = succ.key;
                node.right = delete(node.right, succ.key);
            }
        }

        // 回溯更新高度並平衡
        updateHeight(node);
        return rebalance(node, key);
    }

    // 找到子樹中最小值節點（後繼）
    private Node minValueNode(Node node) {
        Node cur = node;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    // 根據當前節點的平衡因子與操作方向做適當旋轉
    private Node rebalance(Node node, int key) {
        int bf = balanceFactor(node);

        // LL
        if (bf > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        // LR
        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // RR
        if (bf < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        // RL
        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // ====== 旋轉 & 平衡輔助 ======

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
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

    // ====== 側向列印 ======

    private void printSideways(Node node, String prefix, boolean isTail) {
        if (node.right != null) {
            printSideways(node.right,
                prefix + (isTail ? "│   " : "    "),
                false);
        }
        System.out.println(prefix
            + (isTail ? "└── " : "┌── ")
            + node.key + " (h=" + node.height + ")");
        if (node.left != null) {
            printSideways(node.left,
                prefix + (isTail ? "    " : "│   "),
                true);
        }
    }

    // ====== Demo 主程式 ======

    public static void main(String[] args) {
        AVLDeleteExercise tree = new AVLDeleteExercise();
        int[] vals = {20, 10, 30, 5, 15, 35};

        // 建立測試樹
        for (int v : vals) {
            tree.insert(v);
        }
        System.out.println("初始樹：");
        tree.printTree();

        // 刪除葉子節點
        System.out.println("\n刪除葉子節點 35：");
        tree.delete(35);
        tree.printTree();

        // 刪除只有一個子節點的節點
        System.out.println("\n刪除只有一個子節點的 30：");
        tree.delete(30);
        tree.printTree();

        // 刪除有兩個子節點的節點
        System.out.println("\n刪除有兩個子節點的 10：");
        tree.delete(10);
        tree.printTree();
    }
}
