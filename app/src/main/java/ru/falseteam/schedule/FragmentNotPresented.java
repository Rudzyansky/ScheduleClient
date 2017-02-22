package ru.falseteam.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.UserPresented;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.CountPresented;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class FragmentNotPresented extends Fragment implements Redrawable {

    private TextView count;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_not_presented);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_not_presented, container, false);
        Worker.get().sendFromMainThread(CountPresented.getRequest());
        count = (TextView) rootView.findViewById(R.id.count);
        list = (ListView) rootView.findViewById(R.id.list);
        list.setAdapter(new Adapter(getActivity()));
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
            case admin:
            case developer:
                break;
            case disconnected:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_offline), Groups.developer, Groups.admin));
            default:
                ((MainActivity) getActivity()).setFragment(FragmentAccessDenied.init(this, getString(R.string.access_denied_not_allowed), Groups.developer, Groups.admin));
                return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                count.setText(String.valueOf(CountPresented.count));
                ((Adapter) list.getAdapter()).setObjects(CountPresented.ups);
            }
        });
    }


    private class Adapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<UserPresented> objects = new ArrayList<>();

        Adapter(Context context) {
            objects = new ArrayList<>();
            inflater = LayoutInflater.from(context);
        }

        void setObjects(List<UserPresented> objects) {
            this.objects = objects;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_user, parent, false);
            UserPresented user = (UserPresented) getItem(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(user.user.name);
            ((TextView) convertView.findViewById(R.id.group)).setText(String.valueOf(user.notPresented));
            return convertView;
        }
    }
}
