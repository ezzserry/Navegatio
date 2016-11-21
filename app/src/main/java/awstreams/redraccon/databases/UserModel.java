package awstreams.redraccon.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;

/**
 * Created by LENOVO on 21/11/2016.
 */
@Table(database = AppDatabase.class)
public class UserModel {

    @PrimaryKey
    @Unique
    @Column
    String id;

    @Column
    @Unique
    String username;

    @Column
    String email;


    public UserModel(String email, String username, String id) {
        this.email = email;
        this.username = username;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
