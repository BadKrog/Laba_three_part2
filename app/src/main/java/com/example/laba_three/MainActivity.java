package com.example.laba_three;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.laba_three.FeedReaderContract.FeedReaderDbHelper;
import com.example.laba_three.FeedReaderContract.FeedEntry;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    Button but1;
    Button but2;
    Button but3;
    Intent intent;
    FeedReaderDbHelper dbHelper;
    SQLiteDatabase db;

    int currentFIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Текущее имя в списке
        currentFIO = 0;

        // Создадим связь со вторым активити
        intent = new Intent(this, Second_activity.class);

        // Найдем кнопки
        but1 = (Button) findViewById(R.id.button1);
        but2 = (Button) findViewById(R.id.button2);
        but3 = (Button) findViewById(R.id.button3);

        // Создание БД
        dbHelper = new FeedReaderDbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        // Запись в БД 5 студентов
        for (int i=0; i<5; i++) {
            writeToBD();
        }


        // Описываем первую кнопку
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на новое активити
                startActivity(intent);
            }
        });

        // Описываем вторую кнопку
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToBD();
            }
        });

        // Описываем третью кнопку
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.deleteData(db);
        dbHelper.close();
        super.onDestroy();
    }

    // Функция для считывания содержимого из файла с ФИО
    private String readFromRaw(int numFIO)throws ClassCastException, IOException {
        // Создаем читатель файла
        Resources res = this.getResources();
        AssetManager.AssetInputStream buffer = (AssetManager.AssetInputStream) res.openRawResource(R.raw.students);

        // Записываем содержимое файла в буфер
        int findEn = 0;
        int c;
        StringBuilder strFIO = new StringBuilder("");
        while((c=buffer.read())!=-1){
            if((char)c == '\n'){
                findEn++;
            }
            else if(findEn == numFIO) {
                strFIO.append((char) c);
            }
        }

        // Возвращаем содержимое
        return strFIO.toString();
    }

    // Получение текущего времени
    private String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void writeToBD(){
        ContentValues values = new ContentValues();
        // Считываем ФИО студента из файла
        String full_name = "";
        try {
            full_name  = readFromRaw(currentFIO);
        } catch (IOException e) {
            e.printStackTrace();
            full_name = "Произошла ошибка чтения.";
        }

        values.put(FeedEntry.COLUMN_NAME_FIO, full_name);
        values.put(FeedEntry.COLUMN_NAME_TIME, getCurrentTime());

        db.insert(FeedEntry.TABLE_NAME, null, values);

        values.clear();

        currentFIO++;
    }
}
