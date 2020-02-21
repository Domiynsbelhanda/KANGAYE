package cd.belhanda.kangaye;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.ui.SOS.SOSFragment;
import cd.belhanda.kangaye.ui.alerte.AlerteFragment;
import cd.belhanda.kangaye.ui.contact.ContactFragment;
import cd.belhanda.kangaye.ui.home.HomeFragment;
import cd.belhanda.kangaye.ui.profil.ProfilFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DatabaseReference mDatabase;
    String key, nom, prenom, pseudo, telephone, mail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        

        displaySelectedScreen(R.id.nav_home);

        /** Firebase get key start*/
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

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
                Bundle bundle1 = new Bundle();
                bundle1.putString("key", key);
                bundle1.putString("nom", nom);
                bundle1.putString("prenom", prenom);
                bundle1.putString("pseudo", pseudo);
                bundle1.putString("telephone", telephone);
                bundle1.putString("mail", mail);
                fragment = new HomeFragment();
                fragment.setArguments(bundle1);
                break;
            case R.id.nav_profil:
                Bundle bundle = new Bundle();
                bundle.putString("key", key);
                bundle.putString("nom", nom);
                bundle.putString("prenom", prenom);
                bundle.putString("pseudo", pseudo);
                bundle.putString("telephone", telephone);
                bundle.putString("mail", mail);
                fragment = new ProfilFragment();
                fragment.setArguments(bundle);

                break;
            case R.id.nav_contact:
                Bundle bundles = new Bundle();
                bundles.putString("key", key);
                bundles.putString("nom", nom);
                bundles.putString("prenom", prenom);
                bundles.putString("pseudo", pseudo);
                bundles.putString("telephone", telephone);
                bundles.putString("mail", mail);
                fragment = new ContactFragment();
                fragment.setArguments(bundles);
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
}
