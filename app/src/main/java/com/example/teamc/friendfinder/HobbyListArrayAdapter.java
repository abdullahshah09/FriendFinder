package com.example.teamc.friendfinder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HobbyListArrayAdapter extends ArrayAdapter<HobbyCodeActivity.Hobbies>

{

    private final List<HobbyCodeActivity.Hobbies> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView icon;
    }

    public HobbyListArrayAdapter(Activity context, List<HobbyCodeActivity.Hobbies> list) {
        super(context, R.layout.activity_hobby_code_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.activity_hobby_code_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.icon.setImageDrawable(list.get(position).getIcon());
        return view;
    }
}
