package AllocationFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class ZoneModel {
	
	private ArrayList<Process> processList;
    private int numberOfProcesses;
    private int numberOfAllPages;
    private int numberOfActualNeededFrames;
    private Comparator<Page> pageComparatorArrival = new PageComparatorArrival();
    private int thrashing;
    private int windowSize;
    private int WSS;
    private int freeFrames;
    private int totalPageFaults;
    private int numberOfStoppedProcess = 0;
    private int allDone = -1;

    public ZoneModel(ArrayList<Process> processList){
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
        WSS = 0;
        totalPageFaults = 0;
    }
    
    public int run(int numberOfFrames) {
    	System.out.print("\n      ZONE MODEL: \n");
    	freeFrames = numberOfFrames;
        if(numberOfFrames < numberOfProcesses)
            System.out.println("Theres is no frames");
        // giving frames
    	
        for (int i = 0; i < numberOfProcesses; i++) {
        	int howManyPages = processList.get(i).getPageList().size() * numberOfFrames / numberOfAllPages;
        	processList.get(i).setNumberOfFrames(howManyPages);
        }
        
        ArrayList<Page> WSSResearch = new ArrayList<Page>();
        
        for (int i = 0; i < processList.size(); i++) {
        	for (int j = 0; j < processList.get(i).getPageList().size(); j++) {
        		Collections.sort(processList.get(i).getPageList(), pageComparatorArrival);
        	}
        }
        
        int minIndex = 0;
        for (int i = 0; i < processList.size();i++) {
        	WSS = numberOfDuplications(processList.get(i).getPageList(), windowSize);
        	processList.get(i).setNumberOfFrames(WSS);
        	System.out.print("\nProcess number: " +(i+1)+ " number Of pages: "+ processList.get(i).getPageList().size()+
                    " (number of frames: "+ processList.get(i).getNumberOfFrames() +")");
        }
        System.out.println();
        do {
        	for (int i = allDone + 1; i < processList.size(); i++) {
        		if (freeFrames > processList.get(i).getNumberOfFrames() && !processList.get(i).getIsStopped()) {
        			allDone++;
        			numberOfActualNeededFrames = processList.get(i).getNumberOfFrames();
        			freeFrames -= numberOfActualNeededFrames;
        			if (processList.get(i).getNumberOfFrames() != 0) {
        				LRU newLRU = new LRU(processList.get(i).getPageList());
        				int localPageFaults = newLRU.run(processList.get(i).getNumberOfFrames());
        				totalPageFaults += localPageFaults;
        				if (localPageFaults >= 0.5 * windowSize) {
        					thrashing++;
        				}
        			}
        		} else if (!processList.get(i).getIsStopped()) {
        			for (Process p : processList) {
        				if (!p.getIsStopped() && !processList.get(minIndex).getIsStopped() && p.getNumberOfFrames() < processList.get(minIndex).getNumberOfFrames()) {
        					minIndex = p.getProcessNumber() - 1;
        				} else if (!p.getIsStopped() && processList.get(minIndex).getIsStopped()) {
        					minIndex = p.getProcessNumber() - 1;
        				}
        			}
        			allDone++;
        			//processList.get(i).isStopped(true);
        			processList.get(minIndex).isStopped(true);
        			numberOfStoppedProcess++;
        			System.out.println("The process number: " + processList.get(minIndex).getProcessNumber() + " has been stopped." + 
        			"Total number of stopped processes: " + numberOfStoppedProcess);
        		}
        	}
        	freeFrames = numberOfFrames;
        } while (allDone != numberOfProcesses -1);
        
        System.out.println("\nTotal page faults: " + totalPageFaults);
        System.out.println("Total number of thrashing: " + thrashing);
        return totalPageFaults;
    }
    
    public int numberOfDuplications(ArrayList<Page> list, int windowSize) {
    	HashSet<Integer> h = new HashSet();
    	
    	if (windowSize > list.size()) {
    		windowSize = list.size();
    	}
    	
    	for (int i = 0; i < windowSize; i++) {
    		h.add(list.get(i).getPageNumber());
    	}
    	return h.size();
    }

}
