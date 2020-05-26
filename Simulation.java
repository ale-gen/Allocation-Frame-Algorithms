package AllocationFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulation {
    private ArrayList<Process> processList;
    private ArrayList<Process> processList2;
    private Random random;

    public Simulation(int numberOfProcesses) {
        processList = new ArrayList<>();
        processList2 = new ArrayList<>();
        random = new Random();
        for (int i = 0; i < numberOfProcesses; i++) {
            int numberOfPages = random.nextInt(20 * numberOfProcesses) + 8;
            int rangeOfPages = numberOfPages/3 + 5;
            processList.add(new Process(numberOfPages, rangeOfPages, 10, (i+1)));
            processList2.add(new Process(numberOfPages, rangeOfPages, 10, (i+1)));
            Collections.sort(processList.get(i).getPageList(), new PageComparatorArrival());
        }
    }

    public void print() {
        for (int i=0; i<processList.size(); i++) {
            ArrayList<Page> pageList = processList.get(i).getPageList();
            System.out.println("Process "+ (i+1) +" : ");
            for (Page y : pageList) {
               System.out.println(y.toString());
            }
        }
    }

    public static void main(String[] args)  {
        Simulation simulation = new Simulation(10);
        simulation.print();
        Equal equal = new Equal(simulation.processList);
        equal.run(50);
        Propotional proportional = new Propotional(simulation.processList);
        proportional.run(50);
        ZoneModel zoneModel = new ZoneModel(simulation.processList);
        int number = zoneModel.run(50);
        SteeringPageFaults steeringPageFaults = new SteeringPageFaults(simulation.processList2, number);
        steeringPageFaults.run(50);
    }
}
