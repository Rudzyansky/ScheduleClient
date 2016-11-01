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

        pair = (Pair) getIntent().getSerializableExtra("pair");

        setTitle(pair.getName());

        pairName = (TextView) findViewById(R.id.pairName);
        pairAudience = (TextView) findViewById(R.id.pairAudience);
        pairTeacher = (TextView) findViewById(R.id.pairTeacher);
        pairLastTask = (TextView) findViewById(R.id.pairLastTask);

        ((TextView) findViewById(R.id.pairId)).setText(String.valueOf(pair.getId()));
        pairName.setText(pair.getName());
        pairAudience.setText(pair.getAudience());
        pairTeacher.setText(pair.getTeacher());
        pairLastTask.setText(pair.getLastTask());

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
                map.put("command", "change_pair");
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
