package ru.falseteam.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;

//TODO ПЕРЕДЕЛАТЬ ЭТОТ ТРЭШАК
public class FragmentDebug extends Fragment implements Redrawable {

    private TextView group;
    private TextView version;
    private TextView redrawerCallsCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_debug);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_debug, container, false);
        group = (TextView) rootView.findViewById(R.id.group);
        version = (TextView) rootView.findViewById(R.id.version);
        redrawerCallsCounter = (TextView) rootView.findViewById(R.id.redrawer_calls_counter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Redrawer.add(this);
        redraw();
    }

    @Override
    public void onPause() {
        Redrawer.remove(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                group.setText(MainData.getCurrentGroup().name());
                version.setText(StaticData.getClientVersion());
                redrawerCallsCounter.setText(String.valueOf(Redrawer.getRedrawCounter()));
            }
        });
    }
}
