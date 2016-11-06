package ru.falseteam.schedule.management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.serializable.User;

public class EditTemplateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);
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
//        Intent intent = new Intent(ListOfUsersActivity.this, EditUserActivity.class);
//        intent.putExtra("user", user);
//        startActivity(intent);
    }
}
