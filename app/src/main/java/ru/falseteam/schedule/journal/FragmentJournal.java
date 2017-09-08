package ru.falseteam.schedule.journal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import ru.falseteam.schedule.FragmentAccessDenied;
import ru.falseteam.schedule.MainActivity;
import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentJournal extends Fragment implements Redrawable, Worker.OnChangePermissionListener<Groups> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_journal);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView calendarView = (CalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ((MainActivity) getActivity()).setFragmentWithStack(InnerFragment.newInstance(new java.sql.Date(year - 1900, month, dayOfMonth)));
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Worker.get().addOnConnectionChangeStateListener(this);
        onChangePermission(Worker.get().getCurrentPermission());
        Redrawer.addRedrawable(this);
        redraw();
    }

    @Override
    public void onPause() {
        Worker.get().removeOnConnectionChangeStateListener(this);
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        switch (Worker.get().getCurrentPermission()) {
            case developer:
            case admin:
                break;
            case disconnected:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_offline), Groups.admin, Groups.developer));
                return;
            default:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_not_allowed), Groups.admin, Groups.developer));
        }
    }

    @Override
    public void onChangePermission(Groups permission) {
    }
}
