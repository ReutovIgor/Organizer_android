package com.example.ruireutov.organiser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 26-Oct-17.
 */

public class ExpListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;

    public ExpListAdapter(Context context, List<String> groupHeaders, HashMap<String, List<String>> groupItems) {
        this.context = context;
        this.groupHeaders = groupHeaders;
        this.groupItems = groupItems;
    }

    public void setData(List<String> groupHeaders, HashMap<String, List<String>> groupItems) {
        for (String header: groupHeaders) {
            this.groupHeaders.remove(header);
            this.groupHeaders.add(header);
            this.groupItems.remove(header);
            this.groupItems.put(header, groupItems.get(header));
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return this.groupHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.groupItems.get(this.groupHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.groupItems.get(this.groupHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup((groupPosition));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_group_view, null);
        }

//        if (isExpanded){
//            //Изменяем что-нибудь, если текущая Group раскрыта
//        }
//        else{
//            //Изменяем что-нибудь, если текущая Group скрыта
//        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.mainListGroupName);
        textGroup.setText(title);

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) this.getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_child_view, null);
        }

//        if (isExpanded){
//            //Изменяем что-нибудь, если текущая Group раскрыта
//        }
//        else{
//            //Изменяем что-нибудь, если текущая Group скрыта
//        }

        TextView textChild = (TextView) convertView.findViewById(R.id.mainListGroupChild);
        textChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
