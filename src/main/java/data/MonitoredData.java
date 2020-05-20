package data;

public class MonitoredData {
    private String startTime;
    private String endTime;
    private String activityLabel;

    public MonitoredData(String startTime, String endTime, String activityLabel) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityLabel = activityLabel;
    }
    public String toString(){
        return this.startTime+"   "+ endTime+"   "+ activityLabel;
    }
    public String getTime(){
        return this.startTime.substring(0,10);
    }
    public String getActivityLabel(){
        return this.activityLabel;
    }
    public Integer getDay(){
        return new Integer(this.startTime.substring(8,10));
    }
    public String getStartTime(){
        return startTime;
    }
    public String getEndTime(){
        return endTime;
    }
}
