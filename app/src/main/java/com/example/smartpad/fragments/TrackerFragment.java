package com.example.smartpad.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartpad.R;
import com.example.smartpad.objects.Period;
import com.example.smartpad.objects.PeriodDay;
import com.example.smartpad.objects.User;
import com.example.smartpad.utils.Utils;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TrackerFragment extends Fragment {

    private User user;

    public void setUser(User user){
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.tracker_fragment,container,false);

        CustomCalendarView calendarView=rootView.findViewById(R.id.calendar_view_tracker);
        Date date=new Date(System.currentTimeMillis());
        calendarView.markDayAsSelectedDay(date);

        final TextView dateView=rootView.findViewById(R.id.date);
        final TextView periodView=rootView.findViewById(R.id.period);
        final TextView colorView=rootView.findViewById(R.id.color);
        final TextView heavinessView=rootView.findViewById(R.id.heaviness);

        String month =(String) DateFormat.format("MMM",  date);
        String day = (String) DateFormat.format("dd",   date);
        String year = (String) DateFormat.format("yyyy", date);
        dateView.setText(month+" "+day+" "+year);

        long time=date.getTime();
        ArrayList<Period> periods=new ArrayList<>(user.getPeriods().values());
        long minimumDifference=-1;
        long closestPeriodTime=-1;
        for (Period period:periods){
            long difference=time-period.getStartTime();
            if(minimumDifference!=-1){
                if(difference<minimumDifference){
                    minimumDifference=difference;
                    closestPeriodTime=period.getStartTime();
                }
            }else{
                minimumDifference=difference;
                closestPeriodTime=period.getStartTime();
            }
        }
        Log.v("minimum difference", String.valueOf(minimumDifference));
        Log.v("closest time", String.valueOf(closestPeriodTime));
        time= Utils.getDateMillisFromCurrentMillis(time)-18000000;
        Log.v("time", String.valueOf(time));
        if(minimumDifference!=-1){
            Period period=user.getPeriods().get(String.valueOf(closestPeriodTime));
            if(period.getDays().containsKey(String.valueOf(time))){
                ArrayList<PeriodDay> periodDays=new ArrayList<>(period.getDays().values());
                Collections.sort(periodDays, new Comparator<PeriodDay>() {
                    @Override
                    public int compare(PeriodDay o1, PeriodDay o2) {
                        return Long.compare(o1.getDayTime(),o2.getDayTime());
                    }
                });
                int dayNumber=-1;
                for (int i=0;i<periodDays.size(); i++){
                    PeriodDay periodDay=periodDays.get(i);
                    if(periodDay.getDayTime()==time){
                        dayNumber=i+1;
                    }
                }
                periodView.setText("Day "+dayNumber+" of Period");
                PeriodDay periodDay=period.getDays().get(String.valueOf(time));
                final String colorText;
                if(120<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=210 &&
                        0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=165 &&
                        0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=63) {
                    colorText = "BROWN";
                }else if(0<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=50 &&
                        0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=50 &&
                        0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=50){
                    colorText = "BLACK";
                }else if(90<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=200 &&
                        0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=20 &&
                        0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=40){
                    colorText="DARK RED";
                }else if(200<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                        0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=30 &&
                        0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=30){
                    colorText="BRIGHT RED";
                }else if(180<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                        50<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=120 &&
                        0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=35){
                    colorText="ORANGE";
                } else if(199<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                        20<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=192 &&
                        133<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=203){
                    colorText="PINK";
                } else{
                    colorText="RED";
                }
                colorView.setText("Average Color: "+colorText);
                final String heavyText;
                if (periodDay.getAverageHeaviness() > 80) {
                    heavyText="VERY HEAVY";
                } else if (periodDay.getAverageHeaviness() > 60) {
                    heavyText="HEAVY";
                } else if (periodDay.getAverageHeaviness() > 40) {
                    heavyText="MEDIUM";
                } else if (periodDay.getAverageHeaviness() > 20) {
                    heavyText="LIGHT";
                } else {
                    heavyText="VERY LIGHT";
                }
                heavinessView.setText("Average Heaviness: "+heavyText);
            }else{
                periodView.setText("No Period");
                colorView.setText("");
                heavinessView.setText("");
            }
        }else{
            periodView.setText("No Period");
            colorView.setText("");
            heavinessView.setText("");
        }

        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                String month =(String) DateFormat.format("MMM",  date);
                String day = (String) DateFormat.format("dd",   date);
                String year = (String) DateFormat.format("yyyy", date);
                dateView.setText(month+" "+day+" "+year);

                long time=date.getTime();
                ArrayList<Period> periods=new ArrayList<>(user.getPeriods().values());
                long minimumDifference=-1;
                long closestPeriodTime=-1;
                for (Period period:periods){
                    long difference=time-period.getStartTime();
                    if(minimumDifference!=-1){
                        if(difference<minimumDifference){
                            minimumDifference=difference;
                            closestPeriodTime=period.getStartTime();
                        }
                    }else{
                        minimumDifference=difference;
                        closestPeriodTime=period.getStartTime();
                    }
                }
                Log.v("minimum difference", String.valueOf(minimumDifference));
                Log.v("closest time", String.valueOf(closestPeriodTime));
                time= Utils.getDateMillisFromCurrentMillis(time)-18000000;
                Log.v("time", String.valueOf(time));
                if(minimumDifference!=-1){
                    Period period=user.getPeriods().get(String.valueOf(closestPeriodTime));
                    if(period.getDays().containsKey(String.valueOf(time))){
                        ArrayList<PeriodDay> periodDays=new ArrayList<>(period.getDays().values());
                        Collections.sort(periodDays, new Comparator<PeriodDay>() {
                            @Override
                            public int compare(PeriodDay o1, PeriodDay o2) {
                                return Long.compare(o1.getDayTime(),o2.getDayTime());
                            }
                        });
                        int dayNumber=-1;
                        for (int i=0;i<periodDays.size(); i++){
                            PeriodDay periodDay=periodDays.get(i);
                            if(periodDay.getDayTime()==time){
                                dayNumber=i+1;
                            }
                        }
                        periodView.setText("Day "+dayNumber+" of Period");
                        PeriodDay periodDay=period.getDays().get(String.valueOf(time));
                        final String colorText;
                        if(120<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=210 &&
                                0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=165 &&
                                0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=63) {
                            colorText = "BROWN";
                        }else if(0<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=50 &&
                                0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=50 &&
                                0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=50){
                            colorText = "BLACK";
                        }else if(90<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=200 &&
                                0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=20 &&
                                0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=40){
                            colorText="DARK RED";
                        }else if(200<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                                0<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=30 &&
                                0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=30){
                            colorText="BRIGHT RED";
                        }else if(180<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                                50<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=120 &&
                                0<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=35){
                            colorText="ORANGE";
                        } else if(199<=periodDay.getAverageColorRGB().getRed() && periodDay.getAverageColorRGB().getRed()<=255 &&
                                20<=periodDay.getAverageColorRGB().getGreen() && periodDay.getAverageColorRGB().getGreen()<=192 &&
                                133<=periodDay.getAverageColorRGB().getBlue() && periodDay.getAverageColorRGB().getBlue()<=203){
                            colorText="PINK";
                        } else{
                            colorText="RED";
                        }
                        colorView.setText("Average Color: "+colorText);
                        final String heavyText;
                        if (periodDay.getAverageHeaviness() > 80) {
                            heavyText="VERY HEAVY";
                        } else if (periodDay.getAverageHeaviness() > 60) {
                            heavyText="HEAVY";
                        } else if (periodDay.getAverageHeaviness() > 40) {
                            heavyText="MEDIUM";
                        } else if (periodDay.getAverageHeaviness() > 20) {
                            heavyText="LIGHT";
                        } else {
                            heavyText="VERY LIGHT";
                        }
                        heavinessView.setText("Average Heaviness: "+heavyText);
                    }else{
                        periodView.setText("No Period");
                        colorView.setText("");
                        heavinessView.setText("");
                    }
                }else{
                    periodView.setText("No Period");
                    colorView.setText("");
                    heavinessView.setText("");
                }

            }

            @Override
            public void onMonthChanged(Date date) {}
        });

        return rootView;
    }
}
