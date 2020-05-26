package AllocationFrame;

import java.util.Comparator;

public class ComparatorProcessNumber implements Comparator<Process> {
    public int compare(Process o1, Process o2) {
        if (o1.getProcessNumber() > o2.getProcessNumber()) return 1;
        if (o1.getProcessNumber() < o2.getProcessNumber()) return -1;
        return 0;
    }
}


