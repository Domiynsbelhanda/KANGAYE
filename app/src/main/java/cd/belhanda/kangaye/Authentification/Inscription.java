package cd.belhanda.kangaye.Authentification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileOutputStream;
import java.io.IOException;

import cd.belhanda.kangaye.MainActivity;
import cd.belhanda.kangaye.MapsActivity;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Inscription extends AppCompatActivity {

    /**
    * Declaration des EditTexts et TextViews a l'inscription
    * */
    EditText edit_nom, edit_pseudo, edit_phone, edit_password;

    /**
    * Declaration pour Importation du photo de profil a l'inscription
    * */
    CircleImageView photo_importer;
    private static final int FILE_SELECT_CODE = 0;
    Uri uri;

    /**
    * Inscription et enregistrement des données dans la database
    * */
    CardView btn_inscription;

    /**
     * Firebase connection pour l'inscription*/
    DatabaseReference mDatabase;
    String userId;
    boolean booleans;


    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        /**
        * Configuration de vue des EditTexts
        * */
        edit_nom = findViewById(R.id.edit_nom);
        edit_pseudo = findViewById(R.id.edit_pseudo);
        edit_phone = findViewById(R.id.edit_telephone);
        edit_password = findViewById(R.id.edit_password);

        photo_importer = findViewById(R.id.imageProfilImport);

        booleans = true;

        /** Ajout de l'Evenement sur CardView d'importation*/
        photo_importer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        /**
         * Inscription et annule,
         * configuration des vues*/
        btn_inscription = findViewById(R.id.btn_inscrire);

        storageRef = FirebaseStorage.getInstance().getReference();

        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inscription_database();
            }
        });
    }

    /** Importation de la photo de profil*/
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try{
            startActivityForResult(
                    Intent.createChooser(intent, "Selectionne une image"), FILE_SELECT_CODE
            );
        } catch (android.content.ActivityNotFoundException ex){

        }
    }

    /**
    * onActivityResult configuration
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case FILE_SELECT_CODE:
                if(resultCode == RESULT_OK){
                    uri = data.getData();
                    photo_importer.setImageURI(uri);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Ajout a la base des données*/
    public void inscription_database(){
        if (uri != null) {

            if(edit_nom.getText().length() < 6){
                edit_nom.setError("Nom invalide !!!");
                return;
            }

            if(edit_pseudo.getText().length() < 6){
                edit_pseudo.setError("Pseudo invalide !!!");
                return;
            }

            if(edit_phone.getText().length() != 13){
                edit_phone.setError("Numéro incorrect, 13 caractères!!!");
                return;
            }

            if(Integer.parseInt(edit_phone.getText().toString().substring(1, 4)) != 243){
                edit_phone.setError("Doit commencer par +243 !");
                return;
            }

            if(edit_password.getText().length() < 6){
                edit_password.setError("Mot de passe invalide !!!");
                return;
            }

            userId = mDatabase.push().getKey();

            booleans = true;
            if(booleans){
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Inscription_Modele oklm = dataSnapshot1.getValue(Inscription_Modele.class);
                            if (oklm.getPseudo().equals(edit_pseudo.getText().toString())) {
                                booleans = false;
                                edit_pseudo.setError("Pseudo déjà utilisé, modifier");
                            }
                        }

                        if(booleans){
                            Uri file = uri;
                            final StorageReference riversRef = storageRef.child("Profil/" + edit_pseudo.getText().toString());

                            UploadTask uploadTask = riversRef.putFile(file);

                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Inscription_Modele user = new Inscription_Modele(
                                                edit_nom.getText().toString(),
                                                edit_pseudo.getText().toString(),
                                                edit_phone.getText().toString(),
                                                edit_password.getText().toString(),
                                                task.getResult().toString());

                                        mDatabase.child(userId).setValue(user);

                                        Toast.makeText(Inscription.this, "Inscription effectuée", Toast.LENGTH_LONG).show();

                                        try {
                                            FileOutputStream outputStream = openFileOutput("Users.txt", MODE_PRIVATE);
                                            outputStream.write(edit_pseudo.getText().toString().getBytes());

                                            FileOutputStream outputStream1 = openFileOutput("Key.txt", MODE_PRIVATE);
                                            outputStream1.write(userId.getBytes());

                                            if (outputStream != null)
                                                outputStream.close();

                                            if (outputStream1 != null)
                                                outputStream1.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        return;
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        }else{
            Toast.makeText(this, "Veuillez importez une image", Toast.LENGTH_LONG).show();
        }
    }
}
