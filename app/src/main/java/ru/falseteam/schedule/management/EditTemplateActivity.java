package ru.falseteam.schedule.management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.gui.MultiSpinner;
import ru.falseteam.schedule.serializable.Lesson;
import ru.falseteam.schedule.serializable.LessonNumber;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.serializable.WeekDay;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.schedule.socket.commands.DeleteTemplate;
import ru.falseteam.schedule.socket.commands.UpdateTemplate;
import ru.falseteam.vframe.redraw.Redrawable;
import ru.falseteam.vframe.redraw.Redrawer;

public class EditTemplateActivity extends AppCompatActivity implements Redrawable, View.OnClickListener, MultiSpinner.MultiSpinnerListener {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Redrawer.addRedrawable(this);
        Worker.get().getSubscriptionManager().subscribe("GetWeekDays");
        Worker.get().getSubscriptionManager().subscribe("GetLessonNumbers");
        Worker.get().getSubscriptionManager().subscribe("GetLessons");
        redraw();
    }

    @Override
    protected void onPause() {
        Worker.get().getSubscriptionManager().unsubscribe("GetLessons");
        Worker.get().getSubscriptionManager().unsubscribe("GetLessonNumbers");
        Worker.get().getSubscriptionManager().unsubscribe("GetWeekDays");
        Redrawer.removeRedrawable(this);
        super.onPause();
    }

    @Override
    public void redraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Worker.get().getSubscriptionManager().getData("GetWeekDays") != null &&
                        Worker.get().getSubscriptionManager().getData("GetLessonNumbers") != null &&
                        Worker.get().getSubscriptionManager().getData("GetLessons") != null) {
                    emptyView.setVisibility(View.GONE);
                    contentView.setVisibility(View.VISIBLE);
                    initView();
                }
            }
        });
    }

    private boolean init = false;

    private void initView() {
        if (init) {
            ((ArrayAdapter) lesson.getAdapter()).notifyDataSetChanged();
            return;
        }
        init = true;

        ((TextView) contentView.findViewById(R.id.id)).setText(String.valueOf(template.id));

        dayOfWeek = (Spinner) contentView.findViewById(R.id.day_of_week);
        dayOfWeek.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ((List<WeekDay>) Worker.get().getSubscriptionManager().getData("GetWeekDays").get("week_days"))));
        dayOfWeek.setSelection(template.weekDay.id - 1);

        lessonNumber = (Spinner) contentView.findViewById(R.id.lesson_number);
        lessonNumber.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ((List<WeekDay>) Worker.get().getSubscriptionManager().getData("GetLessonNumbers").get("lesson_numbers"))));
        lessonNumber.setSelection(template.lessonNumber.id - 1);

        evenness = (Spinner) contentView.findViewById(R.id.evenness);
        evenness.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.evenness)));
        evenness.setSelection(template.weeks.get(31) ? template.weeks.get(30) ? 0 : template.weeks.get(29) ? 1 : 2 : 3);
        evenness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkVisibleWeeks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkVisibleWeeks();

        MultiSpinner chooseWeeks = (MultiSpinner) findViewById(R.id.choose_weeks);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 18; ++i) items.add(i + 1 + " неделя");
        chooseWeeks.setItems(items, template.weeks, "Выберите", this);

        lesson = (Spinner) contentView.findViewById(R.id.lesson);
        lesson.setAdapter(new ArrayAdapter<>(contentView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ((List<WeekDay>) Worker.get().getSubscriptionManager().getData("GetLessons").get("lessons"))));
        lesson.setSelection(template.lesson.id - 1);

        Button save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(this);

        Button delete = (Button) findViewById(R.id.btnDelete);
        delete.setOnClickListener(this);
    }

    @Override
    public void onItemsSelected(boolean[] checked) {
        for (int i = 0; i < checked.length; ++i) template.weeks.set(i, checked[i]);
        checkWeeks();
    }

    private void checkWeeks() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 18; ++i) if (template.weeks.get(i)) sb.append(i + 1).append(' ');
        ((TextView) findViewById(R.id.total_weeks)).setText(sb.toString());
    }

    private void checkVisibleWeeks() {
        if (evenness.getSelectedItemPosition() == 3) {
            checkWeeks();
            findViewById(R.id.weeks).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.weeks).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                template.weekDay = (WeekDay) dayOfWeek.getSelectedItem();
                template.lessonNumber = (LessonNumber) lessonNumber.getSelectedItem();
                switch (evenness.getSelectedItemPosition()) {
                    case 0:
                        template.weeks.set(31, true);
                        template.weeks.set(30, true);
                        template.weeks.set(29, false);
                        break;
                    case 1:
                        template.weeks.set(31, true);
                        template.weeks.set(30, false);
                        template.weeks.set(29, true);
                        break;
                    case 2:
                        template.weeks.set(31, true);
                        template.weeks.set(30, false);
                        template.weeks.set(29, false);
                        break;
                    case 3:
                        template.weeks.set(31, false);
                        template.weeks.set(30, false);
                        template.weeks.set(29, false);
                        break;
                }
                template.lesson = (Lesson) lesson.getSelectedItem();
                Worker.get().sendFromMainThread(UpdateTemplate.getRequest(template));
                break;
            case R.id.btnDelete:
                Worker.get().sendFromMainThread(DeleteTemplate.getRequest(template));
                break;
        }
        finish();
    }
}
