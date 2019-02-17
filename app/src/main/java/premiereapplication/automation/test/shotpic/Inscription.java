package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Vijay on 11/03/2017.
 */
public class Inscription extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static final String salt="jnrth4r8t4a464erarzvsd";

    private Button bValider;
    private EditText etnom,etprenom,etpseudo,etmail,etmdp,etconfmdp;
    private CheckBox cocher;
    private TextInputLayout inputLayoutEmail;
    private static Connection conn;
    final String userName="shotpicofficiel@gmail.com";
    final String password="shotpicmontreuil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        etnom=(EditText) findViewById(R.id.nom);
        etprenom=(EditText) findViewById(R.id.prenom);
        etpseudo=(EditText) findViewById(R.id.pseudo);
        etmail=(EditText) findViewById(R.id.email);
        etmdp=(EditText) findViewById(R.id.mdp);
        etconfmdp=(EditText) findViewById(R.id.mdp_conf);
        bValider=(Button) findViewById(R.id.valider);
        cocher=(CheckBox) findViewById(R.id.check);

        bValider.setOnClickListener(this);
        cocher.setOnCheckedChangeListener(this);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
        if(isChecked){
            cocher.setText("Vous acceptez les conditions d'utilisations");
        }else{
            cocher.setText("Vous n'acceptez pas les conditions d'utilisations");
        }
    }
    @Override
    public void onClick(View v){
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);
            String sqliD = "SELECT email FROM compte where email='"+etmail.getText().toString()+"'";
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sqliD);



            String sql ="INSERT INTO compte(email, motDePasse,pseudo,nom,prenom,modo,admin,activer )VALUES(?,SHA2(?,256) ,?,?,?,0,0,0)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,etmail.getText().toString());
            preparedStatement.setString(2, etmdp.getText().toString().concat(salt));
            preparedStatement.setString(3,etpseudo.getText().toString());
            preparedStatement.setString(4,etnom.getText().toString());
            preparedStatement.setString(5, etprenom.getText().toString());
            rst.next();
            int nombreLignes = rst.getRow();

                if (etpseudo.getText().toString().equals("") || etmail.getText().toString().equals("") ||
                        etmdp.getText().toString().equals("") || etconfmdp.getText().toString().equals("") ||
                        etnom.getText().toString().equals("") || etprenom.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                } else if (!etmdp.getText().toString().equals(etconfmdp.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Les mots de passes sont différents", Toast.LENGTH_SHORT).show();
                } else if (!cocher.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Vous n'avez pas acceptée les CGU, donc vous pouvez pas vous inscrire !", Toast.LENGTH_SHORT).show();
                }else {
                    preparedStatement.executeUpdate();
                    Toast.makeText(getApplicationContext(), "Vous vous êtes bien inscrit !", Toast.LENGTH_SHORT).show();
                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", 587);
                    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(userName, password);
                        }
                    });
                    try {
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("shotpicofficiel@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(etmail.getText().toString()));
                        message.setSubject("Inscription réussie");
                        message.setText("Bonjour,\n Votre inscription s'est effectué avec succès.\n Voici votre pseudo : " + etpseudo.getText().toString() + "\n Voici votre mot de passe : " + etmdp.getText().toString()+"\n\n\n\n\n\nTeam ShotPic\n" +
                                "Sankar Vijay : Mobile Application Developer \n" +
                                "Vieira Dorian : Mobile Application Developer\n" +
                                "Neto Nicolas : Web Developer\n" +
                                "Benserida Youcef : Web Developer\n");
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    String updateCompte = "UPDATE compte SET activer=1 WHERE pseudo='" + etpseudo.getText().toString() + "'";
                    PreparedStatement preparedStatement2 = conn.prepareStatement(updateCompte);
                    preparedStatement2.executeUpdate();

                    startActivity(new Intent(Inscription.this, Connexion.class));
                }

        }catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Un compte existe deja avec ce pseudo et cette adresse mail", Toast.LENGTH_SHORT).show();
        }
    }
}