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

    private TextView pairName;
    private TextView pairAudience;
    private TextView pairTeacher;
    private TextView pairLastTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pair);

        lesson = (Lesson) getIntent().getSerializableExtra("lesson");

        setTitle(lesson.name);

        pairName = (TextView) findViewById(R.id.name);
        pairAudience = (TextView) findViewById(R.id.vkId);
        pairTeacher = (TextView) findViewById(R.id.teacher);
        pairLastTask = (TextView) findViewById(R.id.lastTask);

        ((TextView) findViewById(R.id.id)).setText(String.valueOf(lesson.id));
        pairName.setText(lesson.name);
        pairAudience.setText(lesson.audience);
        pairTeacher.setText(lesson.teacher);
        pairLastTask.setText(lesson.lastTask);

        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String, Object> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.btnSave:
                lesson.name = pairName.getText().toString();
                lesson.audience = pairAudience.getText().toString();
                lesson.teacher = pairTeacher.getText().toString();
                lesson.lastTask = pairLastTask.getText().toString();
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
