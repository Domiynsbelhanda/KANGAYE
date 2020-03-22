package cd.belhanda.kangaye.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cd.belhanda.kangaye.Modele.AlertesAdd;
import cd.belhanda.kangaye.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmisAdapter  extends RecyclerView.Adapter<EmisAdapter.RecyclerVH> {

    Context c;
    ArrayList<AlertesAdd> mList;
    EmisClick emisClick;

    public void setEmisClick(EmisClick emisClick) {
        this.emisClick = emisClick;
    }

    public EmisAdapter(Context c, ArrayList<AlertesAdd> mList) {
        this.c = c;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.emismodele, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVH holder, int position) {

        final AlertesAdd alertesAdd = mList.get(position);

        holder.categorie.setText(mList.get(position).getCategorie());
        holder.date.setText("Date : " + mList.get(position).getDate() + " Heure : " + mList.get(position).getHeure());
        holder.heure.setText(mList.get(position).getDetails());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emisClick !=null ){
                    emisClick.onItemClick(alertesAdd);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder{

        TextView categorie;
        TextView date, heure;

        public RecyclerVH(@NonNull View itemView) {
            super(itemView);

            categorie = itemView.findViewById(R.id.emisCategorie);
            date = itemView.findViewById(R.id.emisDate);
            heure = itemView.findViewById(R.id.emisHeure);
        }
    }
}
