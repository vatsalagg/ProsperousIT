package com.example.prosperousitsolutions.user_session;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosperousitsolutions.MainActivity;
import com.example.prosperousitsolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;


import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TextView companyName,login_txt;
    private EditText username,phone,password,confirm_password;
    private ProgressBar progressbar;
    private Button register;
    private FirebaseAuth mAuth;
    private ImageView skipLoginBtn;
    public static boolean disableCloseBtn=false;
    RadioGroup radioGroup;
    RadioButton search, offer;
    private boolean isFreeLancer = true;

    private void init()
    {
        companyName= findViewById(R.id.company_name);
        login_txt=findViewById(R.id.login_txt);
        username=findViewById(R.id.username);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        confirm_password=findViewById(R.id.confirm_password);
        progressbar=findViewById(R.id.progressBar);
        register=findViewById(R.id.btn_register);
        search = findViewById(R.id.search);
        offer = findViewById(R.id.offer);
        radioGroup = findViewById(R.id.toggle);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        init();
        Window w = getWindow();
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setStatusBarColor(Color.TRANSPARENT);
        skipLoginBtn=findViewById(R.id.skipLoginImg);

        Intent i= getIntent();
        boolean type =  i.getBooleanExtra("freelancer",true);
        if(!type){
           radioGroup.check(radioGroup.getChildAt(1).getId());
//           search.setChecked(type);
           offer.setTextColor(Color.parseColor("#FFFFFF"));
           search.setTextColor(Color.parseColor("#4FC3F7"));
           offer.setTextSize(20);
           search.setTextSize(10);
           isFreeLancer=false;
        }else{
           search.setTextColor(Color.parseColor("#FFFFFF"));
           offer.setTextColor(Color.parseColor("#4FC3F7"));
           search.setTextSize(20);
           offer.setTextSize(10);
           isFreeLancer=true;
        }

        if (disableCloseBtn){
            skipLoginBtn.setVisibility(View.GONE);
        }else {
            skipLoginBtn.setVisibility(View.VISIBLE);
        }


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        NOTE HAVE TO CHECK
//        ((UserSessionActivity) getActivity()).updateStatusBarColor("#20000000");

// for Company name gradient
        TextPaint paint = companyName.getPaint();
        float width = paint.measureText("PITS");

        Shader textShader = new LinearGradient(0, 0, width, companyName.getTextSize(),
                new int[]{
                        Color.parseColor("#7986CB"),
                        Color.parseColor("#70C3E7"),
                }, null, Shader.TileMode.CLAMP);
        companyName.setTextColor(Color.parseColor("#7986CB"));
        companyName.getPaint().setShader(textShader);



//login fragment on already have an acc. txt
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(l);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                ((UserSessionActivity)getActivity()).setFragment(new LoginFragment());
//                UserSessionActivity.registerFrag =false;

            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.search) {
                    search.setTextColor(Color.parseColor("#FFFFFF"));
                    offer.setTextColor(Color.parseColor("#4FC3F7"));
                    search.setTextSize(20);
                    offer.setTextSize(10);
                    isFreeLancer=true;
                }
                else if (i == R.id.offer){
                    offer.setTextColor(Color.parseColor("#FFFFFF"));
                    search.setTextColor(Color.parseColor("#4FC3F7"));
                    offer.setTextSize(20);
                    search.setTextSize(10);
                    isFreeLancer=false;
                }
            }
        });

//OTP fragment on register btn
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username.setError(null);
                phone.setError(null);
                password.setError(null);
                confirm_password.setError(null);
                if (username.getText().toString().isEmpty()) {
                    username.setError("Required!");
                    return;
                }
                if (phone.getText().toString().isEmpty()) {
                    phone.setError("Required!");
                    return;

                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Required!");
                    return;
                }
                if (confirm_password.getText().toString().isEmpty()) {
                    confirm_password.setError("Required!");
                    return;
                }
                if (!VALID_EMAIL_ADDRESS_REGEX.matcher(username.getText().toString()).find()) {
                    username.setError("Invalid  E-mail");
                    return;
                }
                if (phone.getText().toString().length() != 10) {
                    phone.setError("Invalid Phone Number");
                    return;
                }
                if (password.getText().toString().length()<6){
                    password.setError("Password should be at least 6 characters");
                    return;
                }
                if (!password.getText().toString().equals(confirm_password.getText().toString())){
                    confirm_password.setError("Password Mismatched!");
                    return;
                }

                createAccount();
            }

        });

        skipLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disableCloseBtn){
                    disableCloseBtn=false;
                }else {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("Not Registered", true);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
              finishAffinity();

            }
        });


    }

    private void createAccount(){
        progressbar.setVisibility(View.VISIBLE);

        mAuth.fetchSignInMethodsForEmail(username.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().isEmpty()) {
                        Intent i= new Intent(RegisterActivity.this,OtpActivity.class);
                        i.putExtra("username",username.getText().toString());
                        i.putExtra("phone",phone.getText().toString());
                        i.putExtra("password",password.getText().toString());
                        i.putExtra("freelancer",isFreeLancer);

                        startActivity(i);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //((UserSessionActivity)getActivity()).setFragment(new OTPFragment(username.getText().toString(),phone.getText().toString(),password.getText().toString()));
                    }
                    else {
                        username.setError("Username already exists!");
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    String error=task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this,error, Toast.LENGTH_SHORT).show();

                }
                progressbar.setVisibility(View.INVISIBLE);
            }
        });
    }
}