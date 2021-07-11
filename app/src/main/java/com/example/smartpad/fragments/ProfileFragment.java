package com.example.smartpad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartpad.MainActivity;
import com.example.smartpad.R;
import com.example.smartpad.objects.User;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private User user;

    public void setUser(User user){
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.profile_fragment,container,false);

        TextView nameView=rootView.findViewById(R.id.name);
        TextView ageView=rootView.findViewById(R.id.age);
        TextView lengthView=rootView.findViewById(R.id.ideal_length);
        TextView betweenView=rootView.findViewById(R.id.ideal_between);

        nameView.setText("Name: "+user.getName());
        ageView.setText("Age: "+user.getAge());
        lengthView.setText("Ideal Period Length: "+user.getTheirPeriodLength());
        betweenView.setText("Ideal Length between Periods: "+user.getTheirLengthBetween());

        Button signOutButton=rootView.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        return rootView;
    }
}
