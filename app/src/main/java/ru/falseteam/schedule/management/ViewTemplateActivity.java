package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetTemplates;

public class ViewTemplateActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_template);
        setTitle(getString(R.string.edit_template));

        emptyView = findViewById(R.id.emptyView);

        Redrawer.add(this);
        redraw();

        Worker.get().send(GetTemplates.getRequest());
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
                if (GetTemplates.templates != null) {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }
}
