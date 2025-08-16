package p0814;

import java.util.ArrayList;
import java.util.List;

public class HybridDataStructureExercise {
    private long readCount  = 0;
    private long writeCount = 0;

    private static final double SWITCH_RATIO = 2.0;

    private SearchTree avlTree = new AvlTree();
    private SearchTree rbTree  = new RedBlackTree();

    private SearchTree activeTree = avlTree;

    public void insert(int key) {
        writeCount++;
        avlTree.insert(key);
        rbTree.insert(key);

        rebalanceIfNeeded();
    }

    public void delete(int key) {
        writeCount++;
        avlTree.delete(key);
        rbTree.delete(key);

        rebalanceIfNeeded();
    }
    public boolean search(int key) {
        readCount++;
        rebalanceIfNeeded();
        return activeTree.search(key);
    }

    public void printInOrder() {
        List<Integer> keys = activeTree.inorderKeys();
        System.out.println(keys);
    }

    private void rebalanceIfNeeded() {
        double ratio = (readCount == 0) ? Double.POSITIVE_INFINITY
                        : (double) writeCount / readCount;

        if (ratio > SWITCH_RATIO && activeTree != rbTree) {
            switchTo(rbTree);
        } else if (ratio <= SWITCH_RATIO && activeTree != avlTree) {
            switchTo(avlTree);
        }
    }

    private void switchTo(SearchTree target) {
        System.out.println("Switching active tree to: "
                           + (target instanceof AvlTree ? "AVLTree" : "RedBlackTree"));

        List<Integer> keys = activeTree.inorderKeys();

        target.clear();
        for (int k : keys) {
            target.insert(k);
        }

        activeTree = target;

        readCount  = 0;
        writeCount = 0;
    }
    interface SearchTree {
        void insert(int key);
        void delete(int key);
        boolean search(int key);
        List<Integer> inorderKeys();
        void clear();
    }
    static class AvlTree implements SearchTree {
        private class Node {
            int key, height;
            Node left, right;
            Node(int k) { key = k; height = 1; }
        }

        private Node root;

        @Override
        public void insert(int key) { root = insertRec(root, key); }

        private Node insertRec(Node node, int key) {
            if (node == null) return new Node(key);
            if (key < node.key) node.left  = insertRec(node.left, key);
            else if (key > node.key) node.right = insertRec(node.right, key);
            else return node;

            updateHeight(node);
            return rebalance(node);
        }

        @Override
        public void delete(int key) { root = deleteRec(root, key); }

        private Node deleteRec(Node node, int key) {
            if (node == null) return null;
            if (key < node.key) node.left  = deleteRec(node.left, key);
            else if (key > node.key) node.right = deleteRec(node.right, key);
            else {
                if (node.left == null)  return node.right;
                if (node.right == null) return node.left;
                Node successor = min(node.right);
                node.key = successor.key;
                node.right = deleteRec(node.right, successor.key);
            }
            updateHeight(node);
            return rebalance(node);
        }

        @Override
        public boolean search(int key) {
            Node cur = root;
            while (cur != null) {
                if (key == cur.key) return true;
                cur = (key < cur.key ? cur.left : cur.right);
            }
            return false;
        }

        @Override
        public List<Integer> inorderKeys() {
            List<Integer> list = new ArrayList<>();
            inorderRec(root, list);
            return list;
        }

        private void inorderRec(Node node, List<Integer> out) {
            if (node == null) return;
            inorderRec(node.left, out);
            out.add(node.key);
            inorderRec(node.right, out);
        }

        @Override
        public void clear() { root = null; }

        private void updateHeight(Node n) {
            int lh = (n.left  == null ? 0 : n.left.height);
            int rh = (n.right == null ? 0 : n.right.height);
            n.height = 1 + Math.max(lh, rh);
        }

        private int balanceFactor(Node n) {
            int lh = (n.left  == null ? 0 : n.left.height);
            int rh = (n.right == null ? 0 : n.right.height);
            return lh - rh;
        }

        private Node rebalance(Node n) {
            int bf = balanceFactor(n);
            if (bf > 1) {
                if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
                return rotateRight(n);
            }
            if (bf < -1) {
                if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
                return rotateLeft(n);
            }
            return n;
        }

        private Node rotateRight(Node y) {
            Node x = y.left, T2 = x.right;
            x.right = y; y.left = T2;
            updateHeight(y); updateHeight(x);
            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right, T2 = y.left;
            y.left = x; x.right = T2;
            updateHeight(x); updateHeight(y);
            return y;
        }

        private Node min(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }
    }
    static class RedBlackTree implements SearchTree {
        enum Color { RED, BLACK }
        class Node {
            int key; Color color;
            Node left, right, parent;
            Node(int k, Color c, Node p) { key = k; color = c; parent = p; }
        }

        private Node root;

        @Override
        public void insert(int key) {

        }

        @Override
        public void delete(int key) {
        }

        @Override
        public boolean search(int key) {
            Node cur = root;
            while (cur != null) {
                if (key == cur.key) return true;
                cur = (key < cur.key ? cur.left : cur.right);
            }
            return false;
        }

        @Override
        public List<Integer> inorderKeys() {
            List<Integer> out = new ArrayList<>();
            inorderRec(root, out);
            return out;
        }

        private void inorderRec(Node n, List<Integer> out) {
            if (n == null) return;
            inorderRec(n.left, out);
            out.add(n.key);
            inorderRec(n.right, out);
        }

        @Override
        public void clear() { root = null; }
    }


    public static void main(String[] args) {
        HybridDataStructureExercise hybrid = new HybridDataStructureExercise();

        for (int i = 1; i <= 50; i++) {
            if (i % 5 == 0) {
                hybrid.delete(i - 1);
            } else {
                hybrid.insert(i);
            }
            if (i % 10 == 0) {
                for (int j = 1; j < i; j += 7) {
                    hybrid.search(j);
                }
            }
        }
        System.out.print("最終活躍樹中序：");
        hybrid.printInOrder();
    }
}
