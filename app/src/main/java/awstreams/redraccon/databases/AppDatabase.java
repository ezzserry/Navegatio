package awstreams.redraccon.databases;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by LENOVO on 26/10/2016.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase"; // we will add the .db extension

    public static final int VERSION = 6;
}
