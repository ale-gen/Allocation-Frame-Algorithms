package AllocationFrame;

import java.util.Comparator;

public class PageComparatorArrival implements Comparator <Page> {

    public int compare(Page o1, Page o2) {
        if(o1.getArrivalTime()>o2.getArrivalTime()) return 1;
        if(o1.getArrivalTime()<o2.getArrivalTime()) return -1;
        return 0;
    }


}
