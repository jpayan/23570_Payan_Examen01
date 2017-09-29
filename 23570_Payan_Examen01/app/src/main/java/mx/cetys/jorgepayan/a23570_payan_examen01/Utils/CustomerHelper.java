package mx.cetys.jorgepayan.a23570_payan_examen01.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.cetys.jorgepayan.a23570_payan_examen01.CustomerVisit;

/**
 * Created by jorge.payan on 9/22/17.
 */

public class CustomerHelper {
    private DBUtils dbHelper;
    private SQLiteDatabase database;
    private String[] CUSTOMER_VISITS_TABLE_COLUMNS = {
            DBUtils.CUSTOMER_ID,
            DBUtils.CUSTOMER_POSITION,
            DBUtils.CUSTOMER_NAME,
            DBUtils.CUSTOMER_OPEREATIONS,
            DBUtils.CUSTOMER_CURRENT_OPERATION
    };

    public CustomerHelper(Context context){
        dbHelper = new DBUtils(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<CustomerVisit> getAllCustomerVisits() {
        ArrayList<CustomerVisit> customerVisits = new ArrayList<>();
        Cursor cursor = database.query(DBUtils.CUSTOMER_VISITS_TABLE_NAME, CUSTOMER_VISITS_TABLE_COLUMNS,
                null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            customerVisits.add(parseCustomerVisit(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return customerVisits;
    }

    public CustomerVisit addCustomerVisit(int position, String name, int operations, int currentOperation) {
        ContentValues values = new ContentValues();

        values.put(DBUtils.CUSTOMER_POSITION, position);
        values.put(DBUtils.CUSTOMER_NAME, name);
        values.put(DBUtils.CUSTOMER_OPEREATIONS, operations);
        values.put(DBUtils.CUSTOMER_CURRENT_OPERATION, currentOperation);

        long customerVisitId = database.insert(DBUtils.CUSTOMER_VISITS_TABLE_NAME, null, values);

        Cursor cursor = database.query(DBUtils.CUSTOMER_VISITS_TABLE_NAME, CUSTOMER_VISITS_TABLE_COLUMNS,
                DBUtils.CUSTOMER_ID + " = " + customerVisitId, null, null, null, null);

        cursor.moveToFirst();
        CustomerVisit customerVisit = parseCustomerVisit(cursor);
        cursor.close();

        return customerVisit;
    }

    public void deleteCustomerVisit(int customerVisitId) {
        database.delete(DBUtils.CUSTOMER_VISITS_TABLE_NAME, DBUtils.CUSTOMER_ID + " = " + customerVisitId, null);
    }

    public int getCustomerVisitId(int position) {
        Cursor cursor = database.query(DBUtils.CUSTOMER_VISITS_TABLE_NAME, new String[] {DBUtils.CUSTOMER_ID}, DBUtils.CUSTOMER_POSITION + " = " + position, null, null, null, null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(DBUtils.CUSTOMER_ID));
        return id;
    }

    public void clearTable(String tableName) {
        database.execSQL("DELETE FROM " + tableName);
    }

    private CustomerVisit parseCustomerVisit(Cursor cursor) {
        int position = cursor.getInt(cursor.getColumnIndex(DBUtils.CUSTOMER_POSITION));
        String name = cursor.getString(cursor.getColumnIndex(DBUtils.CUSTOMER_NAME));
        int operations = cursor.getInt(cursor.getColumnIndex(DBUtils.CUSTOMER_OPEREATIONS));
        int currentOperation = cursor.getInt(cursor.getColumnIndex(DBUtils.CUSTOMER_CURRENT_OPERATION));
        CustomerVisit customerVisit = new CustomerVisit(position, name, operations, currentOperation);
        return customerVisit;
    }
}
