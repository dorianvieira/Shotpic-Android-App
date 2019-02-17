package premiereapplication.automation.test.shotpic;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by Vijay on 16/03/2017.
 */


public class MyViewHolder extends RecyclerView.ViewHolder{
    private TextView textViewView,idphoto;
    private ImageView imageView;
    private LinearLayout cadre;
    private MyObject photo;
    private static  final  String url = "jdbc:mysql://sdigo.cf/projets4";
    private static  final String user = "youcef";
    private static final String psw = "benserida";
    private static Connection conn;
    private Statement stmt = null;

    //itemView est la vue correspondante à 1 cellule
    public MyViewHolder(View itemView) {

        super(itemView);
        //c'est ici que l'on fait nos findView
        textViewView = (TextView) itemView.findViewById(R.id.texte);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        cadre=(LinearLayout)itemView.findViewById(R.id.cadre);
        idphoto=(TextView)itemView.findViewById(R.id.idphoto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent (view.getContext(), Vote.class);
                    intent.putExtra("titre",photo.getText());
                    intent.putExtra("image", photo.getImageUrl());
                    intent.putExtra("idphoto", photo.getId());
                    cadre.getContext().startActivity(intent);
            }
        });
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(MyObject myObject){
        this.photo=myObject;
        textViewView.setText(myObject.getText());
        Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);

    }
}