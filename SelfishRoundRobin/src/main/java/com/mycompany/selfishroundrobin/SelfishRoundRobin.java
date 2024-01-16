/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.selfishroundrobin;

/**
 *
 * @author User
 */
import java.util.LinkedList;
import java.util.Queue;

class Process {
    char name;
    int arrivalTime;
    int burstTime;
    int remainingTime;

    public Process(char name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class SelfishRoundRobinScheduler {
    Queue<Process> processQueue;
    int timeQuantum;

    public SelfishRoundRobinScheduler(int timeQuantum) {
        this.processQueue = new LinkedList<>();
        this.timeQuantum = timeQuantum;
    }

    public void addProcess(Process process) {
        processQueue.add(process);
    }

    public void runScheduler() {
        int time = 0;

        while (!processQueue.isEmpty()) {
            Process currentProcess = processQueue.poll();
            int runTime = Math.min(timeQuantum, currentProcess.remainingTime);

            // Simulate process execution
            System.out.println("Time " + time + ": Running process " + currentProcess.name +
                    " for " + runTime + " units of time.");

            currentProcess.remainingTime -= runTime;
            time += runTime;

            // Move to the back of the queue in a selfish manner
            processQueue.add(currentProcess);
        }
    }
}

public class SelfishRoundRobin {
    public static void main(String[] args) {
        SelfishRoundRobinScheduler scheduler = new SelfishRoundRobinScheduler(2);

        // Add processes to the scheduler
        Process p1 = new Process('1', 0, 5);
        Process p2 = new Process('2', 3, 5);
        Process p3 = new Process('3', 6, 5);

        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);

        // Run the scheduler
        scheduler.runScheduler();
    }
}
