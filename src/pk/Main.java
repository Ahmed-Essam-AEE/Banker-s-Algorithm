package pk;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Scanner input = new Scanner(System.in);
    static int[] deadlockedProcess;

    public static void printInfo(int[][] alloc, int[][] need, int[]available, boolean isBanker){
        System.out.println("===Allocated Matrix===");
        for (int i =0; i< alloc.length;i++){
            System.out.print("P" + (i+1) +" ");
            for (int j =0; j<alloc[i].length;j++)
                System.out.print(alloc[i][j]+" ");
            System.out.println();
        }
        if(isBanker){
            System.out.println("===Need Matrix===");

        }else{
            System.out.println("===Requests Matrix===");
        }
        for (int i =0; i< need.length;i++){
            System.out.print("P" + (i+1) +" ");
            for (int j =0; j<need[i].length;j++)
                System.out.print(need[i][j]+" ");
            System.out.println();
        }
        System.out.println("===Available resources===");
        for (int j =0; j<available.length;j++)
            System.out.print(available[j]+" ");
        System.out.println();
        System.out.println();
    }

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

    private static void fillNeed(int n, int m, int[][] need, int[][] maximum, int[][] allocation) {
    }

    public static void fillAlloc(int n, int m, int[][] alloc) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter currently allocated  resources for P" + (i + 1) + " and R" + (j + 1) + ": ");
                alloc[i][j] = sc.nextInt();
            }
        }
    }

   public static void computeNeed(int n, int m, int[][] need, int[][] maxNeed, int[][] allocated) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = maxNeed[i][j] - allocated[i][j];
            }
        }
    }

    public static void request(int p, int m, int[] resources, int[][] need) {
        System.out.println("Request");
        for (int i = 0;i<m;i++) System.out.println(resources[i]);
        for (int i = 0; i < m; i++) {
            need[p][i] += resources[i];
            // check for safe state.

        }
    }

    public static void release(int p, int m, int[] resources, int[][] alloc, int[] available) {
        System.out.print("Release P"+(p+1));
        for (int i = 0;i<m;i++) System.out.print(resources[i]);
        for (int i = 0; i < m; i++) {
            if (resources[i] <= alloc[p][i]) {
                available[i] += resources[i];
                alloc[p][i] -= resources[i];
            }
        }
    }
    public static boolean compareTwoArrays(int n , int [] arr1 ,  int [] arr2 ){
        for(int i=0 ; i<n ; i++)
        {
            if(arr1[i]<=arr2[i]){
                continue;
            }else return false;
        }
        return true;
    }
    public static void banker(int n, int m, int p, int[][] max, int[][] alloc, int[] available , int [][] need , int [] requestedResources){
        computeNeed(n , m , need , max , alloc);
        /*
        Set<Integer> s = null;
        for (int i = 0; i < n; i++) {
                if(s.contains(i) &&  compareTwoArrays(m ,need[i] , available))
                {
                    release(i ,m , alloc[i] , alloc , available );
                    s.add(i);
                    i=0;
                }
        }*/
        if(compareTwoArrays( m , requestedResources , need[p]))
        {
            if(compareTwoArrays(m , requestedResources , available))
            {
                for(int j=0; j<m ; j++)
                {
                    alloc[p][j]+=requestedResources[j];
                    available[j]-=requestedResources[j];
                    need[p][j]-=requestedResources[j];
                }
                System.out.println("Request is being reviewed(check for safe state).");
                if(!safeState(alloc , need , available))
                {
                    System.out.println("Request lead to unsafe state).");
                    for(int j=0; j<m ; j++)
                    {
                        alloc[p][j]-=requestedResources[j];
                        available[j]+=requestedResources[j];
                        need[p][j]+=requestedResources[j];
                    }
                } else{ System.out.println("System is in safe state.");}
            }
            else {
                System.out.println("Unable to accept this request(not available).");
            }
        }
        else {
            System.out.println("Requested resources is more than expected needed resources.");
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
            if (remaining == prevRemaining) {
                System.out.println("Unsafe/Deadlock State");
                deadlockedProcess = new int[remaining];
                int idx =0;
                for(int i =0; i< finished.length;i++){
                    if(!finished[i]){
                        System.out.println("P" + i+" In deadlock");
                        deadlockedProcess[idx]=i;
                        idx++;
                    }
                }
                return false;
            }
            if (remaining == 0)
                break;
            prevRemaining = remaining;
        }
        System.out.println("Safe state");
        return true;
    }


    public static void main(String[] args) {
        System.out.println("Enter the number of processes and the number of resources:");
        int n = sc.nextInt(), m = sc.nextInt();             // n is the number of processes and m is number of resources.
        int[] available = new int[m];                       //the available amount of each resource.
        int[][] maximum = new int[n][m];                    //the maximum demand of each process.
        int[][] allocation = new int[n][m];                 //the amount currently allocated to each process.
        int[][] need = new int[n][m];                       //the remaining needs of each process.
        fillInitialRes(m, available);
        fillAlloc(n, m, allocation);
        fillMax(n, m, maximum);
        fillNeed(n, m, need, maximum, allocation);
        fillAvaRes(available, allocation);
        System.out.println("choose to request, release, recover, or quit:");
        String in="";
        while(!in.equals("Quit")){
            in = input.nextLine();
            if(in.charAt(1)=='Q'){
                //RQ <process#> <r1> <r2> <r3>
                int[] resources = new int[m];
                String[] splitinput = in.split(" ");
                int p = Integer.parseInt(splitinput[1]);
                for(int i = 0 ; i <m;i++) resources[i] = Integer.parseInt(splitinput[i+2]);
                request(p,m,resources,need);
            }
            else if(in.charAt(1)=='L'){
                //RL <process#> <r1> <r2> <r3>
                int[] resources = new int[m];
                String[] splitinput = in.split(" ");
                int p = Integer.parseInt(splitinput[1]);
                for(int i = 0 ; i <m;i++) resources[i] = Integer.parseInt(splitinput[i+2]);
                release(p,m,resources,allocation,available);
            }
            else{
                //Recover
            }
        }
    }
}
