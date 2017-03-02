package ru.falseteam.schedule.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class InnerFragment extends Fragment implements Redrawable {
    private int week;
    private int dayOfWeek;
    private Adapter adapter;
    private View emptyView;
    private ListView listView;

    public InnerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_templates, container, false);

        TextView tv = (TextView) root.findViewById(R.id.day_of_week);
        tv.setText(getResources().getStringArray(R.array.week_days)[dayOfWeek]);

        adapter = new Adapter(root.getContext());
        listView = (ListView) root.findViewById(R.id.list);
        emptyView = root.findViewById(R.id.emptyView);
        emptyView.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);

        return root;
    }

    public static InnerFragment newInstance(int week, int dayOfWeek) {
        InnerFragment fragment = new InnerFragment();
        fragment.week = week;
        fragment.dayOfWeek = dayOfWeek;
        return fragment;
    }

    @Override
    public void redraw() {
        if (Worker.get().getSubscriptionManager().getData("GetTemplates") != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emptyView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            });
    }

    private class Adapter extends BaseAdapter {
        private Context context;
        private List<Template> templates = new ArrayList<>();

        Adapter(Context context) {
            this.context = context;
            for (Template t : ((List<Template>) Worker.get().getSubscriptionManager().getData("GetTemplates").get("templates")))
                if (t.weekDay.id == dayOfWeek + 1 && (
                        (t.weeks.get(31) &&
                                (t.weeks.get(30) || (t.weeks.get(29) && week % 2 == 0) || (!t.weeks.get(29) && week % 2 == 1))
                        ) || t.weeks.get(week)
                )) templates.add(t);
        }

        @Override
        public void notifyDataSetChanged() {
            templates.clear();
            for (Template t : ((List<Template>) Worker.get().getSubscriptionManager().getData("GetTemplates").get("templates")))
                if (t.weekDay.id == dayOfWeek + 1 && (
                        (t.weeks.get(31) &&
                                (t.weeks.get(30) || (t.weeks.get(29) && week % 2 == 0) || (!t.weeks.get(29) && week % 2 == 1))
                        ) || t.weeks.get(week)
                )) templates.add(t);
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return templates.size();
        }

        @Override
        public Template getItem(int position) {
            return templates.get(position);
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
            Template t = getItem(position);
            ((TextView) convertView.findViewById(R.id.lesson_number)).setText(String.valueOf(t.lessonNumber.id));
            ((TextView) convertView.findViewById(R.id.begin))
                    .setText(t.lessonNumber.begin.toString().substring(0, 5));
            ((TextView) convertView.findViewById(R.id.end))
                    .setText(t.lessonNumber.end.toString().substring(0, 5));
            ((TextView) convertView.findViewById(R.id.name)).setText(t.lesson.name);
            ((TextView) convertView.findViewById(R.id.audience)).setText(t.lesson.audience);
            return convertView;
        }
    }
}