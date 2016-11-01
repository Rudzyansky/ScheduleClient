package ru.falseteam.schedule.management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.falseteam.schedule.R;

import static ru.falseteam.schedule.socket.commands.GetPairs.pairs;

public class ListOfPairsActivity extends AppCompatActivity {

    PairAdapter pairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_pairs);

        pairAdapter = new PairAdapter(this, pairs);
        ((ListView) findViewById(R.id.pairs)).setAdapter(pairAdapter);
    }

    private class PairAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<Pair> objects;

        PairAdapter(Context context, ArrayList<Pair> pairs) {
            this.context = context;
            objects = pairs;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) view = inflater.inflate(R.layout.item_pair, parent);
            Pair pair = getPair(i);

            ((TextView) view.findViewById(R.id.pairName)).setText(pair.getName());
            ((TextView) view.findViewById(R.id.pairAudience)).setText(pair.getAudience());
            Button pairEdit = (Button) view.findViewById(R.id.pairEdit);
            pairEdit.setTag(pair);
            pairEdit.setOnClickListener(myOnClickListener);
            return null;
        }

        private Pair getPair(int i) {
            return (Pair) getItem(i);
        }

        private View.OnClickListener myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListOfPairsActivity.this, EditPairActivity.class);
                Pair pair = (Pair) view.getTag();
                intent.putExtra("pair", pair);
//                intent.putExtra("exists", pair.isExists());
//                intent.putExtra("id", pair.getId());
//                intent.putExtra("name", pair.getName());
//                intent.putExtra("audience", pair.getAudience());
//                intent.putExtra("teacher", pair.getTeacher());
//                intent.putExtra("last_task", pair.getLastTask());
                startActivity(intent);
            }
        };
    }
}
