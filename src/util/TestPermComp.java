package util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Class with tests of method of permutations and combinations.
 * 
 * 
 		power set, combinations, 2^n, but only created when requested (index) and fit in, long or bigInteger to binary:
			for i=0, index != 0, i++ index >>= 1
				if index & 1 == 1: list.add(set(i))
 *
 * @author joaquin
 */
public class TestPermComp {

  /** N Permutation: n! */
  public static void permN(char[] a, int pos) {
    if (pos == a.length) {
      System.out.println(new String(a));
      return;
    }
    for (int i = pos; i < a.length; i++) {
      swap(a, pos, i);
      permN(a, pos + 1);
      swap(a, pos, i);
    }
  }

  public static void permNIter(char[] a) {
    Deque<Pair<char[], Integer>> stack = new ArrayDeque<>();
    stack.push(new Pair<>(a.clone(), 0));
    while (!stack.isEmpty()) {
      Pair<char[], Integer> pair = stack.pop();
      char[] arr = pair.getKey();
      int pos = pair.getValue();
      if (pos == arr.length) {
        System.out.println(new String(arr));
        continue;
      }
      for (int i = pos; i < arr.length; i++) {
        swap(arr, pos, i);
        stack.push(new Pair<>(arr.clone(), pos + 1));
        swap(arr, pos, i);
      }
    }
  }

  private static class Pair<K, V> {
    K key;
    V val;

    Pair(K key, V val) {
      this.key = key;
      this.val = val;
    }

    K getKey() {
      return key;
    }

    V getValue() {
      return val;
    }
  }

  // T = O(N!*N), S = O(N!*N)
  private List<List<Integer>> usingBfs(int[] nums) {
    List<List<Integer>> res = new ArrayList<>();
    List<List<Integer>> perms = new ArrayList<>();
    perms.add(new ArrayList<>());
    for (int num : nums) {
      int size = perms.size();
      for (int i = 0; i < size; i++) {
        List<Integer> oldPerm = perms.get(i);
        for (int j = 0; j <= oldPerm.size(); j++) {
          List<Integer> newPerm = new ArrayList<>(oldPerm);
          newPerm.add(j, num);
          if (newPerm.size() == nums.length) res.add(newPerm);
          else perms.add(newPerm);
        }
      }
    }
    return res;
  }

  /** N, K Permutation: n!/(n-k)! */
  public static void permNK(char[] a, int pos, int k) {
    if (k == 0) {
      for (int i = 0; i < pos; i++) {
        System.out.print(a[i]);
      }
      System.out.println();
      return;
    }
    for (int i = pos; i < a.length; i++) {
      swap(a, pos, i);
      permNK(a, pos + 1, k - 1);
      swap(a, pos, i);
    }
  }

  private static void swap(char[] a, int i, int j) {
    char tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }

  /** N Combination (subsets): 2^n */
  public static void combN(String prefix, String s, int pos) {
    if (pos == s.length()) {
      System.out.println(prefix);
      return;
    }
    combN(prefix + s.charAt(pos), s, pos + 1);
    combN(prefix, s, pos + 1);
  }

  /**
   * N, K Combination: n!/k!(n-k)!
   *
   * @param prefix
   * @param s
   * @param k
   */
  public static void combNK(String prefix, String s, int pos, int k) {
    if (k == 0) {
      System.out.println(prefix);
    } else {
      if (pos == s.length()) return;
      combNK(prefix + s.charAt(pos), s, pos + 1, k - 1);
      combNK(prefix, s, pos + 1, k);
    }
  }

  /** Make partitions (2^n-1) */
  private static void partitionsDfs(String s, int pos, List<String> list, List<List<String>> res) {
    if (pos == s.length()) {
      res.add(new ArrayList<>(list));
      return;
    }
    for (int i = pos; i < s.length(); i++) {
      list.add(s.substring(pos, i + 1));
      partitionsDfs(s, i + 1, list, res);
      list.remove(list.size() - 1);
    }
  }

  /** Combinations of phone numbers with letters */
  static List<String> output = new ArrayList<>();

  static String[] phone = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
  // T = O(4^N), S = O(4^N), worst case when all digits are of 4 letters
  public static List<String> letterCombinations(String digits) {
    if (digits == null || digits.length() == 0) return output;
    backtrack(new StringBuilder(), digits, 0);
    return output;
  }

  private static void backtrack(StringBuilder sb, String digits, int pos) {
    if (pos == digits.length()) {
      output.add(sb.toString());
      return;
    }
    String letters = phone[digits.charAt(pos) - '0'];
    for (char c : letters.toCharArray()) {
      backtrack(sb.append(c), digits, pos + 1);
      sb.setLength(sb.length() - 1);
    }
  }

  // Factor Combinations: T = O(nlog(n)), S = O(log(n))
  public List<List<Integer>> getFactors(int n) {
    List<List<Integer>> res = new ArrayList<>();
    dfs(n, 2, new ArrayList<>(), res);
    return res;
  }

  private void dfs(int n, int i, List<Integer> list, List<List<Integer>> res) {
    if (n == 1) {
      if (list.size() > 1) res.add(new ArrayList<>(list));
      return;
    }
    for (int j = i; j <= n; j++) {
      if (n % j == 0) {
        list.add(j);
        dfs(n / j, j, list, res);
        list.remove(list.size() - 1);
      }
    }
  }

  // main
  public static void main(String[] args) {
    System.out.println("N Permutation: n!  --> 'abc', 3 = ");
    permN("abc".toCharArray(), 0);
    System.out.println("-----------------------------------------------");

    System.out.println("N, K Permutation: n!/(n - k)!  --> 'abc', 3, 2 = ");
    permNK("abc".toCharArray(), 0, 2);
    System.out.println("-----------------------------------------------");

    System.out.println("N Combination (subsets): 2^n  --> '', 'abc' = ");
    combN("", "abc", 0);
    System.out.println("-----------------------------------------------");

    System.out.println("N, K Combination: n!/k!(n-k)!  --> '', 'abc', 2 = ");
    combNK("", "abc", 0, 2);
    System.out.println("-----------------------------------------------");

    System.out.println("All string partitions (2^n-1) for abcd");
    List<List<String>> res = new ArrayList<>();
    partitionsDfs("abcd", 0, new ArrayList<>(), res);
    System.out.println(res);
    System.out.println("-----------------------------------------------");

    System.out.println("Letter Combinations of phone number, '', '23', 0 = ");
    String digits = "23";
    List<String> output = letterCombinations(digits);
    System.out.println(output.toString());
  }
}
