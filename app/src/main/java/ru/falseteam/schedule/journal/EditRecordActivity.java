package ru.falseteam.schedule.journal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawable;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.serializable.LessonNumber;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.DeleteTemplate;
import ru.falseteam.schedule.socket.commands.GetLessonNumbers;
import ru.falseteam.schedule.socket.commands.GetLessons;
import ru.falseteam.schedule.socket.commands.GetTemplates;
import ru.falseteam.schedule.socket.commands.GetWeekDays;
import ru.falseteam.schedule.socket.commands.UpdateTemplate;

public class EditRecordActivity extends AppCompatActivity implements Redrawable, View.OnClickListener {
    private View emptyView;
    private View contentView;

    private Spinner dayOfWeek;
    private Spinner lessonNumber;
    private Spinner evenness;
    private Spinner lesson;

    private Template template;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);

        emptyView = findViewById(R.id.emptyView);
        contentView = findViewById(R.id.content);

        template = (Template) getIntent().getSerializableExtra("template");

        Worker.get().sendFromMainThread(GetWeekDays.getRequest());
        Worker.get().sendFromMainThread(GetLessonNumbers.getRequest());
        Worker.get().sendFromMainThread(GetLessons.getRequest());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Redrawer.add(this);
        redraw();
    }

    @Override
    protected void onPause() {
        Redrawer.remove(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (MainData.getWeekDays() != null &&
                        MainData.getLessonNumbers() != null &&
                        MainData.getLessons() != null) {
                    emptyView.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    initView();
                }
            }
        });
    }

    private boolean init = false;

    private void initView() {
        if (init) return;
        init = true;

        ((TextView) contentView.findViewById(R.id.id)).setText(String.valueOf(template.id));

        dayOfWeek = (Spinner) contentView.findViewById(R.id.day_of_week);
        dayOfWeek.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, MainData.getWeekDays()));
        dayOfWeek.setSelection(template.weekDay.id - 1);

        lessonNumber = (Spinner) contentView.findViewById(R.id.lesson_number);
        lessonNumber.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, MainData.getLessonNumbers()));
        lessonNumber.setSelection(template.lessonNumber.id - 1);

        evenness = (Spinner) contentView.findViewById(R.id.evenness);
        evenness.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.evenness)));
        // TODO: 04.02.17
//        evenness.setSelection(template.weekEvenness);

        lesson = (Spinner) contentView.findViewById(R.id.lesson);
        lesson.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, MainData.getLessons()));
        lesson.setSelection(template.lesson.id - 1);

        Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(this);

        Button delete = (Button) findViewById(R.id.btnDelete);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                template.weekDay = (WeekDay) dayOfWeek.getSelectedItem();
                template.lessonNumber = (LessonNumber) lessonNumber.getSelectedItem();
                // TODO: 04.02.17
//                template.weekEvenness = evenness.getSelectedItemPosition();
                template.lesson = (Lesson) lesson.getSelectedItem();
                Worker.get().sendFromMainThread(UpdateTemplate.getRequest(template));
                break;
            case R.id.btnDelete:
                Worker.get().sendFromMainThread(DeleteTemplate.getRequest(template));
                break;
        }
        finish();
        Worker.get().sendFromMainThread(GetTemplates.getRequest());
    }
}
