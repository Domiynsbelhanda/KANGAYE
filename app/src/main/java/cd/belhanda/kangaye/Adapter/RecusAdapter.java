package cd.belhanda.kangaye.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cd.belhanda.kangaye.Modele.AlertesAdd;
import cd.belhanda.kangaye.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecusAdapter extends RecyclerView.Adapter<RecusAdapter.RecyclerVH> {

    Context c;
    ArrayList<AlertesAdd> mListe;
    RecusClick recusClick;

    public void setRecusClick(RecusClick recusClick) {
        this.recusClick = recusClick;
    }

    public RecusAdapter(Context c, ArrayList<AlertesAdd> mListe) {
        this.c = c;
        this.mListe = mListe;
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.recusmodele, parent, false);
        return new RecusAdapter.RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {
        final AlertesAdd alertesAdd = mListe.get(position);

        holder.distance.setText(String.valueOf(mListe.get(position).getDistance()));
        holder.categorie.setText(mListe.get(position).getCategorie());
        holder.date.setText(mListe.get(position).getDate() + " à " + mListe.get(position).getHeure());
        holder.pseudo.setText(mListe.get(position).getPseudo());
        Picasso.get().load(mListe.get(position).getProfil()).into(holder.profil);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recusClick !=null ){
                    recusClick.onItemClick1(alertesAdd);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListe.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder{

        CircleImageView profil;
        TextView pseudo, categorie;
        TextView distance, date;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);

            profil = itemView.findViewById(R.id.profilRecus);
            pseudo = itemView.findViewById(R.id.pseudoRecus);
            categorie = itemView.findViewById(R.id.recusCategorie);
            distance = itemView.findViewById(R.id.recusDistance);
            date = itemView.findViewById(R.id.recusDate);
        }
    }
}
