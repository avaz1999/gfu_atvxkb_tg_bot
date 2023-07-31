package uz.gfu.gfu_atvxkb_tg_bot;


import java.util.Arrays;

/**
 * Absamatov Avaz
 * telegram username : @saralashadmin
 * telegram username : @avazabsamatov
 */

public class Solution {
    public static void main(String[] args) {
        String t = "listen";
        String s = "silent";

        int n = 3;

        int possibleSteps = countWay(n);
        System.out.println("Mumkin bo'lgan usullar soni: " + possibleSteps);

        boolean isAnagram = isAnagram(t, s);
        System.out.println(isAnagram); // true

        int m = 5;
        int[] prices = {10, 12, 15, 11, 9};

        int maxProfit = maxProfit(m, prices);
        System.out.println("Maksimal pul miqdori: $" + maxProfit);
        int[] arr1 = {1, 2, 3, 5, 7}, arr2 = {1, 2, 4, 6, 7};

        System.out.println("Min num " + commonMinElement(arr1, arr2));
    }
    /**
     * 1-problem
     *
     * @param s :String
     * @param t :String
     * @return : Boolean
     */
    public static boolean isAnagram(String s, String t) {
        // O'zgaruvchilarni kichik harflarga o'girish
        t = t.toLowerCase();
        s = s.toLowerCase();

        // Harflarni massivlarga ajratish
        char[] tArray = t.toCharArray();
        char[] sArray = s.toCharArray();

        // Massivlarni tartiblash
        Arrays.sort(tArray);
        Arrays.sort(sArray);

        // Massivlarni tekshirish
        return Arrays.equals(tArray, sArray);
    }


    /**
     * 2-problem
     *
     * @param n:Int
     * @return : Int
     */
    public static int countWay(int n) {
        if (n == 0) {
            return 1; //  zinapoyadan chiqish mumkin
        } else if (n < 0) {
            return 0; //  zinapoyadan chiqish mumkin emas
        } else {
            // Har bir sakrash uchun 1, 2 va 3 qadamga sakrash imkoniyatini hisoblash
            int steps1 = countWay(n - 1);
            int steps2 = countWay(n - 2);
            int steps3 = countWay(n - 3);

            // Jami usullarni hisoblash
            return steps1 + steps2 + steps3;
        }
    }


    /**
     * 3-problem
     *
     * @param n:int
     * @param arr:array
     * @return : int
     */
    public static int maxProfit(int n, int[] arr) {

        int maxProfit = 0;
        int currentMinPrice = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            if (arr[i] < currentMinPrice) {
                currentMinPrice = arr[i]; // Yangi eng arzon narxni belgilash
            } else if (arr[i] - currentMinPrice > maxProfit) {
                maxProfit = arr[i] - currentMinPrice; // Eng yuqori foyda miqdorini hisoblash
            }
        }

        return maxProfit;
    }

    /**
     * 4-problem
     *
     * @param nums1:array
     * @param nums2:array
     * @return :int
     */
    public static int commonMinElement(int[] nums1, int[] nums2) {
        // Har bir arrayni qiymatlarini solishtirib chiqish
        for (int element1 : nums1) {
            for (int element2 : nums2) {
                // Teng bo'lsa qaytarish
                if (element1 == element2) {
                    return element1;
                }
            }
        }
        return -1;
    }

}
