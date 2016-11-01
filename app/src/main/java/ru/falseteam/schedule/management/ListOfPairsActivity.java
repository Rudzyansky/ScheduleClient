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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        setContentView(R.layout.activity_list_of_pairs);
        setTitle(R.string.pairs);

        ListView lv = ((ListView) findViewById(R.id.pairs));

        pairAdapter = new PairAdapter(this);
        lv.setAdapter(pairAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListOfPairsActivity.this, EditPairActivity.class);
                Pair pair = (Pair) pairAdapter.getItem(i);
                intent.putExtra("pair", pair);
                startActivity(intent);
            }
        });
        progressBar = findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        lv.setEmptyView(findViewById(R.id.emptyView));
        Worker.get().send(GetPairs.getRequest());
        Redrawer.add(this);
        redraw();
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pairs, menu);
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
        private ArrayList<Pair> objects;

        PairAdapter(Context context) {
            objects = new ArrayList<>();
            inflater = LayoutInflater.from(context);
        }

        void setObjects(ArrayList<Pair> objects) {
            this.objects = objects;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public Object getItem(int i) {
            return objects.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if (view == null) view = inflater.inflate(R.layout.item_pair, parent, false);
            Pair pair = (Pair) getItem(i);

            ((TextView) view.findViewById(R.id.itemPairName)).setText(pair.name);
            ((TextView) view.findViewById(R.id.itemPairAudience)).setText(pair.audience);
            return view;
        }
    }
}
