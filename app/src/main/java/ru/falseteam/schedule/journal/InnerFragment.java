package ru.falseteam.schedule.journal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class InnerFragment extends Fragment implements Redrawable {
    private java.sql.Date date;
    private Adapter adapter;

    public InnerFragment() {
    }

    private void openRecordEditor(JournalRecord record) {
        Intent intent = new Intent(getActivity(), EditRecordActivity.class);
        intent.putExtra("record", record);
        startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_templates, container, false);

        TextView tv = (TextView) root.findViewById(R.id.day_of_week);
        tv.setText(date.toString());

        adapter = new Adapter(root.getContext(), date);
        ListView list = (ListView) root.findViewById(R.id.list);
        View view = root.findViewById(R.id.emptyView);
        view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        list.setEmptyView(view);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openRecordEditor(adapter.journal.get(position));
            }
        });
        return root;
    }

    @Override
    public void redraw() {
        // TODO: 16.02.17 костыли на костылях. пофиксить
        if (Worker.get().getSubscriptionManager().getData("GetJournal") != null) {
            new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message inputMessage) {
                    adapter.notifyDataSetChanged();
                }
            }.obtainMessage().sendToTarget();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        Worker.get().getSubscriptionManager().subscribe("GetJournal");
        redraw();
    }

    @Override
    public void onPause() {
        Worker.get().getSubscriptionManager().unsubscribe("GetJournal");
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    public static InnerFragment newInstance(java.sql.Date date) {
        InnerFragment fragment = new InnerFragment();
//        fragment.date = date;
//        fragment.date = new Date(date.getYear(), date.getMonth(), date.getDay());
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long time = cal.getTimeInMillis();
        fragment.date = new Date(time);
        return fragment;
    }

    private class Adapter extends BaseAdapter {
        private Context context;
        List<JournalRecord> journal = new ArrayList<>();
        private java.sql.Date date;

        Adapter(Context context, java.sql.Date date) {
            this.context = context;
            this.date = date;
            journal = new ArrayList<>();
            // TODO: 16.02.17 тут тоже костыли
            try {
                for (JournalRecord record : ((List<JournalRecord>) Worker.get().getSubscriptionManager().getData("GetJournal").get("journal"))) {
                    Calendar cal = Calendar.getInstance(); // locale-specific
                    cal.setTime(record.date);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    long time = cal.getTimeInMillis();
                    record.date = new Date(time);
//                long i = record.date.getTime() / 1000 / 60 / 60 / 24;
//                long j = date.getTime() / 1000 / 60 / 60 / 24;
                    if (record.date.getTime() == date.getTime()) {
                        journal.add(record);
                    }
                }
            } catch (Exception ignore) {
            }
        }

        @Override
        public void notifyDataSetChanged() {
            journal = new ArrayList<>();
            for (JournalRecord record : ((List<JournalRecord>) Worker.get().getSubscriptionManager().getData("GetJournal").get("journal"))) {
                Calendar cal = Calendar.getInstance(); // locale-specific
                cal.setTime(record.date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long time = cal.getTimeInMillis();
                record.date = new Date(time);
//                long i = record.date.getTime() / 1000 / 60 / 60 / 24;
//                long j = date.getTime() / 1000 / 60 / 60 / 24;
                if (record.date.getTime() == date.getTime()) {
                    journal.add(record);
                }
            }
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return journal.size();
        }

        @Override
        public JournalRecord getItem(int position) {
            return journal.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.item_template, parent, false);
            JournalRecord record = getItem(position);
            ((TextView) convertView.findViewById(R.id.lesson_number)).setText(String.valueOf(record.lessonNumber.id));
            ((TextView) convertView.findViewById(R.id.begin))
                    .setText(record.lessonNumber.begin.toString().substring(0, 5));
            ((TextView) convertView.findViewById(R.id.end))
                    .setText(record.lessonNumber.end.toString().substring(0, 5));
            ((TextView) convertView.findViewById(R.id.name)).setText(record.lesson.name);
            ((TextView) convertView.findViewById(R.id.audience)).setText(record.lesson.audience);
            return convertView;
        }
    }
}
