package premiereapplication.automation.test.shotpic;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Vijay on 27/03/2017.
 */
public class MesVotes extends AppCompatActivity {
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private ImageView image;
    private TextView texte;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_vote);
        image=(ImageView)findViewById(R.id.image);
        texte=(TextView)findViewById(R.id.texte);
        listView = (ListView) findViewById(R.id.listView1);

        //regarde si connecté
        final int idcompte = (int) getIntent().getIntExtra("idcompte", 0);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        afficherMesVotes();

    }

    public void afficherMesVotes(){
        try{
            //on get l'utilisateur conecté

            Connection conn = DriverManager.getConnection(url, user, psw);
            String sqliD = "SELECT idcompte FROM compte where compte.pseudo='"+ Connexion.pseudoUser+"'";
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sqliD);
            rst.next();
            int idcompte=  rst.getInt("idcompte");

            String sql = "select voter.note , voter.idPhoto ,voter.idcompte,photo.nomPhoto,photo.titrePhoto from voter inner join photo on voter.idPhoto=photo.idPhoto where voter.idcompte='"+ idcompte+"'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<ListeItem> myList = new ArrayList<ListeItem>();
            CustomAdapter adapter = new CustomAdapter(this,myList);

            while(rs.next()){
                myList.add(new ListeItem("Vous avez mis la note de " + rs.getString("note") + "/5","http://5.51.224.40/projetS4/site/source/"+rs.getString("nomPhoto")));
                listView.setAdapter(adapter);
            }






        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
