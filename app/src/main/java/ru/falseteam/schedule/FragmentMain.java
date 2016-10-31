package ru.falseteam.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;

public class FragmentMain extends Fragment implements Redrawable {
    private View container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_main);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.container = new RelativeLayout(container.getContext());
        return this.container;
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
        //
    }
}
