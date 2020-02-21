package cd.belhanda.kangaye.ui.profil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cd.belhanda.kangaye.R;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;

    TextView nom_complet_profil, pseudo_profil, telephone_profil, mail_profil;
    private String key, nom, prenom, pseudo, telephone, mail;
    Button update_numero_profil, update_mail_profil, btn_update;
    Button update_nom, update_prenom, update_pseudo;
    EditText edit_numero_profil, edit_mail_profil, edit_update;
    CardView cardViewUpdate;


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

        nom_complet_profil = root.findViewById(R.id.nom_complet_profil);
        pseudo_profil = root.findViewById(R.id.pseudo_profil);
        telephone_profil = root.findViewById(R.id.telephone_profil);
        mail_profil = root.findViewById(R.id.mail_profil);
        update_numero_profil = root.findViewById(R.id.update_numero_profil);
        update_mail_profil = root.findViewById(R.id.update_mail_profil);
        cardViewUpdate = root.findViewById(R.id.cardViewUpdate);
        btn_update = root.findViewById(R.id.btn_update);
        edit_update = root.findViewById(R.id.edit_update);

        update_pseudo = root.findViewById(R.id.update_pseudo);
        update_nom = root.findViewById(R.id.update_nom);
        update_prenom = root.findViewById(R.id.update_prenom);

        key = getArguments().getString("key");
        nom = getArguments().getString("nom");
        prenom = getArguments().getString("prenom");
        pseudo = getArguments().getString("pseudo");
        telephone = getArguments().getString("telephone");
        mail = getArguments().getString("mail");


        nom_complet_profil.setText(prenom + " " + nom);
        pseudo_profil.setText(pseudo);
        telephone_profil.setText(telephone);
        mail_profil.setText(mail);


        update_numero_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updateData;

                if (telephone_profil.getVisibility() == View.VISIBLE){
                    edit_numero_profil = root.findViewById(R.id.edit_numero_profil);
                    edit_numero_profil.setVisibility(View.VISIBLE);
                    telephone_profil.setVisibility(View.GONE);
                } else if(telephone_profil.getVisibility() == View.GONE){

                    telephone_profil.setVisibility(View.VISIBLE);
                    edit_numero_profil.setVisibility(View.GONE);

                    updateData = FirebaseDatabase.getInstance().getReference("Users").child(key);
                    updateData.child("telephone").setValue(edit_numero_profil.getText().toString());
                }
            }
        });

        update_mail_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference updateData;

                if (mail_profil.getVisibility() == View.VISIBLE){
                    edit_mail_profil = root.findViewById(R.id.edit_mail_profil);
                    edit_mail_profil.setVisibility(View.VISIBLE);
                    mail_profil.setVisibility(View.GONE);
                } else if(mail_profil.getVisibility() == View.GONE){

                    mail_profil.setVisibility(View.VISIBLE);
                    edit_mail_profil.setVisibility(View.GONE);

                    updateData = FirebaseDatabase.getInstance().getReference("Users").child(key);
                    updateData.child("mail").setValue(edit_mail_profil.getText().toString());
            }

            }
        });

        update_pseudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewUpdate.setVisibility(View.VISIBLE);
                edit_update.setHint("Pseudo");
            }
        });

        update_nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewUpdate.setVisibility(View.VISIBLE);
                edit_update.setHint("Nom");
            }
        });

        update_prenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewUpdate.setVisibility(View.VISIBLE);
                edit_update.setHint("Prenom");
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updateData;

                if(edit_update.getHint().toString() == "Pseudo"){
                    updateData = FirebaseDatabase.getInstance().getReference("Users").child(key);
                    updateData.child("pseudo").setValue(edit_update.getText().toString());
                } else if(edit_update.getHint().toString() == "Nom"){
                    updateData = FirebaseDatabase.getInstance().getReference("Users").child(key);
                    updateData.child("nom").setValue(edit_update.getText().toString());
                } else if(edit_update.getHint().toString() == "Prenom"){
                    updateData = FirebaseDatabase.getInstance().getReference("Users").child(key);
                    updateData.child("prenom").setValue(edit_update.getText().toString());
                }

            }
        });

        return root;
    }

}