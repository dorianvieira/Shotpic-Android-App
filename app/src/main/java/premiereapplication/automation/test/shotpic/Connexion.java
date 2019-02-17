package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vijay on 12/03/2017.
 */
public class Connexion extends AppCompatActivity  implements View.OnClickListener {
    private TextView mdpoubli;
    private TextView inscrit;
    private EditText pseudo,mdp;
    private Button bConnexion;
    private Button buttonInscription;
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private static final String salt="jnrth4r8t4a464erarzvsd";
    public static String pseudoUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        pseudo = (EditText)findViewById(R.id.pseudo);
        mdp=(EditText)findViewById(R.id.mdp);
        bConnexion=(Button)findViewById(R.id.button_connexion);

       //Redirection vers la page MdpOublie
        mdpoubli = (TextView)findViewById(R.id.mdpOublie);
        mdpoubli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, MdpOublie.class));
            }
        });

        //Redirection vers la page inscription
        inscrit = (TextView)findViewById(R.id.inscription);
        inscrit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Connexion.this, Inscription.class));
            }
        });

        bConnexion.setOnClickListener(this);


        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    @Override
    public void onClick(View v){
        try {
            Connection conn = DriverManager.getConnection(url, user, psw);

            String sql = "SELECT * FROM compte where pseudo='"+pseudo.getText()+"' and motDePasse=SHA2('"+mdp.getText().toString().concat(salt)+"',256)";

            Statement st = conn.createStatement();

            if(pseudo.getText().toString().equals("") || mdp.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
            }else{
                ResultSet rs = st.executeQuery(sql);
                rs.next();
                pseudoUser=String.valueOf(pseudo.getText());

                Intent intent = new Intent(Connexion.this, AccueilCo.class);
                intent.putExtra("idcompte", rs.getInt("idcompte"));
                intent.putExtra("motDePasse",rs.getString("motDePasse"));
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Vous êtes bien connectée !", Toast.LENGTH_SHORT).show();

            }

        }catch(SQLException e){
            Toast.makeText(getApplicationContext(), "Erreur pseudo ou mot de passe incorrect !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
