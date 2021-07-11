package com.example.smartpad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.smartpad.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.smartpad.utils.PadContract.EMAIL;
import static com.example.smartpad.utils.PadContract.NAME;
import static com.example.smartpad.utils.PadContract.PASSWORD;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageButton backButton;
    @BindView(R.id.continue_button)
    Button continueButton;
    @BindView(R.id.name_sign_up_edit_text)
    AppCompatEditText nameText;
    @BindView(R.id.email_sign_up_edit_text)
    AppCompatEditText emailText;
    @BindView(R.id.password_sign_up_edit_text)
    AppCompatEditText passwordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        ButterKnife.bind(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= Utils.getTrimmedString(emailText);
                String password=Utils.getTrimmedString(passwordText);
                String name=Utils.getTrimmedString(nameText);
                if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty()){
                    Intent intent=new Intent(RegisterActivity.this, SignUpDetailActivity.class);
                    intent.putExtra(NAME, name);
                    intent.putExtra(EMAIL, email);
                    intent.putExtra(PASSWORD, password);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
