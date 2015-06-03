package scu.edu.moodlogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


/**
 * Created by vijay on 5/29/2015.
 */
public class Register_user extends ActionBarActivity {
    EditText fname;
    EditText lname;
    EditText uname;
    EditText pass;
    EditText rpass;

    String firstname, lastname, password, rpassword, username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        final Context context = getApplicationContext();

        final CharSequence no_first_name = "Please Enter First name!";
        CharSequence no_last_name = "Please Enter Last name!";
        CharSequence no_user_name = "Please Enter user name!";
        CharSequence no_password = "Please Enter Password!";
        CharSequence no_re_password = "Please Re-Enter Password!";
        final int duration = Toast.LENGTH_SHORT;

        Button register = (Button) (findViewById(R.id.reg_button));

        fname = (EditText) findViewById(R.id.first_name);
        lname = (EditText) findViewById(R.id.last_name);
        pass = (EditText) findViewById(R.id.password);
        rpass = (EditText) findViewById(R.id.repassword);
        uname = (EditText) findViewById(R.id.username);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fname.getText().length() == 0) {
                    fname.setError("First name Can not be Empty");
                }

                if (lname.getText().length() == 0) {
                    lname.setError("Last name Can not be Empty");
                    return;

                }

                if (uname.getText().length() == 0) {
                    uname.setError("User name Can not be Empty");
                    return;

                }

                if (pass.getText().length() == 0) {
                    pass.setError("Password Can not be Empty");
                    return;

                }

                if (rpass.getText().length() == 0) {
                    rpass.setError("Re enter password");
                    return;
                }

                if (!(pass.getText().toString().equals(rpass.getText().toString()))) {
                    rpass.setError("Password do not match!!");
                    return;

                }
                try {
                    connect_to_db();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(context, Registration_Success.class);
                startActivity(intent);

            }


        });

    }

    void connect_to_db() throws SQLException {

        firstname = fname.getText().toString();
        lastname = lname.getText().toString();
        password = pass.getText().toString();
        username = uname.getText().toString();
        rpassword = rpass.getText().toString();

        DBUserAdapter dbUser = new DBUserAdapter(Register_user.this);
        dbUser.open();

        dbUser.AddUser(firstname, lastname, username, password);



    }


}



