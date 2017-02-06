package ru.falseteam.schedule.journal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import ru.falseteam.schedule.FragmentAccessDenied;
import ru.falseteam.schedule.MainActivity;
import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.OnChangeGroupListener;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetJournal;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentJournal extends Fragment implements Redrawable, OnChangeGroupListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_journal);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        MaterialCalendarView calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                ((MainActivity) getActivity())
                        .setFragment(InnerFragment.newInstance(new java.sql.Date(date.getDate().getTime())));
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        OnChangeGroup.add(this, Groups.admin, Groups.developer);
        onChangeGroup();
        Redrawer.addRedrawable(this);
        redraw();
    }

    @Override
    public void onPause() {
        OnChangeGroup.remove(this);
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        switch (MainData.getCurrentGroup()) {
            case developer:
            case admin:
                break;
            case disconnected:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_offline), Groups.admin, Groups.developer));
                return;
            default:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_not_allowed), Groups.admin, Groups.developer));
                return;
        }
    }

    @Override
    public void onChangeGroup() {
        Worker.get().sendFromMainThread(GetJournal.getRequest());
    }
}
