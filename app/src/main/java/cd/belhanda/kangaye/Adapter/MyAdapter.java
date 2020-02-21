package cd.belhanda.kangaye.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cd.belhanda.kangaye.Modele.Contact;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import cd.belhanda.kangaye.ui.contact.ContactFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerVH> {

    Context c;
    ArrayList<Inscription_Modele> mList;
    String key, nom, prenom, pseudo, telephone, mail;

    public MyAdapter(Context c, ArrayList<Inscription_Modele> mList) {
        this.c = c;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.add_contact_modele, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.RecyclerVH holder, final int position) {

        DatabaseReference mDatabase;

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


        holder.pseudo.setText(mList.get(position).getPseudo());
        holder.telephone.setText(mList.get(position).getTelephone());
        holder.addContactClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference updateData;

                updateData = FirebaseDatabase.getInstance().getReference("Contacts").child(key).child(mList.get(position).getPseudo());
                updateData.child("telephone").setValue(mList.get(position).getTelephone());
                updateData.child("pseudo").setValue(mList.get(position).getPseudo());
                updateData.child("profil").setValue(mList.get(position).getProfil());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder{

        CircleImageView profil;
        TextView telephone;
        TextView pseudo;
        ImageView addContactClick;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);

            profil = (CircleImageView) itemView.findViewById(R.id.profilAddContactReycler);
            telephone = (TextView) itemView.findViewById(R.id.telephoneAddContactRecycler);
            pseudo = (TextView) itemView.findViewById(R.id.pseudoAddCOntactRecycler);
            addContactClick = itemView.findViewById(R.id.addContactBtnClick);

        }
    }
}
