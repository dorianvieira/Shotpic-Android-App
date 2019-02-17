package premiereapplication.automation.test.shotpic;

/**
 * Created by Vijay on 16/03/2017.
 */
public class MyObject {
    private String text;
    private String imageUrl;
    private int id;



    public MyObject(int id,String text, String imageUrl) {
        this.id=id;

        this.text = text;
        this.imageUrl = imageUrl;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}