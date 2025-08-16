package p0814;

import java.util.ArrayList;
import java.util.List;

public class AVLRangeQueryExercise {
    private static class Node {
        int key, height;
        Node left, right;
        Node(int key) { this.key = key; this.height = 1; }
    }

    private Node root;

    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
    if (node == null) {
        return new Node(key);
    }
    if (key < node.key) {
        node.left = insert(node.left, key);
    } else if (key > node.key) {
        node.right = insert(node.right, key);
    } else {
        return node;
    }

    updateHeight(node);

    int bf = balanceFactor(node);
    // LL
    if (bf > 1 && key < node.left.key) {
        return rotateRight(node);
    }
    // RR
    if (bf < -1 && key > node.right.key) {
        return rotateLeft(node);
    }
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

private void updateHeight(Node n) {
    n.height = 1 + Math.max(height(n.left), height(n.right));
}

private int height(Node n) {
    return (n == null) ? 0 : n.height;
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

    public List<Integer> rangeQuery(int min, int max) {
        List<Integer> result = new ArrayList<>();
        rangeQuery(root, min, max, result);
        return result;
    }

    private void rangeQuery(Node node, int min, int max, List<Integer> result) {
        if (node == null) {
            return;
        }
        
        if (node.key > min) {
            rangeQuery(node.left, min, max, result);
        }

        if (node.key >= min && node.key <= max) {
            result.add(node.key);
        }

        if (node.key < max) {
            rangeQuery(node.right, min, max, result);
        }
    }

    public static void main(String[] args) {
        AVLRangeQueryExercise tree = new AVLRangeQueryExercise();
        int[] values = {20, 10, 30, 5, 15, 25, 35};
        for (int v : values) {
            tree.insert(v);
        }

        List<Integer> sub = tree.rangeQuery(10, 25);
        System.out.println("範圍 [10,25] 內的節點：" + sub);
    }
}

