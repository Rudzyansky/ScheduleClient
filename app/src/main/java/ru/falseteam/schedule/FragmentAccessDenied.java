package ru.falseteam.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;

public class FragmentAccessDenied extends Fragment implements Redrawable {

    private Fragment parent;
    private String reason;
    private ArrayList<Groups> groupies = new ArrayList<>();
    private TextView reasonView;

    public static Fragment init(Fragment parent, String reason, Groups... groupies) {
        FragmentAccessDenied fragment = new FragmentAccessDenied();
        fragment.parent = parent;
        fragment.reason = reason;
        Collections.addAll(fragment.groupies, groupies);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.access_denied, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reasonView = (TextView) view.findViewById(R.id.reason);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reasonView.setText(reason);
            }
        });
        if (groupies.contains(Data.getCurrentGroup())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) getActivity()).setFragment(parent);
                }
            });
        }
    }
}
