package com.example.ruireutov.organiser.task;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;

import java.util.HashMap;

public class SpinnerAdapter extends ResourceCursorAdapter {
        public static final String TYPE_CATEGORY = "Category";
        public static final String TYPE_PRIORITY = "Priority";
        private Context context;
        private LayoutInflater layoutInflater;
        private String type;
        private HashMap<String, Integer> mapping;

        public SpinnerAdapter(@NonNull Context context, Cursor cursor, int layout, int flags, String type) {
            super(context, layout, cursor, flags);

            this.context = context;
            this.type = type;
            this.mapping = new HashMap<>();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getItemPosition(String name) {
            return this.mapping.containsKey(name) ? this.mapping.get(name) : 0;
        }

        @Override
        public Object getItem(int position) {
            Cursor c = this.getCursor();
            c.moveToPosition(position);
            String nameKey = this.type == TYPE_CATEGORY ? DatabaseDefines.CATEGORIES_NAME : DatabaseDefines.PRIORITIES_NAME;
            return c.getString(c.getColumnIndex(nameKey));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = this.layoutInflater.inflate(R.layout.task_details_drop_down_item, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleView = view.findViewById(R.id.task_detail_name);
            ImageView imageView = view.findViewById(R.id.task_detail_icon);
            TextView textView = view.findViewById(R.id.task_detail_text);
            switch(this.type) {
                case TYPE_CATEGORY:
                    textView.setVisibility(View.GONE);
                    titleView.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.CATEGORIES_NAME) ));
                    Resources resources = this.context.getResources();
                    String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.CATEGORIES_ICON) );
                    int id = resources.getIdentifier(iconName, "drawable", context.getPackageName());
                    imageView.setImageDrawable(resources.getDrawable(id));
                    imageView.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.CATEGORIES_COLOR) )));
                    break;
                case TYPE_PRIORITY:
                    imageView.setVisibility(View.GONE);
                    titleView.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.PRIORITIES_NAME) ));
                    textView.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.PRIORITIES_MARK) ));
                    textView.setTextColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.PRIORITIES_COLOR) )));
                    break;
            }
        }

        @Override
        public Cursor swapCursor(Cursor c) {
            String key = "";
            this.mapping.clear();
            switch (this.type) {
                case TYPE_CATEGORY:
                    key = DatabaseDefines.CATEGORIES_NAME;
                    break;
                case TYPE_PRIORITY:
                    key = DatabaseDefines.PRIORITIES_NAME;
                    break;
            }
            while(c.moveToNext()) {
                this.mapping.put(c.getString( c.getColumnIndex(key) ), c.getPosition());
            }
            return super.swapCursor(c);
        }
}
