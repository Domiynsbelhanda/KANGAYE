package cd.belhanda.kangaye.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    CardView SOS, ALERTE;
    Button btnSendAlertes;
    EditText editSendAlertes;
    private String key, nom, prenom, pseudo, telephone, mail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        DatabaseReference mDatabase;

        /** Firebase get key start*/
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Inscription_Modele connexion = dataSnapshot1.getValue(Inscription_Modele.class);

                    if (connexion.getPseudo().equals("Dominique")){
                        key = dataSnapshot1.getKey();
                        nom = connexion.getNom();
                        prenom = connexion.getPrenom();
                        pseudo = connexion.getPseudo();
                        telephone = connexion.getTelephone();
                        mail = connexion.getMail();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /** FIrebase get key end*/

        final RelativeLayout fab = root.findViewById(R.id.fab);
        final RelativeLayout alerte = root.findViewById(R.id.alerte_bottom);
        final RelativeLayout sender = root.findViewById(R.id.sendAlerte);

        btnSendAlertes = root.findViewById(R.id.btnSendAlertes);
        editSendAlertes = root.findViewById(R.id.editSendAlertes);
        editSendAlertes.setHint(pseudo);

        sender.setVisibility(View.GONE);
        alerte.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                alerte.setVisibility(View.VISIBLE);
            }
        });

        SOS = root.findViewById(R.id.SOSLANCEUR);
        ALERTE = root.findViewById(R.id.ALERTELANCEUR);

        ALERTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerte.setVisibility(View.GONE);
                sender.setVisibility(View.VISIBLE);

            }
        });

        btnSendAlertes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dates = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String heures = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                DatabaseReference databaseReference;
                databaseReference= FirebaseDatabase.getInstance().getReference("Alertes");
                String key = databaseReference.push().getKey();

                databaseReference = databaseReference.child(key);

                databaseReference.child("pseudo").setValue(pseudo);
                databaseReference.child("nom_alerte").setValue(editSendAlertes.getText().toString());
                databaseReference.child("Localisation").setValue("");
                databaseReference.child("Date").setValue(dates);
                databaseReference.child("Heure").setValue(heures);
            }
        });


        return root;
    }

}