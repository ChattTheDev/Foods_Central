package com.chattthedev.foodscentral.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.chattthedev.foodscentral.Models.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "finalfoods.db";
    private static final int DB_VER = 1;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID", "ProductName", "ProductId", "Quantity", "Price", "Halfprice", "Fullprice", "Half", "Full"};
        String sqlTable = "FinalFoods";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Halfprice")),
                        c.getString(c.getColumnIndex("Fullprice")),
                        c.getString(c.getColumnIndex("Half")),
                        c.getString(c.getColumnIndex("Full"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO FinalFoods(ProductId,ProductName,Quantity,Price,Halfprice,Fullprice,Half,Full) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getFullprice(),
                order.getHalfprice(),
                order.getFull(),
                order.getHalf());
        db.execSQL(query);
    }

    public void removeFromCart(int order){

        SQLiteDatabase db = getReadableDatabase();

        String query = String.format(Locale.ENGLISH, "DELETE FROM FinalFoods WHERE ID= %d", order);
        db.execSQL(query);
    }
    public void updatecart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE FinalFoods SET Quantity = %s WHERE ID = %d", order.getQuantity(), order.getID());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM FinalFoods");
        db.execSQL(query);
    }

    public int getcouncart(){

        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM FinalFoods");
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;

    }

}
