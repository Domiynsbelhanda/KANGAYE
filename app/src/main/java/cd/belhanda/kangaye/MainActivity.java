package cd.belhanda.kangaye;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;

import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.ui.SOS.SOSFragment;
import cd.belhanda.kangaye.ui.alerte.AlerteFragment;
import cd.belhanda.kangaye.ui.contact.ContactFragment;
import cd.belhanda.kangaye.ui.home.HomeFragment;
import cd.belhanda.kangaye.ui.profil.ProfilFragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference mDatabase;
    CircleImageView headerProfil;
    TextView headerPseudo, headerNom;

    String pseudo, key;

    private GoogleMap mMap;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static final int INITIAL_REQUEST=1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        try {
            FileInputStream inputStream = openFileInput("Users.txt");
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
            FileInputStream inputStream = openFileInput("Key.txt");
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


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        

        displaySelectedScreen(R.id.nav_home);

        headerProfil = view.findViewById(R.id.headerProfil);
        headerNom = view.findViewById(R.id.headerNom);
        headerPseudo = view.findViewById(R.id.headerPseudo);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Inscription_Modele inscription = dataSnapshot.getValue(Inscription_Modele.class);

                Picasso.get().load(inscription.getProfil()).into(headerProfil);
                headerNom.setText(inscription.getNom());
                headerPseudo.setText(inscription.getPseudo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        displaySelectedScreen(menuItem.getItemId());

        return true;
    }

    public void displaySelectedScreen(int itemId){

        Fragment fragment = null;

        switch (itemId){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_profil:
                fragment = new ProfilFragment();
                break;
            case R.id.nav_contact:
                fragment = new ContactFragment();
                break;
            case R.id.nav_alert:
                fragment = new AlerteFragment();
                break;
            case R.id.nav_sos:
                fragment = new SOSFragment();
                break;
            case R.id.nav_carte:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            case R.id.nav_deconnecte:
                fragment = new HomeFragment();
                break;
        }

        if(itemId == R.id.nav_deconnecte){
            deconnection();
        }

        if(fragment != null && itemId != R.id.nav_carte){

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            } else{
                startActivity(new Intent(this, MapsActivity.class));
        }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
    }

    public void deconnection(){
        try {
            FileOutputStream outputStream = openFileOutput("Users.txt", MODE_PRIVATE);
            outputStream.write(("").getBytes());

            FileOutputStream outputStream1 = openFileOutput("Key.txt", MODE_PRIVATE);
            outputStream1.write(("").getBytes());

            if (outputStream != null)
                outputStream.close();

            if (outputStream1 != null)
                outputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, Splash_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public String key(){
        return key;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }
}
