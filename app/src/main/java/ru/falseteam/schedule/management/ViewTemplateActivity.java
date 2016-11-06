package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.falseteam.schedule.R;

public class ViewTemplateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_template);
        setTitle(getString(R.string.edit_template));
    }
}
