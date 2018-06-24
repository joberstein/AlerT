package com.jesseoberstein.alert.activities.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jesseoberstein.alert.data.database.AppDatabase;

import java.util.Optional;

public abstract class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDatabase(this);
    }

    protected AppDatabase getDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }
}