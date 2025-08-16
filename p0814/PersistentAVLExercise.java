package p0814;

import java.util.ArrayList;
import java.util.List;

public class PersistentAVLExercise {
    private static class Node {
        final int key, height;
        final Node left, right;
        Node(int key, Node left, Node right) {
            this.key    = key;
            this.left   = left;
            this.right  = right;
            this.height = 1 + Math.max(height(left), height(right));
        }
        private static int height(Node n) {
            return n == null ? 0 : n.height;
        }
    }

    private static Node insert(Node node, int key) {
        if (node == null) return new Node(key, null, null);
        if (key < node.key) {
            Node newLeft = insert(node.left, key);
            return balance(new Node(node.key, newLeft, node.right));
        } else if (key > node.key) {
            Node newRight = insert(node.right, key);
            return balance(new Node(node.key, node.left, newRight));
        } else {
            return node;
        }
    }

    private static Node balance(Node n) {
        int bf = Node.height(n.left) - Node.height(n.right);
        if (bf > 1 && Node.height(n.left.left) >= Node.height(n.left.right)) {
            return rotateRight(n);
        }
        if (bf > 1) {
            Node leftRotated = rotateLeft(n.left);
            return rotateRight(new Node(n.key, leftRotated, n.right));
        }
        if (bf < -1 && Node.height(n.right.right) >= Node.height(n.right.left)) {
            return rotateLeft(n);
        }
        if (bf < -1) {
            Node rightRotated = rotateRight(n.right);
            return rotateLeft(new Node(n.key, n.left, rightRotated));
        }
        return n;
    }

    private static Node rotateLeft(Node x) {
        Node y = x.right;
        Node newLeft = new Node(x.key, x.left, y.left);
        return new Node(y.key, newLeft, y.right);
    }

    private static Node rotateRight(Node y) {
        Node x = y.left;
        Node newRight = new Node(y.key, x.right, y.right);
        return new Node(x.key, x.left, newRight);
    }

    private final List<Node> versions = new ArrayList<>();

    public PersistentAVLExercise() {
        versions.add(null);
    }

    public int insert(int v, int key) {
        Node base = versions.get(v);
        Node next = insert(base, key);
        versions.add(next);
        return versions.size() - 1;
    }

    public boolean search(int v, int key) {
        Node cur = versions.get(v);
        while (cur != null) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    public static void main(String[] args) {
        PersistentAVLExercise tree = new PersistentAVLExercise();

        int v1 = tree.insert(0, 50);
        int v2 = tree.insert(v1, 30);
        int v3 = tree.insert(v2, 70);
        int v4 = tree.insert(v3, 40);

        System.out.println(tree.search(v2, 70));
        System.out.println(tree.search(v3, 70));
        System.out.println(tree.search(v4, 30));
    }
}