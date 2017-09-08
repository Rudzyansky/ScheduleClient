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

import java.util.Calendar;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentSchedule extends Fragment implements Redrawable {
    private View emptyView;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_schedule);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_of_templates, container, false);

        emptyView = rootView.findViewById(R.id.emptyView);
        viewPager = (ViewPager) rootView.findViewById(R.id.content);

        Adapter adapter = new Adapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

//        int week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6 - 1;
        // TODO: 08.09.17 Мегакостыль
        viewPager.setCurrentItem((Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6 - 27 - 1 - 2));

        rootView.findViewById(R.id.week_number).setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        Worker.get().getSubscriptionManager().subscribeWithCache("GetTemplates");
//        Worker.get().getSubscriptionManager().subscribeWithCache("GetWeekNum");
        redraw();
    }

    @Override
    public void onPause() {
//        Worker.get().getSubscriptionManager().unsubscribe("GetWeekNum");
        Worker.get().getSubscriptionManager().unsubscribe("GetTemplates");
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        if (Worker.get().getSubscriptionManager().getData("GetTemplates") != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emptyView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            });
//        if (Worker.get().getSubscriptionManager().getData("GetWeekNum") != null)
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    viewPager.setCurrentItem((Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6 - 27 - 1));
//                }
//            });
    }

    private class Adapter extends FragmentPagerAdapter {
        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeekFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 17;
        }
    }
}
