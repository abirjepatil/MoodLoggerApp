package scu.edu.moodlogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import scu.edu.moodlogger.PieChart;

public class ChartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        float[] datas = new float[6];
        datas[0] = 34;
        datas[1] = 24;
        datas[2] = 32;
        datas[3] = 24;
        datas[4] = 53;
        datas[5] = 23;
        pieChart.setData(datas);

        String[] labels = new String[6];
        labels[0] = "JOHN";
        labels[1] = "GEORGE";
        labels[2] = "RAYMOND";
        labels[3] = "STEPHEN";
        labels[4] = "JACK";
        labels[5] = "BOBBY";
        pieChart.setLabels(labels);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
