package ru.kondratj3v.tltsusch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.kondratj3v.tltsusch.R;
import ru.kondratj3v.tltsusch.models.Group;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<Group> listGroup;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView groupTextView;
        private TextView instTextView;

        ViewHolder(View itemView) {
            super(itemView);
            groupTextView = (TextView) itemView.findViewById(R.id.group_text_view);
            instTextView = (TextView) itemView.findViewById(R.id.inst_text_view);
        }
    }

    public GroupAdapter(List<Group> listGroup) {
        this.listGroup = listGroup;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        return new GroupAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = listGroup.get(position);
        holder.instTextView.setText(group.getInst());
        holder.groupTextView.setText(group.getGroup());
    }

    @Override
    public int getItemCount() {
        return listGroup.size();
    }
}