package AllocationFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SteeringPageFaults {
	
	private ArrayList<Process> processList;
    private int numberOfProcesses;
    private int numberOfAllPages;
    private Comparator <Page> pageComparatorArrival = new PageComparatorArrival();
    private int thrashing;
    private int windowSize;
    private int PPF;
    private int totalPageFaults;
    private int freeFrames;
    private int u;
    private int I;
    private int doneProcesses;
    private int processStopped;
    private int WindowSize = 20;
    int minIndex = 0;
    int maxIndex = 0;
    int localPageFaults;

    public SteeringPageFaults(ArrayList<Process> processList, int number){
        this.processList = new ArrayList<> (processList);
        numberOfAllPages = 0;
        for(Process process: processList){
            Collections.sort(process.getPageList(), new PageComparatorArrival());
            process.setNumberOfFrames(0);
            for(Page page: process.getPageList()){
                numberOfAllPages ++;
                page.setNotUsedTime(0);
            }
        }
        numberOfProcesses = processList.size();
        windowSize = 10;
        PPF = 0;
        totalPageFaults = 0;
        freeFrames = 0;
        u = (int) 0.6 * windowSize;
        I = (int) 0.2 * windowSize;
        doneProcesses = 0;
        processStopped = 0;
    }
 
    public int run(int numberOfFrames) {
        System.out.print("\n      STEERING PAGE FAULTS FREQUENTLY: \n");
        if(numberOfFrames < numberOfProcesses)
            System.out.println("Theres is no frames");
        // giving frames
        for (int i = 0; i < numberOfProcesses; i++) {
        	int howManyPages = processList.get(i).getPageList().size() * numberOfFrames / numberOfAllPages;
        	processList.get(i).setNumberOfFrames(howManyPages);
        }
        
        for (int i = 0; i < processList.size(); i++) {
        	for (int j = 0; j < processList.get(i).getPageList().size(); j++) {
        		Collections.sort(processList.get(i).getPageList(), pageComparatorArrival);
        	}
        }
        ArrayList<Page> localPageFaultsForProcessInWindow = new ArrayList<Page>();
        
        while (doneProcesses != numberOfProcesses) {
        	for (int i = 0; i < processList.size(); i++) {
        		if (!processList.get(i).getIsStopped()) {
        			if (processList.get(i).getPageList().size() > windowSize) {
        				for (int j = 0; j < windowSize; j++) {
                			localPageFaultsForProcessInWindow.add(processList.get(i).getPageList().get(j));
        				}
        				LRU newLRU = new LRU(localPageFaultsForProcessInWindow);
        				localPageFaults = newLRU.run2(localPageFaultsForProcessInWindow, processList.get(i).getNumberOfFrames());
        				totalPageFaults += localPageFaults;
        				//System.out.println(totalPageFaults);
        				if (localPageFaults >= 0.5 * WindowSize) {
        					thrashing++;
        				}
        				PPF = (int) localPageFaults / windowSize;
        				if (PPF > I) {
        					if (freeFrames > 0) {
        					//System.out.println("The process number: " + processList.get(i).getProcessNumber() + " increase the upwards border.");
        					for (int x = 0; x < processList.size(); x++) {
        						if (!processList.get(x).getIsStopped() && processList.get(x).getNumberOfFrames() < processList.get(minIndex).getNumberOfFrames() && !processList.get(minIndex).getIsStopped()) {
        							if (processList.get(minIndex).getNumberOfFrames() > 1) {
        								minIndex = x;
        							}
        						} 
        					}
        					if (minIndex != i) {
        						if (processList.get(minIndex) != null) {
        						processList.get(i).setNumberOfFrames(processList.get(i).getNumberOfFrames() + 1 + freeFrames);
        						processList.get(minIndex).setNumberOfFrames(processList.get(minIndex).getNumberOfFrames() - 1);
        						}
        					}
        					} else {
        						processList.get(i).isStopped(true);
        						processStopped++;
        						doneProcesses++;
        						System.out.println("The process number: " + processList.get(i).getProcessNumber() + " has been stopped. Total number of stopped process: " + processStopped);
        						freeFrames += processList.get(i).getNumberOfFrames();
        						continue;
        					}
        				}
        				if (PPF < u) {
        					System.out.println("The process number: " + processList.get(i).getProcessNumber() + " increase the downwards border.");
        					for (int x = 0; x < processList.size() - 1; x++) {
        						if (!processList.get(x).getIsStopped() && processList.get(x).getNumberOfFrames() > processList.get(maxIndex).getNumberOfFrames() && !processList.get(maxIndex).getIsStopped()) {
        							if (processList.get(x).getNumberOfFrames() > 1) {
        								maxIndex = x;
        							}
        						}
        					}
        					if (maxIndex != i) {
        						processList.get(i).setNumberOfFrames(processList.get(i).getNumberOfFrames() - 1);
        						freeFrames += 1;
        					}
        				}
        				for (int j = 0; j < windowSize ; j++) {
        						processList.get(i).getPageList().remove(0);
        				}
        				localPageFaultsForProcessInWindow.clear();
        			} else if (processList.get(i).getPageList().size() <= windowSize) {
        				LRU newLRU = new LRU(processList.get(i).getPageList());
        				localPageFaults = newLRU.run2(processList.get(i).getPageList() ,processList.get(i).getNumberOfFrames());
        				totalPageFaults += localPageFaults;
        				if (localPageFaults >= 0.5 * windowSize) {
        					thrashing++;
        				}
        				System.out.println("The process number: " + processList.get(i).getProcessNumber() + " has been done");
        				doneProcesses++;
        				freeFrames += processList.get(i).getNumberOfFrames();
        				processList.get(i).isStopped(true);;
        			} 
        		}
        	}
        }
        	
        System.out.println("\nTotal page faults: " + totalPageFaults + " Total number of stopped process: " + processStopped);
        System.out.println("Total number of thrashing: " + thrashing);
        return totalPageFaults;
    }

}
