package cd.belhanda.kangaye.ui.contact;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cd.belhanda.kangaye.Adapter.AddClickListener;
import cd.belhanda.kangaye.Adapter.ItemClickListener;
import cd.belhanda.kangaye.Adapter.MyAdapter;
import cd.belhanda.kangaye.Adapter.MyAdapterAffichage;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;

public class ContactFragment extends Fragment implements ItemClickListener, AddClickListener {

    private ContactViewModel contactViewModel;

    private Button btnAddContact, btnDeleteContact;
    private EditText editAddContact;
    RecyclerView recyclerViewAddContact, recyclerContact;
    ArrayList<Inscription_Modele> mList = new ArrayList<>();
    ArrayList<Inscription_Modele> mListe = new ArrayList<>();
    private String key, pseudo;

    MyAdapter adapter;
    MyAdapterAffichage adaptere;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                ViewModelProviders.of(this).get(ContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        contactViewModel.getText().observe(this, new Observer<String>() {
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

        recyclerContact = root.findViewById(R.id.recyclerDeleteContact);
        recyclerViewAddContact = root.findViewById(R.id.recyclerAddContact);

        btnAddContact = root.findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAddContact.setVisibility(View.VISIBLE);
                recyclerViewAddContact.setVisibility(View.VISIBLE);

            }
        });


        recyclerContact.setVisibility(View.GONE);
        recyclerContact.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference references;
        references = FirebaseDatabase.getInstance().getReference("Contacts").child(key);
        references.keepSynced(true);


        Query ref = references.orderByChild("pseudo");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mListe.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Inscription_Modele n = dataSnapshot1.getValue(Inscription_Modele.class);
                    mListe.add(n);
                }

                recyclerContact.setAdapter(adaptere);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        adaptere = new MyAdapterAffichage(getActivity(), mListe);
        adaptere.setAddClickListener(this);

        btnDeleteContact = root.findViewById(R.id.btnContactDelete);
        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerContact.setVisibility(View.VISIBLE);
                editAddContact.setVisibility(View.GONE);
                btnAddContact.setVisibility(View.GONE);
            }
        });

        recyclerViewAddContact.setLayoutManager(new LinearLayoutManager(getActivity()));

        editAddContact = root.findViewById(R.id.editAddContact);
        editAddContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DatabaseReference reference;
                reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.keepSynced(true);

                String recherche = editAddContact.getText().toString();

                Query ref = reference.orderByChild("pseudo").startAt(recherche.toUpperCase()).endAt(recherche.toLowerCase()+"\uf8ff");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mList.clear();

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            Inscription_Modele n = dataSnapshot1.getValue(Inscription_Modele.class);

                            if (!n.getPseudo().equals(pseudo)){
                                mList.add(n);
                            }
                        }
                        recyclerViewAddContact.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        adapter = new MyAdapter(getActivity(), mList);
        adapter.setItemClickListener(this);

        return root;
    }

    @Override
    public void onItemClick(Inscription_Modele inscription_modele) {

        DatabaseReference updateData;
        updateData = FirebaseDatabase.getInstance().getReference("Contacts").child(key);
        updateData = updateData.child(inscription_modele.getPseudo());

        updateData.child("telephone").setValue(inscription_modele.getTelephone());
        updateData.child("pseudo").setValue(inscription_modele.getPseudo());
        updateData.child("profil").setValue(inscription_modele.getProfil());

        Toast.makeText(getActivity(), inscription_modele.getPseudo() + " a été ajouté", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteClick(Inscription_Modele inscription_modele) {
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Contacts").child(key).child(inscription_modele.getPseudo());
        mDatabase.removeValue();

        Toast.makeText(getActivity(), inscription_modele.getPseudo() + " a été supprimé", Toast.LENGTH_LONG).show();
    }
}