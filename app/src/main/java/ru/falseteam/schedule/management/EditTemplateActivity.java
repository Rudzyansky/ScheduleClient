package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.redraw.Redrawable;
import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.GetLessonNumbers;
import ru.falseteam.schedule.socket.commands.GetLessons;
import ru.falseteam.schedule.socket.commands.GetWeekDays;

public class EditTemplateActivity extends AppCompatActivity implements Redrawable {
    private View emptyView;
    private View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_template);

        emptyView = findViewById(R.id.emptyView);
        contentView = findViewById(R.id.content);

        Redrawer.add(this);
        redraw();

        Worker worker = Worker.get();
        worker.send(GetWeekDays.getRequest());
        worker.send(GetLessonNumbers.getRequest());
        worker.send(GetLessons.getRequest());
    }

    @Override
    protected void onDestroy() {
        Redrawer.remove(this);
        super.onDestroy();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (GetWeekDays.weekDays != null &&
                        GetLessonNumbers.lessonNumbers != null &&
                        GetLessons.lessons != null) {
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

        Spinner sp = (Spinner) contentView.findViewById(R.id.day_of_week);
        sp.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_item, (GetWeekDays.weekDays)));
        sp = (Spinner) contentView.findViewById(R.id.lesson_number);
        sp = (Spinner) contentView.findViewById(R.id.lesson);
    }
}
