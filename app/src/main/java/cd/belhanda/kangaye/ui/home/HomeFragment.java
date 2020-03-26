package cd.belhanda.kangaye.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cd.belhanda.kangaye.MapsUsing;
import cd.belhanda.kangaye.Modele.AlertesAdd;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import cd.belhanda.kangaye.ui.profil.ProfilFragment;
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

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static final int INITIAL_REQUEST=1337;

    private HomeViewModel homeViewModel;

    CardView SOS, ALERTE, Assistance, AssistanceNon;
    FirebaseDatabase mDatabase;
    boolean alerteSos;
    String pseudo, key, categorieMenaces, detailMenaces, datesEnvoie, heuresEnvoie;

    Dialog dialog;
    View viewLancer;

    Button send_sos_alerte, annule_send;
    EditText categorieMenace, detailMenace, dateEnvoie, heureEnvoie;

    double Longitude, Latitude;
    TextView ChoixSurCarte;
    RadioButton ChoixVotrePosition;

    String pseudoss, telephoness, profilss;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat heureFormat = new SimpleDateFormat("HH:mm");

    private static final int SECOND_ACTIVIT = 0;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mape);
        mapFragment.getMapAsync(this);

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

        mDatabase = FirebaseDatabase.getInstance();

        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewLancer = LayoutInflater.from(getActivity()).inflate(R.layout.sos_alerte_send, null);

        send_sos_alerte = viewLancer.findViewById(R.id.btnSendAlertes);
        annule_send = viewLancer.findViewById(R.id.btnAnnules);

        annule_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                AssistanceNon.setVisibility(View.VISIBLE);
            }
        });

        dateEnvoie = viewLancer.findViewById(R.id.dateEnvoie);
        heureEnvoie = viewLancer.findViewById(R.id.heureEnvoie);
        categorieMenace = viewLancer.findViewById(R.id.categoriemenace);
        detailMenace = viewLancer.findViewById(R.id.detailMenace);
        ChoixSurCarte = viewLancer.findViewById(R.id.ChoixsurCarte);
        ChoixVotrePosition = viewLancer.findViewById(R.id.ChoixVotrePosition);

        Date currentTime = Calendar.getInstance().getTime();
        dateEnvoie.setHint(dateFormat.format(currentTime));
        heureEnvoie.setHint(heureFormat.format(currentTime));

        dialog.setContentView(viewLancer);

        Assistance = root.findViewById(R.id.cardAssistance);
        AssistanceNon = root.findViewById(R.id.cardAssistancenon);

        SOS = root.findViewById(R.id.SOSLANCEUR);
        ALERTE = root.findViewById(R.id.ALERTELANCEUR);

        AssistanceNon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrolling();
            }
        });
        Assistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrolling2();
            }
        });

        ALERTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssistanceNon.setVisibility(View.GONE);
                Assistance.setVisibility(View.GONE);
                alerteSos = true;
                dialog.show();

            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssistanceNon.setVisibility(View.GONE);
                Assistance.setVisibility(View.GONE);
                alerteSos = false;
                dialog.show();
            }
        });

        ChoixSurCarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsUsing.class);
                intent.putExtra("position", "position");
                startActivityForResult(intent, SECOND_ACTIVIT);
            }
        });

        ChoixVotrePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.getReference("Users").child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);
                        Longitude = inscription.getLongitude();
                        Latitude = inscription.getLatitude();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                ChoixSurCarte.setText("Long : " + Longitude + " et Lat : " + Latitude + " " +
                        "Ou clickez ici pour Choisir");
            }

        });

        send_sos_alerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorieMenaces = categorieMenace.getText().toString();
                detailMenaces = detailMenace.getText().toString();
                datesEnvoie = dateEnvoie.getText().toString();
                heuresEnvoie = heureEnvoie.getText().toString();

                if(TextUtils.isEmpty(categorieMenaces)){
                    categorieMenace.setError("Veuillez entrée la catérogie");
                    return;
                }

                if(TextUtils.isEmpty(detailMenaces)){
                    detailMenaces = "Pas de détail Publier";
                }

                if(TextUtils.isEmpty(datesEnvoie)){
                    datesEnvoie = dateFormat.format(currentTime);
                }

                if(TextUtils.isEmpty(heuresEnvoie)){
                    heuresEnvoie = heureFormat.format(currentTime);
                }

                if(ChoixVotrePosition.isChecked() == false && TextUtils.isEmpty(String.valueOf(Latitude))){
                    Toast.makeText(getActivity(), "Veuillez choisir la position", Toast.LENGTH_LONG).show();
                    return;
                }

                if(ChoixVotrePosition.isChecked()){
                    mDatabase.getReference("Users").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);
                            Longitude = inscription.getLongitude();
                            Latitude = inscription.getLatitude();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                ChoixSurCarte.setText("Long : " + Longitude + " et Lat : " + Latitude);

                if(alerteSos){
                    Alertes();
                    dialog.cancel();
                    AssistanceNon.setVisibility(View.VISIBLE);
                } else{
                    SOS();
                    dialog.cancel();
                    AssistanceNon.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    public void scrolling(){
        Assistance.setVisibility(View.VISIBLE);
        AssistanceNon.setVisibility(View.GONE);
    }

    public void scrolling2(){
        Assistance.setVisibility(View.GONE);
        AssistanceNon.setVisibility(View.VISIBLE);
    }

    public void Alertes(){
        mDatabase.getReference("Users").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);
                telephoness = inscription.getTelephone();
                pseudoss = inscription.getPseudo();
                profilss = inscription.getProfil();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String nom = mDatabase.getReference("Alertes").push().getKey();
        AlertesAdd alertesAdd = new AlertesAdd(
                categorieMenaces, detailMenaces, heuresEnvoie, datesEnvoie, nom, key, Longitude, Latitude, pseudoss, telephoness
        );
        mDatabase.getReference("Alertes").child(nom).setValue(alertesAdd);
        mDatabase.getReference("Users").child(key).child("Alertes").child("EMIS").child(nom).setValue(alertesAdd);

        DatabaseReference mDatabase1;
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Inscription_Modele inscription = dataSnapshot1.getValue(Inscription_Modele.class);

                    LatLng from = new LatLng(Latitude, Longitude);
                    LatLng to = new LatLng(inscription.getLatitude(), inscription.getLongitude());

                    final double distance = SphericalUtil.computeDistanceBetween(from, to);

                    if (distance < 1000){
                        if(!key.equals(dataSnapshot1.getKey())){

                            DatabaseReference add = mDatabase.getReference("Alertes")
                                    .child(nom)
                                    .child("SendTo")
                                    .child(dataSnapshot1.getKey());
                            AlertesAdd alertesAdd1 = new AlertesAdd(distance, inscription.getPseudo(),
                                    inscription.getProfil(), inscription.getTelephone(), Longitude, Latitude);
                            add.setValue(alertesAdd1);

                            DatabaseReference adde = mDatabase.getReference("Users")
                                    .child(dataSnapshot1.getKey())
                                    .child("Alertes")
                                    .child("RECUS")
                                    .child(nom);

                            AlertesAdd alertesAdd2 = new AlertesAdd(
                                    categorieMenaces, detailMenaces, heuresEnvoie, datesEnvoie,
                                    nom, key, Longitude, Latitude, pseudoss, telephoness, false, false, distance, inscription.getProfil()
                            );

                            adde.setValue(alertesAdd2);

                            final String ACCOUNT_SID = "ACd8f59147afe1457001b2293925774726";
                            final String AUTH_TOKEN = "1b1c4957c59486a48133c8cc1ce75f52";

                            String body = "Vous avez recu un SOS de la part de : " + pseudoss +
                                    "\n Connectez-vous avec l'application pour plus de détail.";
                            String fromx = "+15028920185";
                            String tox = inscription.getTelephone();

                            String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

                            Map<String, String> data = new HashMap<>();
                            data.put("From", fromx);
                            data.put("To", tox);
                            data.put("Body", body);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://api.twilio.com/2010-04-01/")
                                    .build();

                            TwilioApi api = retrofit.create(TwilioApi.class);
                            api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, data).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SOS(){
        mDatabase.getReference("Users").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);
                telephoness = inscription.getTelephone();
                pseudoss = inscription.getPseudo();
                profilss = inscription.getProfil();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String nom = mDatabase.getReference("SOS").push().getKey();
        AlertesAdd alertesAdd = new AlertesAdd(
                categorieMenaces, detailMenaces, heuresEnvoie, datesEnvoie, nom, key, Longitude, Latitude, pseudoss, telephoness
        );
        mDatabase.getReference("SOS").child(nom).setValue(alertesAdd);
        mDatabase.getReference("Users").child(key).child("SOS").child("EMIS").child(nom).setValue(alertesAdd);

        DatabaseReference mDatabase1;
        mDatabase1 = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Inscription_Modele inscription = dataSnapshot1.getValue(Inscription_Modele.class);

                    LatLng from = new LatLng(Latitude, Longitude);
                    LatLng to = new LatLng(inscription.getLatitude(), inscription.getLongitude());

                    final double distance = SphericalUtil.computeDistanceBetween(from, to);

                    if (distance < 1000){
                        if(!key.equals(dataSnapshot1.getKey())){

                            DatabaseReference add = mDatabase.getReference("SOS")
                                    .child(nom)
                                    .child("SendTo")
                                    .child(dataSnapshot1.getKey());
                            AlertesAdd alertesAdd1 = new AlertesAdd(distance, inscription.getPseudo(),
                                    inscription.getProfil(), inscription.getTelephone(), Longitude, Latitude);
                            add.setValue(alertesAdd1);

                            DatabaseReference adde = mDatabase.getReference("Users")
                                    .child(dataSnapshot1.getKey())
                                    .child("SOS")
                                    .child("RECUS")
                                    .child(nom);

                            AlertesAdd alertesAdd2 = new AlertesAdd(
                                    categorieMenaces, detailMenaces, heuresEnvoie, datesEnvoie,
                                    nom, key, Longitude, Latitude, pseudoss, telephoness, false, false, distance, inscription.getProfil()
                            );

                            adde.setValue(alertesAdd2);


                            final String ACCOUNT_SID = "ACd8f59147afe1457001b2293925774726";
                            final String AUTH_TOKEN = "1b1c4957c59486a48133c8cc1ce75f52";

                            String body = "Vous avez recu un SOS de la part de : " + pseudoss +
                                    "\n Connectez-vous avec l'application pour plus de détail.";
                            String fromx = "+15028920185";
                            String tox = inscription.getTelephone();

                            String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

                            Map<String, String> data = new HashMap<>();
                            data.put("From", fromx);
                            data.put("To", tox);
                            data.put("Body", body);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("https://api.twilio.com/2010-04-01/")
                                    .build();

                            TwilioApi api = retrofit.create(TwilioApi.class);
                            api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, data)
                                    .enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                            DatabaseReference contact = mDatabase.getReference("Contacts").child(key);
                            contact.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        Inscription_Modele inscription = dataSnapshot1.getValue(Inscription_Modele.class);

                                        final String ACCOUNT_SID = "ACd8f59147afe1457001b2293925774726";
                                        final String AUTH_TOKEN = "1b1c4957c59486a48133c8cc1ce75f52";

                                        String body = "Vous avez recu un SOS de la part de : " + pseudoss +
                                                "\n Connectez-vous avec l'application pour plus de détail.";
                                        String fromx = "+15028920185";
                                        String tox = inscription.getTelephone();

                                        String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);

                                        Map<String, String> data = new HashMap<>();
                                        data.put("From", fromx);
                                        data.put("To", tox);
                                        data.put("Body", body);

                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl("https://api.twilio.com/2010-04-01/")
                                                .build();

                                        TwilioApi api = retrofit.create(TwilioApi.class);
                                        api.sendMessage(ACCOUNT_SID, base64EncodedCredentials, data)
                                                .enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                    }
                                                });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    interface TwilioApi{
        @FormUrlEncoded
        @POST("Accounts/{ACCOUNT_SID}/SMS/Messages")
        Call<ResponseBody> sendMessage(
                @Path("ACCOUNT_SID") String accountSId,
                @Header("Authorization") String signature,
                @FieldMap Map<String, String> metadata
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.clear();
                if(!TextUtils.isEmpty(key)){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(key).child("longitude").setValue(location.getLongitude());
                    databaseReference.child(key).child("latitude").setValue(location.getLatitude());
                }


                LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title("Votre position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==getActivity().checkSelfPermission(perm));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SECOND_ACTIVIT && data != null){
            Longitude = Double.parseDouble(data.getStringExtra("longitude"));
            Latitude = Double.parseDouble(data.getStringExtra("latitude"));
            ChoixSurCarte.setText("Long : " + Longitude + " Lat : " + Latitude);
        }
    }
}