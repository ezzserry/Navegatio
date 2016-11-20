package awstreams.redraccon.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.IndexGroup;
import com.raizlabs.android.dbflow.annotation.InheritedColumn;
import com.raizlabs.android.dbflow.annotation.InheritedPrimaryKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.annotation.UniqueGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.lang.annotation.Annotation;

import awstreams.redraccon.models.Category;

/**
 * Created by LENOVO on 26/10/2016.
 */
@Table(database = AppDatabase.class)
public class CategoryModel extends BaseModel {
    public CategoryModel() {
    }

    public CategoryModel(String id, String slug, String title) {
        this.id = id;
        this.slug = slug;
        this.title = title;
    }

    @PrimaryKey
    @Column
    @Unique
    String id; // package-private recommended, not required

    @Column
    @Unique
    String slug;

    @Column
    private String title; // private with getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return new Category(id, slug, title);
    }

}
