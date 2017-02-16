package ru.falseteam.schedule.journal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.UpdateJournalRecord;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class EditRecordActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;
    private View contentView;

    private JournalRecord record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_presented);

        emptyView = findViewById(R.id.emptyView);
        contentView = findViewById(R.id.content);

        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

        record = (JournalRecord) getIntent().getSerializableExtra("record");
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
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Worker.get().getSubscriptionManager().getData("GetUsers") != null) {
                    emptyView.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    initView();
                }
            }
        });
    }

    ListView list;

    private void initView() {
        if (list != null) {
            ((Adapter) list.getAdapter()).notifyDataSetChanged();
            return;
        }

        ((TextView) contentView.findViewById(R.id.lesson_number)).setText(String.valueOf(record.lessonNumber.id));
        ((TextView) contentView.findViewById(R.id.lesson_name)).setText(String.valueOf(record.lesson.name));
        ((TextView) contentView.findViewById(R.id.lesson_audience)).setText(String.valueOf(record.lesson.audience));

        list = ((ListView) contentView.findViewById(R.id.list));
        ArrayList<User> users = new ArrayList<>();
        //noinspection unchecked
        for (User u : (List<User>) Worker.get().getSubscriptionManager().getData("GetUsers").get("users")) {
            if (u.permissions.equals(Groups.developer) ||
                    u.permissions.equals(Groups.admin) ||
                    u.permissions.equals(Groups.user))
                users.add(u);
        }
        list.setAdapter(new Adapter(this, users));
        for (int i = 0; i < users.size(); ++i)
            if (record.presented.get(i)) list.setSelection(i);

        Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.get().sendFromMainThread(UpdateJournalRecord.getRequest(record));
                finish();
            }
        });
    }

    private class Adapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<User> objects;

        @Override
        public void notifyDataSetChanged() {
            objects = ((ArrayList<User>) Worker.get().getSubscriptionManager().getData("GetUsers").get("users"));
            super.notifyDataSetChanged();
        }

        Adapter(Context context, ArrayList<User> objects) {
            this.context = context;
            this.objects = objects;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public User getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            CheckBox cb;
            if (view == null) {
                ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
                cb = new CheckBox(context);
                cb.setLayoutParams(lp);
                view = cb;
            }
            User u = getItem(position);
            cb = (CheckBox) view;
            cb.setText(u.name);
            cb.setTag(u.atList);
            cb.setChecked(record.presented.get(u.atList));
            cb.setOnCheckedChangeListener(onCheckedChangeListener);
            return view;
        }

        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                record.presented.set((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}
