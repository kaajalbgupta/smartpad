package com.example.smartpad;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartpad.fragments.AboutUsFragment;
import com.example.smartpad.fragments.DashboardFragment;
import com.example.smartpad.fragments.ProfileFragment;
import com.example.smartpad.fragments.TrackerFragment;
import com.example.smartpad.objects.Period;
import com.example.smartpad.objects.PeriodDay;
import com.example.smartpad.objects.User;
import com.example.smartpad.utils.FirebaseUtils;
import com.example.smartpad.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.smartpad.utils.PadContract.END_TIME;
import static com.example.smartpad.utils.PadContract.PAD_NUMBER;
import static com.example.smartpad.utils.PadContract.PERIODS;
import static com.example.smartpad.utils.PadContract.USERS;

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationViewEx bottomNavigationView;
    @BindView(R.id.header)
    TextView headerView;
    @BindView(R.id.period_button)
    FloatingActionButton periodButton;
    @BindView(R.id.period_button_text) TextView periodButtonText;

    private Fragment fragment;

    private DashboardFragment dashboardFragment;
    private TrackerFragment trackerFragment;
    private ProfileFragment profileFragment;
    private AboutUsFragment aboutUsFragment;

    private boolean isInitial=true;

    DatabaseReference databaseReference;
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Log.v("exists","ya");
            if(snapshot.exists()){
                Log.v("hello","ya");
                final User user= FirebaseUtils.getUserFromDataSnapshot(snapshot);
                if(user.getPadNumber()==-1){
                    periodButtonText.setText("START\n PERIOD");
                }else{
                    periodButtonText.setText("STOP\n PERIOD");
                }
                if(isInitial){
                    dashboardFragment=new DashboardFragment();
                    trackerFragment=new TrackerFragment();
                    profileFragment=new ProfileFragment();
                    aboutUsFragment=new AboutUsFragment();
                    isInitial=false;

                    final FragmentManager fragmentManager=getSupportFragmentManager();

                    for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.remove(fragment);

                        fragmentTransaction.commitAllowingStateLoss();
                    }

                    fragmentManager.beginTransaction().add(R.id.user_fragment_container, dashboardFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.user_fragment_container, trackerFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.user_fragment_container, profileFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().add(R.id.user_fragment_container, aboutUsFragment).commitAllowingStateLoss();

                    fragmentManager.beginTransaction().hide(trackerFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(profileFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(aboutUsFragment).commitAllowingStateLoss();

                    fragment=dashboardFragment;

                    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.dashboard_menu:
                                    if(fragment!=dashboardFragment){
                                        fragmentManager.beginTransaction().hide(fragment).show(dashboardFragment).commitAllowingStateLoss();
                                        fragment=dashboardFragment;
                                        headerView.setText("Dashboard");
                                    }
                                    break;
                                case R.id.tracker_menu:
                                    if(fragment!=trackerFragment){
                                        fragmentManager.beginTransaction().hide(fragment).show(trackerFragment).commitAllowingStateLoss();
                                        fragment=trackerFragment;
                                        headerView.setText("Tracker");
                                    }
                                    break;
                                case R.id.profile_menu:
                                    if(fragment!=profileFragment){
                                        fragmentManager.beginTransaction().hide(fragment).show(profileFragment).commitAllowingStateLoss();
                                        fragment=profileFragment;
                                        headerView.setText("Profile");
                                    }
                                    break;
                                case R.id.about_menu:
                                    if(fragment!=aboutUsFragment){
                                        fragmentManager.beginTransaction().hide(fragment).show(aboutUsFragment).commitAllowingStateLoss();
                                        fragment=aboutUsFragment;
                                        headerView.setText("About Us");
                                    }
                                    break;
                            }
                            return true;
                        }
                    });

                    periodButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(user.getPadNumber()==-1){
                                user.setPadNumber(0);
                                databaseReference.child(PAD_NUMBER).setValue(0);
                                LinkedHashMap<String, PeriodDay> periodDays=new LinkedHashMap<>();
                                Period period=new Period(Utils.getDateMillisFromCurrentMillis(System.currentTimeMillis()), periodDays, -1);
                                user.getPeriods().put(String.valueOf(Utils.getDateMillisFromCurrentMillis(System.currentTimeMillis())), period);
                                databaseReference.child(PERIODS).child(String.valueOf(Utils.getDateMillisFromCurrentMillis(System.currentTimeMillis()))).setValue(period);
                            }else{
                                user.setPadNumber(-1);
                                databaseReference.child(PAD_NUMBER).setValue(-1);
                                ArrayList<Period> periods=new ArrayList<>(user.getPeriods().values());
                                Period recentPeriod= Collections.max(periods,new Comparator<Period>() {
                                    public int compare(Period o1, Period o2) {
                                        return Long.compare(o1.getStartTime(), o2.getStartTime());
                                    }
                                });
                                user.getPeriods().get(String.valueOf(recentPeriod.getStartTime())).setEndTime(Utils.getDateMillisFromCurrentMillis(System.currentTimeMillis()));
                                databaseReference.child(PERIODS).child(String.valueOf(recentPeriod.getStartTime())).child(END_TIME).setValue(Utils.getDateMillisFromCurrentMillis(System.currentTimeMillis()));
                            }
                        }
                    });
                }
                dashboardFragment.setUser(user);
                trackerFragment.setUser(user);
                profileFragment.setUser(user);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        ButterKnife.bind(this);

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child(USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        databaseReference.removeEventListener(valueEventListener);
        super.onStop();
    }
}
