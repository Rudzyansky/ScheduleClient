package ru.falseteam.schedule.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetTemplates;

public class ListOfTemplatesActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_templates);
        setTitle(getString(R.string.edit_template));

        emptyView = findViewById(R.id.emptyView);
        viewPager = (ViewPager) findViewById(R.id.content);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));

        Redrawer.add(this);
        redraw();

        Worker.sendFromMainThread(GetTemplates.getRequest());
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_via_add_btn, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                openTemplateEditor(Template.Factory.getDefault());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openTemplateEditor(Template template) {
        Intent intent = new Intent(this, EditTemplateActivity.class);
        intent.putExtra("template", template);
        startActivity(intent);
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getTemplates() != null) {
                    emptyView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Adapter from page viewer.
     */
    private class Adapter extends FragmentPagerAdapter {
        Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return InnerFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 7;
        }
    }

    /**
     * Fragment for {@link ListOfTemplatesActivity.Adapter}
     */
    public static class InnerFragment extends Fragment {
        private int dayOfWeek;

        public InnerFragment() {
        }

        private void openTemplateEditor(Template template) {
            Intent intent = new Intent(getActivity(), EditTemplateActivity.class);
            intent.putExtra("template", template);
            startActivity(intent);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_templates, container, false);

            TextView tv = (TextView) root.findViewById(R.id.day_of_week);
            tv.setText(getResources().getStringArray(R.array.week_days)[dayOfWeek]);

            final Adapter adapter = new Adapter(root.getContext(), dayOfWeek);
            ListView list = (ListView) root.findViewById(R.id.list);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openTemplateEditor(adapter.getItem(position));
                }
            });

            View view = root.findViewById(R.id.emptyView);
            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            list.setEmptyView(view);
            list.setAdapter(adapter);

            return root;
        }

        public static InnerFragment newInstance(int dayOfWeek) {
            InnerFragment fragment = new InnerFragment();
            fragment.dayOfWeek = dayOfWeek;
            return fragment;
        }

        private class Adapter extends BaseAdapter {
            private Context context;
            private List<Template> templates = new ArrayList<>();

            public Adapter(Context context, int dayOfWeek) {
                this.context = context;
                for (Template t : MainData.getTemplates())
                    if (t.weekDay.id == dayOfWeek + 1) templates.add(t);
            }

            @Override
            public int getCount() {
                return templates.size();
            }

            @Override
            public Template getItem(int position) {
                return templates.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = LayoutInflater.from(context)
                            .inflate(R.layout.item_template, parent, false);
                Template t = getItem(position);
                ((TextView) convertView.findViewById(R.id.lesson_number)).setText(String.valueOf(t.lessonNumber.id));
                TextView weekEvenness = (TextView) convertView.findViewById(R.id.week_evenness);
                if (t.weekEvenness > 0) weekEvenness.setVisibility(View.VISIBLE);
                weekEvenness.setText(t.weekEvenness == 1 ? "II" : "I");
                ((TextView) convertView.findViewById(R.id.begin))
                        .setText(t.lessonNumber.begin.toString().substring(0, 5));
                ((TextView) convertView.findViewById(R.id.end))
                        .setText(t.lessonNumber.end.toString().substring(0, 5));
                ((TextView) convertView.findViewById(R.id.name)).setText(t.lesson.name);
                ((TextView) convertView.findViewById(R.id.audience)).setText(t.lesson.audience);
                return convertView;
            }
        }
    }
}
