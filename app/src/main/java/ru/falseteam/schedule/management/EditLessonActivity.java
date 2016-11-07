package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetLessons;

public class EditLessonActivity extends AppCompatActivity implements View.OnClickListener {

    private Lesson lesson;

    private TextView name;
    private TextView audience;
    private TextView teacher;
    private TextView lastTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);

        lesson = (Lesson) getIntent().getSerializableExtra("lesson");

        setTitle(lesson.name);

        name = (TextView) findViewById(R.id.name);
        audience = (TextView) findViewById(R.id.vkId);
        teacher = (TextView) findViewById(R.id.teacher);
        lastTask = (TextView) findViewById(R.id.lastTask);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(lesson.id));
        name.setText(lesson.name);
        audience.setText(lesson.audience);
        teacher.setText(lesson.teacher);
        lastTask.setText(lesson.lastTask);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                lesson.name = name.getText().toString();
                lesson.audience = audience.getText().toString();
                lesson.teacher = teacher.getText().toString();
                lesson.lastTask = lastTask.getText().toString();
                map.clear();
                map.put("command", "update_lesson");
                break;
            case R.id.btnDelete:
                map.put("command", "delete_lesson");
                break;
        }
        map.put("lesson", lesson);
        Worker.sendFromMainThread(map);
        finish();
        Worker.sendFromMainThread(GetLessons.getRequest());
    }
}
