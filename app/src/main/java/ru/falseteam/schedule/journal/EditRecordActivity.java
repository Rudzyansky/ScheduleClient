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

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetJournal;
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

        record = (JournalRecord) getIntent().getSerializableExtra("record");

        Worker.get().sendFromMainThread(GetJournal.getRequest());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        redraw();
    }

    @Override
    protected void onPause() {
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getJournal() != null) {
                    emptyView.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    initView();
                }
            }
        });
    }

    private boolean init = false;

    private void initView() {
        if (init) return;
        init = true;

        ((TextView) contentView.findViewById(R.id.lesson_number)).setText(String.valueOf(record.lessonNumber.id));
        ((TextView) contentView.findViewById(R.id.lesson_name)).setText(String.valueOf(record.lesson.name));
        ((TextView) contentView.findViewById(R.id.lesson_audience)).setText(String.valueOf(record.lesson.audience));

        ListView list = ((ListView) contentView.findViewById(R.id.list));
        list.setAdapter(new Adapter(this, MainData.getUsers()));
        for (int i = 0; i < MainData.getUsers().size(); ++i)
            if (record.presented.get(i)) list.setSelection(i);

        Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker.get().sendFromMainThread(UpdateJournalRecord.getRequest(record));
                finish();
                Worker.get().sendFromMainThread(GetJournal.getRequest());
            }
        });
    }

    private class Adapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<User> objects;

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
            cb.setTag(position);
            cb.setChecked(record.presented.get(position));
            cb.setOnCheckedChangeListener(onCheckedChangeListener);
            return null;
        }

        OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                record.presented.set((Integer) buttonView.getTag(), isChecked);
            }
        };
    }
}
