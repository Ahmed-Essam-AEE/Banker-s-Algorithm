package pk;

import java.util.Scanner;
import java.util.*;
import java.lang.*;
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void fillAvaRes(int m , int [] available)
    {
        for (int i=0 ; i<m ; i++)
        {
            System.out.print("Enter available resources for R"+(i+1)+": ");
            available[i]=sc.nextInt();

        }
        /*for (int i=0 ; i<m ; i++)
        {
            System.out.print(available[i] +" ");
        }*/
    }
    public static void fillMax(int n, int m , int [] [] maximum)
    {
        for (int i=0 ; i<n ; i++)
        {
            for (int j=0 ; j<m ; j++){
                System.out.print("Enter maximum demand of P"+(i+1)+ " for R" + (j+1) +": ");
                maximum[i][j]=sc.nextInt();
            }
        }
    }
    public static void fillAlloc(int n, int m , int [] [] alloc)
    {
        for (int i=0 ; i<n ; i++)
        {
            for (int j=0 ; j<m ; j++){
                System.out.print("Enter currently allocated  resources for P"+(i+1)+ " and R" + (j+1) +": ");
                alloc[i][j]=sc.nextInt();
            }
        }
    }
    public static void fillNeed(int n, int m , int [] [] need)
    {
        for (int i=0 ; i<n ; i++)
        {
            for (int j=0 ; j<m ; j++){
                System.out.print("Enter remaining needs resources for P"+(i+1)+ " and R" + (j+1) +": ");
                need[i][j]=sc.nextInt();
            }
        }
    }
    public static void main(String[] args) {

        int n=sc.nextInt() , m=sc.nextInt();                  // n is the number of processes and m is number of resources.
        int [] available = new int[m];                       //the available amount of each resource.
        int [][] maximum = new int[n][m];                    //the maximum demand of each process.
        int [][] allocation = new int[n][m];                 //the amount currently allocated to each process.
        int [][] need = new int[n][m];                       //the remaining needs of each process.
        fillAvaRes(m , available);
        fillAlloc(n,m, allocation);
        fillMax(n,m, maximum);
        fillNeed(n,m,need);




    }
}
