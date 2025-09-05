package finalexam;
import java.util.*;
public class LC39_CombinationSum_PPE {
    //I
    public static List<List<Integer>> combinationSumI(int[] candidates, int target) {
        Arrays.sort(candidates); // 升序方便剪枝
        List<List<Integer>> res = new ArrayList<>();
        backtrackI(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrackI(int[] candidates, int remain, int start,
                                    List<Integer> path, List<List<Integer>> res) {
        if (remain == 0) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remain) break; // 剪枝
            path.add(candidates[i]);
            backtrackI(candidates, remain - candidates[i], i, path, res); // i → 可重複
            path.remove(path.size() - 1);
        }
    }

    //II
    public static List<List<Integer>> combinationSumII(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        backtrackII(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrackII(int[] candidates, int remain, int start,
                                     List<Integer> path, List<List<Integer>> res) {
        if (remain == 0) {
            res.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) continue; // 跳過重複
            if (candidates[i] > remain) break;
            path.add(candidates[i]);
            backtrackII(candidates, remain - candidates[i], i + 1, path, res); // i+1 → 不重複
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            int target = sc.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = sc.nextInt();

            //I版
            List<List<Integer>> ans1 = combinationSumI(arr, target);
            for (List<Integer> comb : ans1) {
                for (int i = 0; i < comb.size(); i++) {
                    System.out.print(comb.get(i));
                    if (i < comb.size() - 1) System.out.print(" ");
                }
                System.out.println();
            }

            System.out.println("---");

            //II版
            List<List<Integer>> ans2 = combinationSumII(arr, target);
            for (List<Integer> comb : ans2) {
                for (int i = 0; i < comb.size(); i++) {
                    System.out.print(comb.get(i));
                    if (i < comb.size() - 1) System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
}
