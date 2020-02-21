package cd.belhanda.kangaye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Splash_Activity extends AppCompatActivity {
    String ret;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        try{
            AssetManager am = this.getAssets();
            InputStream inputStream = am.open("authentification");
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString="";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch(FileNotFoundException e){

        } catch(IOException e){

        }

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);
                    if(ret != ""){
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
