package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Accueil extends AppCompatActivity {
    public static Button buttonInscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        //Redirection vers la page inscription
      buttonInscription = (Button) findViewById(R.id.button_inscription);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancer l'activite Inscription
                Intent intent = new Intent(Accueil.this, Inscription.class);
                startActivity(intent);
            }
        });

        //Redirection vers la page de connexion
        Button buttonConnexion = (Button) findViewById(R.id.button_connexion);
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancer l'activite Connexion
                Intent intent = new Intent(Accueil.this, Connexion.class);
                startActivity(intent);
            }
        });

        //Redirection vers la page regle
        Button buttonRegle=(Button) findViewById(R.id.button_regle);
        buttonRegle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accueil.this,Regle.class);
                startActivity(intent);
            }
        });
        //Redirection vers la page photos
        Button buttonPhotos=(Button) findViewById(R.id.button_photos);
        buttonPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Accueil.this,Photos.class);
                startActivity(intent);
            }
        });
    }
}
