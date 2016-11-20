package awstreams.redraccon.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LENOVO on 08/09/2016.
 */
public class ImagesInfo implements Serializable {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("height")
    private String height;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @SerializedName("width")
    private String width;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
