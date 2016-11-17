package ru.falseteam.schedule.journal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.OnChangeGroupListener;
import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetJournal;

public class FragmentJournal extends Fragment implements Redrawable, OnChangeGroupListener {
    private View emptyView;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_journal);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_of_templates, container, false);

        emptyView = rootView.findViewById(R.id.emptyView);
        viewPager = (ViewPager) rootView.findViewById(R.id.content);
        Adapter adapter = new Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(MainData.getJournal() == null ? 0 : MainData.getJournal().size());

        OnChangeGroup.add(this, Groups.admin, Groups.developer);
        onChangeGroup();
        Redrawer.add(this);
        redraw();
        return rootView;
    }

    @Override
    public void onDestroy() {
        OnChangeGroup.remove(this);
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public void redraw() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getJournal() != null) {
                    emptyView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onChangeGroup() {
        Worker.sendFromMainThread(GetJournal.getRequest());
    }

    /**
     * Adapter from page viewer.
     */
    private class Adapter extends FragmentPagerAdapter {
        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return InnerFragment.newInstance(MainData.getJournal().get(position).date);
        }

        @Override
        public int getCount() {
            return MainData.getJournal() == null ? 0 : MainData.getJournal().size();
        }
    }


    public static class InnerFragment extends Fragment {
        private java.sql.Date date;

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

            final Adapter adapter = new Adapter(root.getContext(), new java.sql.Date(new Date().getTime()));
            ListView list = (ListView) root.findViewById(R.id.list);
            View view = root.findViewById(R.id.emptyView);
            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            list.setEmptyView(view);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openRecordEditor(MainData.getJournal().get(position));
                }
            });

            return root;
        }

        public static InnerFragment newInstance(java.sql.Date date) {
            InnerFragment fragment = new InnerFragment();
            fragment.date = date;
            return fragment;
        }

        private class Adapter extends BaseAdapter {
            private Context context;
            private List<JournalRecord> journal = new ArrayList<>();

            Adapter(Context context, java.sql.Date date) {
                this.context = context;
                for (JournalRecord record : MainData.getJournal())
                    if (record.date == date)
                        journal.add(record);
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
}
