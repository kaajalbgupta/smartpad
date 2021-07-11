package com.example.smartpad.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpad.R;
import com.example.smartpad.UserActivity;
import com.example.smartpad.objects.Color;
import com.example.smartpad.objects.HourlyInstance;
import com.example.smartpad.objects.Period;
import com.example.smartpad.objects.PeriodDay;
import com.example.smartpad.objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static com.example.smartpad.utils.PadContract.AGE;
import static com.example.smartpad.utils.PadContract.BLUE;
import static com.example.smartpad.utils.PadContract.CLOTTING;
import static com.example.smartpad.utils.PadContract.COLOR;
import static com.example.smartpad.utils.PadContract.DAYS;
import static com.example.smartpad.utils.PadContract.DAY_TIME;
import static com.example.smartpad.utils.PadContract.END_TIME;
import static com.example.smartpad.utils.PadContract.GREEN;
import static com.example.smartpad.utils.PadContract.HEAVINESS;
import static com.example.smartpad.utils.PadContract.HOURLY_INSTANCES;
import static com.example.smartpad.utils.PadContract.HOUR_TIME;
import static com.example.smartpad.utils.PadContract.NAME;
import static com.example.smartpad.utils.PadContract.PADS_CHANGED;
import static com.example.smartpad.utils.PadContract.PAD_NUMBER;
import static com.example.smartpad.utils.PadContract.PERIODS;
import static com.example.smartpad.utils.PadContract.RED;
import static com.example.smartpad.utils.PadContract.SATURATION;
import static com.example.smartpad.utils.PadContract.START_TIME;
import static com.example.smartpad.utils.PadContract.THEIR_LENGTH_BETWEEN;
import static com.example.smartpad.utils.PadContract.THEIR_PERIOD_LENGTH;
import static com.example.smartpad.utils.PadContract.UID;
import static com.example.smartpad.utils.PadContract.USERS;

public class FirebaseUtils {

    public static User getUserFromDataSnapshot(DataSnapshot dataSnapshot){
        HashMap<Object, Object> map=(HashMap<Object,Object>) dataSnapshot.getValue();
        return getUserFromHashMap(map);
    }

    public static User getUserFromHashMap(HashMap<Object,Object> map){
        String name=map.get(NAME).toString();
        int age=Integer.parseInt(map.get(AGE).toString());
        int theirPeriodLength=Integer.parseInt(map.get(THEIR_PERIOD_LENGTH).toString());
        int theirLengthBetween=Integer.parseInt(map.get(THEIR_LENGTH_BETWEEN).toString());
        String uid=map.get(UID).toString();

        LinkedHashMap<String, Period> periods=new LinkedHashMap<>();
        if(map.get(PERIODS)!=null){
            HashMap<String, Object> hashMap=(HashMap<String, Object>) map.get(PERIODS);
            Iterator iterator=hashMap.entrySet().iterator();
            while (iterator.hasNext()){
                HashMap.Entry<String, Object> pair=(HashMap.Entry<String, Object>) iterator.next();
                periods.put(pair.getKey(),getPeriodFromHashMap((HashMap<Object,Object>) pair.getValue()));
            }
        }

        int padNumber=Integer.parseInt(map.get(PAD_NUMBER).toString());

        User user=new User(name, uid, age, theirPeriodLength, theirLengthBetween, periods, padNumber);
        return user;
    }

    public static Period getPeriodFromHashMap(HashMap<Object,Object> map){
        long startTime=Long.parseLong(map.get(START_TIME).toString());
        long endTime=Long.parseLong(map.get(END_TIME).toString());
        LinkedHashMap<String, PeriodDay> days=new LinkedHashMap<>();
        if(map.get(DAYS)!=null){
            HashMap<String, Object> hashMap=(HashMap<String, Object>) map.get(DAYS);
            Iterator iterator=hashMap.entrySet().iterator();
            while (iterator.hasNext()){
                HashMap.Entry<String, Object> pair=(HashMap.Entry<String, Object>) iterator.next();
                days.put(pair.getKey(),getPeriodDayFromHashMap((HashMap<Object,Object>) pair.getValue()));
            }
        }
        Period period=new Period(startTime, days, endTime);
        return period;
    }

    public static PeriodDay getPeriodDayFromHashMap(HashMap<Object,Object> map){
        LinkedHashMap<String, HourlyInstance> hourlyInstances=new LinkedHashMap<>();
        if(map.get(HOURLY_INSTANCES)!=null){
            HashMap<String, Object> hashMap=(HashMap<String, Object>) map.get(HOURLY_INSTANCES);
            Iterator iterator=hashMap.entrySet().iterator();
            while (iterator.hasNext()){
                HashMap.Entry<String, Object> pair=(HashMap.Entry<String, Object>) iterator.next();
                ArrayList<HourlyInstance> pastHours=new ArrayList<>(hourlyInstances.values());
                hourlyInstances.put(pair.getKey(),getHourlyInstanceFromHashMap((HashMap<Object,Object>) pair.getValue(), pastHours));
            }
        }
        ArrayList<HourlyInstance> hourlyInstancesArrayList=new ArrayList<>(hourlyInstances.values());
        Collections.sort(hourlyInstancesArrayList, new Comparator<HourlyInstance>() {
            @Override
            public int compare(HourlyInstance o1, HourlyInstance o2) {
                return Long.compare(o1.getHourTime(),o2.getHourTime());
            }
        });
        for(int i=0;i<hourlyInstancesArrayList.size();i++){
            HourlyInstance hourlyInstance=hourlyInstancesArrayList.get(i);
            int totalPadsChanged=0;
            for (int j=0; j<i; j++){
                totalPadsChanged+=hourlyInstancesArrayList.get(j).getPadsChanged();
            }
            Log.v("position", String.valueOf(i));
            Log.v("total pads changed", String.valueOf(totalPadsChanged));
            if((i==1 || i==2) && (totalPadsChanged>=1)){
                hourlyInstances.get(String.valueOf(hourlyInstance.getHourTime())).setHeaviness(90);
            }else if((totalPadsChanged==2) && (i==3)){
                hourlyInstances.get(String.valueOf(hourlyInstance.getHourTime())).setHeaviness(70);
            }else if((totalPadsChanged==1) && (i==3)){
                hourlyInstances.get(String.valueOf(hourlyInstance.getHourTime())).setHeaviness(50);
            }else if((totalPadsChanged==1) && (i>3)){
                hourlyInstances.get(String.valueOf(hourlyInstance.getHourTime())).setHeaviness(30);
            }else{

                hourlyInstances.get(String.valueOf(hourlyInstance.getHourTime())).setHeaviness(10);
            }
        }
        long dayTime=Long.parseLong(map.get(DAY_TIME).toString());
        PeriodDay periodDay=new PeriodDay(hourlyInstances, dayTime);
        return periodDay;
    }

    public static HourlyInstance getHourlyInstanceFromHashMap(HashMap<Object,Object> map, ArrayList<HourlyInstance> pastHours){
        Color color=getColorFromHashMap((HashMap<Object,Object>) map.get(COLOR));
        int clotting=Integer.parseInt(map.get(CLOTTING).toString());
        int saturation=Integer.parseInt(map.get(SATURATION).toString());
        long hourTime=Long.parseLong(map.get(HOUR_TIME).toString());
        int padsChanged=Integer.parseInt(map.get(PADS_CHANGED).toString());
        int heaviness=0;
        return new HourlyInstance(color, heaviness, clotting, saturation, hourTime, padsChanged);
    }

    public static Color getColorFromHashMap(HashMap<Object,Object> map){
        int red=Integer.parseInt(map.get(RED).toString());
        int green=Integer.parseInt(map.get(GREEN).toString());
        int blue=Integer.parseInt(map.get(BLUE).toString());
        return new Color(red, green, blue);
    }

    public static void createUserInFirebase(final User user, final String email, final String password, final View view, final Context context, final AppCompatActivity activity){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String uid=task.getResult().getUser().getUid();
                    user.setUid(uid);
                    setValueOfUserInFirebase(user);
                    Intent intent=new Intent(context, UserActivity.class);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }else{
                    Exception e = task.getException();
                    if (e!=null) Snackbar.make(view,e.getLocalizedMessage(),Snackbar.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view,e.getLocalizedMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public static void setValueOfUserInFirebase(User user){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child(USERS).child(user.getUid()).child(NAME).setValue(user.getName());
        databaseReference.child(USERS).child(user.getUid()).child(UID).setValue(user.getUid());
        databaseReference.child(USERS).child(user.getUid()).child(AGE).setValue(user.getAge());
        databaseReference.child(USERS).child(user.getUid()).child(THEIR_PERIOD_LENGTH).setValue(user.getTheirPeriodLength());
        databaseReference.child(USERS).child(user.getUid()).child(THEIR_LENGTH_BETWEEN).setValue(user.getTheirLengthBetween());
        databaseReference.child(USERS).child(user.getUid()).child(PERIODS).setValue(user.getPeriods());
        databaseReference.child(USERS).child(user.getUid()).child(PAD_NUMBER).setValue(user.getPadNumber());
    }
}
