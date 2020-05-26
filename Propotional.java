package AllocationFrame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Propotional {
    private ArrayList<Process> processList;
    private int numberOfProcesses;
    private int numberOfAllPages;
    private Comparator <Process> comparatorFramesPerPage = new ComparatorFramesPerPage();
    private Comparator <Process> comparatorProcessNumber = new ComparatorProcessNumber();
    private int windowSize;
    private int thrashing;

    public Propotional(ArrayList <Process> processList){
        this.processList = new ArrayList<> (processList);
        numberOfAllPages = 0;
        for(Process process: processList){
            Collections.sort(process.getPageList(), new PageComparatorArrival());
            process.setNumberOfFrames(0);
            for(Page page: process.getPageList()){
                numberOfAllPages ++;
            }
        }
        numberOfProcesses = processList.size();
        windowSize = 10;
    }

    public int run(int numberOfFrames) {
        System.out.print("\n      PROPORTIONAL: ");
        if(numberOfFrames < numberOfProcesses)
            System.out.println("Theres is no frames");
        
        int remainingFrames = numberOfFrames - numberOfProcesses; 
                                                                  
        for(Process process: processList) {
            process.addFrames(1); 
        }

        while(remainingFrames>0){
            Collections.sort(processList, comparatorFramesPerPage);  
            processList.get(0).addFrames(1);
            remainingFrames--;
        }

        Collections.sort(processList, comparatorProcessNumber);

        int totalPageFaults = 0;
        for(int i=0; i<processList.size(); i++){
            LRU newLRU = new LRU(processList.get(i).getPageList());

            int pageFaults = newLRU.run(processList.get(i).getNumberOfFrames());
            totalPageFaults +=pageFaults;
            System.out.print("\nProcess number: " +(i+1)+ " number Of pages: "+ processList.get(i).getPageList().size()+" -> page faults: "+pageFaults +
                    " (number of frames: "+ processList.get(i).getNumberOfFrames() +")");
        }
        /*if(numberOfFrames < numberOfProcesses){
            System.out.println("There is no enough frames");
         }
        for (int i = 0; i < numberOfProcesses; i++) {
        	int howManyPages = processList.get(i).getPageList().size() * numberOfFrames / numberOfAllPages;
        	processList.get(i).setNumberOfFrames(howManyPages);
        }
         int totalPageFaults = 0;
         for(int i=0; i<processList.size(); i++){
             LRU newLRU = new LRU(processList.get(i).getPageList());
             int pageFaults = newLRU.run(processList.get(i).getNumberOfFrames());
             totalPageFaults +=pageFaults;
             System.out.print("\nProcess number: " +(i+1)+  " number Of pages: " + processList.get(i).getPageList().size()  + " -> page faults: "+pageFaults +
                     " (number of frames: "+ processList.get(i).getNumberOfFrames()+")");
         }*/
        ArrayList<Page> thrashingResearch = new ArrayList<Page>();
        
        for (int i = 0; i < processList.size(); i++) {
        	for (int j = 0; j < processList.get(i).getPageList().size(); j++) {
        		thrashingResearch.add(processList.get(i).getPageList().get(j));
        		if (j % windowSize == 0 && j > 0) {
        			 LRU newLRU = new LRU(thrashingResearch);
        			int localPageFaults = newLRU.run(processList.get(i).getNumberOfFrames());
        			if (localPageFaults >= 0.5 * windowSize) {
        				thrashing++;
        			}
        			thrashingResearch.clear();
        		}
        	}
        }
        System.out.println("\nTotal page faults: " + totalPageFaults);
        System.out.println("Total number of thrashing: " + thrashing);
        return totalPageFaults;
    }
}
