package scu.edu.moodlogger;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MoodLoggingActivity extends Activity {

    GridView gridView;
    DBUserAdapter dbUser = new DBUserAdapter(MoodLoggingActivity.this);

    static final String[] images = new String[]{"happy", "confused", "naughty", "angry",
            "excited", "cool", "bored", "sleepy", "neutral", "crying", "romantic", "sad"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_logging);

        gridView = (GridView) findViewById(R.id.gridView_emoticons);
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);

        gridView.setAdapter(new MyAdapter(this, images));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v,
                                    int position, long id) {

                try {
                    dbUser.open();
                    //getting the user id that has been temporarily stored
                    SharedPreferences sp = getSharedPreferences("user_pref", Activity.MODE_PRIVATE);
                    String userid = sp.getString("user_key", "");
                    String date_current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String mood = ((TextView) v.findViewById(R.id.label_emoticon)).getText().toString();
                    //insertRow(String userId, int moodId, String date, String picture, String notes, String location) {

                    dbUser.insertMood(userid, position + 1, mood, date_current, "", "", "");

                    Toast.makeText(
                            getApplicationContext(),
                            "added to database", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    Log.i("database", "Error in Adding to database");

                }
                Toast.makeText(
                        getApplicationContext(),
                        ((TextView) v.findViewById(R.id.label_emoticon)).getText(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mood_logging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}