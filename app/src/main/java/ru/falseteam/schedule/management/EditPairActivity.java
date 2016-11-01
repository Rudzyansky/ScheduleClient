package ru.falseteam.schedule.management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Pair;
import ru.falseteam.schedule.socket.Worker;

public class EditPairActivity extends AppCompatActivity implements View.OnClickListener {

    private Pair pair;

    private TextView pairName;
    private TextView pairAudience;
    private TextView pairTeacher;
    private TextView pairLastTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pair);

//        pair = new Pair();
//        pair.setExists(getIntent().getBooleanExtra("exists", false));
//        pair.setId(getIntent().getIntExtra("id", 0));
//        pair.setName(getIntent().getStringExtra("name"));
//        pair.setAudience(getIntent().getStringExtra("audience"));
//        pair.setLastTask(getIntent().getStringExtra("last_task"));
        pair = (Pair) getIntent().getSerializableExtra("pair");

        ((TextView) findViewById(R.id.pairId)).setText(String.valueOf(pair.getId()));
        pairName = (TextView) findViewById(R.id.pairName);
        pairAudience = (TextView) findViewById(R.id.pairAudience);
        pairTeacher = (TextView) findViewById(R.id.pairTeacher);
        pairLastTask = (TextView) findViewById(R.id.pairLastTask);

        findViewById(R.id.btnSend).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                pair.setName(pairName.getText().toString());
                pair.setAudience(pairAudience.getText().toString());
                pair.setTeacher(pairTeacher.getText().toString());
                pair.setLastTask(pairLastTask.getText().toString());
                Map<String, Object> map = new HashMap<>();
                map.put("command", "changePair");
//                map.put("exists", pair.isExists());
//                map.put("id", pair.getId());
//                map.put("name", pair.getName());
//                map.put("audience", pair.getAudience());
//                map.put("teacher", pair.getTeacher());
//                map.put("last_task", pair.getLastTask());
                map.put("pair", pair);
                Worker.get().send(map);
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
