package cd.belhanda.kangaye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Authentification extends AppCompatActivity {

    private CardView inscription_click, connexion_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        inscription_click = (CardView) findViewById(R.id.inscription_click);
        inscription_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inscription = new Intent(Authentification.this, Inscription.class);
                startActivity(inscription);
            }
        });

        connexion_click = findViewById(R.id.connexion_click);
        connexion_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connexion = new Intent(Authentification.this, Connexion.class);
                startActivity(connexion);
            }
        });
    }
}
