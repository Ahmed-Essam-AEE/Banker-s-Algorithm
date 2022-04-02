package pk;

import java.util.Scanner;
import java.util.*;
import java.lang.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void fillInitialRes(int m, int[] available) {
        for (int i = 0; i < m; i++) {
            System.out.print("Enter available resources for R" + (i + 1) + ": ");
            available[i] = sc.nextInt();

        }
        /*for (int i=0 ; i<m ; i++)
        {
            System.out.print(available[i] +" ");
        }*/
    }

    public static void fillAvaRes(int[] available, int[][] allocated) {
        for (int i = 0; i < allocated.length; i++) {
            for (int j = 0; j < available.length; j++) {
                available[j] -= allocated[i][j];
            }
        }
    }

    public static void fillMax(int n, int m, int[][] maximum) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter maximum demand of P" + (i + 1) + " for R" + (j + 1) + ": ");
                maximum[i][j] = sc.nextInt();
            }
        }
    }

    public static void fillAlloc(int n, int m, int[][] alloc) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter currently allocated  resources for P" + (i + 1) + " and R" + (j + 1) + ": ");
                alloc[i][j] = sc.nextInt();
            }
        }
    }

    public static void fillNeed(int n, int m, int[][] need, int[][] maxNeed, int[][] allocated) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = maxNeed[i][j] - allocated[i][j];
            }
        }
    }

    public static void request(int p, int m, int[] resources, int[][] need) {
        for (int i = 0; i < m; i++) {
            need[p][i] += resources[i];
            // check for safe state.

        }
    }

    public static void release(int p, int m, int[] resources, int[][] alloc, int[][] available) {
        for (int i = 0; i < m; i++) {
            if (resources[i] <= alloc[p][i]) {
                alloc[p][i] -= resources[i];
                available[p][i] += alloc[p][i];
            }
        }
    }

    public static boolean safeState(int[][] x, int[][] y, int[] z) {
        int n = x.length;
        int m = z.length;
        int[] available = new int[m];
        int[][] need = new int[n][m];
        int[][] allocated = new int[n][m];
        boolean[] finished = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                available[j] = z[j];
                need[i][j] = y[i][j];
                allocated[i][j] = x[i][j];
            }
        }
        for (int i = 0; i < n; i++)
            finished[i] = false;
        int prevRemaining = n;
        while (true) {
            int remaining = n;
            for (int i = 0; i < n; i++) {
                int count = 0;
                if (finished[i]) {
                    remaining--;
                    continue;
                }
                for (int j = 0; j < m; j++) {
                    if (need[i][j] <= available[j])
                        count++;
                }
                if (count == m) {
                    finished[i] = true;
                    remaining--;
                    for (int j = 0; j < available.length; j++) {
                        available[j] += allocated[i][j];
                    }
                }

            }
            if (remaining == prevRemaining)
                return false;
            if (remaining == 0)
                break;
            prevRemaining = remaining;
        }
        return true;
    }

    public static void main(String[] args) {

        int n = sc.nextInt(), m = sc.nextInt();                  // n is the number of processes and m is number of resources.
        int[] available = new int[m];                       //the available amount of each resource.
        int[][] maximum = new int[n][m];                    //the maximum demand of each process.
        int[][] allocation = new int[n][m];                 //the amount currently allocated to each process.
        int[][] need = new int[n][m];                       //the remaining needs of each process.
        fillInitialRes(m, available);
        fillAlloc(n, m, allocation);
        fillMax(n, m, maximum);
        fillNeed(n, m, need, maximum, allocation);
        fillAvaRes(available, allocation);
    }
}
