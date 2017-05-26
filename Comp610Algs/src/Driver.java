// Author Eric Vance
// Comp 610
//Max2Stat Project

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {
        PrintWriter writer = null;
        try{
            writer = new PrintWriter("results.txt");
        } catch (FileNotFoundException fne) {
            System.out.println("File write error. Exiting program");
            System.exit(0);
        }
        boolean continueRunning;
        do {
            System.out.print("Please input the name of the file: ");
            Scanner input = new Scanner(System.in);  // Reading from System.in
            String fileName = input.next();

            System.out.println("Starting algorithms...");
            DriverThread d1 = new DriverThread(2, fileName);
            DriverThread d2 = new DriverThread(3, fileName);
            DriverThread d3 = new DriverThread(4, fileName);

            DriverThread[] driverThreads = {d1, d2, d3};

            Thread t1 = new Thread(d1);
            Thread t2 = new Thread(d2);
            Thread t3 = new Thread(d3);
            Thread[] threads = {t1, t2, t3};

            t1.start();
            t2.start();
            t3.start();


            for (int i = 0; i < threads.length; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            DriverThread maxDriverThread = driverThreads[0];
            for (DriverThread driverThread : driverThreads) {
                if (driverThread.getMax2Stat().getMaxTrues() > maxDriverThread.getMax2Stat().getMaxTrues())
                    maxDriverThread = driverThread;

            }
            System.out.println(maxDriverThread.getMax2Stat());

            writer.println("\n" + fileName + "results:\n" + maxDriverThread.getMax2Stat().toString());

            System.out.print("Enter 0 to exit and save results file or enter 1 to run with new test file: ");
            int cont = input.nextInt();
            if (cont == 0)
                continueRunning = false;
            else
                continueRunning = true;

        }while(continueRunning);
        writer.close();

    }

}
class DriverThread implements Runnable {

    private Max2Stat max2Stat;
    private int localSearchChoice;

    public DriverThread(int localSearchChoice, String fileName) {
        this.max2Stat = ReadTextFile.readClauseTextFile(fileName);
        this.localSearchChoice = localSearchChoice;
    }

    public void run() {
        //choose the algorithm
        switch (localSearchChoice){
            case 1:
                max2Stat.localSearchConstantBit();
                break;
            case 2:
                max2Stat.localSearchFlip();
                break;
            case 3:
                max2Stat.localSearchFlipRandom();
                break;
            case 4:
                max2Stat.localSearchRandomStartConstantBit();
                break;
            case 5:
                max2Stat.localSearchRandom();
                break;
            default:
                break;
        }

    }

    public Max2Stat getMax2Stat(){
        return max2Stat;
    }

}
