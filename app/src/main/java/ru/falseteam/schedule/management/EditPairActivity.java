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
import ru.falseteam.schedule.socket.commands.GetPairs;

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

        setTitle(pair.name);

        pairName = (TextView) findViewById(R.id.name);
        pairAudience = (TextView) findViewById(R.id.audience);
        pairTeacher = (TextView) findViewById(R.id.teacher);
        pairLastTask = (TextView) findViewById(R.id.lastTask);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(pair.id));
        pairName.setText(pair.name);
        pairAudience.setText(pair.audience);
        pairTeacher.setText(pair.teacher);
        pairLastTask.setText(pair.lastTask);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                pair.name = pairName.getText().toString();
                pair.audience = pairAudience.getText().toString();
                pair.teacher = pairTeacher.getText().toString();
                pair.lastTask = pairLastTask.getText().toString();
                map.clear();
                map.put("command", "change_pair");
                map.put("pair", pair);
                Worker.get().send(map);
                finish();
                break;
            case R.id.btnDelete:
                map.clear();
                map.put("command", "delete_pair");
                map.put("id", pair.id);
                Worker.get().send(map);
                finish();
                break;
        }
        Worker.get().send(GetPairs.getRequest());
    }
}
