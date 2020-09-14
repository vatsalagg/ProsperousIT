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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prosperousitsolutions.MainActivity;
import com.example.prosperousitsolutions.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {



    private TextView verification_txt,otp_sent_txt;
    private EditText enter_otp ;
    private Button verify,resend;
    private ProgressBar progressBar;
    private Timer timer;
    private int count=60;
    private String username,phone,password;
    boolean freelancer;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    private FirebaseAuth firebaseAuth;

    DatabaseReference reference;



//    public OtpActivity(String username, String phone, String password) {
//        this.username = username;
//        this.phone = phone;
//        this.password = password;
//    }


    private void init(){
        verification_txt=findViewById(R.id.verification_txt);
        otp_sent_txt=findViewById(R.id.otp_sent_txt);
        enter_otp=findViewById(R.id.enter_otp);
        verify=findViewById(R.id.verify_btn);
        resend=findViewById(R.id.resend_btn);
        progressBar=findViewById(R.id.progressBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        getSupportActionBar().hide();

        Window w = getWindow();
        //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.setStatusBarColor(Color.TRANSPARENT);

        init();

        Intent intent = getIntent();
        username= intent.getStringExtra("username");
        phone= intent.getStringExtra("phone");
        password= intent.getStringExtra("password");
        freelancer= intent.getBooleanExtra("freelancer",true);




        TextPaint paint = verification_txt.getPaint();
        float width = paint.measureText("Verification");

        Shader textShader = new LinearGradient(0, 0, width, verification_txt.getTextSize(),
                new int[]{
                        Color.parseColor("#7986CB"),
                        Color.parseColor("#70C3E7"),
                }, null, Shader.TileMode.CLAMP);
        verification_txt.setTextColor(Color.parseColor("#7986CB"));
        verification_txt.getPaint().setShader(textShader);


        otp_sent_txt.setText("OTP has been sent to +91"+phone);
        sendOTP();

        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                OtpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count==0){
                            resend.setText("Resend");
                            resend.setEnabled(true);
                            resend.setAlpha(1f);
                        }
                        else {
                            resend.setText("Resend In "+count);
                            count--;
                        }
                    }
                });

            }
        },0,1000);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter_otp.setError(null);
                if (enter_otp.getText()== null || enter_otp.getText().toString().isEmpty()){
                    enter_otp.setError("Required!");
                    return;
                }

                String code =enter_otp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);

                progressBar.setVisibility(View.VISIBLE);


            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendotp();
                resend.setEnabled(false);
                resend.setAlpha(0.5f);
                count=60;

            }
        });

        firebaseAuth=FirebaseAuth.getInstance();


    }

    private void sendOTP(){
        mcallback =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
//                        Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                        signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
//                        Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    enter_otp.setError(e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    enter_otp.setError(e.getMessage());
                }
                progressBar.setVisibility(View.INVISIBLE);

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                        Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpActivity.this,               // Activity (for callback binding)
                mcallback);        // OnVerificationStateChangedCallbacks
    }

    private  void  resendotp(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OtpActivity.this,               // Activity (for callback binding)
                mcallback,mResendToken);        // OnVerificationStateChangedCallbacks

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener( OtpActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

                            final FirebaseUser user = task.getResult().getUser();
                            AuthCredential credential= EmailAuthProvider.getCredential(username,password);
                            user.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

                                        final HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("id", firebaseAuth.getCurrentUser().getUid());
                                        hashMap.put("username", username);
                                        hashMap.put("imageURL", "default");
//                                        hashMap.put("status", "offline");
                                        hashMap.put("search", username.toLowerCase());


                                        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("email",username);
                                        map.put("phone", phone);
                                        map.put("freelancer",freelancer);

                                        firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                                    Intent mainIntent = new Intent (OtpActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    // Toast.makeText(getContext(), "Account Successfully Created!", Toast.LENGTH_SHORT).show();

//                                                    firebase      Firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
//                                                            .collection("UserData").document("Wishlist")
//                                                            .set(documentFields).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()){
//                                                                Intent mainIntent = new Intent (getContext(), MainActivity.class);
//                                                                startActivity(mainIntent);
////                                                    disableCloseBtn=false;
//                                                                getActivity().finish();
//
//                                                            }else{
//                                                                String error = task.getException().getMessage();
//                                                                Toast.makeText(getContext(),error , Toast.LENGTH_SHORT).show();
//                                                                progressBar.setVisibility(View.INVISIBLE);
//
//                                                            }
//                                                        }
//                                                    });

                                                }else{
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(OtpActivity.this,error , Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                        });

                                    }
                                    else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OtpActivity.this,error , Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                enter_otp.setError("Invalid OTP ");// The verification code entered was invalid
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }



}
