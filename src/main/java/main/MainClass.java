package main;

import business.TaskManager;

import java.io.IOException;
import java.text.ParseException;

public class MainClass {
    public static void main(String []args){

        TaskManager taskManager=new TaskManager();
        taskManager.readFromFile();
        taskManager.countDistinctDays();
        taskManager.activityFrequency();
        taskManager.dailyActivityFrequency();
        try {
            taskManager.totalActivityDuration();
            taskManager.filter();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
