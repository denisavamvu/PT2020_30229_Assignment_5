package business;

import data.MonitoredData;

import java.io.*;
import java.nio.file.Files;
import java.text.*;
import java.util.*;
import java.util.stream.*;
import presentation.FileWriter;

public class TaskManager {
    private FileWriter fileWriter=new FileWriter();;
    private ArrayList<MonitoredData> data=new ArrayList<MonitoredData>();
    private ArrayList<String> distinctActivities=new ArrayList<String>();
    private ArrayList<Integer> distinctDates;

    public TaskManager(){
    }
    public void readFromFile(){
        String toWrite=new String();
        String fileName="./activities.txt";
        File file=new File(fileName);

        try (Stream<String> linesStream = Files.lines(file.toPath())) {
            linesStream.forEach(line-> data.add(new MonitoredData(line.substring(0,19),line.substring(21,40),line.substring(42))));
        } catch (IOException e) {
            e.printStackTrace();
        } ;
        for(MonitoredData monitoredData: data){
           toWrite+=monitoredData.toString()+"\n";
        }
        fileWriter.write("Task_1.txt",toWrite);
    }
    public void countDistinctDays(){
        ArrayList<String> dateList = new ArrayList<>();

        data.stream().forEach(d -> dateList.add(d.getTime()));
        int numberOfDays = (int) dateList.stream().distinct().count();

        String toWrite= Integer.toString(numberOfDays);
        fileWriter.write("Task_2.txt",toWrite);
    }

    public HashMap<String,Integer> activityFrequency(){
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        ArrayList<String> activities = new ArrayList<>();
        data.stream().forEach(d -> activities.add(d.getActivityLabel()));

        distinctActivities= (ArrayList<String>) activities.stream().distinct().collect(Collectors.toList());
        distinctActivities.forEach(a -> map.put(a, (int)data.stream().filter(d-> d.getActivityLabel().equals(a)).count()));

        String toWrite= new String();
        for(String s: distinctActivities)
            toWrite+=s+"  -> "+map.get(s)+"\n";
        fileWriter.write("Task_3.txt",toWrite);
        return map;
    }

    public HashMap<Integer, HashMap<String, Integer>> dailyActivityFrequency(){
        HashMap<Integer, HashMap<String, Integer>> map = new HashMap<Integer, HashMap<String, Integer>>();

        ArrayList<Integer> dates=new ArrayList<Integer>();
        data.stream().forEach(d -> dates.add(d.getDay()));

        distinctDates=(ArrayList<Integer>) dates.stream().distinct().collect(Collectors.toList());

        for(Integer d: distinctDates){
            HashMap<String, Integer> activity= new HashMap<String, Integer>();
            for(String a: distinctActivities)
            {
                int c=0;
                for(MonitoredData m: data){
                    if(m.getDay().equals(d)&&m.getActivityLabel().equals(a))
                        c++;
                }
                activity.put(a,c);

            }
            map.put(d,activity);
        }

        String toWrite= new String();
        for(Map.Entry<Integer, HashMap<String, Integer>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            HashMap value = entry.getValue();
            toWrite+=key+" "+value+"\n";
        }
        fileWriter.write("Task_4.txt",toWrite);
        return map;
    }
    public HashMap<String, Integer> totalActivityDuration() throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Integer> activitiesTotalDuration = new HashMap<String, Integer>();

        for (String a : distinctActivities) {
            activitiesTotalDuration.put(a, 0);
        }

        distinctActivities.stream().forEach(a -> data.stream().filter(d -> d.getActivityLabel().equals(a)).forEach(d -> {
            try {
                activitiesTotalDuration.put(a, (int) (activitiesTotalDuration.get(a)+(format.parse(d.getEndTime()).getTime() / 1000 - format.parse(d.getStartTime()).getTime() / 1000)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }));
        String toWrite=new String();
        for(String a : distinctActivities)
        {
            toWrite+=a+" total duration: ";
            toWrite+= activitiesTotalDuration.get(a)/3600 + "h "+
                    activitiesTotalDuration.get(a)/60%60 + "min " +
                    activitiesTotalDuration.get(a)%60 + "s\n";
        }
        fileWriter.write("Task_5.txt",toWrite);
        return activitiesTotalDuration;
    }
    public ArrayList<String> filter() throws IOException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ArrayList<MonitoredData> shortActivities;
        ArrayList<String> result=new ArrayList<String>();

        HashMap<String, Integer> map = new HashMap<>();

        shortActivities = (ArrayList<MonitoredData>)data.stream().filter(
                a -> 	{
                    try {
                        return (format.parse(a.getEndTime()).getTime()/1000 - format.parse(a.getStartTime()).getTime()/1000)/60 % 60 < 5;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());

        distinctActivities.stream().forEach(
                a -> map.put(a, (int) shortActivities.stream().filter(activity -> activity.getActivityLabel().equals(a)).count()));

        String toWrite= new String();

        HashMap<String, Integer>frequencyList = activityFrequency();
        for(String s : distinctActivities)
        {
            float freq = (float)map.get(s) / frequencyList.get(s);
            if(freq > 0.9f)
            {
                result.add(s);
                toWrite+=s + "\n";
            }
        }
        fileWriter.write("Task_6.txt",toWrite);
        return result;
    }
    }
