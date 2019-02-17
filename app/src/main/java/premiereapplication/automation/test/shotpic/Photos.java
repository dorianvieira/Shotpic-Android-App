package premiereapplication.automation.test.shotpic;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Vijay on 12/03/2017.
 */
public class Photos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyObject> photos = new ArrayList<MyObject>();
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private Statement stmt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        //faire comme une liste verticale
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new MyAdapter(photos));


        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ajouterPhotos();

    }

    public void ajouterPhotos(){
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);
            stmt = conn.createStatement();

            String sql = "SELECT * from projets4.photo";
            ResultSet rs = stmt.executeQuery(sql);
            int nombreLignes = rs.getRow();

            while(rs.next()){
                for (int i = 0; i <= nombreLignes; i++) {
                    photos.add(new MyObject(rs.getInt("idPhoto"), rs.getString("titrePhoto"),"http://5.51.224.40/projetS4/site/source/"+rs.getString("nomPhoto")));
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        Collections.shuffle(photos);
    }
}