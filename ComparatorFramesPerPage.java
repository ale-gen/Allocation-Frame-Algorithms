package AllocationFrame;

import java.util.Comparator;

public class ComparatorFramesPerPage implements Comparator<Process> {
    public int compare(Process o1, Process o2) {
        if (o1.numberOfFramesForPage() > o2.numberOfFramesForPage()) return 1;
        if (o1.numberOfFramesForPage() < o2.numberOfFramesForPage()) return -1;
        return 0;
    }
}

