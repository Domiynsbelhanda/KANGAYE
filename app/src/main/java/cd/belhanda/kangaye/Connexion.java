package cd.belhanda.kangaye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cd.belhanda.kangaye.Modele.Inscription_Modele;

public class Connexion extends AppCompatActivity {

    EditText user, password;
    CardView btn_connexion;
    TextView notification_connexion;

    DatabaseReference databaseReference, getKey;

    boolean verification;
    String pseudo, key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        user = findViewById(R.id.edit_user_connexion);
        password = findViewById(R.id.edit_password_connexion);
        btn_connexion = findViewById(R.id.btn_connexion);

        notification_connexion = findViewById(R.id.notification_connexion);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().length() == 0 || password.getText().length() == 0){
                    notification_connexion.setVisibility(View.VISIBLE);
                    notification_connexion.setText("Veuillez completez vos coordonnées");

                    verification = false;
                }else {

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Inscription_Modele connexion = dataSnapshot1.getValue(Inscription_Modele.class);

                                if (connexion.getPseudo().equals(user.getText().toString()) &&
                                    connexion.getMot_de_passe().equals(password.getText().toString())) {
                                    verification = true;
                                    pseudo = connexion.getPseudo().toString();
                                    break;

                                } else {
                                    verification = false;
                                    notification_connexion.setVisibility(View.VISIBLE);
                                    notification_connexion.setText("Verifiez vos coordonnées ou Inscrivez-vous.");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                    if(verification){
                        getKey = FirebaseDatabase.getInstance().getReference("Users");
                        Query req = getKey.orderByChild("pseudo").equalTo(pseudo);
                        req
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                            key = dataSnapshot2.getKey();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        FileOutputStream output = null;
                        String userName = pseudo;
                        try {
                            output = openFileOutput("authentification", MODE_PRIVATE);
                            output.write(userName.getBytes());
                            if(output != null)
                                output.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                        Toast.makeText(Connexion.this, pseudo, Toast.LENGTH_LONG).show();
                    }

                }

        });

    }
}
