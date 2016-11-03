package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetPairs;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private TextView userName;

    private List<String> groups;
    private Spinner userGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        user = (User) getIntent().getSerializableExtra("user");

        setTitle(user.name);

        userName = (TextView) findViewById(R.id.name);
        Button userVkId = (Button) findViewById(R.id.vk);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(user.id));
        userName.setText(user.name);
        userVkId.setText(String.valueOf(user.vkId));

        groups = new ArrayList<>();
        groups.add(Groups.unconfirmed.name());
        groups.add(Groups.user.name());
        groups.add(Groups.admin.name());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGroup = (Spinner) findViewById(R.id.group);
        userGroup.setAdapter(adapter);
        userGroup.setPrompt("Группа доступа");
        userGroup.setSelection(groups.indexOf(user.group.name()));

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                user.name = userName.getText().toString();
                user.group = Groups.valueOf(groups.get(userGroup.getSelectedItemPosition()));
                map.clear();
                map.put("command", "update_user");
                map.put("user", user);
                Worker.get().send(map);
                finish();
                break;
            case R.id.btnDelete:
                map.clear();
                map.put("command", "delete_user");
                map.put("user", user);
                Worker.get().send(map);
                finish();
                break;
        }
        Worker.get().send(GetPairs.getRequest());
    }
}
