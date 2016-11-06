package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;

public class EditTemplateActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;
    private View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);

        emptyView = findViewById(R.id.emptyView);
        contentView = findViewById(R.id.content);

        Redrawer.add(this);
        redraw();
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (true) {
                    emptyView.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
