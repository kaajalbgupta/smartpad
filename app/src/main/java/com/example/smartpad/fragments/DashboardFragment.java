package com.example.smartpad.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.smartpad.R;
import com.example.smartpad.objects.HourlyInstance;
import com.example.smartpad.objects.Period;
import com.example.smartpad.objects.PeriodDay;
import com.example.smartpad.objects.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.smartpad.utils.PadContract.DAYS;
import static com.example.smartpad.utils.PadContract.HOURLY_INSTANCES;
import static com.example.smartpad.utils.PadContract.PADS_CHANGED;
import static com.example.smartpad.utils.PadContract.PAD_NUMBER;
import static com.example.smartpad.utils.PadContract.PERIODS;
import static com.example.smartpad.utils.PadContract.USERS;

public class DashboardFragment extends Fragment {

    private User user;
    private View rootView;
    private DatabaseReference databaseReference;

    public void setUser(User user){
        this.user=user;

        if(databaseReference==null){
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            databaseReference=firebaseDatabase.getReference().child(USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        if(rootView!=null) {
            setUp();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.dashboard_fragment,container,false);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child(USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        setUp();

        return rootView;
    }

    private void setUp(){
        BarChart colorsBarChart=rootView.findViewById(R.id.colors_chart);
        colorsBarChart.getLegend().setEnabled(false);
        colorsBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis colorsXAxis = colorsBarChart.getXAxis();
        colorsXAxis.setAxisMinimum(0);
        colorsXAxis.setDrawGridLines(false);
        colorsXAxis.setLabelCount(6);
        colorsXAxis.setAxisMaximum(6);
        colorsXAxis.setTextColor(getContext().getResources().getColor(R.color.colorSecondaryText));

        YAxis colorsYAxis = colorsBarChart.getAxisLeft();
        colorsYAxis.setDrawGridLines(false);
        colorsYAxis.setAxisMinimum(0);
        colorsYAxis.setTextColor(getContext().getResources().getColor(R.color.colorSecondaryText));
        YAxis rightColorsYAxis = colorsBarChart.getAxisRight();
        rightColorsYAxis.setDrawGridLines(false);
        rightColorsYAxis.setDrawAxisLine(false);
        rightColorsYAxis.setDrawLabels(false);

        Description desc = new Description();
        desc.setText("");
        colorsBarChart.setDescription(desc);
        colorsBarChart.setPinchZoom(false);

        ArrayList<BarEntry> colorEntries;

        ArrayList<Period> reversedPeriodList=new ArrayList<>(user.getPeriods().values());
        Collections.sort(reversedPeriodList, new Comparator<Period>() {
            @Override
            public int compare(Period o1, Period o2) {
                return Long.compare(o1.getStartTime(),o2.getStartTime());
            }
        });
        Collections.reverse(reversedPeriodList);

        int count;
        if(reversedPeriodList.size()>=5){
            count=5;
        }else{
            count=reversedPeriodList.size();
        }
        colorEntries=new ArrayList<>(count);

        ArrayList<Integer> colorColors = new ArrayList<>();

        if(count==5){
            int index=0;
            for (int i = 4; i>=0; i--){
                Period period=reversedPeriodList.get(i);
                ArrayList<PeriodDay> periodDays=new ArrayList<>(period.getDays().values());
                float[] colorDayLengths=new float[periodDays.size()];
                int j=0;
                for (PeriodDay periodDay: periodDays){
                    String color=periodDay.getAverageColor();
                    colorColors.add(Color.parseColor(color));
                    colorDayLengths[j]=1;
                    j++;
                }
                colorEntries.add(new BarEntry(index+1, colorDayLengths));
                index++;
            }
        }else if(count!=0){
            int index=0;
            for (int i=count-1; i>=0; i--){
                Period period=reversedPeriodList.get(i);
                ArrayList<PeriodDay> periodDays=new ArrayList<>(period.getDays().values());
                float[] colorDayLengths=new float[periodDays.size()];
                int j=0;
                for (PeriodDay periodDay: periodDays){
                    String color=periodDay.getAverageColor();
                    colorColors.add(Color.parseColor(color));
                    colorDayLengths[j]=1;
                    j++;
                }
                colorEntries.add(new BarEntry(index+1, colorDayLengths));
                index++;
            }
        }

        BarDataSet colorDataSet = new BarDataSet(colorEntries, "");

        colorsXAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) return " ";
                if(value==6) return " ";
                else return String.valueOf((int) value);
            }
        });

        colorDataSet.setDrawValues(false);
        colorDataSet.setColors(colorColors);
        final BarData colorData = new BarData(colorDataSet);
        colorData.setHighlightEnabled(true);
        colorData.setBarWidth(0.5f);
        colorsBarChart.setData(colorData);
        colorsBarChart.invalidate();

        BarChart heavinessBarChart=rootView.findViewById(R.id.heaviness_chart);
        heavinessBarChart.getLegend().setEnabled(false);
        heavinessBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis heavinessXAxis = heavinessBarChart.getXAxis();
        heavinessXAxis.setAxisMinimum(0);
        heavinessXAxis.setDrawGridLines(false);
        heavinessXAxis.setLabelCount(6);
        heavinessXAxis.setAxisMaximum(6);
        heavinessXAxis.setTextColor(getContext().getResources().getColor(R.color.colorSecondaryText));

        YAxis heavinessYAxis = heavinessBarChart.getAxisLeft();
        heavinessYAxis.setDrawGridLines(false);
        heavinessYAxis.setAxisMinimum(0);
        heavinessYAxis.setAxisMaximum(100);
        heavinessYAxis.setTextColor(getContext().getResources().getColor(R.color.colorSecondaryText));
        YAxis rightHeavinessYAxis = heavinessBarChart.getAxisRight();
        rightHeavinessYAxis.setDrawGridLines(false);
        rightHeavinessYAxis.setDrawAxisLine(false);
        rightHeavinessYAxis.setDrawLabels(false);

        Description desc1 = new Description();
        desc1.setText("");
        heavinessBarChart.setDescription(desc1);
        heavinessBarChart.setPinchZoom(false);

        ArrayList<BarEntry> heavinessEntries = new ArrayList<>();
        int index=0;
        for (int i=count-1; i>=0; i--){
            Period period=reversedPeriodList.get(i);
            heavinessEntries.add(new BarEntry(index+1, period.getAverageHeaviness()));
            index++;
        }

        BarDataSet heavinessDataSet = new BarDataSet(heavinessEntries, "");
        ArrayList<Integer> heavinessColors = new ArrayList<>();
        for (BarEntry entry : heavinessEntries) {
            int number = (int) entry.getY();
            if (number <= 30) {
                heavinessColors.add(getContext().getResources().getColor(R.color.pink));
            } else if (number <= 60) {
                heavinessColors.add(getContext().getResources().getColor(R.color.brightRed));
            } else if (number <= 100) {
                heavinessColors.add(getContext().getResources().getColor(R.color.darkRed));
            }
        }

        heavinessXAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) return " ";
                if(value==6) return " ";
                else return String.valueOf((int) value);
            }
        });

        heavinessDataSet.setDrawValues(false);
        heavinessDataSet.setColors(heavinessColors);
        final BarData heavinessData = new BarData(heavinessDataSet);
        heavinessData.setHighlightEnabled(true);
        heavinessData.setBarWidth(0.5f);
        heavinessBarChart.setData(heavinessData);
        heavinessBarChart.invalidate();

        CardView colorCard=rootView.findViewById(R.id.color_card);
        TextView colorView=rootView.findViewById(R.id.color_text);
        TextView heavinessView=rootView.findViewById(R.id.heaviness);
        TextView consistencyView=rootView.findViewById(R.id.consistencyy);
        CardView heavinessCard=rootView.findViewById(R.id.heaviness_card);
        CardView consistencyCard=rootView.findViewById(R.id.consistency);
        View colorOverlay=rootView.findViewById(R.id.color_overlay);
        View heavinessOverlay=rootView.findViewById(R.id.heaviness_overlay);
        View consistencyOverlay=rootView.findViewById(R.id.consistency_overlay);
        TextView filledPercentView=rootView.findViewById(R.id.percent_filled_text);
        TextView dayNumberView=rootView.findViewById(R.id.day_number_text);
        TextView padNumberView=rootView.findViewById(R.id.pad_number_text);

        View recommendationsOverlay=rootView.findViewById(R.id.recommendations_overlay);

        recommendationsOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(getContext());
                if (dialog.getWindow()!=null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                View contentView=LayoutInflater.from(getContext()).inflate(R.layout.alert_color_period,null);
                dialog.setContentView(contentView);

                TextView header=dialog.findViewById(R.id.header);
                TextView content=dialog.findViewById(R.id.content);
                Button doneButton=dialog.findViewById(R.id.done);

                String text="";
                ArrayList<Period> periods=new ArrayList<>(user.getPeriods().values());
                if(user.getAge()>30){
                    Collections.sort(periods, new Comparator<Period>() {
                        @Override
                        public int compare(Period o1, Period o2) {
                            return Long.compare(o1.getStartTime(),o2.getStartTime());
                        }
                    });
                    if(periods.size()>=2){
                        if((periods.get(periods.size()-1).getStartTime()-periods.get(periods.size()-2).getStartTime())/(24*60*60*1000)<(user.getTheirLengthBetween()-5)){
                            text+="Your period is closer apart than is normal--you may be going through perimenopause. Consult a doctor if you would like to learn more. \n\n";
                        }
                        Period period=periods.get(periods.size()-1);
                        if(period.getEndTime()==-1){
                            period=periods.get(periods.size()-2);
                        }
                        if((period.getEndTime()-period.getStartTime())/(24*60*60*1000)<(user.getTheirPeriodLength()-1)){
                            text+="Your period is longer than normal--you may be going through perimenopause. Consult a doctor if you would like to learn more. \n\n";
                        }
                        if(period.getAverageHeaviness()>80 && period.getAverageClotting()>80){
                            text+="Your period is heavier than normal--you may be going through perimenopause. Consult a doctor if you would like to learn more. \n\n";
                        }
                    }
                }

                Collections.reverse(periods);
                int count;
                if(periods.size()<3){
                    count=periods.size();
                }else{
                    count=3;
                }
                int heaviness=0;
                int clotting=0;
                int red=0;
                int green=0;
                int blue=0;
                for (int i=0; i<count; i++){
                    heaviness+=periods.get(i).getAverageHeaviness();
                    clotting+=periods.get(i).getAverageClotting();
                    red+=periods.get(i).getAverageColorRGB().getRed();
                    green+=periods.get(i).getAverageColorRGB().getGreen();
                    blue+=periods.get(i).getAverageColorRGB().getBlue();
                }
                heaviness=heaviness/count;
                clotting=clotting/count;
                red=red/count;
                green=green/count;
                blue=blue/count;

                if(120<=red && red<=210 &&
                        0<=green && green<=165 &&
                        0<=blue && blue<=63) {
                    text+="Your average period color is brown. This indicates that the blood has taken time to leave the body due to slow blood flow. Usually occurs at the spinning or end of your period. It could also result from pregnancy spotting or lochia (bleeding experienced for the first four to six weeks after delivering a baby is called lochia). \n\n";
                }else if(0<=red && red<=50 &&
                        0<=green && green<=50 &&
                        0<=blue && blue<=50){
                    text+="Your average period color is black. This indicates that the blood has taken time to leave the body due to slow blood flow. Usually occurs at the spinning or end of your period. It could also result from pregnancy spotting or lochia (bleeding experienced for the first four to six weeks after delivering a baby is called lochia). \n\n";
                }else if(90<=red && red<=200 &&
                        0<=green && green<=20 &&
                        0<=blue && blue<=40){
                    text+="Your average period color is dark red. This is a color that is regarded to be “normal period” color. This color is common during walking and other physical activities performed as well as lying down for long periods of time. The color is just a measure of how oxidised the blood is. However, thick dark red flow could be a sign of clotted blood, which is dangerous. \n\n";
                }else if(200<=red && red<=255 &&
                        0<=green && green<=30 &&
                        0<=blue && blue<=30){
                    text+="Your average period color is bright red. Usually representative of fresh blood. Your blood is flowing quickly and may stay this way your whole period or may darken as your flow slows. If you encounter this color blood in between periods, it could be a sign of infection. Heavy and thick blood of this color could point towards non-cancerous growth in the uterus such as polyps or fibroids. \n\n";
                }else if(180<=red && red<=255 &&
                        50<=green && green<=120 &&
                        0<=blue && blue<=35){
                    text+="Your average period color is orange. When blood mixes with cervical fluid it may also appear orange. As a result, you may see orange discharge for the same reasons you see pink discharge. \n\n";
                } else if(199<=red && red<=255 &&
                        20<=green && green<=192 &&
                        133<=blue && blue<=203){
                    text+="Your average period color is pink. This lighter shade likely indicates that the blood has mixed with your cervical fluid, diluting its hue. This color is associated with lower levels of estrogen and if continued throughout the cycle could indicate hormonal imbalances. It is also a color associated with mid-cycle spotting around ovulation time. \n\n";
                } else{
                    text+="Your average period color is red. Usually representative of fresh blood. Your blood is flowing quickly and may stay this way your whole period or may darken as your flow slows. If you encounter this color blood in between periods, it could be a sign of infection. Heavy and thick blood of this color could point towards non-cancerous growth in the uterus such as polyps or fibroids. \n\n";
                }

                if (heaviness > 80) {
                    text+="Your period is extremely heavy on average, you may want to see a doctor to check if something is wrong. \n\n";
                } else if (heaviness > 60) {
                    text+="Your period is heavy on average, you may want to see a doctor to check if you could take any additional vitamins. \n\n";
                } else if (heaviness > 40) {
                    text+="Your period has normal heaviness on average! \n\n";
                } else if (heaviness > 20) {
                    text+="Your period is light on average. Lighter periods are not a cause for concern, as some months will naturally have lighter periods than others. However, in some cases, it could indicate pregnancy or a hormone-related condition such as PCOS (polycystic ovary syndrome). \n\n";
                } else {
                    text+="Your period is very light on average. This may be due to a number of reasons: if you've just started your period, are undergoing menopause, are stressed, have a hormone imbalance, or if you're taking birth control. \n\n";
                }

                if (clotting > 80) {
                    text+="You have very heavy clotting on average. Clots are formed when the body's ability to produce anticoagulants (blood thinners) cannot keep up with the blood flow. If you have multiple blood clots larger than the size of a quarter that can be an indication of heavy menstrual bleeding. It is advised to wear a high-absorbency pad in these circumstances.";
                } else if (clotting > 60) {
                    text+="You have heavy clotting on average. Clots are formed when the body's ability to produce anticoagulants (blood thinners) cannot keep up with the blood flow. If you have multiple blood clots larger than the size of a quarter that can be an indication of heavy menstrual bleeding. It is advised to wear a high-absorbency pad in these circumstances.";
                } else if (clotting > 40) {
                    text+="You have medium consistency menstrual blood on average.";
                } else if (clotting > 20) {
                    text+="You have thin menstrual blood on average, without much clotting.";
                } else {
                    text+="You have very thin menstrual blood on average, without much clotting.";
                }

                header.setText("Analysis");
                content.setText(text);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        ArrayList<Period> periods=new ArrayList<>(user.getPeriods().values());
        if(periods.size()>0){
            final Period recentPeriod=Collections.max(periods,new Comparator<Period>() {
                public int compare(Period o1, Period o2) {
                    return Long.compare(o1.getStartTime(), o2.getStartTime());
                }
            });
            if(recentPeriod.getEndTime()==-1){
                ArrayList<PeriodDay> periodDays=new ArrayList<PeriodDay>(recentPeriod.getDays().values());
                if(periodDays.size()>0) {
                    final PeriodDay recentPeriodDay = Collections.max(periodDays, new Comparator<PeriodDay>() {
                        public int compare(PeriodDay o1, PeriodDay o2) {
                            return Long.compare(o1.getDayTime(), o2.getDayTime());
                        }
                    });
                    ArrayList<HourlyInstance> hourlyInstances = new ArrayList<HourlyInstance>(recentPeriodDay.getHourlyInstances().values());
                    final HourlyInstance recentHourlyInstance = Collections.max(hourlyInstances, new Comparator<HourlyInstance>() {
                        public int compare(HourlyInstance o1, HourlyInstance o2) {
                            return Long.compare(o1.getHourTime(), o2.getHourTime());
                        }
                    });

                    CardView numberOfPadsCard=rootView.findViewById(R.id.number_of_pads_card);
                    Button lessPadButton=rootView.findViewById(R.id.less_pad_button);
                    Button morePadButton=rootView.findViewById(R.id.more_pad_button);
                    TextView numberOfPadsView=rootView.findViewById(R.id.number_of_pads_text);

                    if(user.getPadNumber()==-1){
                        numberOfPadsCard.setVisibility(View.GONE);
                    }else{
                        numberOfPadsCard.setVisibility(View.VISIBLE);
                        numberOfPadsView.setText(String.valueOf(user.getPadNumber()));
                        lessPadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(user.getPadNumber()!=1 && user.getPadNumber()!=0){
                                    user.setPadNumber(user.getPadNumber()-1);
                                    databaseReference.child(PAD_NUMBER).setValue(user.getPadNumber());
                                    if(recentHourlyInstance.getPadsChanged()!=0){
                                        databaseReference.child(PERIODS).child(String.valueOf(recentPeriod.getStartTime())).child(DAYS).child(String.valueOf(recentPeriodDay.getDayTime()))
                                                .child(HOURLY_INSTANCES).child(String.valueOf(recentHourlyInstance.getHourTime())).child(PADS_CHANGED).setValue(recentHourlyInstance.getPadsChanged()-1);
                                    }
                                }
                            }
                        });
                        morePadButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                user.setPadNumber(user.getPadNumber()+1);
                                databaseReference.child(PAD_NUMBER).setValue(user.getPadNumber());
                                databaseReference.child(PERIODS).child(String.valueOf(recentPeriod.getStartTime())).child(DAYS).child(String.valueOf(recentPeriodDay.getDayTime()))
                                        .child(HOURLY_INSTANCES).child(String.valueOf(recentHourlyInstance.getHourTime())).child(PADS_CHANGED).setValue(recentHourlyInstance.getPadsChanged()+1);
                            }
                        });
                    }

                    filledPercentView.setText(recentHourlyInstance.getSaturation() + "% Filled");
                    Log.v("pad number", String.valueOf(user.getPadNumber()));
                    dayNumberView.setText("DAY " + (periodDays.size()));
                    padNumberView.setText("PAD NUMBER " + user.getPadNumber());

                    final String heavyText;
                    if (recentHourlyInstance.getHeaviness() > 80) {
                        heavyText="VERY HEAVY";
                    } else if (recentHourlyInstance.getHeaviness() > 60) {
                        heavyText="HEAVY";
                    } else if (recentHourlyInstance.getHeaviness() > 40) {
                        heavyText="MEDIUM";
                    } else if (recentHourlyInstance.getHeaviness() > 20) {
                        heavyText="LIGHT";
                    } else {
                        heavyText="VERY LIGHT";
                    }
                    heavinessView.setText(heavyText);

                    final String consistencyText;
                    if (recentHourlyInstance.getClotting() > 80) {
                        consistencyText="VERY THICK";
                    } else if (recentHourlyInstance.getClotting() > 60) {
                        consistencyText="THICK";
                    } else if (recentHourlyInstance.getClotting() > 40) {
                        consistencyText="MEDIUM";
                    } else if (recentHourlyInstance.getClotting() > 20) {
                        consistencyText="THIN";
                    } else {
                        consistencyText="VERY THIN";
                    }
                    consistencyView.setText(consistencyText);

                    final String colorText;
                    if(120<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=210 &&
                            0<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=165 &&
                            0<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=63) {
                        colorText = "BROWN";
                    }else if(0<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=50 &&
                                0<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=50 &&
                                0<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=50){
                        colorText = "BLACK";
                    }else if(90<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=200 &&
                            0<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=20 &&
                            0<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=40){
                        colorText="DARK RED";
                    }else if(200<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=255 &&
                            0<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=30 &&
                            0<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=30){
                        colorText="BRIGHT RED";
                    }else if(180<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=255 &&
                            50<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=120 &&
                            0<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=35){
                        colorText="ORANGE";
                    } else if(199<=recentHourlyInstance.getColor().getRed() && recentHourlyInstance.getColor().getRed()<=255 &&
                            20<=recentHourlyInstance.getColor().getGreen() && recentHourlyInstance.getColor().getGreen()<=192 &&
                            133<=recentHourlyInstance.getColor().getBlue() && recentHourlyInstance.getColor().getBlue()<=203){
                        colorText="PINK";
                    } else{
                        colorText="RED";
                    }
                    colorView.setText(colorText);

                    colorOverlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog=new Dialog(getContext());
                            if (dialog.getWindow()!=null)
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            View contentView=LayoutInflater.from(getContext()).inflate(R.layout.alert_color_period,null);
                            dialog.setContentView(contentView);

                            TextView header=dialog.findViewById(R.id.header);
                            TextView content=dialog.findViewById(R.id.content);
                            Button doneButton=dialog.findViewById(R.id.done);

                            header.setText("Color: "+colorText);

                            if (colorText.equals("BROWN") || colorText.equals("BLACK")) {
                                content.setText("Indicates that the blood has taken time to leave the body due to slow blood flow. Usually occurs at the spinning or end of your period. It could also result from pregnancy spotting or lochia (bleeding experienced for the first four to six weeks after delivering a baby is called lochia).");
                            } else if (colorText.equals("DARK RED")) {
                                content.setText("This is a color that is regarded to be “normal period” color. This color is common during walking and other physical activities performed as well as lying down for long periods of time. The color is just a measure of how oxidised the blood is. However, thick dark red flow could be a sign of clotted blood, which is dangerous.");
                            } else if (colorText.equals("BRIGHT RED") || colorText.equals("RED")) {
                                content.setText("Usually representative of fresh blood. Your blood is flowing quickly and may stay this way your whole period or may darken as your flow slows. If you encounter this color blood in between periods, it could be a sign of infection. Heavy and thick blood of this color could point towards non-cancerous growth in the uterus such as polyps or fibroids.");
                            } else if (colorText.equals("ORANGE")) {
                                content.setText("When blood mixes with cervical fluid it may also appear orange. As a result, you may see orange discharge for the same reasons you see pink discharge.");
                            } else {
                                content.setText("This lighter shade likely indicates that the blood has mixed with your cervical fluid, diluting its hue. This color is associated with lower levels of estrogen and if continued throughout the cycle could indicate hormonal imbalances. It is also a color associated with mid-cycle spotting around ovulation time.");
                            }

                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });

                    heavinessOverlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog=new Dialog(getContext());
                            if (dialog.getWindow()!=null)
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            View contentView=LayoutInflater.from(getContext()).inflate(R.layout.alert_color_period,null);
                            dialog.setContentView(contentView);

                            TextView header=dialog.findViewById(R.id.header);
                            TextView content=dialog.findViewById(R.id.content);
                            Button doneButton=dialog.findViewById(R.id.done);

                            if (heavyText.equals("VERY HEAVY")) {
                                content.setText("Severe vaginal bleeding or gushing means that you are soaking 1 or 2 pads or tampons in 1 or 2 hours, unless that is normal for you. For most women, passing clots of blood from the vagina and soaking through their usual pads or tampons every hour for 2 or more hours is not normal and is considered severe. We recommend seeing a doctor regarding your heavy bleeding. This may be due to a number of issues, such as uterine and hormone disorders.");
                            } else if (heavyText.equals("HEAVY")) {
                                content.setText("Heavy bleeding means changing a high-absorbency pad once every 3 to 4 hours or around 2 regular-absorbency pads every 3 to 4 hours. This is normal towards the start of your period, but should gradually decrease as it continues.");
                            } else if (heavyText.equals("MEDIUM")) {
                                content.setText("Moderate bleeding means that you are soaking more than 1 pad or tampon in 3 hours. This is perfectly normal.");
                            } else if (heavyText.equals("LIGHT")) {
                                content.setText("Mild bleeding means that you are soaking less than 1 pad or tampon in more than 3 hours. Lighter periods are not a cause for concern, as some months will naturally have lighter periods than others. However, in some cases, it could indicate pregnancy or a hormone-related condition such as PCOS (polycystic ovary syndrome).");
                            } else {
                                content.setText("Very light periods may be due to a number of reasons: if you've just started your period, are undergoing menopause, are stressed, have a hormone imbalance, or if you're taking birth control.");
                            }

                            header.setText("Heaviness: "+heavyText);

                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });

                    consistencyOverlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog dialog=new Dialog(getContext());
                            if (dialog.getWindow()!=null)
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            View contentView=LayoutInflater.from(getContext()).inflate(R.layout.alert_color_period,null);
                            dialog.setContentView(contentView);

                            TextView header=dialog.findViewById(R.id.header);
                            TextView content=dialog.findViewById(R.id.content);
                            Button doneButton=dialog.findViewById(R.id.done);

                            header.setText("Consistency: "+consistencyText);

                            if (consistencyText.equals("VERY THICK")) {
                                content.setText("Clots are formed when the body's ability to produce anticoagulants (blood thinners) cannot keep up with the blood flow. If you have multiple blood clots larger than the size of a quarter that can be an indication of heavy menstrual bleeding. It is advised to wear a high-absorbency pad in these circumstances.");
                            } else if (consistencyText.equals("THICK")) {
                                content.setText("Clots are formed when the body's ability to produce anticoagulants (blood thinners) cannot keep up with the blood flow. If you have multiple blood clots larger than the size of a quarter that can be an indication of heavy menstrual bleeding. It is advised to wear a high-absorbency pad in these circumstances.");
                            } else if (consistencyText.equals("MEDIUM")) {
                                content.setText("Your blood consistency has some clots.");
                            } else if (consistencyText.equals("THIN")) {
                                content.setText("You have thin menstrual blood on average, without much clotting.");
                            } else {
                                content.setText("You have very thin menstrual blood on average, without much clotting.");
                            }

                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });
                }else{
                    colorView.setText("--");
                    filledPercentView.setText("--");
                    dayNumberView.setText("DAY --");
                    padNumberView.setText("PAD NUMBER --");
                }
            }else{
                colorView.setText("--");
                filledPercentView.setText("--");
                dayNumberView.setText("DAY --");
                padNumberView.setText("PAD NUMBER --");
            }
        }else{
            colorView.setText("--");
            filledPercentView.setText("--");
            dayNumberView.setText("DAY --");
            padNumberView.setText("PAD NUMBER --");
        }
    }
}
