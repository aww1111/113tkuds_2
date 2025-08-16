package p0814;
import java.util.*;

public class AVLLeaderboardSystem {
    private static class Node {
        int playerId, score, height, size;
        Node left, right;
        Node(int id, int score) {
            this.playerId = id;
            this.score    = score;
            this.height   = 1;
            this.size     = 1;
        }
    }

    private Node root;
    private Map<Integer,Integer> map = new HashMap<>();

    private int height(Node n) { return n == null ? 0 : n.height; }
    private int size(Node n)   { return n == null ? 0 : n.size; }
    private void update(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
        n.size   = 1 + size(n.left) + size(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left, T2 = x.right;
        x.right = y; y.left = T2;
        update(y); update(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right, T2 = y.left;
        y.left = x; x.right = T2;
        update(x); update(y);
        return y;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node balance(Node node) {
        int bf = balanceFactor(node);
        if (bf > 1 && balanceFactor(node.left) >= 0)     return rotateRight(node);
        if (bf > 1) { node.left = rotateLeft(node.left);  return rotateRight(node); }
        if (bf < -1 && balanceFactor(node.right) <= 0)   return rotateLeft(node);
        if (bf < -1) { node.right = rotateRight(node.right); return rotateLeft(node); }
        return node;
    }

    private Node insert(Node node, int id, int score) {
        if (node == null) return new Node(id, score);

        if (score < node.score ||
           (score == node.score && id < node.playerId)) {
            node.left  = insert(node.left,  id, score);
        } else {
            node.right = insert(node.right, id, score);
        }

        update(node);
        return balance(node);
    }

    private Node minNode(Node n) {
        return (n.left == null) ? n : minNode(n.left);
    }

    private Node delete(Node node, int id, int score) {
        if (node == null) return null;
        if (score < node.score ||
           (score == node.score && id < node.playerId)) {
            node.left = delete(node.left, id, score);
        } else if (score > node.score ||
           (score == node.score && id > node.playerId)) {
            node.right = delete(node.right, id, score);
        } else {

            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node succ = minNode(node.right);
                node.playerId = succ.playerId;
                node.score    = succ.score;
                node.right    = delete(node.right, succ.playerId, succ.score);
            }
        }
        if (node != null) {
            update(node);
            node = balance(node);
        }
        return node;
    }

    public void addOrUpdate(int playerId, int newScore) {
        if (map.containsKey(playerId)) {
            int old = map.get(playerId);
            root = delete(root, playerId, old);
        }
        root = insert(root, playerId, newScore);
        map.put(playerId, newScore);
    }

    public int rank(int playerId) {
        if (!map.containsKey(playerId)) return -1;
        return countGreater(root, playerId, map.get(playerId)) + 1;
    }

    private int countGreater(Node node, int id, int score) {
        if (node == null) return 0;
        boolean greater = node.score > score ||
                          (node.score == score && node.playerId < id);
        if (greater) {
            return size(node.right) + 1 + countGreater(node.left, id, score);
        } else {
            return countGreater(node.right, id, score);
        }
    }

    public int select(int k) {
        if (k < 1 || k > size(root)) return -1;

        int target = size(root) - k + 1;
        Node node  = selectKth(root, target);
        return node == null ? -1 : node.playerId;
    }

    private Node selectKth(Node node, int k) {
        if (node == null) return null;
        int ls = size(node.left);
        if (k <= ls)            return selectKth(node.left, k);
        else if (k == ls + 1)   return node;
        else                    return selectKth(node.right, k - ls - 1);
    }

    public List<Integer> topK(int K) {
        List<Integer> res = new ArrayList<>();
        topK(root, K, res);
        return res;
    }

    private void topK(Node node, int K, List<Integer> res) {
        if (node == null || res.size() >= K) return;
        topK(node.right, K, res);
        if (res.size() < K) {
            res.add(node.playerId);
            topK(node.left, K, res);
        }
    }

    // Demo
    public static void main(String[] args) {
        AVLLeaderboardSystem sys = new AVLLeaderboardSystem();
        sys.addOrUpdate(1001, 500);
        sys.addOrUpdate(1002, 750);
        sys.addOrUpdate(1003, 600);
        sys.addOrUpdate(1002, 800);

        System.out.println("排名 1: " + sys.select(1));
        System.out.println("玩家 1003 的排名: " + sys.rank(1003));
        System.out.println("前 3 名: " + sys.topK(3));
    }
}