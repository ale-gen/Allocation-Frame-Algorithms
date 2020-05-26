package AllocationFrame;

import java.util.ArrayList;
import java.util.Collections;

public class Equal {
    private ArrayList<Process> processList;
    private int numberOfProcesses;
    private int thrashing;
    private int windowSize;

    public Equal(ArrayList <Process> processList){
        this.processList = new ArrayList<> (processList);
        for(int i=0; i<processList.size(); i++){
            Collections.sort(processList.get(i).getPageList(), new PageComparatorArrival());
        }
        numberOfProcesses = processList.size();
        windowSize = 10;
    }

    public int run(int numberOfFrames) {
        System.out.print("\n      EQUAL: ");
        if(numberOfFrames < numberOfProcesses){
           System.out.println("There is no enough frames");
        }
        int howManyFrames = numberOfFrames / numberOfProcesses;
        int totalPageFaults = 0;
        for(int i=0; i<processList.size(); i++){
            LRU newLRU = new LRU(processList.get(i).getPageList());
            int pageFaults = newLRU.run(howManyFrames);
            totalPageFaults +=pageFaults;
            System.out.print("\nProcess number: " +(i+1)+  " number Of pages: " + processList.get(i).getPageList().size()  + " -> page faults: "+pageFaults +
                    " (number of frames: "+ howManyFrames+")");
        }
        ArrayList<Page> thrashingResearch = new ArrayList<Page>();
        
        for (int i = 0; i < processList.size(); i++) {
        	for (int j = 0; j < processList.get(i).getPageList().size(); j++) {
        		thrashingResearch.add(processList.get(i).getPageList().get(j));
        		if (j % windowSize == 0 && j > 0) {
        			 LRU newLRU = new LRU(thrashingResearch);
        			int localPageFaults = newLRU.run(howManyFrames);
        			if (localPageFaults >= 0.5 * windowSize) {
        				thrashing++;
        			}
        			thrashingResearch.clear();
        		}
        	}
        }
        
        System.out.println("\nTotal page faults: " + totalPageFaults);
        System.out.println("Number of thrashing: " + thrashing);
        return totalPageFaults;
    }
}

