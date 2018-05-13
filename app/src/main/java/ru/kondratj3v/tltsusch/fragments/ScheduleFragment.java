package ru.kondratj3v.tltsusch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.kondratj3v.tltsusch.R;
import ru.kondratj3v.tltsusch.adapters.ScheduleAdapter;
import ru.kondratj3v.tltsusch.models.Day;

public class ScheduleFragment extends Fragment {
    private static final String ARG_DAY = "section_number";

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(Day day) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Day day = getArguments().getParcelable(ARG_DAY);
        if (day != null && !day.getLessons().isEmpty()) {
            ScheduleAdapter adapter = new ScheduleAdapter(getContext(), day.getLessons());
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }
}