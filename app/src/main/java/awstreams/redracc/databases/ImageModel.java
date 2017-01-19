package awstreams.redracc.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import awstreams.redracc.models.ImagesInfo;

/**
 * Created by LENOVO on 04/12/2016.
 */
@Table(database = AppDatabase.class)
public class ImageModel extends BaseModel {
    public ImageModel() {

    }

    public ImageModel(String imgUrl, String imgWidth, String imgHeight) {
        this.imgUrl = imgUrl;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    @PrimaryKey
    @Column
    String imgUrl;


    @Column
    String imgWidth;

    @Column
    String imgHeight;

    @Column
    Boolean isThumbnail;



    public ImagesInfo getImage() {
        return new ImagesInfo(imgUrl, imgWidth, imgWidth);
    }



}
