package premiereapplication.automation.test.shotpic;

/**
 * Created by Dorian on 22/03/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//pour modifier le mot de passe après avoir cliquer dans le profil
public class MdpChange extends AppCompatActivity {

    private static final String url = "jdbc:mysql://sdigo.cf/projets4";
    private static final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private Statement st = null;
    private static final String salt="jnrth4r8t4a464erarzvsd";
    private EditText mdpChange1;
    private EditText mdpChange2;
    private EditText mdpActuel;
    //private Button modifMdp;
    private Button confimerMdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiermdp);
         int idcompte = (int) getIntent().getIntExtra("idcompte", 0);


        mdpActuel= (EditText) findViewById(R.id.votreMdp);
        mdpChange1 = (EditText) findViewById(R.id.mdp1);
        mdpChange2 = (EditText) findViewById(R.id.mdp2);

        confimerMdp = (Button) findViewById(R.id.button_confirmMdp);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        changeMotdep();

    }

    public void changeMotdep() {


        confimerMdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String mdp = (String) getIntent().getStringExtra("motDePasse");

                    Connection conn = DriverManager.getConnection(url, user, psw);
                    //on fait la verification du mdp


                    String sqlmdp = ("SELECT motDePasse FROM projets4.compte where pseudo='"+ Connexion.pseudoUser +"' and motDePasse=SHA2('"+mdpActuel.getText().toString().concat(salt)+"',256)");
                    st = conn.createStatement();

                    //resultat du mdp
                    ResultSet rs = st.executeQuery(sqlmdp);
                    rs.next();

                    if(mdpActuel.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Renseignez votre mot de passe actuel", Toast.LENGTH_SHORT).show();
                    }
                    else if(!mdp.equals(rs.getString("motDePasse"))){
                        Toast.makeText(getApplicationContext(), "Votre mot de passe est incorrect" , Toast.LENGTH_SHORT).show();
                    }
                    else if (!mdpChange1.getText().toString().equals(mdpChange2.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Les mots de passes sont différents", Toast.LENGTH_SHORT).show();
                    }

                    else if (mdpChange1.getText().toString().isEmpty() || mdpChange2.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Renseignez le nouveau mot de passe", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        String sql = "Update projets4.compte set motDePasse= SHA2('" + mdpChange1.getText().toString().concat(salt) + "',256)" + "where pseudo='" + Connexion.pseudoUser + "'";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.executeUpdate(sql);
                        //si ok on modifie le mdp
                        Toast.makeText(getApplicationContext(), "Mot de passe changé avec succès!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MdpChange.this, Connexion.class));
                    }
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "Erreur modification mot de passe!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}