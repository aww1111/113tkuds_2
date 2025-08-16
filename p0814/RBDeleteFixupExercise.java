package p0814;

public class RBDeleteFixupExercise {

    enum Color { RED, BLACK }

    static class Node {
        int key;
        Color color;
        Node left, right, parent;
        Node(int key, Color color, Node parent) {
            this.key    = key;
            this.color  = color;
            this.parent = parent;
        }
    }

    private Node root;

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.right = x;
        x.parent = y;
    }

    public void insert(int key) {
        Node z = new Node(key, Color.RED, null);
        Node y = null, x = root;
        while (x != null) {
            y = x;
            x = (z.key < x.key) ? x.left : x.right;
        }
        z.parent = y;
        if (y == null) root = z;
        else if (z.key < y.key) y.left = z;
        else y.right = z;
        insertFixup(z);
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == Color.RED) {
            Node p = z.parent, g = p.parent;
            if (g == null) break;
            if (p == g.left) {
                Node u = g.right;
                if (u != null && u.color == Color.RED) {
                    p.color = u.color = Color.BLACK;
                    g.color = Color.RED;
                    z = g;
                } else {
                    if (z == p.right) {
                        z = p;
                        rotateLeft(z);
                        p = z.parent;
                        g = p.parent;
                    }
                    p.color = Color.BLACK;
                    g.color = Color.RED;
                    rotateRight(g);
                }
            } else {
                Node u = g.left;
                if (u != null && u.color == Color.RED) {
                    p.color = u.color = Color.BLACK;
                    g.color = Color.RED;
                    z = g;
                } else {
                    if (z == p.left) {
                        z = p;
                        rotateRight(z);
                        p = z.parent;
                        g = p.parent;
                    }
                    p.color = Color.BLACK;
                    g.color = Color.RED;
                    rotateLeft(g);
                }
            }
        }
        root.color = Color.BLACK;
    }

    private Node minimum(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        if (v != null) v.parent = u.parent;
    }

    public void delete(int key) {
        Node z = search(root, key);
        if (z == null) return;
        Node y = z;
        Color yOriginalColor = y.color;
        Node x;

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                if (x != null) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == Color.BLACK) {
            deleteFixup(x, z.parent);
        }
    }

    private void deleteFixup(Node x, Node parent) {
        while ((x != root) && (x == null || x.color == Color.BLACK)) {
            Node w;
            if (x == parent.left) {
                w = parent.right;
                if (w != null && w.color == Color.RED) {
                    w.color = Color.BLACK;
                    parent.color = Color.RED;
                    rotateLeft(parent);
                    w = parent.right;
                }
                if ((w.left == null || w.left.color == Color.BLACK) &&
                    (w.right == null || w.right.color == Color.BLACK)) {
                    if (w != null) w.color = Color.RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.right == null || w.right.color == Color.BLACK) {
                        if (w.left != null) w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rotateRight(w);
                        w = parent.right;
                    }
                    w.color = parent.color;
                    parent.color = Color.BLACK;
                    if (w.right != null) w.right.color = Color.BLACK;
                    rotateLeft(parent);
                    x = root;
                }
            } else {
                w = parent.left;

                if (w != null && w.color == Color.RED) {
                    w.color = Color.BLACK;
                    parent.color = Color.RED;
                    rotateRight(parent);
                    w = parent.left;
                }
                if ((w.left == null || w.left.color == Color.BLACK) &&
                    (w.right == null || w.right.color == Color.BLACK)) {
                    if (w != null) w.color = Color.RED;
                    x = parent;
                    parent = x.parent;
                } else {
                    if (w.left == null || w.left.color == Color.BLACK) {
                        if (w.right != null) w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        rotateLeft(w);
                        w = parent.left;
                    }
                    w.color = parent.color;
                    parent.color = Color.BLACK;
                    if (w.left != null) w.left.color = Color.BLACK;
                    rotateRight(parent);
                    x = root;
                }
            }
        }
        if (x != null) x.color = Color.BLACK;
    }

    private Node search(Node x, int key) {
        while (x != null && key != x.key) {
            x = (key < x.key) ? x.left : x.right;
        }
        return x;
    }

    public void inorderPrint() {
        inorder(root);
        System.out.println();
    }
    private void inorder(Node n) {
        if (n == null) return;
        inorder(n.left);
        System.out.print("(" + n.key + "," + n.color + ") ");
        inorder(n.right);
    }

    public static void main(String[] args) {
        RBDeleteFixupExercise tree = new RBDeleteFixupExercise();
        int[] toInsert = {20, 10, 30, 5, 15, 25, 35, 3, 7};
        for (int k : toInsert) tree.insert(k);

        System.out.print("初始: ");
        tree.inorderPrint();

        int[] toDelete = {5, 30, 20};
        for (int k : toDelete) {
            tree.delete(k);
            System.out.print("刪除 " + k + ": ");
            tree.inorderPrint();
        }
    }
}