package cd.belhanda.kangaye.ui.SOS;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cd.belhanda.kangaye.Adapter.EmisAdapter;
import cd.belhanda.kangaye.Adapter.EmisClick;
import cd.belhanda.kangaye.Adapter.RecusAdapter;
import cd.belhanda.kangaye.Adapter.RecusClick;
import cd.belhanda.kangaye.MapsUsing;
import cd.belhanda.kangaye.Modele.AlertesAdd;
import cd.belhanda.kangaye.R;
import cd.belhanda.kangaye.ui.alerte.AlerteViewModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class SOSFragment extends Fragment implements EmisClick, RecusClick {

    private AlerteViewModel alerteViewModel;

    Button btnEmis, btnRecus, cancel, appelerphone, geolocalisation;
    ArrayList<AlertesAdd> mList = new ArrayList<>();
    ArrayList<AlertesAdd> mListe = new ArrayList<>();

    RecyclerView Emis, Recus;
    private String key, pseudo, appelerphones;

    private double longitudedraw, latitudedraw;

    private SOSViewModel SOSViewModel;

    private EmisAdapter emisAdapter;
    private RecusAdapter recusAdapter;

    Dialog dialog;
    View viewLancer;

    private CircleImageView profilDetail;

    private TextView vousavezrecu, delapart, heuredate, distance;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SOSViewModel =
                ViewModelProviders.of(this).get(SOSViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sos, container, false);
        SOSViewModel.getText().observe(this, new Observer<String>() {
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
            pseudo = lu.toString();
            if(inputStream != null)
                inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        btnEmis = root.findViewById(R.id.btnSOSEmis);

        btnRecus = root.findViewById(R.id.btnSOSRecus);

        Emis = root.findViewById(R.id.recyclerSOSEmis);
        Emis.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference = reference.child(key).child("SOS").child("EMIS");
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    AlertesAdd n = dataSnapshot1.getValue(AlertesAdd.class);
                    mList.add(n);
                }
                Emis.setAdapter(emisAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        emisAdapter = new EmisAdapter(getActivity(), mList);
        emisAdapter.setEmisClick(this);

        Recus = root.findViewById(R.id.recycleSOSRecus);
        Recus.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference references;
        references= FirebaseDatabase.getInstance().getReference("Users");
        references = references.child(key).child("SOS").child("RECUS");
        references.keepSynced(true);
        references.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListe.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        AlertesAdd n = dataSnapshot1.getValue(AlertesAdd.class);
                        mListe.add(n);
                    }
                    Recus.setAdapter(recusAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recusAdapter = new RecusAdapter(getActivity(), mListe);
        recusAdapter.setRecusClick(this);


        btnRecus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                Emis.setVisibility(View.GONE);
                Recus.setVisibility(View.VISIBLE);
            }
        });

        btnEmis.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {
                Recus.setVisibility(View.GONE);
                Emis.setVisibility(View.VISIBLE);
            }
        });

        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewLancer = LayoutInflater.from(getActivity()).inflate(R.layout.affichage_alerte_sos, null);

        cancel = viewLancer.findViewById(R.id.btncloseDialog);
        geolocalisation = viewLancer.findViewById(R.id.btnGeolocalisation);
        appelerphone = viewLancer.findViewById(R.id.btnAppelerSonmobile);

        appelerphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = appelerphones;
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(phoneIntent);
            }
        });

        geolocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsUsing.class);
                intent.putExtra("longdraw", String.valueOf(longitudedraw));
                intent.putExtra("latdraw", String.valueOf(latitudedraw));
                startActivity(intent);
            }
        });

        delapart = viewLancer.findViewById(R.id.pseudocontainer);
        vousavezrecu = viewLancer.findViewById(R.id.vousavezrecu);
        distance = viewLancer.findViewById(R.id.distanceAffichage);
        heuredate = viewLancer.findViewById(R.id.DateAffichageok);
        profilDetail = viewLancer.findViewById(R.id.profildetailsos);

        dialog.setContentView(viewLancer);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return root;
    }

    @Override
    public void onItemClick(AlertesAdd alertesAdd) {

        vousavezrecu.setText("VOUS AVEZ EMIS\n UN SOS");
        profilDetail.setVisibility(View.GONE);
        heuredate.setText("Date : " + alertesAdd.getDate() + " et Heure : " + alertesAdd.getHeure());
        delapart.setText("Categorie : " + alertesAdd.getCategorie() +"\n" +
                " Details : " + alertesAdd.getDetails());
        distance.setVisibility(View.GONE);
        geolocalisation.setVisibility(View.GONE);
        appelerphone.setVisibility(View.GONE);


        dialog.show();
    }

    @Override
    public void onItemClick1(AlertesAdd alertesAdd) {

        vousavezrecu.setText("VOUS AVEZ RECUS\n UN SOS");
        Picasso.get().load(alertesAdd.getProfil()).into(profilDetail);
        profilDetail.setVisibility(View.VISIBLE);

        heuredate.setText("Date : " + alertesAdd.getDate() + " et Heure : " + alertesAdd.getHeure());
        delapart.setText("Categorie : " + alertesAdd.getCategorie() +"\n" +
                " Details : " + alertesAdd.getDetails());
        distance.setText("a " + String.valueOf(alertesAdd.getDistance()) + " mètres de votre position.");
        distance.setVisibility(View.VISIBLE);

        appelerphone.setVisibility(View.VISIBLE);
        appelerphones = alertesAdd.getTelephone();

        geolocalisation.setVisibility(View.VISIBLE);

        longitudedraw = alertesAdd.getLongitude();
        latitudedraw = alertesAdd.getLatitude();


        dialog.show();
    }
}