package cd.belhanda.kangaye.ui.profil;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cd.belhanda.kangaye.MainActivity;
import cd.belhanda.kangaye.MapsActivity;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import cd.belhanda.kangaye.ui.home.HomeFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static android.app.Activity.RESULT_OK;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;

    private CircleImageView photoProfil, imageupdate;
    private TextView pseudo, nom, position, mail, phone;

    private String key, pseudos, yourpass, snom, smail, sphone, spassword;

    private LinearLayout linearProfil, linearUpdate;

    private Button btnUpdateProfil;
    private EditText update_nom, update_mail, update_contact, update_password, your_password;

    private static final int FILE_SELECT_CODE = 0;
    Uri uri;

    DatabaseReference mDatabase;
    StorageReference storageRef;

    Dialog dialog;
    View viewLancer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_profil, container, false);


        profilViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        try {
            FileInputStream inputStream = getActivity().openFileInput("Key.txt");
            int value;
            StringBuffer lu = new StringBuffer();
            while((value = inputStream.read()) != -1){
                lu.append((char)value);
            }
            key = lu.toString();
            if(inputStream != null)
                inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            FileInputStream inputStream = getActivity().openFileInput("Users.txt");
            int value;
            StringBuffer lu = new StringBuffer();
            while((value = inputStream.read()) != -1){
                lu.append((char)value);
            }
            pseudos = lu.toString();
            if(inputStream != null)
                inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        photoProfil = root.findViewById(R.id.profilPhoto);
        pseudo = root.findViewById(R.id.pseudo_profil);
        nom = root.findViewById(R.id.nom_complet_profil);
        position = root.findViewById(R.id.profilLocalisation);
        mail = root.findViewById(R.id.mail_profil);
        phone = root.findViewById(R.id.telephone_profil);

        linearProfil = root.findViewById(R.id.LinearProfil);
        linearUpdate = root.findViewById(R.id.linearUpdate);

        /** Firebase get key start*/
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);

                Picasso.get().load(inscription.getProfil()).into(photoProfil);
                pseudo.setText(inscription.getPseudo());
                nom.setText(inscription.getNom());
                mail.setText(inscription.getMail());
                phone.setText(inscription.getTelephone());
                position.setText("Longitude : " + inscription.getLongitude() + " et Latitude : " + inscription.getLatitude());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //
        btnUpdateProfil = root.findViewById(R.id.btnUpdateProfil);

        btnUpdateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });


        return root;
    }

    private void addNotification() {
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewLancer = LayoutInflater.from(getActivity()).inflate(R.layout.update_profil_modele, null);

    }

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case FILE_SELECT_CODE:
                if(resultCode == RESULT_OK){
                    uri = data.getData();
                    imageupdate.setImageURI(uri);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatedata(){
        if(your_password.getText().length() < 6){
            update_nom.setError("Mot de passe trop court");
            return;
        }

        if(update_nom.getText().length() < 6){
            update_nom.setError("Nom trop court !");
            return;
        }

        if(Integer.parseInt(update_contact.getText().toString().substring(1, 4)) != 243){
            update_contact.setError("Doit commencer par +243 !");
            return;
        }

        if(update_contact.getText().toString().length() < 13){
            update_contact.setError("Trop court");
            return;
        }

        if(update_password.getText().length() <6){
            update_password.setError("Trop court");
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);
                yourpass = inscription.getMot_de_passe().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!yourpass.equals(your_password.getText().toString())){
            your_password.setError("Mot de passe différent, veuillez réessayer");
            return;
        }


        Uri file = uri;
        final StorageReference riversRef = storageRef.child("Profil/" + pseudos);

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
                            update_nom.getText().toString(),
                            pseudos,
                            update_contact.getText().toString(),
                            update_password.getText().toString(),
                            task.getResult().toString());

                    mDatabase.child(key).setValue(user);

                } else {
                    return;
                }
            }
        });

    }

}