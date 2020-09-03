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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosperousitsolutions.MainActivity;
import com.example.prosperousitsolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "usernname";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private EditText username_or_phone,password;
    private TextView forgot_password_txt,sign_up__txt;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressbar;
    private ImageView skipLoginBtn;
    private  TextView appName;
    public static boolean disableCloseBtn=false;


    private void init(){
        username_or_phone=findViewById(R.id.usernameOrPhone);
        password=findViewById(R.id.password);
        forgot_password_txt=findViewById(R.id.forgot_password_txt);
        sign_up__txt=findViewById(R.id.sign_up_txt);
        login=findViewById(R.id.btn_login);
        progressbar=findViewById(R.id.progressBar);
    }

    private void login(String username){
        progressbar.setVisibility(View.VISIBLE);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(username,password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (disableCloseBtn){
                        disableCloseBtn=false;
                    }else {
                        Intent mainIntent =new Intent(LoginActivity.this, MainActivity.class);
//                      mainIntent.putExtra("Not Registered", false);
                        startActivity(mainIntent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                        finish();

                }
                else{ String error=task.getException().getMessage();
                    Toast.makeText(LoginActivity.this,error,  Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
        skipLoginBtn=findViewById(R.id.skipLoginImg);

        appName=findViewById(R.id.appNametxt);
        TextPaint paint =appName.getPaint();
        float width = paint.measureText("PITS");


        Shader textShader = new LinearGradient(0, 0, width, appName.getTextSize(),
                new int[]{
                        Color.parseColor("#7986CB"),
                        Color.parseColor("#70C3E7"),
                }, null, Shader.TileMode.CLAMP);
        appName.setTextColor(Color.parseColor("#7986CB"));
        appName.getPaint().setShader(textShader);




        if (disableCloseBtn){
            skipLoginBtn.setVisibility(View.GONE);
        }else {
            skipLoginBtn.setVisibility(View.VISIBLE);
        }


        sign_up__txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(s);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        forgot_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s=new Intent(LoginActivity.this,Forgot_PasswordActivity.class);
                startActivity(s);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_or_phone.setError(null);
                password.setError(null);


                if (username_or_phone.getText().toString().isEmpty()) {
                    username_or_phone.setError("Required!");
                    return;

                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Required!");
                    return;
                }

                if (VALID_EMAIL_ADDRESS_REGEX.matcher(username_or_phone.getText().toString()).find()) {
                    login(username_or_phone.getText().toString());

                } else if (username_or_phone.getText().toString().matches("\\d{10}")) {
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseAuth=FirebaseAuth.getInstance();
                    firebaseFirestore.collection("Users").whereEqualTo("phone", username_or_phone.getText().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                List<DocumentSnapshot> document =task.getResult().getDocuments();
                                if(document.isEmpty()){
                                    username_or_phone.setError("Phone number not found!");
                                    progressbar.setVisibility(View.VISIBLE);
                                    return;
                                }else{
                                    String email=document.get(0).get("email").toString();
                                    login(email);
                                }
                            }else {
                                String error =task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
                    username_or_phone.setError("Invalid Email or Phone");

                }
            }
        });

        skipLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
//                intent.putExtra("Not Registered", true);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                disableCloseBtn=false;
                finishAffinity();

            }
        });
    }
}