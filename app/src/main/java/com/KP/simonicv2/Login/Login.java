package com.KP.simonicv2.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.KP.simonicv2.MainActivity;
import com.KP.simonicv2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;


public class Login extends AppCompatActivity {
    Button login;
    TextView jdl;
    EditText Email,Password;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = findViewById(R.id.btn_login);
        Email = findViewById(R.id.username_input);
        jdl = findViewById(R.id.login);
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/BebasNeue-Regular.ttf");
        jdl.setTypeface(tf);
        Password = findViewById(R.id.password_input);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkCrededenttials();
           /*Intent intent = new Intent(Login.this, MainActivity.class);
           startActivity(intent);*/
            }
        });
        mAuth=FirebaseAuth.getInstance();
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        mLoadingBar=new ProgressDialog(Login.this);

        if (mUser != null){
            startActivity(new Intent(Login.this,MainActivity.class));
            finish();
        }

    }
    private void checkCrededenttials() {
        String email=Email.getText().toString();
        String password=Password.getText().toString();

        if (email.isEmpty() || !email.contains("@")){
            showError(Email,"Email harus diisi");
        }
        else if (password.isEmpty())
        {
            showError(Password,"Password harus diisi");
        }else if (password.length()<7){
            showError(Password,"Panjang password minimal 7");
        }
        else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mLoadingBar.dismiss();
                        DynamicToast.makeSuccess(Login.this, "Login Berhasil").show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }else{
                        DynamicToast.makeError(Login.this, "Email atau Password Salah, Silahkan Masukkan Kembali").show();
                        mLoadingBar.dismiss();
                        Password.setText("");
                    }

                }
            });

        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
