package pk;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static Scanner input = new Scanner(System.in);
    static int[][] allocated;
    static int[][] requested;
    static int[][] need;
    static int[][] maxNeed;
    static int[] available;
    static ArrayList<Integer> pendingRequests = new ArrayList<>();
    static int n, m;
    static int[] deadlockedProcess;

    public static void printInfo(int[][] need_, boolean isBanker) {
        System.out.println("===Allocated Matrix===");
        for (int i = 0; i < allocated.length; i++) {
            System.out.print("P" + (i + 1) + " ");
            for (int j = 0; j < allocated[i].length; j++)
                System.out.print(allocated[i][j] + " ");
            System.out.println();
        }
        if (isBanker) {
            System.out.println("===Need Matrix===");

        } else {
            System.out.println("===Requests Matrix===");
        }
        for (int i = 0; i < need_.length; i++) {
            System.out.print("P" + (i + 1) + " ");
            for (int j = 0; j < need_[i].length; j++)
                System.out.print(need_[i][j] + " ");
            System.out.println();
        }
        System.out.println("===Available resources===");
        for (int j = 0; j < available.length; j++)
            System.out.print(available[j] + " ");
        System.out.println();
        System.out.println();
    }

    public static void fillInitialRes() {
        for (int i = 0; i < m; i++) {
            System.out.print("Enter initial available resources for R" + (i + 1) + ": ");
            available[i] = sc.nextInt();

        }
    }

    public static void computeAvaRes() { //Compute remaining available resources
        for (int i = 0; i < allocated.length; i++) {
            for (int j = 0; j < available.length; j++) {
                available[j] -= allocated[i][j];
            }
        }
    }

    public static void fillMax() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter maximum demand of P" + i + " for R" + (j + 1) + ": ");
                maxNeed[i][j] = sc.nextInt();
            }
        }
    }

    public static void computeNeed() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = maxNeed[i][j] - allocated[i][j];
            }
        }
    }


    public static void fillAlloc() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter currently allocated  resources for P" + i + " and R" + (j + 1) + ": ");
                allocated[i][j] = sc.nextInt();
            }
        }
    }

    public static void fillReq() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print("Enter currently Requeted  resources for P" + (i + 1) + " and R" + (j + 1) + ": ");
                requested[i][j] = sc.nextInt();
            }
        }
    }

    public static boolean compareTwoArrays(int n, int[] arr1, int[] arr2) {
        for (int i = 0; i < n; i++) {
            if (arr1[i] <= arr2[i]) {
                continue;
            } else return false;
        }
        return true;
    }

    public static void banker(int p, int[] requestedResources) {

        if (compareTwoArrays(m, requestedResources, need[p])) {
            if (compareTwoArrays(m, requestedResources, available)) {
                for (int j = 0; j < m; j++) {
                    allocated[p][j] += requestedResources[j];
                    available[j] -= requestedResources[j];
                    need[p][j] -= requestedResources[j];
                }
                System.out.println("Request is being reviewed(check for safe state).");
                if (!safeState(need)) {
                    System.out.println("Request lead to unsafe state).");
                    for (int j = 0; j < m; j++) {
                        allocated[p][j] -= requestedResources[j];
                        available[j] += requestedResources[j];
                        need[p][j] += requestedResources[j];
                    }
                } else {
                    System.out.println("System is in safe state.");
                }
            } else {
                System.out.println("Unable to accept this request(not available).");
            }
        } else {
            System.out.println("Requested resources is more than expected needed resources.");
        }

    }

    public static boolean safeState(int[][] y) {

        int[] av = new int[m];
        int[][] need_ = new int[n][m];
        int[][] alloc = new int[n][m];
        boolean[] finished = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                av[j] = available[j];
                need_[i][j] = y[i][j];
                alloc[i][j] = allocated[i][j];
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
                    if (need_[i][j] <= av[j])
                        count++;
                }
                if (count == m) {
                    finished[i] = true;
                    remaining--;
                    for (int j = 0; j < av.length; j++) {
                        av[j] += alloc[i][j];
                    }
                }

            }
            if (remaining == prevRemaining) {
                System.out.println("Unsafe/Deadlock State");
                deadlockedProcess = new int[remaining];
                int idx = 0;
                for (int i = 0; i < finished.length; i++) {
                    if (!finished[i]) {
                        System.out.println("P" + i + " In deadlock");
                        deadlockedProcess[idx] = i;
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

    public static void request(int id, int[] resources) {
        for (int i = 0; i < m; i++) {
            requested[id][i] += resources[i];
        }
        for (int i = 0; i < m; i++) {
            if (requested[id][i] > available[i]) {
                System.out.println("There is not enough available resources");
                pendingRequests.add(id);
                return;
            }
        }
        System.out.println("Resources allocated successfully");
        for (int i = 0; i < m; i++) {
            available[i] -= resources[i];
            allocated[id][i] += resources[i];
            requested[id][i] -= resources[i];
        }
    }

    public static void release(int id, int[] resources) {
        boolean flag = true;
        for (int i = 0; i < m; i++) {
            if (allocated[id][i] < resources[i]) {
                flag = false;
                break;
            }
        }
        if (flag) {
            for (int i = 0; i < m; i++) {
                allocated[id][i] -= resources[i];
                available[i] += resources[i];
            }
            System.out.println("Resources released successfully");
        } else System.out.println("Released resources are greater than allocated ones");


        for (int i = 0; i < pendingRequests.size(); i++) {
            boolean isValid = true;
            for (int j = 0; j < m; j++) {
                if (requested[pendingRequests.get(i)][j] > available[j])
                    isValid = false;
            }
            if (isValid) {
                for (int j = 0; j < m; j++) {
                    available[j] -= requested[pendingRequests.get(i)][j];
                    allocated[pendingRequests.get(i)][j] += requested[pendingRequests.get(i)][j];
                    requested[pendingRequests.get(i)][j] = 0;
                }
                System.out.println("Process " + pendingRequests.get(i) + " successfully allocated to resources.");
                pendingRequests.remove(i);
            }
        }
    }

    public static void recover() {
        while (!safeState(requested)) {
            int maxId = maxAllocatedProcess();
            for (int i = 0; i < m; i++) {
                available[i] += allocated[maxId][i];
                allocated[maxId][i] = 0;
                requested[maxId][i] = 0;
            }
            System.out.println("Terminate process " + maxId);
            pendingRequests.remove(pendingRequests.indexOf(maxId));
        }
    }

    public static int maxAllocatedProcess() {
        int max = 0, sum, id = 0;
        for (int i = 0; i < deadlockedProcess.length; i++) {
            sum = 0;
            for (int j = 0; j < m; j++) {
                sum += allocated[deadlockedProcess[i]][j];
            }
            if (max < sum) {
                max = sum;
                id = deadlockedProcess[i];
            }
        }
        return id;
    }


    public static void main(String[] args) {
        System.out.println("Enter the number of processes and the number of resources:");
        n = sc.nextInt();
        m = sc.nextInt();
        allocated = new int[n][m];
        need = new int[n][m];
        maxNeed = new int[n][m];
        requested = new int[n][m];
        available = new int[m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                requested[i][j] = 0;

        fillInitialRes();
        fillAlloc();
        fillMax();
        computeNeed();
        computeAvaRes();

        int[] req = new int[m];
        System.out.println("Test cases for banker");
        for (int i = 0; i < 3; i++) {
            System.out.println("Test case " + (i + 1));
            System.out.print("Process ID: ");
            int p = sc.nextInt();
            System.out.print("Requested Resources: ");
            for (int j = 0; j < m; j++) {
                req[j] = sc.nextInt();
            }
            banker(p, req);
            printInfo(need, true);
        }
        System.out.println("===========Test Cases Recover===========");
        System.out.println("choose to request, release, recover, or quit:");
        String in = "";
        while (!in.equals("Quit")) {
            in = input.nextLine();
            if (in.charAt(1) == 'Q') {
                //RQ <process#> <r1> <r2> <r3>
                int[] resources = new int[m];
                String[] splitinput = in.split(" ");
                int p = Integer.parseInt(splitinput[1]);
                for (int i = 0; i < m; i++) resources[i] = Integer.parseInt(splitinput[i + 2]);
                request(p, resources);
                printInfo(requested, false);
            } else if (in.charAt(1) == 'L') {
                //RL <process#> <r1> <r2> <r3>
                int[] resources = new int[m];
                String[] splitinput = in.split(" ");
                int p = Integer.parseInt(splitinput[1]);
                for (int i = 0; i < m; i++) resources[i] = Integer.parseInt(splitinput[i + 2]);
                release(p, resources);
                printInfo(requested, false);
            } else {
                recover();
                printInfo(requested, false);
            }
        }
    }
}
