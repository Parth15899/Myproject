package com.SGK.sgk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyfpotp extends AppCompatActivity {

    Button btn1;
    FirebaseAuth mAuth;
    EditText et1;
    String contact,verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyfpotp);

        btn1 = findViewById(R.id.btnverifyfpotp);
        et1 = findViewById(R.id.fpotp);

        contact = getIntent().getStringExtra("contact");

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(contact);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = et1.getText().toString();

                if(code.isEmpty() || code.length()< 6)
                {
                    et1.setError("Enter OTP");
                    et1.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInwithCredential(credential);

    }

    private void signInwithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Intent intent = new Intent(verifyfpotp.this,changepass.class);
                            intent.putExtra("contact",contact);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(verifyfpotp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String code)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + contact,
                60,
                TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,
                mCallBacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();

                    if(code!=null)
                    {
                        et1.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(verifyfpotp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }
            };
}


