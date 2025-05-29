package com.example.list3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RateManager  {
    private static DBhelper dbHelper;
    private static String TBNAME;
    public RateManager(Context context) {
        this.dbHelper = new DBhelper(context);
        this.TBNAME = DBhelper.TB_NAME;
    }
    public static void addAll(ArrayList<HashMap<String, String>> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (HashMap<String, String> item : list) {
            ContentValues values = new ContentValues();
            values.put("curname",item.get("ItemTitle"));
            values.put("currate", item.get("Price"));
            db.insert(TBNAME, null, values);
        }
            db.close();
    }
    public static void deleteAll(ArrayList<HashMap<String, String>> listItems) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }
    public static List<HashMap<String, String>> listAll() {
        List<HashMap<String, String>> result = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT curname, currate FROM " + TBNAME, null);

        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();
            item.put("curname", cursor.getString(0));
            item.put("currate", cursor.getString(1));
            result.add(item);
        }
        cursor.close();
        db.close();
        return result;
    }
}