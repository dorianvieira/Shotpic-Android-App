package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Vijay on 16/03/2017.
 */
public class AccueilCo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int idcompte = (int) getIntent().getIntExtra("idcompte", 0);
        final String mdp = (String) getIntent().getStringExtra("motDePasse");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueilco);
        //Redirection vers la page regle
        Button buttonRegle = (Button) findViewById(R.id.reglement);

        buttonRegle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilCo.this, Regle.class);
                intent.putExtra("idcompte", idcompte);
                startActivity(intent);
            }
        });
        //Redirection vers la page photos
        Button buttonPhotos = (Button) findViewById(R.id.photos);
        buttonPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilCo.this, Photos.class);
                intent.putExtra("idcompte", idcompte);
                startActivity(intent);

            }
        });

        Button buttonDeco = (Button) findViewById(R.id.deconnexion);
        buttonDeco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilCo.this, Accueil.class);
                startActivity(intent);
            }
        });

        //Redirection vers la profil de l'utilisateur
        Button buttonProfil = (Button) findViewById(R.id.profil);
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccueilCo.this, Profil.class);
                intent.putExtra("motDePasse",mdp);
                startActivity(intent);


            }
        });
    }
}

