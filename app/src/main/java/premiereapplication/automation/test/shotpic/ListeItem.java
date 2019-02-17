package premiereapplication.automation.test.shotpic;

/**
 * Created by Vijay on 28/03/2017.
 */
public class ListeItem {
    private String texte;
    private String image;

    public ListeItem(String texte, String image) {
        this.texte = texte;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    @Override
    public String toString() {
        return "ListeItem{" +
                "texte='" + texte + '\'' +
                ", image=" + image +
                '}';
    }
}
