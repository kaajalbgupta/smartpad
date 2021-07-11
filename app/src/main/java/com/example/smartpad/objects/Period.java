package com.example.smartpad.objects;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Period {

  private long startTime, endTime;
  private LinkedHashMap<String, PeriodDay> days;

  public Period(long startTime, LinkedHashMap<String, PeriodDay> days, long endTime){
    this.startTime=startTime;
    this.endTime=endTime;
    this.days=days;
  }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public LinkedHashMap<String, PeriodDay> getDays() {
        return days;
    }

    public void setDays(LinkedHashMap<String, PeriodDay> days) {
        this.days = days;
    }

    public int getAverageHeaviness(){
        int heaviness=0;
        ArrayList<PeriodDay> periodDays=new ArrayList<>(this.days.values());
        for (PeriodDay periodDay: periodDays){
            heaviness+=periodDay.getAverageHeaviness();
        }
        heaviness=heaviness/periodDays.size();
        return heaviness;
    }

    public int getAverageClotting(){
      int clotting=0;
        ArrayList<PeriodDay> periodDays=new ArrayList<>(this.days.values());
        for (PeriodDay periodDay: periodDays){
            clotting+=periodDay.getAverageClotting();
        }
        clotting=clotting/periodDays.size();
        Log.v("Period: ", String.valueOf(clotting));
        return clotting;
    }

    public Color getAverageColorRGB(){
        int red=0;
        int green=0;
        int blue=0;
        ArrayList<PeriodDay> periodDays=new ArrayList<>(this.days.values());
        for (PeriodDay periodDay: periodDays){
            red+=periodDay.getAverageColorRGB().getRed();
            green+=periodDay.getAverageColorRGB().getGreen();
            blue+=periodDay.getAverageColorRGB().getBlue();
        }
        red=red/this.days.size();
        green=green/this.days.size();
        blue=blue/this.days.size();
        return new Color(red, green, blue);
    }
}
