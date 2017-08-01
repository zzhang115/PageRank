import org.apache.hadoop.util.hash.Hash;

import java.util.*;

/**
 * Created by zzc on 7/29/17.
 */
public class Driver
{
    public static void main(String[] args)
    {
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
        System.out.println(divide(Integer.MAX_VALUE, 10));
    }
    public static int divide(int dividend, int divisor) {
        int newDividend = Math.abs(dividend);
        int newDivisor = Math.abs(divisor);
        boolean isNeg = dividend > 0 != divisor > 0;
        if (newDividend < newDivisor || divisor == 0) {
            return 0;
        }
        int i = 0;
        double base = 1;
        int count = 0;
        while ((newDivisor << count) <= newDividend) {
            count++;
            System.out.println(count);
        }
        while (i  < count - 1) {
            base = base + base;
            i++;
        }
        int next = newDividend - (newDivisor << (count - 1));
        if (dividend < 0) {
            next = -next;
        }
        System.out.println(next + " "+divisor);
        double result = base + divide(next, divisor);
        if (isNeg) {
            result = -result;
        }
        if (result > Integer.MAX_VALUE) {
            result = Integer.MAX_VALUE;
        }
        if (result < Integer.MIN_VALUE) {
            result = Integer.MIN_VALUE;
        }
        return (int)result;
    }
}
