package scu.edu.moodlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private final String[] emoticons;

    public MyAdapter(Context context, String[] emoticons) {
        this.context = context;
        this.emoticons = emoticons;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.emoticons, null);

            TextView textView = (TextView) gridView.findViewById(R.id.label_emoticon);

            textView.setText(emoticons[position]);

            ImageView imageEmoticons = (ImageView) gridView.findViewById(R.id.image_emoticon);

            String images = emoticons[position];

            if (images.equals("Angry")) {
                imageEmoticons.setImageResource(R.drawable.angry);
            } else if (images.equals("Happy")) {
                imageEmoticons.setImageResource(R.drawable.happy);
            } else if (images.equals("Confused")) {
                imageEmoticons.setImageResource(R.drawable.confused);
            } else {
                imageEmoticons.setImageResource(R.drawable.romantic);
            }

        } else {
            gridView =  convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return emoticons.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}