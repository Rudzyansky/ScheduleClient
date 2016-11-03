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
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetPairs;

public class ListOfPairsActivity extends AppCompatActivity implements Redrawable {

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
                Intent intent = new Intent(ListOfPairsActivity.this, EditPairActivity.class);
                Pair pair = (Pair) pairAdapter.getItem(position);
                intent.putExtra("pair", pair);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        lv.setEmptyView(findViewById(R.id.emptyView));
        Redrawer.add(this);
        redraw();
        Worker.get().send(GetPairs.getRequest());
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
            case R.id.action_add_pair:
                Intent intent = new Intent(ListOfPairsActivity.this, EditPairActivity.class);
                Pair pair = Pair.Factory.getDefault();
                intent.putExtra("pair", pair);
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
                if (GetPairs.pairs != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setText(R.string.empty_list);
                    pairAdapter.setObjects(GetPairs.pairs);
                }
            }
        });
    }

    private class PairAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Pair> objects;

        PairAdapter(Context context) {
            objects = new ArrayList<>();
            inflater = LayoutInflater.from(context);
        }

        void setObjects(List<Pair> objects) {
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
                convertView = inflater.inflate(R.layout.item_pair, parent, false);
            Pair pair = (Pair) getItem(position);

            ((TextView) convertView.findViewById(R.id.name)).setText(pair.name);
            ((TextView) convertView.findViewById(R.id.audience)).setText(pair.audience);
            return convertView;
        }
    }
}
