package p0814;
import java.util.Scanner;

public class TreeVisualizerExercise {
    private static final int ANIM_DELAY = 500;

    private static void clearConsole() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            for (int i = 0; i < 50; i++) System.out.println();
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private static void pause() {
        try {
            Thread.sleep(ANIM_DELAY);
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("選擇樹類型 (avl / rbt)：");
        String choice = scanner.nextLine().trim().toLowerCase();

        if ("avl".equals(choice)) {
            AvlTree tree = new AvlTree();
            System.out.println("AVL Tree 動畫示範，格式： i <key> 插入, d <key> 刪除, exit 離開");
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if ("exit".equals(line)) break;
                if (line.startsWith("i ")) {
                    int k = Integer.parseInt(line.substring(2));
                    tree.insert(k);
                } else if (line.startsWith("d ")) {
                    int k = Integer.parseInt(line.substring(2));
                    tree.delete(k);
                }
            }

        } else if ("rbt".equals(choice)) {
            RedBlackTree tree = new RedBlackTree();
            System.out.println("Red-Black Tree 動畫示範，格式： i <key> 插入, d <key> 刪除, exit 離開");
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if ("exit".equals(line)) break;
                if (line.startsWith("i ")) {
                    int k = Integer.parseInt(line.substring(2));
                    tree.insert(k);
                } else if (line.startsWith("d ")) {
                    int k = Integer.parseInt(line.substring(2));
                    tree.delete(k);
                }
            }

        } else {
            System.out.println("無效選項，請重啟並輸入 avl 或 rbt。");
        }
        scanner.close();
    }

    static class AvlTree {
        class Node {
            int key, height;
            Node left, right;
            Node(int k) {
                key = k;
                height = 1;
            }
        }

        private Node root;

        public void insert(int key) {
            root = insertRec(root, key);
            display();
        }

        private Node insertRec(Node node, int key) {
            if (node == null) return new Node(key);
            if (key < node.key) {
                node.left = insertRec(node.left, key);
            } else if (key > node.key) {
                node.right = insertRec(node.right, key);
            } else {
                return node;
            }
            updateHeight(node);
            int bf = balanceFactor(node);
            if (bf > 1 && key < node.left.key) {
                node = rotateRight(node);
            } else if (bf < -1 && key > node.right.key) {
                node = rotateLeft(node);
            } else if (bf > 1 && key > node.left.key) {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            } else if (bf < -1 && key < node.right.key) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
            return node;
        }

        public void delete(int key) {
            root = deleteRec(root, key);
            display();
        }

        private Node deleteRec(Node node, int key) {
            if (node == null) return null;
            if (key < node.key) {
                node.left = deleteRec(node.left, key);
            } else if (key > node.key) {
                node.right = deleteRec(node.right, key);
            } else {
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;
                Node succ = node.right;
                while (succ.left != null) succ = succ.left;
                node.key = succ.key;
                node.right = deleteRec(node.right, succ.key);
            }
            updateHeight(node);
            int bf = balanceFactor(node);
            if (bf > 1 && balanceFactor(node.left) >= 0) {
                node = rotateRight(node);
            } else if (bf > 1 && balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            } else if (bf < -1 && balanceFactor(node.right) <= 0) {
                node = rotateLeft(node);
            } else if (bf < -1 && balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
            return node;
        }

        private void updateHeight(Node n) {
            int lh = n.left == null ? 0 : n.left.height;
            int rh = n.right == null ? 0 : n.right.height;
            n.height = 1 + Math.max(lh, rh);
        }

        private int balanceFactor(Node n) {
            int lh = n.left == null ? 0 : n.left.height;
            int rh = n.right == null ? 0 : n.right.height;
            return lh - rh;
        }

        private Node rotateRight(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            updateHeight(y);
            updateHeight(x);
            display();
            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            updateHeight(x);
            updateHeight(y);
            display();
            return y;
        }

        private void display() {
            clearConsole();
            System.out.println("─── AVL Tree (key(bf)) ───");
            TreePrinter.printAvl(root, "", true);
            pause();
        }
    }

    static class RedBlackTree {
        enum Color { RED, BLACK }
        class Node {
            int key;
            Color color;
            Node left, right, parent;
            Node(int k, Color c, Node p) {
                key = k;
                color = c;
                parent = p;
            }
        }

        private Node root;

        public void insert(int key) {
            Node z = new Node(key, Color.RED, null);
            Node y = null;
            Node x = root;
            while (x != null) {
                y = x;
                x = key < x.key ? x.left : x.right;
            }
            z.parent = y;
            if (y == null) {
                root = z;
            } else if (key < y.key) {
                y.left = z;
            } else {
                y.right = z;
            }
            insertFixup(z);
            display();
        }

        private void insertFixup(Node z) {
            while (z.parent != null && z.parent.color == Color.RED) {
                Node p = z.parent;
                Node g = p.parent;
                if (g == null) break;
                if (p == g.left) {
                    Node u = g.right;
                    if (u != null && u.color == Color.RED) {
                        p.color = Color.BLACK;
                        u.color = Color.BLACK;
                        g.color = Color.RED;
                        z = g;
                        display();
                    } else {
                        if (z == p.right) {
                            z = p;
                            rotateLeft(z);
                        }
                        p.color = Color.BLACK;
                        g.color = Color.RED;
                        rotateRight(g);
                    }
                } else {
                    Node u = g.left;
                    if (u != null && u.color == Color.RED) {
                        p.color = Color.BLACK;
                        u.color = Color.BLACK;
                        g.color = Color.RED;
                        z = g;
                        display();
                    } else {
                        if (z == p.left) {
                            z = p;
                            rotateRight(z);
                        }
                        p.color = Color.BLACK;
                        g.color = Color.RED;
                        rotateLeft(g);
                    }
                }
            }
            root.color = Color.BLACK;
        }

        public void delete(int key) {
            Node node = root;
            Node parent = null;
            while (node != null && node.key != key) {
                parent = node;
                node = key < node.key ? node.left : node.right;
            }
            if (node == null) return;
            Node child = node.left != null ? node.left : node.right;
            if (parent == null) {
                root = child;
            } else if (node == parent.left) {
                parent.left = child;
            } else {
                parent.right = child;
            }
            if (child != null) child.parent = parent;
            display();
        }

        private void rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            if (y.left != null) y.left.parent = x;
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
            display();
        }

        private void rotateRight(Node x) {
            Node y = x.left;
            x.left = y.right;
            if (y.right != null) y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == null) {
                root = y;
            } else if (x == x.parent.right) {
                x.parent.right = y;
            } else {
                x.parent.left = y;
            }
            y.right = x;
            x.parent = y;
            display();
        }

        private void display() {
            clearConsole();
            System.out.println("─── Red-Black Tree (key[Color]) ───");
            TreePrinter.printRbt(root, "", true);
            pause();
        }
    }

    static class TreePrinter {
        static void printAvl(AvlTree.Node node, String prefix, boolean isTail) {
            if (node == null) return;
            String label = node.key + "(" + node.height + ")";
            System.out.println(prefix + (isTail ? "└── " : "├── ") + label);
            String childPrefix = prefix + (isTail ? "    " : "│   ");
            printAvl(node.left, childPrefix, false);
            printAvl(node.right, childPrefix, true);
        }

        static void printRbt(RedBlackTree.Node node, String prefix, boolean isTail) {
            if (node == null) return;
            String c = node.color == RedBlackTree.Color.RED ? "R" : "B";
            String label = node.key + "[" + c + "]";
            System.out.println(prefix + (isTail ? "└── " : "├── ") + label);
            String childPrefix = prefix + (isTail ? "    " : "│   ");
            printRbt(node.left, childPrefix, false);
            printRbt(node.right, childPrefix, true);
        }
    }
}
