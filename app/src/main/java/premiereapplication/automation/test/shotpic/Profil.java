package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Dorian on 22/03/2017.
 */
public class Profil extends AppCompatActivity {
    private static final String url = "jdbc:mysql://sdigo.cf/projets4";
    private static final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private Statement st = null;
    private TextView nom;
    private TextView pseudo;
    private TextView prenom;
    private EditText email;
    private Button changeMdp;
    private Button valider;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        //regarde si connecté
        final int idcompte = (int) getIntent().getIntExtra("idcompte", 0);
        final String mdp = (String) getIntent().getStringExtra("motDePasse");

        nom = (TextView) findViewById(R.id.nom);
        pseudo = (TextView) findViewById(R.id.pseudo);
        prenom = (TextView) findViewById(R.id.prenom);
        email = (EditText) findViewById(R.id.email);
        changeMdp = (Button) findViewById(R.id.button_modifMdp);
        valider = (Button) findViewById(R.id.button_valider);
        image = (ImageView) findViewById(R.id.photoUpload);

        //Redirection vers la page modfier mot de passe (MdpChange)+ activity_modifiermdp
        Button buttonModif = (Button) findViewById(R.id.button_modifMdp);
        buttonModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, MdpChange.class);
                intent.putExtra("idcompte", idcompte);
                intent.putExtra("motDePasse",mdp);
                startActivity(intent);
            }
        });

        //si on clique sur le bouton pour valider et modifier l'email
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Connection conn = DriverManager.getConnection(url, user, psw);
                    st = conn.createStatement();
                    //pour recuperer l'utilisateur connecté
                    PreparedStatement sql = conn.prepareStatement("Update projets4.compte set email = ? where pseudo='" + Connexion.pseudoUser + "'");
                    sql.setString(1, email.getText().toString());
                    sql.executeUpdate();
                    Toast.makeText(getApplicationContext(), "Email changé !", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //redirige vers le profil
                Intent intent = new Intent(Profil.this, Profil.class);
                startActivity(intent);
            }
        });


        //regarde si bien connecté
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            //affichage sur l'appli
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        profilUtilisateur();

        //si clique sur mes commentaires donc redirection
        Button buttonCommentaire = (Button) findViewById(R.id.commentaires);
        buttonCommentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profil.this, MesCommentaires.class);
                startActivity(intent);


            }
        });

        Button buttonVote = (Button) findViewById(R.id.votes);
        buttonVote.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                startActivity(new Intent(Profil.this,MesVotes.class));
            }
        });
    }

    //permet de restituer le profil de l'user
    public void profilUtilisateur(){
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);
            st = conn.createStatement();

            //afficher information utilisateurs
            String sql ="SELECT * from projets4.compte where pseudo='" + Connexion.pseudoUser + "'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                nom.setText(rs.getString("nom"));
                prenom.setText(rs.getString("prenom"));
                email.setText(rs.getString("email"));
                pseudo.setText(rs.getString("pseudo"));

            }
            //get id de l'utilisateur connecté
            String sqliD = "SELECT idcompte FROM compte where compte.pseudo='"+ Connexion.pseudoUser+"'";
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sqliD);
            rst.next();
            //on récupere l'id dans var
            int idcompte=  rst.getInt("idcompte");
            String sqlphoto ="SELECT * FROM `photo` natural join compte where compte.idcompte=photo.idcompte and compte.idcompte='" + idcompte + "'";
            ResultSet rs2 = st.executeQuery(sqlphoto);
            rs2.next();
            Picasso.with(getApplicationContext()).load("http://5.51.224.40/projetS4/site/source/" + rs2.getString("nomPhoto")).into(image);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}