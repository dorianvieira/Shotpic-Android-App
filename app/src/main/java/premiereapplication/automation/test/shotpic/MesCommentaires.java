package premiereapplication.automation.test.shotpic;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dorian on 25/03/2017.
 */

public class MesCommentaires extends AppCompatActivity {

    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private ListView liste;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mescoms);
        //regarde si connecté
        final int idcompte = (int) getIntent().getIntExtra("idcompte", 0);
        liste = (ListView) findViewById(R.id.liste);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        afficherMesCommentaires();

    }

    public void afficherMesCommentaires(){
        try{

            //on get l'utilisateur conecté
            Connection conn = DriverManager.getConnection(url, user, psw);
            String sqliD = "SELECT idcompte FROM compte where compte.pseudo='"+ Connexion.pseudoUser+"'";
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sqliD);
            rst.next();
            int idcompte=  rst.getInt("idcompte");



            //on va récupérer les commentaires postés par l'user
            List<HashMap<String, String>> listeItem = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(this, listeItem, R.layout.liste_item, new String[]{"First line", "Second line", "Third line", "Fourth line"}, new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4});


            //String sql ="SELECT * from commentaire inner join compte where commentaire.idcompte='" + idcompte + "'";
            String sql = "select commentaire.contenu , commentaire.dateCom,commentaire.heureCom,photo.titrePhoto,photo.nomPhoto,photo.idPhoto from commentaire inner join photo on commentaire.idPhoto=photo.idPhoto where commentaire.idcompte='"+ idcompte+"'";
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);

            int nombreLignes = rs.getRow();
            // String ps = Connexion.pseudoUser;
            while (rs.next()) {

                for (int i = 0; i <= nombreLignes; i++) {
                    HashMap<String, String> listeComm = new HashMap<>();
                    listeComm.put(rs.getString("heureCom"), rs.getString("dateCom"));
                    listeComm.put(rs.getString("contenu"), rs.getString("titrePhoto"));
                    Iterator it = listeComm.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap<String, String> result = new HashMap<>();
                        Map.Entry pair = (Map.Entry) it.next();
                        Map.Entry pair2 = (Map.Entry) it.next();
                        result.put("First line", pair.getKey().toString());
                        result.put("Second line", pair.getValue().toString());

                        result.put("Third line", pair2.getKey().toString());
                        result.put("Fourth line", pair2.getValue().toString());
                        listeItem.add(result);
                    }
                }
                liste.setAdapter(adapter);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}