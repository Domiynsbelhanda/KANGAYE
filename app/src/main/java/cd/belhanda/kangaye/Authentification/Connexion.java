package cd.belhanda.kangaye.Authentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;

import cd.belhanda.kangaye.MainActivity;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;

public class Connexion extends AppCompatActivity {

    EditText user, password;
    CardView btn_connexion;

    DatabaseReference databaseReference, getKey;

    boolean verification = false;
    String pseudo, key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        user = findViewById(R.id.edit_user_connexion);
        password = findViewById(R.id.edit_password_connexion);
        btn_connexion = findViewById(R.id.btn_connexion);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user.getText().length() < 6 || user.getText().toString() == null){
                    user.setError("Entrez un pseudonyme correct");
                    return;
                }

                if(password.getText().length() < 6 || password.getText().toString() == null){
                    password.setError("Entrez un mot de passe correct");
                    return;
                }

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
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if(verification){
                    getKey = FirebaseDatabase.getInstance().getReference("Users");
                    Query req = getKey.orderByChild("pseudo").equalTo(pseudo);
                    req
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                        key = dataSnapshot2.getKey();
                                        try{
                                            FileOutputStream outputStream = openFileOutput("Users.txt", MODE_PRIVATE);
                                            outputStream.write(pseudo.getBytes());

                                            FileOutputStream outputStream1 = openFileOutput("Key.txt", MODE_PRIVATE);
                                            outputStream1.write(key.getBytes());

                                            if(outputStream != null)
                                                outputStream.close();

                                            if(outputStream1 != null)
                                                outputStream1.close();
                                        } catch (IOException e){
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                    Toast.makeText(Connexion.this, "Connecté", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    }

                }

        });

    }
}
