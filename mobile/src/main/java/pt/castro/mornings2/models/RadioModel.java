package pt.castro.mornings2.models;

/**
 * Created by lourenco on 31/05/2017.
 */

public class RadioModel {

    private String mName;
    private String mImageUrl;
    private String mImage;

    public RadioModel() {

    }

    public RadioModel(String name, String image) {
        this.mName = name;
        this.mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }
}
