package ru.falseteam.schedule.management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetTemplates;

public class ListOfTemplatesActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_templates);
        setTitle(getString(R.string.edit_template));

        emptyView = findViewById(R.id.emptyView);

        Redrawer.add(this);
        redraw();

        Worker.sendFromMainThread(GetTemplates.getRequest());
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_via_add_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_pair:
                openTemplateEditor(Template.Factory.getDefault());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTemplateEditor(Template user) {
        Intent intent = new Intent(this, EditTemplateActivity.class);
        intent.putExtra("template", user);
        startActivity(intent);
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
