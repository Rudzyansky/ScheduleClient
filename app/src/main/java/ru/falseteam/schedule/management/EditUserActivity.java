package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import ru.falseteam.schedule.socket.commands.GetUsers;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private TextView name;
    private EditText vkId;

    private List<String> groups;
    private Spinner group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        user = (User) getIntent().getSerializableExtra("user");

        setTitle(user.name);

        name = (TextView) findViewById(R.id.name);
        Button userVkId = (Button) findViewById(R.id.vk);
        this.vkId = (EditText) findViewById(R.id.vkId);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(user.id));
        name.setText(user.name);
        userVkId.setTag(String.valueOf(user.vkId));

        groups = new ArrayList<>();
        groups.add(Groups.unconfirmed.name());
        groups.add(Groups.user.name());
        groups.add(Groups.admin.name());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group = (Spinner) findViewById(R.id.group);
        group.setAdapter(adapter);
        group.setPrompt("Группа доступа");
        group.setSelection(groups.indexOf(user.group.name()));

        if (user.exists) this.vkId.setVisibility(View.INVISIBLE);
        else userVkId.setVisibility(View.INVISIBLE);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                user.name = name.getText().toString();
                user.group = Groups.valueOf(groups.get(group.getSelectedItemPosition()));
                if (!user.exists) user.vkId = Integer.parseInt(vkId.getText().toString());
                map.put("command", "update_user");
                break;
            case R.id.btnDelete:
                map.put("command", "delete_user");
                break;
        }
        map.put("user", user);
        Worker.sendFromMainThread(map);
        finish();
        Worker.sendFromMainThread(GetUsers.getRequest());
    }
}
