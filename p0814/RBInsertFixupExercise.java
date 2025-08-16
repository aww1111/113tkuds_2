package p0814;

public class RBInsertFixupExercise {

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

    public void insert(int key) {
        Node z = new Node(key, Color.RED, null);
        root = bstInsert(root, z);
        insertFixup(z);
    }

    private Node bstInsert(Node root, Node z) {
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (z.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        z.parent = y;
        if (y == null) {
            root = z;
        } else if (z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }
        return root;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == Color.RED) {
            Node p = z.parent;
            Node g = p.parent;
            if (g == null) break;

            if (p == g.left) {
                Node uncle = g.right;
                if (uncle != null && uncle.color == Color.RED) {
                    p.color = Color.BLACK;
                    uncle.color = Color.BLACK;
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
                Node uncle = g.left;
                if (uncle != null && uncle.color == Color.RED) {
                    p.color = Color.BLACK;
                    uncle.color = Color.BLACK;
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
        if (root != null) {
            root.color = Color.BLACK;
        }
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.left) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }
        x.right = y;
        y.parent = x;
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
        RBInsertFixupExercise tree = new RBInsertFixupExercise();
        int[] seq = {10, 20, 30, 15, 25, 5, 1};
        for (int key : seq) {
            tree.insert(key);
            System.out.print("Insert " + key + ": ");
            tree.inorderPrint();
        }
    }
}