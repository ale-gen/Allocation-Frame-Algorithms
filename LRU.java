package AllocationFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class LRU {
    private ArrayList<Page> list;

    public LRU(ArrayList <Page> List){
        list = new ArrayList<>(List);
        for(Page x: list){
            x.setNotUsedTime(0);
        }
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

    private Pair contains(ArrayList<Page> list, int pageNumber){
        boolean contains=false;
        int index = -1;
        ArrayList <Integer> pageNumbersList = new ArrayList<>();
        for(Page x: list){
            pageNumbersList.add(x.getPageNumber());
        }

        for(int i=0; i<pageNumbersList.size(); i++){
            if(pageNumbersList.get(i)==pageNumber){
                contains = true;
                index = i;
                break;
            }
        }
        return new Pair(contains, index);
    }

    public int run(int numberOfFrames) {
        int mistakes = 0;
        int notUsedTime;
        int indexOfLongestNotUsedTime;

        ArrayList<Page> pageList = new ArrayList<>(list);
        ArrayList<Page> frames = new ArrayList<>();

        while (!pageList.isEmpty()) {
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
                }
            }
            for(int i=0; i<frames.size(); i++){
                frames.get(i).increaseNotUsedTime();
            }
            pageList.remove(0);
        }
        return mistakes;
    }
    
    public int run2(ArrayList<Page> list, int frameNumber) {
		
		int pageFaults = 0;
		
		ArrayList<Page> list1 = new ArrayList<Page>(list.size());
		HashSet<Integer> frame = new HashSet<Integer>(); 
		HashMap<Integer, Integer> indexes = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < list.size(); i++) {
			list1.add(list.get(i));
		}
		
		for (int i = 0; i < list.size(); i++) {
			
			if (frame.size() < frameNumber) {
				if (!frame.contains(list1.get(i).getPageNumber())) {
					frame.add(list1.get(i).getPageNumber());
					//System.out.println("Wolne miejsce: dotychczasowy rozmiar: " + frame.size());
					//System.out.println("Strona: " + list1.get(i).getPageNumber());
					pageFaults++;
				}
				indexes.put(list1.get(i).getPageNumber(), i);
				//System.out.println(indexes.toString());
			} else {
				if (!frame.contains(list1.get(i).getPageNumber())) {
					int lru = Integer.MAX_VALUE;
					int val = Integer.MIN_VALUE;
					
					Iterator<Integer> itr = frame.iterator();
					
					while (itr.hasNext()) {
						int temp = itr.next();
						if (indexes.get(temp) < lru) {
							lru = indexes.get(temp);
							val = temp;
						}
					}
					frame.remove(val);
					indexes.remove(val);
					frame.add(list1.get(i).getPageNumber());
					pageFaults++;
				}
				indexes.put(list1.get(i).getPageNumber(), i);
			}
		}
		return pageFaults;
	}
    
}

