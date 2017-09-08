package ru.falseteam.schedule.statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.falseteam.schedule.FragmentAccessDenied;
import ru.falseteam.schedule.MainActivity;
import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

import static android.view.View.GONE;

public class FragmentStatistics extends Fragment implements Redrawable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_not_presented);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_not_presented, container, false);
        rootView.findViewById(R.id.header).setVisibility(GONE);
        ListView list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new Adapter(getActivity()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).setFragmentWithStack(FragmentNotPresented.newInstance(position + 1));
            }
        });
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
            case user:
            case admin:
            case developer:
                break;
            case disconnected:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_offline), Groups.developer, Groups.admin, Groups.user));
                break;
            default:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_not_allowed), Groups.developer, Groups.admin, Groups.user));
                break;
        }
    }

    private class Adapter extends BaseAdapter {
        private LayoutInflater inflater;

        Adapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 17;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_user, parent, false);

            ((TextView) convertView.findViewById(R.id.name)).setText(position + 1 + " неделя");
            return convertView;
        }
    }
}
