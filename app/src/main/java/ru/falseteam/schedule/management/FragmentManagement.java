package ru.falseteam.schedule.management;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.FragmentAccessDenied;
import ru.falseteam.schedule.R;
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Groups;

public class FragmentManagement extends Fragment implements Redrawable, View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_management);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_management, container, false);
        rootView.findViewById(R.id.buttonLessons).setOnClickListener(this);
        rootView.findViewById(R.id.buttonUsers).setOnClickListener(this);
        rootView.findViewById(R.id.buttonTemplate).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Redrawer.add(this);
        redraw();
    }

    @Override
    public void onDestroyView() {
        Redrawer.remove(this);
        super.onDestroyView();
    }

    @Override
    public void redraw() {
        switch (Data.getCurrentGroup()) {
            case developer:
            case admin:
                break;
            case disconnected:
                (new FragmentAccessDenied()).init(getActivity(), this, R.string.access_denied_offline, Groups.admin, Groups.developer);
                return;
            default:
                (new FragmentAccessDenied()).init(getActivity(), this, R.string.access_denied_not_allowed, Groups.admin, Groups.developer);
                return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLessons:
                startActivity(new Intent(getActivity(), ListOfLessonsActivity.class));
                break;
            case R.id.buttonUsers:
                startActivity(new Intent(getActivity(), ListOfUsersActivity.class));
                break;
            case R.id.buttonTemplate:
                startActivity(new Intent(getActivity(), ViewTemplateActivity.class));
                break;
        }
    }
}
