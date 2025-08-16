package p0814;

import java.util.*;

public class BenchmarkFrameworkExercise {

    interface SearchTree {
        void insert(int key);
        void delete(int key);
        boolean search(int key);
    }

    static class AvlTree implements SearchTree {
        @Override public void insert(int key) { /* … */ }
        @Override public void delete(int key) { /* … */ }
        @Override public boolean search(int key) { return false; /* … */ }
    }

    static class RedBlackTree implements SearchTree {

        @Override public void insert(int key) { /* … */ }
        @Override public void delete(int key) { /* … */ }
        @Override public boolean search(int key) { return false; /* … */ }
    }

    static class BenchmarkResult {
        final String treeName;
        final String scenario;
        final long ops;
        final long timeNs;

        BenchmarkResult(String treeName, String scenario, long ops, long timeNs) {
            this.treeName = treeName;
            this.scenario = scenario;
            this.ops      = ops;
            this.timeNs   = timeNs;
        }

        String summary() {
            double secs = timeNs / 1e9;
            double opsPerSec = ops / secs;
            return String.format("%-15s | %-20s | %,10d ops | %7.3f s | %,10.0f ops/s",
                treeName, scenario, ops, secs, opsPerSec);
        }
    }

    private static int[] generateSequence(int n, boolean random) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        if (random) {
            Random rnd = new Random(42);
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int t = a[i]; a[i] = a[j]; a[j] = t;
            }
        }
        return a;
    }

    private static long benchInsert(SearchTree tree, int[] keys) {
        long start = System.nanoTime();
        for (int k : keys) {
            tree.insert(k);
        }
        return System.nanoTime() - start;
    }

    private static long benchSearch(SearchTree tree, int[] queries) {
        long start = System.nanoTime();
        for (int k : queries) {
            tree.search(k);
        }
        return System.nanoTime() - start;
    }

    private static long benchMixed(SearchTree tree, int[] insertKeys, int[] searchQueries) {
        long start = System.nanoTime();
        int n = Math.min(insertKeys.length, searchQueries.length);
        for (int i = 0; i < n; i++) {
            tree.insert(insertKeys[i]);
            tree.search(searchQueries[i]);
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {

        final int N = 100_000;
        int[] seqAsc   = generateSequence(N, false);
        int[] seqRnd   = generateSequence(N, true);
        int[] searchAsc = generateSequence(N, false);
        int[] searchRnd = generateSequence(N, true);

        List<BenchmarkResult> results = new ArrayList<>();

        Map<String, SearchTree> trees = Map.of(
            "AVLTree",      new AvlTree(),
            "RedBlackTree", new RedBlackTree()
        );

        for (var entry : trees.entrySet()) {
            String name = entry.getKey();
            SearchTree tree;

            tree = entry.getValue();
            results.add(new BenchmarkResult(
                name,
                "Insert Asc",
                N,
                benchInsert(tree, seqAsc)
            ));

            tree = entry.getValue();
            results.add(new BenchmarkResult(
                name,
                "Insert Rnd",
                N,
                benchInsert(tree, seqRnd)
            ));

            tree = entry.getValue();
            benchInsert(tree, seqAsc);
            results.add(new BenchmarkResult(
                name,
                "Search Asc",
                N,
                benchSearch(tree, searchAsc)
            ));

            tree = entry.getValue();
            tree = entry.getValue();
            benchInsert(tree, seqRnd);
            results.add(new BenchmarkResult(
                name,
                "Search Rnd",
                N,
                benchSearch(tree, searchRnd)
            ));

            tree = entry.getValue();
            results.add(new BenchmarkResult(
                name,
                "Mixed Rnd",
                N,
                benchMixed(tree, seqRnd, searchRnd)
            ));
        }

        System.out.println("Tree Performance Benchmark Report");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-15s | %-20s | %-12s | %-8s | %-12s%n",
            "Tree", "Scenario", "Operations", "Time(s)", "Ops/sec");
        System.out.println("-------------------------------------------------------------");
        for (BenchmarkResult r : results) {
            System.out.println(r.summary());
        }
    }
}