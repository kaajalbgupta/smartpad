package com.example.smartpad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.smartpad.objects.Period;
import com.example.smartpad.objects.User;
import com.example.smartpad.utils.FirebaseUtils;
import com.example.smartpad.utils.Utils;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.smartpad.utils.PadContract.EMAIL;
import static com.example.smartpad.utils.PadContract.NAME;
import static com.example.smartpad.utils.PadContract.PASSWORD;

public class SignUpDetailActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageButton backButton;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.age_edit_text)
    AppCompatEditText ageText;
    @BindView(R.id.length_edit_text)
    AppCompatEditText lengthText;
    @BindView(R.id.between_edit_text)
    AppCompatEditText betweenText;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_detail_activity);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        final String name=intent.getStringExtra(NAME);
        final String email=intent.getStringExtra(EMAIL);
        final String password=intent.getStringExtra(PASSWORD);

        titleView.setText("Hello "+name+"!");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("signing up", "yep");
                String ageString= Utils.getTrimmedString(ageText);
                String lengthString=Utils.getTrimmedString(lengthText);
                String betweenString=Utils.getTrimmedString(betweenText);
                if(!ageString.isEmpty() && !lengthString.isEmpty() && !betweenString.isEmpty()) {
                    Log.v("there", "yep");
                    int age=Integer.parseInt(ageString);
                    int length=Integer.parseInt(lengthString);
                    int between=Integer.parseInt(betweenString);
                    LinkedHashMap<String, Period> periods=new LinkedHashMap<>();
                    User user=new User(name, null, age, length, between, periods, -1);
                    FirebaseUtils.createUserInFirebase(user, email, password, constraintLayout, SignUpDetailActivity.this, SignUpDetailActivity.this);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
