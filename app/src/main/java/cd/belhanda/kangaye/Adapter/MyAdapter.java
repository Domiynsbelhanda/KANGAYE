package cd.belhanda.kangaye.Adapter;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cd.belhanda.kangaye.Modele.Inscription_Modele;
import cd.belhanda.kangaye.R;
import de.hdodenhof.circleimageview.CircleImageView;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerVH> {

    Context c;
    ArrayList<Inscription_Modele> mList;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyAdapter(Context c, ArrayList<Inscription_Modele> mList){
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

        final Inscription_Modele modele = mList.get(position);

        holder.pseudo.setText(mList.get(position).getPseudo());
        holder.telephone.setText(mList.get(position).getTelephone());
        holder.addContactClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemClickListener != null){
                    itemClickListener.onItemClick(modele);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {

        CircleImageView profil;
        TextView telephone;
        TextView pseudo;
        ImageView addContactClick;


        public RecyclerVH(@NonNull final View itemView) {
            super(itemView);

            profil = (CircleImageView) itemView.findViewById(R.id.profilAddContactReycler);
            telephone = (TextView) itemView.findViewById(R.id.telephoneAddContactRecycler);
            pseudo = (TextView) itemView.findViewById(R.id.pseudoAddCOntactRecycler);
            addContactClick = itemView.findViewById(R.id.addContactBtnClick);

        }
    }
}
