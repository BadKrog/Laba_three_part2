package com.example.laba_three;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.laba_three.FeedReaderContract.FeedReaderDbHelper;
import com.example.laba_three.FeedReaderContract.FeedEntry;

import java.util.ArrayList;
import java.util.List;

public class Second_activity extends AppCompatActivity {

    // Поля для БД
    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;

    // Поля для списка
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);

        // Создаем список
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Адаптер создадим после открытия БД

        // Открываем БД
        Log.d("MyTag", "Сейчас попробую открыть БД");
        dbHelper = new FeedReaderDbHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();

        Log.d("MyTag", "У меня получилось");

        // Чтение из БД
        String[] projection = {
                BaseColumns._ID,
                FeedEntry.COLUMN_NAME_FIO,
                FeedEntry.COLUMN_NAME_TIME
        };

        String sortOrder =
                FeedEntry.COLUMN_NAME_FIO + " ASC";

        Log.d("MyTag", "Хочу получить курсор");

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List itemIDs = new ArrayList<>();   // ID
        List itemFIOs = new ArrayList<>();  // full name student
        List itemDates = new ArrayList<>(); // date added

        Log.d("MyTag", "Пробую читать");

        while (cursor.moveToNext()) {
            // Получения ID
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BaseColumns._ID));
            // Получение ФИО студента
            String itemFIO = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_FIO));
            // Получение времени добавления
            String itemDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TIME));

            itemIDs.add(itemId);
            itemFIOs.add(itemFIO);
            itemDates.add(itemDate);

            Log.d("MyTag", "Id:" + itemId + "; FIO:" + itemFIO + "; Time:" + itemDate);
        }

        cursor.close();

        mAdapter = new MyAdapter(itemIDs, itemFIOs, itemDates);
        recyclerView.setAdapter(mAdapter);
    }


}
