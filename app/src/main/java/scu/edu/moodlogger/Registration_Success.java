package scu.edu.moodlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by vijay on 5/30/2015.
 */
public class Registration_Success extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_success);
    }

    public void redirect_to_login(View view) {
        Intent intent = new Intent(this, Login_user.class);
        startActivity(intent);
    }
}
