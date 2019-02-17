package premiereapplication.automation.test.shotpic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.SecureRandom;
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
 * Created by Vijay on 15/03/2017.
 */
public class MdpOublie extends AppCompatActivity implements View.OnClickListener {
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static final String salt="jnrth4r8t4a464erarzvsd";
    private static Connection conn;
    private EditText etmail;
    private TextInputLayout inputLayoutEmail;
    private Button bValider;
    final String userName="shotpicofficiel@gmail.com";
    final String password="shotpicmontreuil";
    private SecureRandom random = new SecureRandom();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdpoublie);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        etmail=(EditText) findViewById(R.id.email);
        bValider=(Button)findViewById(R.id.valider);
        bValider.setOnClickListener(this);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onClick(View v){
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);

            String sql ="SELECT email,idcompte,motDePasse FROM projets4.compte WHERE email='"+etmail.getText()+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            if(etmail.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Veuillez remplir le champs", Toast.LENGTH_SHORT).show();
            }
            else {
                String newmdp = new BigInteger(40, random).toString(32);
                String updateMDP = "UPDATE compte SET motDePasse=SHA2(?,256) WHERE email='"+etmail.getText()+"'";
                PreparedStatement preparedStatement = conn.prepareStatement(updateMDP);
                preparedStatement.setString(1, newmdp.toString().concat(salt));
                preparedStatement .executeUpdate();

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
                try{
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("shotpicofficiel@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(etmail.getText().toString()));
                    message.setSubject("Récupération de votre mot de passe");
                    message.setText("Bonjour,\n Voici votre mot de passe : " + newmdp.toString() + "\n\n\n\n\n\n\n\n\nTeam ShotPic\n" +
                            "Sankar Vijay : Mobile Application Developer \n" +
                            "Vieira Dorian : Mobile Application Developer\n" +
                            "Neto Nicolas : Web Developer\n" +
                            "Benserida Youcef : Web Developer\n");
                    Transport.send(message);

                }catch (MessagingException e){
                    e.printStackTrace();
                }
                AlertDialog alertMDP = new AlertDialog.Builder(this).create();
                alertMDP.setTitle("Mot de passe oublié");
                alertMDP.setMessage("Votre mot de passe vous a été envoyé par mail ");
                alertMDP.setButton(AlertDialog.BUTTON_NEUTRAL,"Ok",new DialogInterface.OnClickListener(){

                    public void  onClick (DialogInterface dialog , int which){
                        dialog.dismiss();
                        MdpOublie.this.finish();
                    }
                });
                alertMDP.show();
            }
        }catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Erreur ", Toast.LENGTH_SHORT).show();
        }
    }
}
