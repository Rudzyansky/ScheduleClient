package ru.falseteam.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.OnChangeGroupListener;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetTemplates;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentMain extends Fragment implements Redrawable, OnChangeGroupListener {
    private View emptyView;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_main);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_of_templates, container, false);

        emptyView = rootView.findViewById(R.id.emptyView);
        viewPager = (ViewPager) rootView.findViewById(R.id.content);
        Adapter adapter = new Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        Calendar c = GregorianCalendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK) - 2;
        if (day == -1) day = 7;

        ((TextView) rootView.findViewById(R.id.week_number)).setText(((Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6) + " неделя"));

        viewPager.setCurrentItem(day);

        OnChangeGroup.add(this, Groups.user, Groups.admin, Groups.developer);
        onChangeGroup();
        Redrawer.addRedrawable(this);
        redraw();
        return rootView;
    }

    @Override
    public void onDestroy() {
        OnChangeGroup.remove(this);
        Redrawer.removeRedrawable(this);
        super.onDestroy();
    }

    @Override
    public void redraw() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getTemplates() != null) {
                    emptyView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onChangeGroup() {
        Worker.get().sendFromMainThread(GetTemplates.getRequest());
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
            return InnerFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 7;
        }
    }


    public static class InnerFragment extends Fragment {
        private int dayOfWeek;

        public InnerFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_templates, container, false);

            TextView tv = (TextView) root.findViewById(R.id.day_of_week);
            tv.setText(getResources().getStringArray(R.array.week_days)[dayOfWeek]);

            final Adapter adapter = new Adapter(root.getContext(), dayOfWeek);
            ListView list = (ListView) root.findViewById(R.id.list);
            View view = root.findViewById(R.id.emptyView);
            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            list.setEmptyView(view);
            list.setAdapter(adapter);

            return root;
        }

        public static InnerFragment newInstance(int dayOfWeek) {
            InnerFragment fragment = new InnerFragment();
            fragment.dayOfWeek = dayOfWeek;
            return fragment;
        }

        private class Adapter extends BaseAdapter {
            private Context context;
            private List<Template> templates = new ArrayList<>();

            Adapter(Context context, int dayOfWeek) {
                this.context = context;
                int week = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6);
                for (Template t : MainData.getTemplates())
                    if (t.weekDay.id == dayOfWeek + 1 && (
                            (t.weeks.get(31) &&
                                    (t.weeks.get(30) || (t.weeks.get(29) && week % 2 == 1) || (!t.weeks.get(29) && week % 2 == 0))
                            ) || t.weeks.get(week - 1)
                    )) templates.add(t);
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
}
