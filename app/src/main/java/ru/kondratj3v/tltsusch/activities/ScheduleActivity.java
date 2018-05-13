package ru.kondratj3v.tltsusch.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.kondratj3v.tltsusch.R;
import ru.kondratj3v.tltsusch.fragments.ScheduleFragment;
import ru.kondratj3v.tltsusch.managers.ThemeManager;
import ru.kondratj3v.tltsusch.models.Day;
import ru.kondratj3v.tltsusch.models.Lesson;
import ru.kondratj3v.tltsusch.utils.DateUtils;

public class ScheduleActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private List<Day> listWeek;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    SharedPreferences sharedPreference;

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance(listWeek.get(position));
        }

        @Override
        public int getCount() {
            return listWeek.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listWeek.get(position).getDate();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeManager.getThemeStyle(R.style.AppTheme));
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // показываем стрелку возврата на предыдущее активити в тулбаре
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        // ищем viewpager и инициализируем вкладки
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        listWeek = new ArrayList<>();
        // Настраиваем доступ к хранилищу
        sharedPreference = getPreferences(MODE_PRIVATE);
        // Получаем строки из интента
        Intent intent = getIntent();
        String inst = intent.getStringExtra("inst");
        String group = intent.getStringExtra("group");
        int week = intent.getIntExtra("week", 0);
        // устанавливаем заголовок
        setTitle(group + " | " + week + " неделя");
        // создаем новый поток для получения расписания
        getTimetable(inst, group, week);
    }

    public void getTimetable(final String inst, final String group, final int week) {
        dialog = ProgressDialog.show(this, "Подождите...", "Загрузка актуального расписания.", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int day = 1; day < 7; day++) {
                        // подключение к серверу расписания
                        Document doc = Jsoup.connect("http://tt.binarus.ru/php/opentt.php")
                                .data("inst", inst)
                                .data("grup", group)
                                .data("ned", String.valueOf(week))
                                .data("day", String.valueOf(day))
                                .post();
                        // объект день, инициализатор создания вкладки для выбранного дня
                        Day currentDay = new Day(DateUtils.getTabName(day));
                        // выбираем пару, используя html
                        Elements elements = doc.select("td[colspan=6]");
                        for (int i = 0; i < elements.size(); i++) {
                            Element element = elements.get(i);
                            if (!element.text().isEmpty()) {
                                // создаем объект урок заполняем его парсером с сервера
                                Lesson lesson = getLessonFromServer(i, element);
                                // добавляем наш урок в список выбранного дня
                                currentDay.addLesson(lesson);
                            }
                        }
                        if (!currentDay.getLessons().isEmpty()) {
                            // если список не пуст, добавляем выбранный день в общий список фрагментов
                            listWeek.add(currentDay);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // заполняем adapter и viewpager
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        tabLayout.setupWithViewPager(mViewPager);
                        dialog.dismiss();
                        // если список с расписанием пустой
                        if (listWeek.isEmpty()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
                            builder.setTitle("Упс...")
                                    .setMessage("Похоже, расписание для группы на эту неделю отстуствует. Возвращаемся.")
                                    .setCancelable(true)
                                    .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                });
            }
        }).start();
    }

    public Lesson getLessonFromServer(int i, Element el) {
        String number = String.valueOf(i + 1);
        String name = el.text().substring(0, el.text().indexOf('('));
        String teacher = el.text().substring(el.text().indexOf(')') + 2, el.text().indexOf('.') + 3);
        String type = el.text().substring(el.text().indexOf('(') + 1, el.text().indexOf(')'));
        String room = el.select(".kor").text().substring(el.select(".kor").text().indexOf(')') + 2).replace(' ', '\n');
        String time = getResources().getStringArray(R.array.class_time)[i];
        return new Lesson(number, time, type, room, name, teacher);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_week:

                return true;
            case android.R.id.home:
                /*sPref.edit().putString("rememberedInst", "")
                        .putString("rememberedGroup", "").apply();
                */
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}