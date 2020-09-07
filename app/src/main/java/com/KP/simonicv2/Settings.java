package com.KP.simonicv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.KP.simonicv2.Login.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {
Button logout;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);

            }
        });

    }

}
