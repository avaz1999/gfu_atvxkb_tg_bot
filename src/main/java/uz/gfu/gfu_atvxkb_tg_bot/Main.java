package uz.gfu.gfu_atvxkb_tg_bot;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String t = "listen";
        String s = "silent";

        int n = 3;

        int possibleSteps = countPossibleSteps(n);
        System.out.println("Mumkin bo'lgan usullar soni: " + possibleSteps);

        boolean isAnagram = isAnagram(t, s);
        System.out.println(isAnagram); // true

        int m = 5;
        int[] prices = {73, 31, 96, 24, 46};

        int maxProfit = calculateMaxProfit(m, prices);
        System.out.println("Maksimal pul miqdori: $" + maxProfit);
        int[] arr1 = {1, 2, 3, 5, 7}, arr2 = {1, 2, 4, 6, 7};

        System.out.println("Min num " + findMinimumCommonElement(arr1, arr2));
    }

    public static boolean isAnagram(String t, String s) {
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

    public static int countPossibleSteps(int n) {
        if (n == 0) {
            return 1; // Bazis holat: zinapoyadan chiqish mumkin
        } else if (n < 0) {
            return 0; // Bazis holat: zinapoyadan chiqish mumkin emas
        } else {
            // Har bir sakrash uchun 1, 2 va 3 qadamga sakrash imkoniyatini hisoblash
            int steps1 = countPossibleSteps(n - 1);
            int steps2 = countPossibleSteps(n - 2);
            int steps3 = countPossibleSteps(n - 3);

            // Jami usullarni hisoblash
            return steps1 + steps2 + steps3;
        }
    }

    public static int calculateMaxProfit(int n, int[] prices) {
        int maxProfit = 0;
        int currentMinPrice = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            if (prices[i] < currentMinPrice) {
                currentMinPrice = prices[i]; // Yangi eng arzon narxni belgilash
            } else if (prices[i] - currentMinPrice > maxProfit) {
                maxProfit = prices[i] - currentMinPrice; // Eng yuqori foyda miqdorini hisoblash
            }
        }

        return maxProfit;
    }

    public static int findMinimumCommonElement(int[] arr1, int[] arr2) {
        for (int element1 : arr1) {
            for (int element2 : arr2) {
                if (element1 == element2) {
                    return element1;
                }
            }
        }
        return -1;
    }
}
