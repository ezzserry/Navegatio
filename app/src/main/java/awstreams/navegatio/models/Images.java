package awstreams.navegatio.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LENOVO on 08/09/2016.
 */
public class Images implements Serializable{
    @SerializedName("full")
    private ImagesInfo full;

    @SerializedName("thumbnail")
    private ImagesInfo thumbnail;


    @SerializedName("medium")
    private ImagesInfo medium;

    public ImagesInfo getFull() {
        return full;
    }

    public void setFull(ImagesInfo full) {
        this.full = full;
    }

    public ImagesInfo getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImagesInfo thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImagesInfo getMedium() {
        return medium;
    }

    public void setMedium(ImagesInfo medium) {
        this.medium = medium;
    }
}
