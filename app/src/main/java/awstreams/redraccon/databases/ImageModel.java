package awstreams.redraccon.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import awstreams.redraccon.models.Images;
import awstreams.redraccon.models.ImagesInfo;
import awstreams.redraccon.models.NewsItem;

/**
 * Created by LENOVO on 04/12/2016.
 */
@Table(database = AppDatabase.class)
public class ImageModel extends BaseModel {
    public ImageModel() {

    }

    public ImageModel(String imgUrl, String imgWidth, String imgHeight,Boolean isThumbnail) {
        this.imgUrl = imgUrl;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.isThumbnail = isThumbnail;
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
