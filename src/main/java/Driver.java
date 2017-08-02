import org.apache.hadoop.util.hash.Hash;

import java.util.*;

/**
 * Created by zzc on 7/29/17.
 */
public class Driver {
    public static void main(String[] args) {
        String abc = "abc";
        int a = 'b';
        System.out.println(abc.lastIndexOf(a));
//        abc.lastIndexOf()
//        abc.contains();
        Queue<Integer> queue = new LinkedList<Integer>();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1,2);
        map.put(1, map.get(1) + 1);
        System.out.println(map.get(1));
        map.remove(1);
        int[][] matrix = new int[0][0];
        map.values();
    }
}
