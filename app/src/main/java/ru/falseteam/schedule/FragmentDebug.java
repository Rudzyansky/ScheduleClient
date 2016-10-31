package ru.falseteam.schedule;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;

import static ru.falseteam.schedule.Data.Groups.developer;

public class FragmentDebug extends Fragment implements Redrawable {

    TextView group;
    TextView version;

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
            case disconnected:
            case developer:
                break;
//            case disconnected:
//                (new FragmentAccessDenied()).init(getActivity(), this, R.string.access_denied_offline, developer);
//                return;
            default:
                (new FragmentAccessDenied()).init(getActivity(), this, R.string.access_denied_not_allowed, developer);
                return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                group.setText(Data.getCurrentGroup().name());
                version.setText(Data.getClientVersion());
            }
        });
    }
}
