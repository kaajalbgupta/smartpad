package com.example.smartpad.objects;

import java.util.LinkedHashMap;

public class HourlyInstance {

    private Color color;
    private int heaviness, clotting, saturation, padsChanged;
    private long hourTime;

    public HourlyInstance(Color color, int heaviness, int clotting, int saturation, long hourTime, int padsChanged){
        this.color=color;
        this.heaviness=heaviness;
        this.clotting=clotting;
        this.saturation=saturation;
        this.hourTime=hourTime;
        this.padsChanged=padsChanged;
    }

    public int getPadsChanged() {
        return padsChanged;
    }

    public void setPadsChanged(int padsChanged) {
        this.padsChanged = padsChanged;
    }

    public long getHourTime() {
        return hourTime;
    }

    public void setHourTime(long hourTime) {
        this.hourTime = hourTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getHeaviness() {
        return heaviness;
    }

    public void setHeaviness(int heaviness) {
        this.heaviness = heaviness;
    }

    public int getClotting() {
        return clotting;
    }

    public void setClotting(int clotting) {
        this.clotting = clotting;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
