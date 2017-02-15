package ru.falseteam.schedule.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class ListOfUsersActivity extends AppCompatActivity implements Redrawable {

    private Adapter adapter;
    private View progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.users);

        ListView lv = ((ListView) findViewById(R.id.list));

        adapter = new Adapter(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openUserEditor((User) adapter.getItem(position));
            }
        });
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        lv.setEmptyView(findViewById(R.id.emptyView));
//        Worker.get().sendFromMainThread(GetUsers.getRequest());
    }

    private void openUserEditor(User user) {
        Intent intent = new Intent(ListOfUsersActivity.this, EditUserActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        Worker.get().getSubscriptionManager().subscribe("GetUsers");
        redraw();
    }

    @Override
    protected void onPause() {
        Worker.get().getSubscriptionManager().unsubscribe("GetUsers");
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_via_add_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                openUserEditor(User.Factory.getDefault());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (MainData.getUsers() != null) {
                if (Worker.get().getSubscriptionManager().getData("GetUsers") != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText(R.string.empty_list);
//                    adapter.setObjects(MainData.getUsers());
                    adapter.setObjects((List<User>) Worker.get().getSubscriptionManager().getData("GetUsers").get("users"));
                }
            }
        });
    }

    private class Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<User> objects;

        Adapter(Context context) {
            objects = new ArrayList<>();
            inflater = LayoutInflater.from(context);
        }

        void setObjects(List<User> objects) {
            this.objects = objects;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_user, parent, false);
            User user = (User) getItem(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(user.name);
            ((TextView) convertView.findViewById(R.id.group)).setText(user.permissions.name());
            return convertView;
        }
    }
}
