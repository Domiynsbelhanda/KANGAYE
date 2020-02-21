package cd.belhanda.kangaye.ui.contact;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.ArrayList;

import cd.belhanda.kangaye.Adapter.MyAdapter;
import cd.belhanda.kangaye.Adapter.MyAdapterAffichage;
import cd.belhanda.kangaye.Modele.Contact;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;

public class ContactFragment extends Fragment{

    private ContactViewModel contactViewModel;

    private CardView cardViewAddContact;
    private Button btnAddContact, btnDeleteContact;
    private EditText editAddContact;
    RecyclerView recyclerViewAddContact, recyclerContact;
    ArrayList<Inscription_Modele> mList = new ArrayList<>();
    ArrayList<Inscription_Modele> mListe = new ArrayList<>();
    private String key, nom, prenom, pseudo, telephone, mail;

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

        key = getArguments().getString("key");
        nom = getArguments().getString("nom");
        prenom = getArguments().getString("prenom");
        pseudo = getArguments().getString("pseudo");
        telephone = getArguments().getString("telephone");
        mail = getArguments().getString("mail");

        cardViewAddContact = root.findViewById(R.id.cardViewAddContact);
        cardViewAddContact.setVisibility(View.GONE);

        btnAddContact = root.findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewAddContact.setVisibility(View.VISIBLE);
                btnAddContact.setVisibility(View.GONE);
            }
        });

        recyclerContact = root.findViewById(R.id.recyclerDeleteContact);
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
                recyclerContact.setAdapter(new MyAdapterAffichage(getActivity(), mListe));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnDeleteContact = root.findViewById(R.id.btnContactDelete);
        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerContact.setVisibility(View.VISIBLE);
            }
        });


        recyclerViewAddContact = root.findViewById(R.id.recyclerAddContact);
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
                        recyclerViewAddContact.setAdapter(new MyAdapter(getActivity(), mList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });





        return root;
    }

}