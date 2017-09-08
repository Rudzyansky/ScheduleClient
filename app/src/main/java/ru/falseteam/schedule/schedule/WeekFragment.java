package ru.falseteam.schedule.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.falseteam.schedule.R;

public class WeekFragment extends Fragment {
    private int week;

    public WeekFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_list_of_templates, container, false);

        ((TextView) root.findViewById(R.id.week_number)).setText((week + 1) + " неделя");

        View emptyView = root.findViewById(R.id.emptyView);
        emptyView.setVisibility(View.GONE);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.content);
        viewPager.setVisibility(View.VISIBLE);

        Adapter adapter = new Adapter(getChildFragmentManager(), week);
        viewPager.setAdapter(adapter);

        int day;
        // TODO: 08.09.17 мегакостыль
        if (week < (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6 - 27 - 1 - 2)) {
            day = 6;
        } else if (week > (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6 - 27 - 1 - 2)) {
            day = 0;
        } else {
            Calendar c = GregorianCalendar.getInstance();
            day = c.get(Calendar.DAY_OF_WEEK) - 2;
            if (day == -1) day = 6;
        }
        viewPager.setCurrentItem(day);

        return root;
    }

    public static WeekFragment newInstance(int week) {
        WeekFragment fragment = new WeekFragment();
        fragment.week = week;
        return fragment;
    }

    private class Adapter extends FragmentPagerAdapter {
        private int week;

        Adapter(FragmentManager fm, int week) {
            super(fm);
            this.week = week;
        }

        @Override
        public Fragment getItem(int position) {
            return InnerFragment.newInstance(week, position);
        }

        @Override
        public int getCount() {
            return 7;
        }
    }
}