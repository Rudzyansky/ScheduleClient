package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetPairs;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private TextView userName;
    private TextView userAudience;
    private TextView userTeacher;
    private TextView userLastTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        user = (User) getIntent().getSerializableExtra("user");

        setTitle(user.name);

        userName = (TextView) findViewById(R.id.name);
        userAudience = (TextView) findViewById(R.id.audience);
        userTeacher = (TextView) findViewById(R.id.teacher);
        userLastTask = (TextView) findViewById(R.id.lastTask);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(user.id));
        userName.setText(user.name);
//        userAudience.setText(user.audience);
//        userTeacher.setText(user.teacher);
//        userLastTask.setText(user.lastTask);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                user.name = userName.getText().toString();
//                user.audience = userAudience.getText().toString();
//                user.teacher = userTeacher.getText().toString();
//                user.lastTask = userLastTask.getText().toString();
                map.clear();
                map.put("command", "change_pair");
                map.put("pair", user);
                Worker.get().send(map);
                finish();
                break;
            case R.id.btnDelete:
                map.clear();
                map.put("command", "delete_pair");
                map.put("id", user.id);
                Worker.get().send(map);
                finish();
                break;
        }
        Worker.get().send(GetPairs.getRequest());
    }
}
