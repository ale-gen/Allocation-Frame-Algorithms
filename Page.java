package AllocationFrame;

public class Page {
    private int pageNumber;
    private int arrivalTime;
    private int notUsedTime;

    public Page(int pageNumber, int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.pageNumber = pageNumber;
        notUsedTime = 0;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getNotUsedTime(){
        return notUsedTime;
    }

    public void setNotUsedTime(int notUsedTime){
        this.notUsedTime = notUsedTime;
    }

    public void increaseNotUsedTime(){
        notUsedTime++;
    }

    public String toString(){
        return "Page number: " + pageNumber + "; arrival time: "+ arrivalTime;
    }

}
