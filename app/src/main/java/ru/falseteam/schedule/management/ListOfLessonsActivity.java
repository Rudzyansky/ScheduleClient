package ru.falseteam.schedule.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetLessons;

public class ListOfLessonsActivity extends AppCompatActivity implements Redrawable {

    private PairAdapter pairAdapter;
    private View progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle(R.string.pairs);

        ListView lv = ((ListView) findViewById(R.id.list));

        pairAdapter = new PairAdapter(this);
        lv.setAdapter(pairAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfLessonsActivity.this, EditLessonActivity.class);
                Lesson lesson = (Lesson) pairAdapter.getItem(position);
                intent.putExtra("lesson", lesson);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        lv.setEmptyView(findViewById(R.id.emptyView));
        Redrawer.add(this);
        redraw();
        Worker.sendFromMainThread(GetLessons.getRequest());
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
                Intent intent = new Intent(ListOfLessonsActivity.this, EditLessonActivity.class);
                Lesson lesson = Lesson.Factory.getDefault();
                intent.putExtra("lesson", lesson);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getLessons() != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText(R.string.empty_list);
                    pairAdapter.setObjects(MainData.getLessons());
                }
            }
        });
    }

    private class PairAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Lesson> objects;

        PairAdapter(Context context) {
            objects = new ArrayList<>();
            inflater = LayoutInflater.from(context);
        }

        void setObjects(List<Lesson> objects) {
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
                convertView = inflater.inflate(R.layout.item_lesson, parent, false);
            Lesson lesson = (Lesson) getItem(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(lesson.name);
            ((TextView) convertView.findViewById(R.id.audience)).setText(lesson.audience);
            return convertView;
        }
    }
}
