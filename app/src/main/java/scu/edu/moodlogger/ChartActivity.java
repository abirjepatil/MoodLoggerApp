package scu.edu.moodlogger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import scu.edu.moodlogger.PieChart;

public class ChartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //getting the user id that has been temporarily stored
        SharedPreferences sp = getSharedPreferences("user_pref", Activity.MODE_PRIVATE);
        String userid = sp.getString("user_key","");
        DataTypeChart data= new DataTypeChart();
        /**
         * Connection with the database
         *
         */
        try {
            DBUserAdapter dbUser = new DBUserAdapter(ChartActivity.this);
            dbUser.open();
           // String data_String;
            data=dbUser.getData(userid);

            Context context = getApplicationContext();
            CharSequence text = userid+data.numberOfData;
            int duration = Toast.LENGTH_SHORT;


            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        catch(Exception e)
        {
            Context context = getApplicationContext();
            CharSequence text = "Failed";
            int duration = Toast.LENGTH_SHORT;


            Toast toast = Toast.makeText(context, text, duration);
            toast.show();


        }






        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);
        float[] datas = new float[12];
        String[] labels = new String[12];
        int count=0;
        for(int i=0;i<12;i++)
        {

            if(data.datas[i]!=0)
            {
                datas[count]=data.datas[i];
                labels[count++]=data.labels[i];

            }


        }


        pieChart.setData(datas);
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
