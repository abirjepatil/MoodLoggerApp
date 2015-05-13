package scu.edu.moodlogger;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button moodBtn = (Button) findViewById(R.id.imageButton_logmood);
        Button calendarBtn = (Button) findViewById(R.id.imageButton_calendar);
        Button chartBtn = (Button) findViewById(R.id.imageButton_chart);
        Button reminderBtn = (Button) findViewById(R.id.imageButton_reminder);

        moodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent moodIntent = new Intent(getApplicationContext(), MoodLoggingActivity.class);
                startActivity(moodIntent);

            }
        });


        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent calIntent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(calIntent);

            }
        });

        chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chartIntent = new Intent(getApplicationContext(), ChartActivity.class);
                startActivity(chartIntent);

            }
        });

        reminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent reminderIntent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(reminderIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
