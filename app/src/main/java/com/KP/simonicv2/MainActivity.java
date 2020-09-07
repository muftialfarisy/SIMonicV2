package com.KP.simonicv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.KP.simonicv2.Individu.Informasi;
import com.KP.simonicv2.Login.Login;
import com.KP.simonicv2.Registrasi.Registrasi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    CardView cd_regis,cd_info,cd_radar,cd_setting;
    private FirebaseUser mUser;
    FirebaseUser mlogout;
    private static final Integer request= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validasi();
        cd_regis = (CardView) findViewById(R.id.cd_register);
        cd_regis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Registrasi.class);
                startActivity(intent);
            }
        });
        cd_info = (CardView) findViewById(R.id.cd_info);
        cd_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Informasi.class);
                startActivity(intent);
            }
        });
        cd_radar = (CardView) findViewById(R.id.cd_radar);
        cd_radar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        cd_setting = (CardView) findViewById(R.id.cd_setting);
        cd_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }
    public void validasi(){
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null){
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
    }

}