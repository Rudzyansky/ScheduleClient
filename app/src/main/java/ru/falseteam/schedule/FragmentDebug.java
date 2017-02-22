package ru.falseteam.schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentDebug extends Fragment implements Redrawable {

    private TextView group;
    private TextView version;

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
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        redraw();
    }

    @Override
    public void onPause() {
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        switch (Worker.get().getCurrentPermission()) {
            case disconnected:
            case admin:
            case developer:
                break;
            default:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_not_allowed), Groups.developer, Groups.admin, Groups.disconnected));
                return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                group.setText(Worker.get().getCurrentPermission().name());
                version.setText(StaticData.getClientVersion());
            }
        });
    }
}
