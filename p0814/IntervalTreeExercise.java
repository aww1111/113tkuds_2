package p0814;

import java.util.ArrayList;
import java.util.List;

public class IntervalTreeExercise {

    enum Color { RED, BLACK }
    private static class Node {
        int low, high;
        int max;
        Color color;
        Node left, right, parent;

        Node(int low, int high, Color color, Node parent) {
            this.low    = low;
            this.high   = high;
            this.max     = high;
            this.color  = color;
            this.parent = parent;
        }
    }

    private Node root;

    private int max(Node n) {
        return n == null ? Integer.MIN_VALUE : n.max;
    }

    private void updateMax(Node n) {
        if (n != null) {
            n.max = Math.max(n.high, Math.max(max(n.left), max(n.right)));
        }
    }

    private Node sibling(Node n) {
        if (n.parent == null) return null;
        return (n == n.parent.left) ? n.parent.right : n.parent.left;
    }

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

        updateMax(x);
        updateMax(y);
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;

        updateMax(y);
        updateMax(x);
    }

    private void fixupMaxUpwards(Node n) {
        while (n != null) {
            int old = n.max;
            updateMax(n);
            if (n.max == old) break;
            n = n.parent;
        }
    }
    public void insert(int low, int high) {
        Node z = new Node(low, high, Color.RED, null);
        Node p = null, x = root;
        while (x != null) {
            p = x;
            if (z.low < x.low) x = x.left;
            else x = x.right;
        }
        z.parent = p;
        if (p == null) {
            root = z;
        } else if (z.low < p.low) {
            p.left = z;
        } else {
            p.right = z;
        }

        fixupMaxUpwards(p);
        insertFixup(z);
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == Color.RED) {
            Node p = z.parent;
            Node g = p.parent;
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

    public void delete(int low, int high) {
        Node z = searchNode(root, low, high);
        if (z == null) return;

        Node y = z;
        Color yColor = y.color;
        Node x;

        if (z.left == null) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yColor = y.color;
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
            updateMax(y);
        }

        fixupMaxUpwards((x != null) ? x.parent : y.parent);

        if (yColor == Color.BLACK) {
            deleteFixup(x, (x != null) ? x.parent : y.parent);
        }
    }

    private void deleteFixup(Node x, Node parent) {
        while ((x != root) && (x == null || x.color == Color.BLACK)) {
            Node w = (x == parent.left) ? parent.right : parent.left;
            if (w != null && w.color == Color.RED) {
                w.color = Color.BLACK;
                parent.color = Color.RED;
                if (x == parent.left) rotateLeft(parent);
                else rotateRight(parent);
                w = (x == parent.left) ? parent.right : parent.left;
            }

            if ((w.left == null || w.left.color == Color.BLACK) &&
                (w.right == null || w.right.color == Color.BLACK)) {
                w.color = Color.RED;
                x = parent;
                parent = x.parent;
            } else {
                if (x == parent.left) {
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

    private Node searchNode(Node n, int low, int high) {
        while (n != null) {
            if (low < n.low)                n = n.left;
            else if (low > n.low)           n = n.right;
            else if (high != n.high)        return null;
            else                            return n;
        }
        return null;
    }

    public List<int[]> queryOverlap(int low, int high) {
        List<int[]> res = new ArrayList<>();
        queryOverlap(root, low, high, res);
        return res;
    }

    private void queryOverlap(Node n, int low, int high, List<int[]> res) {
        if (n == null) return;

        if (n.left != null && n.left.max >= low) {
            queryOverlap(n.left, low, high, res);
        }

        if (n.low <= high && n.high >= low) {
            res.add(new int[]{n.low, n.high});
        }

        if (n.low <= high) {
            queryOverlap(n.right, low, high, res);
        }
    }

    public List<int[]> queryPoint(int x) {
        List<int[]> res = new ArrayList<>();
        queryPoint(root, x, res);
        return res;
    }

    private void queryPoint(Node n, int x, List<int[]> res) {
        if (n == null) return;

        if (n.left != null && n.left.max >= x) {
            queryPoint(n.left, x, res);
        }

        if (n.low <= x && n.high >= x) {
            res.add(new int[]{n.low, n.high});
        }

        if (n.low <= x) {
            queryPoint(n.right, x, res);
        }
    }
    public static void main(String[] args) {
        IntervalTreeExercise tree = new IntervalTreeExercise();

        tree.insert(15, 20);
        tree.insert(10, 30);
        tree.insert(17, 19);
        tree.insert(5, 20);
        tree.insert(12, 15);
        tree.insert(30, 40);

        System.out.println("重疊 [14,16] 的區間：");
        for (int[] iv : tree.queryOverlap(14, 16)) {
            System.out.printf(" [%d,%d]", iv[0], iv[1]);
        }
        System.out.println();

        System.out.println("包含點 14 的區間：");
        for (int[] iv : tree.queryPoint(14)) {
            System.out.printf(" [%d,%d]", iv[0], iv[1]);
        }
        System.out.println();

        tree.delete(10, 30);
        System.out.println("刪除 [10,30] 後，重疊 [14,16]：");
        for (int[] iv : tree.queryOverlap(14, 16)) {
            System.out.printf(" [%d,%d]", iv[0], iv[1]);
        }
        System.out.println();
    }
}