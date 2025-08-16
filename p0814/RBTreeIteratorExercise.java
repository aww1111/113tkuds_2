package p0814;

import java.util.*;

public class RBTreeIteratorExercise {
    enum Color { RED, BLACK }

    static class Node {
        int key;
        Color color;
        Node left, right, parent;
        Node(int key, Color color) {
            this.key   = key;
            this.color = color;
        }
    }

    private Node root;

    public void setRoot(Node r) {
        this.root = r;
    }

    public Iterator<Integer> iterator() {
        return new InOrderIterator(root);
    }
    public Iterator<Integer> reverseIterator() {
        return new ReverseIterator(root);
    }
    public Iterator<Integer> rangeIterator(int min, int max) {
        return new RangeIterator(root, min, max);
    }

    private static class InOrderIterator implements Iterator<Integer> {
        private final Deque<Node> stack = new ArrayDeque<>();

        InOrderIterator(Node root) {
            pushLeft(root);
        }

        private void pushLeft(Node n) {
            while (n != null) {
                stack.push(n);
                n = n.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node n = stack.pop();
            pushLeft(n.right);
            return n.key;
        }
    }

    private static class ReverseIterator implements Iterator<Integer> {
        private final Deque<Node> stack = new ArrayDeque<>();

        ReverseIterator(Node root) {
            pushRight(root);
        }

        private void pushRight(Node n) {
            while (n != null) {
                stack.push(n);
                n = n.right;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node n = stack.pop();
            pushRight(n.left);
            return n.key;
        }
    }

    private static class RangeIterator implements Iterator<Integer> {
        private final Deque<Node> stack = new ArrayDeque<>();
        private final int min, max;

        RangeIterator(Node root, int min, int max) {
            this.min = min;
            this.max = max;
            pushLeftInRange(root);
        }

        private void pushLeftInRange(Node n) {
            while (n != null) {
                if (n.key < min) {
                    n = n.right;
                } else {
                    stack.push(n);
                    n = n.left;
                }
            }
        }

        @Override
        public boolean hasNext() {
            while (!stack.isEmpty() && stack.peek().key > max) {
                stack.pop();
            }
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node n = stack.pop();
            int result = n.key;
            pushLeftInRange(n.right);
            return result;
        }
    }

    public static void main(String[] args) {
        RBTreeIteratorExercise tree = new RBTreeIteratorExercise();

        Node n1 = new Node(10, Color.BLACK);
        Node n2 = new Node(5,  Color.RED);
        Node n3 = new Node(15, Color.RED);
        Node n4 = new Node(3,  Color.BLACK);
        Node n5 = new Node(7,  Color.BLACK);
        n1.left = n2; n1.right = n3;
        n2.parent = n1; n3.parent = n1;
        n2.left = n4; n2.right = n5;
        n4.parent = n2; n5.parent = n2;
        tree.setRoot(n1);

        System.out.print("In-order: ");
        Iterator<Integer> it = tree.iterator();
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();

        System.out.print("Reverse: ");
        it = tree.reverseIterator();
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();

        System.out.print("Range [5,10]: ");
        it = tree.rangeIterator(5, 10);
        while (it.hasNext()) System.out.print(it.next() + " ");
        System.out.println();
    }
}