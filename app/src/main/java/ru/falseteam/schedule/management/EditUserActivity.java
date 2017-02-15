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
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetUsers;
import ru.falseteam.vframe.socket.Container;

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
        group.setSelection(groups.indexOf(user.permissions.name()));

        if (user.exists) this.vkId.setVisibility(View.GONE);
        else userVkId.setVisibility(View.GONE);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Container c = null;
        String newGroup = null;
        switch (view.getId()) {
            case R.id.btnSave:
                user.name = name.getText().toString();
                user.permissions = null;
                //user.permissions = Groups.valueOf(groups.get(group.getSelectedItemPosition()));
                newGroup = groups.get(group.getSelectedItemPosition());
                if (!user.exists) user.vkId = Integer.parseInt(vkId.getText().toString());
                c = new Container("UpdateUser", true);
                break;
            case R.id.btnDelete:
                c = new Container("DeleteUser", true);
                break;
        }
        assert c != null;
        c.data.put("user", user);
        // TODO: 09.02.17 мегакостыль орундий-256
        c.data.put("group", newGroup);
        Worker.get().sendFromMainThread(c);
        finish();
//        Worker.get().sendFromMainThread(GetUsers.getRequest());
    }
}
