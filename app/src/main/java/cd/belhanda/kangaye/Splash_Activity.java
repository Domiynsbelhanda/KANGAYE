package cd.belhanda.kangaye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cd.belhanda.kangaye.Authentification.Authentification;

import static android.text.TextUtils.isEmpty;

public class Splash_Activity extends AppCompatActivity {
    String users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 1);
        }

        try {
            FileInputStream inputStream = openFileInput("Users.txt");
            int value;
            StringBuffer lu = new StringBuffer();
            while((value = inputStream.read()) != -1){
                lu.append((char)value);
            }
            users = lu.toString();
            if(inputStream != null)
                inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(users)){
            try {
                FileOutputStream outputStream = openFileOutput("Users.txt", MODE_PRIVATE);
                outputStream.write(("").getBytes());

                FileOutputStream outputStream1 = openFileOutput("Key.txt", MODE_PRIVATE);
                outputStream1.write(("").getBytes());

                if (outputStream != null)
                    outputStream.close();

                if (outputStream1 != null)
                    outputStream1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileInputStream inputStream = openFileInput("Users.txt");
                int value;
                StringBuffer lu = new StringBuffer();
                while((value = inputStream.read()) != -1){
                    lu.append((char)value);
                }
                users = lu.toString();
                if(inputStream != null)
                    inputStream.close();
            }catch (IOException e){
                e.printStackTrace();}
        }

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                        if(!users.equals("")){
                            startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                            finish();
                        } else{
                            startActivity(new Intent(Splash_Activity.this, Authentification.class));
                            finish();
                        }

                } catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        splashThread.start();

    }
}
