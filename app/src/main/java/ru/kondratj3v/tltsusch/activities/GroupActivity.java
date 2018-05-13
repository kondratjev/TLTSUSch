package ru.kondratj3v.tltsusch.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arlib.floatingsearchview.FloatingSearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kondratj3v.tltsusch.R;
import ru.kondratj3v.tltsusch.adapters.GroupAdapter;
import ru.kondratj3v.tltsusch.models.Group;
import ru.kondratj3v.tltsusch.utils.DateUtils;
import ru.kondratj3v.tltsusch.utils.Utils;

public class GroupActivity extends AppCompatActivity {
    FloatingSearchView mSearchView;
    ProgressDialog mDialog;
    ArrayAdapter<String> adapter;
    RecyclerView mRecyclerView;
    ListView listView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String inst;
    String group;
    int week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        final SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.group_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        week = DateUtils.getCurrentWeek();
        mySharedPreferences.edit().putInt("weekCounter", 1).apply();
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_set_week:
                        String[] weeks = new String[]{
                                DateUtils.getCurrentWeek() - 1 + " - предыдущая",
                                DateUtils.getCurrentWeek() + " - текущая",
                                DateUtils.getCurrentWeek() + 1 + " - следующая"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                        builder.setTitle("Выберите неделю")
                                .setCancelable(true)
                                .setSingleChoiceItems(weeks, mySharedPreferences.getInt("weekCounter", 0), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        week = (DateUtils.getCurrentWeek() - 1) + which;
                                        mySharedPreferences.edit().putInt("weekCounter", which).apply();
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    case android.R.id.home:
                        finish();
                        break;
                }
            }
        });

        final List<Group> groupsList = new ArrayList<>();
        if (Utils.isOnline(this)) {
            mDialog = ProgressDialog.show(this, "Подождите...", "Получение списка с группами", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int instCounter = 0; instCounter < 12; instCounter++) {
                        String inst = getResources().getStringArray(R.array.institutes_server)[instCounter];
                        try {
                            Document doc = Jsoup.connect("http://tt.binarus.ru/php/ttgr.php")
                                    .data("inst", inst)
                                    .post();
                            Elements elements = doc.select("a"); // получаем группы
                            for (int i = 0; i < elements.size(); i++) {
                                Element element = elements.get(i);
                                if (!element.text().isEmpty()) {
                                    groupsList.add(new Group(inst, element.html()));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new GroupAdapter(groupsList);
                            mRecyclerView.setAdapter(mAdapter);
                            mDialog.dismiss();

                            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                    // скрытие клавиатуру, если в фокусе
                                    if (getCurrentFocus() != null) {
                                        InputMethodManager inputMethodManager =
                                                (InputMethodManager) getSystemService(
                                                        Activity.INPUT_METHOD_SERVICE);
                                        inputMethodManager.hideSoftInputFromWindow(
                                                getCurrentFocus().getWindowToken(), 0);
                                    }

                                    // передача параметров расписания активи и ее вызов
                                    Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                                    inst = groupsList.get(position).getInst();
                                    group = groupsList.get(position).getGroup();
                                    intent.putExtra("inst", inst);
                                    intent.putExtra("group", group);
                                    intent.putExtra("week", week);
                                    startActivity(intent);
                                }
                            });*/

                            /*mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                }
                            });*/
                        }
                    });
                }
            }).start();
            mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                @Override
                public void onSearchTextChanged(String oldQuery, final String newQuery) {
                    adapter.getFilter().filter(newQuery);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}