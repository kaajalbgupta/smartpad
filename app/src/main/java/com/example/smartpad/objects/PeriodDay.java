package com.example.smartpad.objects;

import android.util.Log;

import com.example.smartpad.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PeriodDay {

    private LinkedHashMap<String, HourlyInstance> hourlyInstances;
    private long dayTime;

    public PeriodDay(LinkedHashMap<String, HourlyInstance> hourlyInstances, long dayTime){
        this.hourlyInstances=hourlyInstances;
        this.dayTime=dayTime;
    }

    public LinkedHashMap<String, HourlyInstance> getHourlyInstances() {
        return hourlyInstances;
    }

    public void setHourlyInstances(LinkedHashMap<String, HourlyInstance> hourlyInstances) {
        this.hourlyInstances = hourlyInstances;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public String getAverageColor(){
        int red=0;
        int green=0;
        int blue=0;
        ArrayList<HourlyInstance> hourlyInstances=new ArrayList<>(this.hourlyInstances.values());
        for (HourlyInstance hourlyInstance: hourlyInstances){
            red+=hourlyInstance.getColor().getRed();
            green+=hourlyInstance.getColor().getGreen();
            blue+=hourlyInstance.getColor().getBlue();
        }
        red=red/this.hourlyInstances.size();
        green=green/this.hourlyInstances.size();
        blue=blue/this.hourlyInstances.size();
        return Utils.getHexFromRGB(red, green, blue);
    }

    public Color getAverageColorRGB(){
        int red=0;
        int green=0;
        int blue=0;
        ArrayList<HourlyInstance> hourlyInstances=new ArrayList<>(this.hourlyInstances.values());
        for (HourlyInstance hourlyInstance: hourlyInstances){
            red+=hourlyInstance.getColor().getRed();
            green+=hourlyInstance.getColor().getGreen();
            blue+=hourlyInstance.getColor().getBlue();
        }
        red=red/this.hourlyInstances.size();
        green=green/this.hourlyInstances.size();
        blue=blue/this.hourlyInstances.size();
        return new Color(red, green, blue);
    }

    public int getAverageHeaviness(){
        int heaviness=0;
        ArrayList<HourlyInstance> hourlyInstances=new ArrayList<>(this.hourlyInstances.values());
        for (HourlyInstance hourlyInstance: hourlyInstances){
            heaviness+=hourlyInstance.getHeaviness();
        }
        heaviness=heaviness/hourlyInstances.size();
        return heaviness;
    }

    public int getAverageClotting(){
        int clotting=0;
        ArrayList<HourlyInstance> hourlyInstances=new ArrayList<>(this.hourlyInstances.values());
        for (HourlyInstance hourlyInstance: hourlyInstances){
            clotting+=hourlyInstance.getClotting();
        }
        clotting=clotting/hourlyInstances.size();
        Log.v("Period day: ", String.valueOf(clotting));
        return clotting;
    }

}
