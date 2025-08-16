package p0814;

import java.util.ArrayList;
import java.util.List;

public class RBValidationExercise {

    enum Color { RED, BLACK }

    static class Node {
        int key;
        Color color;
        Node left, right;

        Node(int key, Color color) {
            this.key   = key;
            this.color = color;
        }
    }

    public static class ValidationResult {
        public final boolean valid;
        public final List<String> errors;

        private ValidationResult(boolean valid, List<String> errors) {
            this.valid  = valid;
            this.errors = errors;
        }
    }

    public static ValidationResult validate(Node root) {
        List<String> errors = new ArrayList<>();

        if (root != null && root.color != Color.BLACK) {
            errors.add("根節點必須為黑色，但發現 " + root.color);
        }

        computeBlackHeight(root, 0, errors);

        return new ValidationResult(errors.isEmpty(), errors);
    }

    private static int computeBlackHeight(Node node, int blackCount, List<String> errors) {
        if (node == null) {

            return blackCount + 1;
        }

        if (node.color == Color.RED) {
            if ((node.left  != null && node.left.color  == Color.RED) ||
                (node.right != null && node.right.color == Color.RED)) {
                errors.add("紅節點 " + node.key + " 的子節點中有紅色節點");
            }
        }

        int bc = blackCount + (node.color == Color.BLACK ? 1 : 0);

        int leftBH  = computeBlackHeight(node.left,  bc, errors);
        int rightBH = computeBlackHeight(node.right, bc, errors);

        if (leftBH != rightBH) {
            errors.add("節點 " + node.key +
                       " 左右子樹黑高度不一致：left=" + leftBH +
                       ", right=" + rightBH);
        }

        return Math.max(leftBH, rightBH);
    }

    public static void main(String[] args) {

        Node root = new Node(10, Color.RED);
        root.left  = new Node(5,  Color.BLACK);
        root.right = new Node(15, Color.BLACK);

        root.left.left = new Node(3, Color.RED);
        root.left.left.left = new Node(1, Color.RED);

        ValidationResult res = validate(root);
        System.out.println("valid = " + res.valid);
        System.out.println("errors:");
        for (String err : res.errors) {
            System.out.println(" - " + err);
        }
    }
}