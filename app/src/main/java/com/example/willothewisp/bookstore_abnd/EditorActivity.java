package com.example.willothewisp.bookstore_abnd;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.willothewisp.bookstore_abnd.data.BooksContract.BooksEntry;
import com.example.willothewisp.bookstore_abnd.data.BooksDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER = 0;

    private Uri mCurrentItemUri;

    private EditText mTitle;
    private EditText mPrice;
    private EditText mQuantity;
    private EditText mSupplier;
    private EditText mPhone;

    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

@Override
    protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);

    Intent intent = getIntent();
    mCurrentItemUri = intent.getData();

    if (mCurrentItemUri == null) {
        setTitle(getString(R.string.add_an_item));

        invalidateOptionsMenu();
    } else {
        setTitle(getString(R.string.edit_item));

        getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
    }

    mTitle =  findViewById(R.id.edit_item_name);
    mPrice = findViewById(R.id.edit_price);
    mQuantity = findViewById(R.id.edit_quantity);
    mSupplier= findViewById(R.id.edit_supplier);
    mPhone = findViewById(R.id.edit_phone);

    mTitle.setOnTouchListener(mTouchListener);
    mPrice.setOnTouchListener(mTouchListener);
    mQuantity.setOnTouchListener(mTouchListener);
    mSupplier.setOnTouchListener(mTouchListener);
    mPhone.setOnTouchListener(mTouchListener);

}
    private void saveItem() {

     BooksDbHelper mDbHelper = new BooksDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String nameString = mTitle.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        //int price = Integer.parseInt(priceString);
        String quantityString = mQuantity.getText().toString().trim();
        //int quantity = Integer.parseInt(quantityString);
        String supplierString = mSupplier.getText().toString().trim();
        String phoneString = mPhone.getText().toString().trim();

        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierString)
                && TextUtils.isEmpty(phoneString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_ITEM, nameString);
        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(BooksEntry.COLUMN_PRICE, price);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BooksEntry.COLUMN_QUANTITY, quantity);

        values.put(BooksEntry.COLUMN_SUPPLIER, supplierString);
        values.put(BooksEntry.COLUMN_QUANTITY, phoneString);

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(BooksEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BooksEntry._ID,
                BooksEntry.COLUMN_ITEM,
                BooksEntry.COLUMN_PRICE,
                BooksEntry.COLUMN_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER,
                BooksEntry.COLUMN_PHONE};

        return new CursorLoader(this,   // Parent activity context
                mCurrentItemUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_ITEM);
            int priceColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(BooksEntry.COLUMN_PHONE);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);


            mTitle.setText(name);
            mPrice.setText(Integer.toString(price));
            mQuantity.setText(Integer.toString(quantity));
            mSupplier.setText(supplier);
            mPhone.setText(phone);

            }
        }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTitle.setText("");
        mPrice.setText("");
        mQuantity.setText("");
        mSupplier.setText("");
        mPhone.setText("");
    }

}

