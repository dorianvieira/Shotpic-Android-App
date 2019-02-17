package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay on 16/03/2017.
 */
public class Vote extends AppCompatActivity{
    private RatingBar ratingBar;
    private TextView result;
    private ImageView image;
    private TextView texte,idphoto;
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private ImageButton logo;
    private EditText eCom;
    private Button commenter;
    private ListView liste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        image =(ImageView)findViewById(R.id.image);
        texte=(TextView)findViewById(R.id.titre);
        idphoto=(TextView)findViewById(R.id.idphoto);
        logo=(ImageButton)findViewById(R.id.logo);
        commenter=(Button)findViewById(R.id.button_commenter);
        eCom=(EditText)findViewById(R.id.ecrit_comm);
       liste=(ListView) findViewById(R.id.liste);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Vote.this, AccueilCo.class));
            }
        });

        Button buttonInscription = (Button) findViewById(R.id.inscription);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancer l'activite Inscription
                Intent intent = new Intent(Vote.this, Inscription.class);
                startActivity(intent);
            }
        });

        Button buttonProfil=(Button) findViewById(R.id.profil);
        buttonProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vote.this, Profil.class);
                startActivity(intent);
            }
        });

        Button buttonPhotos=(Button) findViewById(R.id.photo);
        buttonPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Vote.this, Photos.class);
                startActivity(intent);
            }
        });

        addListenerOnRatingBar();
        Intent intent =getIntent();
        this.texte.setText(intent.getStringExtra("titre"));
        Picasso.with(image.getContext()).load((intent.getStringExtra("image"))).fit().into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setRotation(image.getRotation() + 90);
            }
        });
        commenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentaire();
            }
        });
        afficherCommentaire();



        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Problème au niveau du driver", Toast.LENGTH_SHORT).show();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void addCommentaire(){
        String comm = eCom.getText().toString();
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);
            String sqliD = "SELECT idcompte FROM compte where compte.pseudo='"+ Connexion.pseudoUser+"'";
            Statement st = conn.createStatement();
            ResultSet rst = st.executeQuery(sqliD);
            rst.next();
            int idcompte=  rst.getInt("idcompte");
            int idphoto = (int) getIntent().getIntExtra("idphoto",0);
            if(idcompte==0){
                Toast.makeText(getApplicationContext(),"Connectez-vous pour commenter", Toast.LENGTH_SHORT).show();
            }else {
                String sqlInsert = "insert into commentaire values (default,?,curDate(),curTime(),?,?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert);
                preparedStatement.setString(1, comm);
                preparedStatement.setInt(2, idcompte);
                preparedStatement.setInt(3, idphoto);
                preparedStatement.executeUpdate();
                Toast.makeText(getApplicationContext(),"Votre commentaire a été ajoutée !", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void afficherCommentaire(){
        try{
            Connection conn = DriverManager.getConnection(url, user, psw);
            Statement stmt = conn.createStatement();
            List<HashMap<String, String>> listeItem = new ArrayList<>();
            SimpleAdapter adapter = new SimpleAdapter(this, listeItem, R.layout.liste_item, new String[]{"First line", "Second line", "Third line", "Fourth line"}, new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4});

            int idphoto = (int) getIntent().getIntExtra("idphoto",0);
            String sql = "select * from commentaire natural join compte where idPhoto='"+ idphoto+"'";
            ResultSet rs = stmt.executeQuery(sql);
            int nombreLignes = rs.getRow();

            while (rs.next()) {
                for (int i = 0; i <= nombreLignes; i++) {
                    HashMap<String, String> listeComm = new HashMap<>();
                    listeComm.put(rs.getString("heureCom"), rs.getString("contenu"));
                    listeComm.put(rs.getString("pseudo"), rs.getString("dateCom"));
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
                adapter.notifyDataSetChanged();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addListenerOnRatingBar(){
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
       LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
      stars.getDrawable(0).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
      stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        result=(TextView) findViewById(R.id.note);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                try{
                    Connection conn = DriverManager.getConnection(url, user, psw);
                    String sqliD = "SELECT idcompte FROM compte where compte.pseudo='"+ Connexion.pseudoUser+"'";
                    Statement st = conn.createStatement();
                    ResultSet rst = st.executeQuery(sqliD);
                    rst.next();
                    int idcompte=  rst.getInt("idcompte");
                    int idphoto = (int) getIntent().getIntExtra("idphoto",0);
                    result.setText("Note de : " + String.valueOf(rating));

                    if(idcompte==0) {
                        Toast.makeText(getApplicationContext(), "Connectez-vous pour voter", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        String sqlVote="select * from voter where idcompte='"+idcompte+"' and idPhoto='"+idphoto+"'";
                        ResultSet rst2 = st.executeQuery(sqlVote);

                        if(rst2.next()==true ) {
                            Toast.makeText(getApplicationContext(), "vous avez déjà voter", Toast.LENGTH_SHORT).show();
                        }else{

                            String sql2 ="INSERT INTO voter VALUES(?,?,?)";
                            PreparedStatement preparedStatement = conn.prepareStatement(sql2);
                            preparedStatement.setString(1,String.valueOf(rating));
                            preparedStatement.setInt(2, idcompte);
                            preparedStatement.setInt(3, idphoto);
                            preparedStatement.executeUpdate();
                            Toast.makeText(getApplicationContext(),"Vote effectué !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
