package AllocationFrame;

import java.util.ArrayList;
import java.util.Random;

public class Process {
    private ArrayList<Page> pageList;
    private Random rand;
    private int numberOfPages;
    private int numberOfFrames;
    private int processNumber;
    private int actualNumberOfFrames;
    private int rangeOfPages;

    public Process(int numberOfPages, int rangeOfPages, int rangeOfArrivalTime, int processNumber){
        this.numberOfPages = numberOfPages;
        pageList = new ArrayList<>();
        rand = new Random();
        for(int i=0; i<numberOfPages; i++){
            pageList.add(new Page(rand.nextInt(rangeOfPages), rand.nextInt(rangeOfArrivalTime)));
        }
        this.processNumber = processNumber;
        numberOfFrames = 0;
        actualNumberOfFrames = 0;
    }

    public int getProcessNumber(){
        return processNumber;
    }
    public int getNumberOfFrames(){
        return numberOfFrames;
    }
    public int getRangeOfPages() {
    	return rangeOfPages;
    }
    public double numberOfFramesForPage(){
        return (double) numberOfFrames/numberOfPages;
    }

    public void setNumberOfFrames(int numberOfFrames){
        this.numberOfFrames = numberOfFrames;
        actualNumberOfFrames = numberOfFrames;
    }

    public void addFrames(int numberOfNewFrames){
        numberOfFrames = numberOfFrames + numberOfNewFrames;
        actualNumberOfFrames = actualNumberOfFrames + numberOfNewFrames;
    }

    public ArrayList<Page> getPageList(){
        return pageList;
    }

    public void setPageList(ArrayList<Page> newList){
        pageList = newList;
    }

    public int getNumberOfPages(){
        return numberOfPages;
    }

    private class Pair{
        private boolean contains;
        private int index;

        public Pair(boolean contains, int index){
            this.contains=contains;
            this.index=index;
        }

        public boolean getBoolean(){
            return contains;
        }

        public int getIndex(){
            return index;
        }

    }

    private  ArrayList<Page> frames = new ArrayList<>();

    private Pair contains(ArrayList<Page> list, int pageNumber){
        boolean Contains=false;
        int index = -1;
        ArrayList <Integer> pageNumbersList = new ArrayList<>();
        for(Page x: list){
            pageNumbersList.add(x.getPageNumber());
        }

        for(int i=0; i<pageNumbersList.size(); i++){
            if(pageNumbersList.get(i)==pageNumber){
                Contains = true;
                index = i;
                break;
            }
        }
        return new Pair(Contains, index);
    }

    public int getActualNumberOfFrames(){
        return actualNumberOfFrames;
    }
    private int faults = 0;

    public void addFaults(int newMistakes){
        faults = faults+newMistakes;
    }
    public int getFaults(){
        return faults;
    }

    private int freeeFrames;

    public int getFreeFrames(){
        return freeeFrames;
    }

    private boolean isStopped = false;
    
    public void isStopped(boolean isStopped) {
    	this.isStopped = isStopped;
    }

    public boolean getIsStopped(){
        return isStopped;
    }

    public int controlMistakes(int numberOfFrames, int timePart, double min, double max, int freeFrames) {  // 1 done Page = 1 slice of time Part
        int mistakes = 0;
        int notUsedTime;
        int indexOfLongestNotUsedTime;
        int pagesDone = 0;
        int mistakesDuringTimePart = 0;
        actualNumberOfFrames = numberOfFrames;
        freeeFrames = freeFrames;

        while (!pageList.isEmpty()) {
            if(pagesDone==timePart){
                if((double) mistakes/timePart < min && numberOfFrames>1 ) {
                    numberOfFrames--;
                    actualNumberOfFrames--;
                    if(frames.size()>0) frames.remove(frames.size()-1);
                    freeeFrames++;
                    break;
                }
                else if((double) mistakes/timePart > max){
                    if(freeFrames>0) {
                        numberOfFrames++;
                        actualNumberOfFrames++;
                        freeeFrames--;
                    }
                    else{
                        isStopped=true;
                        pageList.clear();
                    }
                    break;
                }
                break;
            }

            notUsedTime = -1;
            indexOfLongestNotUsedTime=-1;

            Pair isAlreadyInFrames = contains(frames, pageList.get(0).getPageNumber());

            if(isAlreadyInFrames.getBoolean() == true){
                frames.get(isAlreadyInFrames.getIndex()).setNotUsedTime(0);
            }

            else if (frames.size() < numberOfFrames) {
                if (isAlreadyInFrames.getBoolean() == false) {
                    frames.add(pageList.get(0));
                    mistakes++;
                    mistakesDuringTimePart++;
                }
            }
            else {
                if(isAlreadyInFrames.getBoolean() == false) {
                    for(int i=0; i<frames.size(); i++) {
                        if (frames.get(i).getNotUsedTime() > notUsedTime){
                            notUsedTime = frames.get(i).getNotUsedTime();
                            indexOfLongestNotUsedTime = i;
                        }
                    }
                    frames.remove(indexOfLongestNotUsedTime);
                    frames.add(pageList.get(0));
                    mistakes++;
                    mistakesDuringTimePart++;
                }
            }
            for(int i=0; i<frames.size(); i++){
                frames.get(i).increaseNotUsedTime();
            }
            pageList.remove(0);
            pagesDone++;
        }
        return mistakes;
    }
}
