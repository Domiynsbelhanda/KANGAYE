package cd.belhanda.kangaye.Adapter;

import android.content.Context;
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

import cd.belhanda.kangaye.MainActivity;
import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import cd.belhanda.kangaye.ui.contact.ContactFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapterAffichage extends RecyclerView.Adapter<MyAdapterAffichage.RecyclerVH> {

    Context c;
    ArrayList<Inscription_Modele> mListe;
    AddClickListener addClickListener;

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    public MyAdapterAffichage(Context c, ArrayList<Inscription_Modele> mListe) {
        this.c = c;
        this.mListe = mListe;
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.delete_contact_modele, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapterAffichage.RecyclerVH holder, final int position) {

        final Inscription_Modele modele = mListe.get(position);

        holder.pseudo_Aff.setText(mListe.get(position).getPseudo());
        holder.telephone_Aff.setText(mListe.get(position).getTelephone());
        holder.delContactClick_Aff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addClickListener != null){
                    addClickListener.onDeleteClick(modele);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListe.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder{

        CircleImageView profil_Aff;
        TextView telephone_Aff;
        TextView pseudo_Aff;
        ImageView delContactClick_Aff;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);

            profil_Aff = (CircleImageView) itemView.findViewById(R.id.profil_Aff);
            telephone_Aff = (TextView) itemView.findViewById(R.id.telephone_Aff);
            pseudo_Aff = (TextView) itemView.findViewById(R.id.pseudo_Aff);
            delContactClick_Aff = itemView.findViewById(R.id.delAffichageContact);

        }
    }
}
