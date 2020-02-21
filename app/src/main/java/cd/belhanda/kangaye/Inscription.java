package cd.belhanda.kangaye;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigator;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cd.belhanda.kangaye.Modele.Inscription_Modele;
import de.hdodenhof.circleimageview.CircleImageView;

public class Inscription extends AppCompatActivity {

    /**
    * Declaration des EditTexts et TextViews a l'inscription
    * */
    EditText edit_nom, edit_prenom, edit_pseudo, edit_phone, edit_password;
    TextView text_nom, text_prenom, text_pseudo, text_phone, text_password, notification_text;

    /**
    * Declaration pour Importation du photo de profil a l'inscription
    * */
    CardView importer_photo;
    CircleImageView photo_importer;
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = null;
    Uri uri;

    /**
    * Inscription et enregistrement des données dans la database
    * */
    CardView btn_inscription, btn_annule;

    /**
     * Firebase connection pour l'inscription*/
    DatabaseReference mDatabase;
    String userId;
    boolean booleans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        /**
        * Configuration de vue des EditTexts
        * */
        edit_nom = findViewById(R.id.edit_nom);
        edit_prenom = findViewById(R.id.edit_prenom);
        edit_pseudo = findViewById(R.id.edit_pseudo);
        edit_phone = findViewById(R.id.edit_phone);
        edit_password = findViewById(R.id.edit_password);

        /**
        * Configuration de vue des TextViews
        * */
        text_nom = findViewById(R.id.text_nom);
        text_prenom = findViewById(R.id.text_prenom);
        text_pseudo = findViewById(R.id.text_pseudo);
        text_phone = findViewById(R.id.text_phone);
        text_password = findViewById(R.id.text_password);

        /**
        * Ajout des evenements aux EditTexts et
        * Actions sur les TextViews qui les accompagnes.
        * */
        edit_nom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_nom.getText().length() < 3){
                    text_nom.setVisibility(View.VISIBLE);
                    text_nom.setText("Nom trop court");
                }
                else if(edit_nom.getText().length() < 6){
                    text_nom.setText("Nom moyen");
                } else {
                    text_nom.setText("Taille respectée");
                }
            }
        });
        edit_prenom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_prenom.getText().length() < 3){
                    text_prenom.setVisibility(View.VISIBLE);
                    text_prenom.setText("Prénom trop court");
                }
                else if(edit_prenom.getText().length() < 6){
                    text_prenom.setText("Prénom moyen");
                } else {
                    text_prenom.setText("Taille respectée");
                }
            }
        });
        edit_pseudo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_pseudo.getText().length() < 6){
                    text_pseudo.setVisibility(View.VISIBLE);
                    text_pseudo.setText("Pseudo court, au moins 6 caractères");
                } else {
                    text_pseudo.setText("Taille respectée");
                }
            }
        });
        edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_phone.getText().length() < 13){
                    text_phone.setVisibility(View.VISIBLE);
                    text_phone.setText("Numero court");
                } else{
                    text_phone.setText("Numéro valide");
                }
            }
        });
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_password.getText().length() < 6){
                    text_password.setVisibility(View.VISIBLE);
                    text_password.setText("Trop court, au moins 6 caractères");
                } else {
                    text_password.setText("Taille respectée");
                }
            }
        });

        /**
        * Configuration de vue sur les objets
        * de l'importation de la photo de profil
        * */
        importer_photo = findViewById(R.id.importer_photo);
        photo_importer = findViewById(R.id.photo_importer);

        /** Ajout de l'Evenement sur CardView d'importation*/
        importer_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        /**
         * Inscription et annule,
         * configuration des vues*/
        btn_inscription = findViewById(R.id.btn_inscrire);
        btn_annule = findViewById(R.id.btn_annule);

        btn_annule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accueil_intent = new Intent(Inscription.this, Authentification.class);
                startActivity(accueil_intent);
            }
        });

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
            if (edit_nom.getText().length() < 3 || edit_prenom.getText().length() < 3
                    || edit_pseudo.getText().length() < 6 || edit_prenom.getText().length() < 3
                    || Integer.parseInt(String.valueOf(edit_phone.getText()).substring(1, 4)) != 243 ||
                    edit_password.getText().length() < 6) {

                if (edit_nom.getText().length() < 3) {
                    edit_nom.setBackgroundColor(R.color.red);
                    text_nom.setText("Verifiez votre nom");
                }
                if (edit_prenom.getText().length() < 3) {
                    edit_prenom.setBackgroundColor(R.color.red);
                    text_prenom.setText("Verifiez votre prenom");
                }
                if (edit_pseudo.getText().length() < 6) {
                    edit_pseudo.setBackgroundColor(R.color.red);
                    text_pseudo.setText("Verifiez votre pseudo");
                }
                if (edit_prenom.getText().length() < 3) {
                    edit_prenom.setBackgroundColor(R.color.red);
                    text_prenom.setText("Verifiez votre prenom");
                }
                if (edit_phone.getText().length() < 13){
                    edit_phone.setBackgroundColor(R.color.red);
                    text_phone.setText("Numéro Incorrect");
                }
                else if(Integer.parseInt(String.valueOf(edit_phone.getText()).substring(1, 4)) != 243) {
                    text_phone.setText("Doit commencer par +243");
                    edit_phone.setBackgroundColor(R.color.red);
                }
                if (edit_password.getText().length() < 6) {
                    edit_password.setBackgroundColor(R.color.red);
                    text_password.setVisibility(View.VISIBLE);
                    text_password.setText("Mot de passe trop court");
                }

            } else {

                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                userId = mDatabase.push().getKey();

                booleans = false;


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Inscription_Modele oklm = dataSnapshot1.getValue(Inscription_Modele.class);
                            if (oklm.getPseudo().equals(edit_pseudo.getText().toString())){
                                booleans = true;
                            }
                            if (booleans){
                                notification_text = findViewById(R.id.notification_text);
                                notification_text.setText("Ce Pseudo a deja été utilisé, \nVeuillez changer votre pseudo\n Merci!");
                                edit_pseudo.setBackgroundColor(R.color.red);
                            }
                            else {
                                Inscription_Modele user = new Inscription_Modele(
                                        edit_nom.getText().toString(),
                                        edit_prenom.getText().toString(),
                                        edit_pseudo.getText().toString(),
                                        edit_phone.getText().toString(),
                                        edit_password.getText().toString(),
                                        uri.toString());

                                mDatabase.child(userId).setValue(user);

                                Toast.makeText(Inscription.this, "Inscription effectuée", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Inscription.this, Connexion.class);
                                startActivity(intent);
                                finish();
                                
                            }
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
