package mx.cetys.jorgepayan.a23570_payan_examen01.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jorge.payan on 9/22/17.
 */

public class DBUtils extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Banpatito.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CUSTOMER_VISITS_TABLE_NAME = "CUSTOMER_VISITS";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_CODE = "code";
    public static final String CUSTOMER_POSITION = "position";
    public static final String CUSTOMER_NAME = "customer";
    public static final String CUSTOMER_OPERATIONS = "number_of_operations";
    public static final String CUSTOMER_CURRENT_OPERATION = "current_operation";
    public static final String CUSTOMER_DATE_ADDED = "date_added";

    public static final String DATABASE_CREATE =
            "CREATE TABLE " + CUSTOMER_VISITS_TABLE_NAME + "(" +
                    CUSTOMER_ID + " integer primary key autoincrement, " +
                    CUSTOMER_CODE + " text not null, " +
                    CUSTOMER_POSITION + " integer not null, " +
                    CUSTOMER_NAME + " text not null, " +
                    CUSTOMER_OPERATIONS + "  integer not null, " +
                    CUSTOMER_CURRENT_OPERATION + " integer not null," +
                    CUSTOMER_DATE_ADDED + " text not null" +
            ")";

    public DBUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_VISITS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
