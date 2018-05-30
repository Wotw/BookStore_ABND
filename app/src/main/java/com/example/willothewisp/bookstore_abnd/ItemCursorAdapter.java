package com.example.willothewisp.bookstore_abnd;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.willothewisp.bookstore_abnd.data.BooksContract;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_ITEM);
        int priceColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRICE);
        int supplierColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRICE);
        int phoneColumnIndex = cursor.getColumnIndex(BooksContract.BooksEntry.COLUMN_PRICE);

        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        int itemQuantity = cursor.getInt(quantityColumnIndex);
        String supplier = cursor.getString(supplierColumnIndex);
        String phone = cursor.getString(phoneColumnIndex);

        nameTextView.setText(itemName);
        summaryTextView.setText(itemPrice);
    }
}
