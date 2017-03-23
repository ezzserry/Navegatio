package awstreams.navegatio.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LENOVO on 08/09/2016.
 */
public class Attachments implements Serializable{
    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String url;

    @SerializedName("slug")
    private String slug;

    @SerializedName("title")
    private String title;

    @SerializedName("images")
    private Images images;

}
