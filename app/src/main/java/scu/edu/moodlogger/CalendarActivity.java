package scu.edu.moodlogger;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.MONTH;


public class CalendarActivity extends Activity implements View.OnClickListener, GestureOverlayView.OnGesturePerformedListener {
    private static final String tag = "Main";
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year, date;
    private String gridCellMonth, gridCellYear, gridCellDate;
    private int spaces;
    private String gridDate;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    DBUserAdapter myDb;
    private String photoPath = "";
    private GestureLibrary mLibrary;
    DBUserAdapter dbUser = new DBUserAdapter(CalendarActivity.this);

    private void getRequestParameters() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras != null) {
                    Log.d(tag, "+++++----------------->" + extras.getString("params"));
                }
            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveImage();

        GestureOverlayView gestureOverlayView = new GestureOverlayView(this);
        View inflate = getLayoutInflater().inflate(
                R.layout.activity_calendar, null);
        gestureOverlayView.addView(inflate);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureOverlayView.setGestureColor(Color.TRANSPARENT);
        gestureOverlayView.setUncertainGestureColor(Color.TRANSPARENT);
        // speeds up the reaction time a little
        gestureOverlayView.setFadeOffset(0);
        gestureOverlayView.setHapticFeedbackEnabled(true);
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            finish();
        }

        setContentView(gestureOverlayView);
        // setContentView(R.layout.activity_gallery);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(MONTH);
        year = _calendar.get(Calendar.YEAR);
        date = _calendar.get(Calendar.DAY_OF_MONTH);

        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (Button) this.findViewById(R.id.currentMonth);
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);


        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
        for (Prediction prediction : predictions) {
            if (prediction.name.equals("Previous")) {

                if (month <= 1) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
                setGridCellAdapterToDate(month, year);
                return;


            } else {
                if (prediction.name.equals("Next")) {

                    if (month >= 11) {
                        month = 0;
                        year++;
                    } else {
                        month++;
                    }
                    setGridCellAdapterToDate(month, year);
                    return;

                }
            }

        }

    }


    @Override
    public void onClick(View v) {
        if (v == prevMonth) {

            if (month <= 1) {
                month = 11;
                year--;
            } else {
                month--;
            }
            setGridCellAdapterToDate(month, year);

        }
        if (v == nextMonth) {

            if (month >= 11) {
                month = 0;
                year++;
            } else {
                month++;
            }
            setGridCellAdapterToDate(month, year);

        }
    }

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.day_gridcell, month, year);
        _calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;
        private final List<String> list;
        private final String[] weekdays = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final int month, year;
        int daysInMonth, prevMonthDays;
        private final int currentDayOfMonth;
        private Button gridcell;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            this.month = month;
            this.year = year;

            Log.d(tag, "Month: " + month + " " + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            printMonth(month, year);
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        private void printMonth(int mm, int yy) {
            // The number of days to leave blank at
            // the start of this month.
            int trailingSpaces = 0;
            int leadSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            //GregorianCalendar cal = new GregorianCalendar(yy, mm, currentDayOfMonth);
            GregorianCalendar cal = new GregorianCalendar(yy, mm, 1);


            // Days in Current Month
            daysInMonth = daysOfMonth[mm];
            int currentMonth = mm;
            if (currentMonth == 11) {
                prevMonth = 10;
                daysInPrevMonth = daysOfMonth[prevMonth];
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = daysOfMonth[prevMonth];
                nextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = daysOfMonth[prevMonth];
            }

            // Compute how much to leave before the first day of the
            // month.
            // getDay() returns 0 for Sunday.
            trailingSpaces = cal.get(Calendar.DAY_OF_WEEK) - 2;
            if (trailingSpaces < 0) {
                trailingSpaces = 6;
            }
            spaces = trailingSpaces;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + 1) + i) + "-GREY" + "-" + months[prevMonth] + "-" + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                list.add(String.valueOf(i) + "-BLACK" + "-" + months[mm] + "-" + yy);
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + months[nextMonth]);
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + months[nextMonth] + "-" + nextYear);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.d(tag, "getView ...");
            View row = convertView;
            if (row == null) {
                // ROW INFLATION
                Log.d(tag, "Starting XML Row Inflation ... ");
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.day_gridcell, parent, false);

                Log.d(tag, "Successfully completed XML Row Inflation!");
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING
            Log.d(tag, "Current Day: " + currentDayOfMonth);
            String[] day_color = list.get(position).split("-");
            gridcell.setText(day_color[0]);


            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(Color.LTGRAY);
            }
            if (day_color[1].equals("BLACK")) {
                gridcell.setTextColor(Color.BLACK);

                int gridCellMon = month + 1;

                gridCellMonth = String.valueOf(gridCellMon);
                gridCellYear = String.valueOf(year);
                gridCellDate = gridcell.getText().toString();

                if (Integer.valueOf(gridCellMonth) < 10) {
                    gridCellMonth = "0" + gridCellMonth;

                }
                if (Integer.valueOf(gridCellDate) < 10) {
                    gridCellDate = "0" + gridCellDate;
                }


                gridDate = gridCellYear +
                        "-" + gridCellMonth + "-" + gridCellDate;
                gridcell.setTag(gridDate);

                Log.i("gridDate", gridDate);

                try {
                    openDB();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Bitmap b = null;
                try {
                    try {
                        b = getThumbnail(gridDate);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//
//
                if (b != null) {
                    BitmapDrawable ob = new BitmapDrawable(getResources(), b);
                    gridcell.setBackground(ob);
                }

            }
            if (position - spaces + 1 == currentDayOfMonth) {
                gridcell.setTextColor(Color.BLUE);
            }

            return row;
        }

        @Override
        public void onClick(View view) {

            try {
                dbUser.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //getting the user id that has been temporarily stored
            SharedPreferences sp = getSharedPreferences("user_pref", Activity.MODE_PRIVATE);
            String userid = sp.getString("user_key", "");

            String gridCellTag = (String) view.getTag();
            String s = myDb.getMood(gridCellTag, userid);
            if(s!=null){
                Toast.makeText(
                        getApplicationContext(),"You were "+s+" on "+ gridCellTag , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(
                        getApplicationContext(),"No mood recorded" , Toast.LENGTH_SHORT).show();
            }
//               Intent intent = new Intent(getApplicationContext(), ViewPhotoActivity.class);
//                intent.putExtra("gridcell", gridCellTag);
//                startActivity(intent);

        }
    }


    //Fetch thumbnail of the image for a date
    private Bitmap getThumbnail(String sDate) throws SQLException, URISyntaxException {
        openDB();
        Bitmap sourceBitmap = null;

        dbUser.open();

        //getting the user id that has been temporarily stored
        SharedPreferences sp = getSharedPreferences("user_pref", Activity.MODE_PRIVATE);
        String userid = sp.getString("user_key", "");

        String s = myDb.getMood(sDate, userid);

        if (s != null) {
            Log.i("mood", s);
            //  String path = getBitmapImagePath(s);
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory.getAbsolutePath(), s + ".png");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;  // Experiment with different sizes
            sourceBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
            Log.i("path", f.getAbsolutePath().toString() );
        }
        return sourceBitmap;

    }

    private void saveToInternalStorage(Bitmap bitmapImage, String mood) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, mood + ".png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return directory.getAbsolutePath();
    }


    private void saveImage() {
        Bitmap bitMapHappy = BitmapFactory.decodeResource(getResources(), R.drawable.happy);
        saveToInternalStorage(bitMapHappy, "happy");

        Bitmap bitMapConfused = BitmapFactory.decodeResource(getResources(), R.drawable.confused);
        saveToInternalStorage(bitMapConfused, "confused");

        Bitmap bitMapNaughty = BitmapFactory.decodeResource(getResources(), R.drawable.naughty);
        saveToInternalStorage(bitMapNaughty, "naughty");

        Bitmap bitMapAngry = BitmapFactory.decodeResource(getResources(), R.drawable.angry);
        saveToInternalStorage(bitMapAngry, "angry");

        Bitmap bitMapExcited = BitmapFactory.decodeResource(getResources(), R.drawable.excited);
        saveToInternalStorage(bitMapExcited, "excited");

        Bitmap bitMapCool = BitmapFactory.decodeResource(getResources(), R.drawable.cool);
        saveToInternalStorage(bitMapCool, "cool");

        Bitmap bitMapBored = BitmapFactory.decodeResource(getResources(), R.drawable.bored);
        saveToInternalStorage(bitMapBored, "bored");

        Bitmap bitMapSleepy = BitmapFactory.decodeResource(getResources(), R.drawable.sleepy);
        saveToInternalStorage(bitMapSleepy, "sleepy");

        Bitmap bitMapNeutral = BitmapFactory.decodeResource(getResources(), R.drawable.neutral);
        saveToInternalStorage(bitMapNeutral, "neutral");

        Bitmap bitMapCrying = BitmapFactory.decodeResource(getResources(), R.drawable.crying);
        saveToInternalStorage(bitMapCrying, "crying");

        Bitmap bitMapRomantic = BitmapFactory.decodeResource(getResources(), R.drawable.romantic);
        saveToInternalStorage(bitMapRomantic, "romantic");

        Bitmap bitMapHSad = BitmapFactory.decodeResource(getResources(), R.drawable.sad);
        saveToInternalStorage(bitMapHSad, "sad");
    }


    private void openDB() throws SQLException {
        myDb = new DBUserAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

}
