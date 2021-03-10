package de.hauke_stieler.geonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hauke_stieler.geonotes.settings.SettingsActivity;
// CODE iS INFULUNCED BY A YOUTUBE SOURCE : https://www.youtube.com/watch?v=TwHmrZxiPA8&ab_channel=SmallAcademy

/**
 * Author: Mustafa Al-Obaidi
 */

public class Login extends AppCompatActivity {

    EditText Email1, Password1;
    Button LoginBtn1;
    TextView CreateBtn1;
    FirebaseAuth Auth1;
    TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email1 = findViewById(R.id.Email);
        Password1 = findViewById(R.id.password);
        Auth1 = FirebaseAuth.getInstance();
        FirebaseUser user1 = Auth1.getCurrentUser();
        LoginBtn1 = findViewById(R.id.LoginBtn);
        CreateBtn1 = findViewById(R.id.textView3);
        forgotPassword = findViewById(R.id.forgotPassword);



        LoginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTest = Email1.getText().toString().trim();
                String passwordTest = Password1.getText().toString().trim();
                if (TextUtils.isEmpty(emailTest)) {
                    Email1.setError("Email is Required");
                    return;
                } else if (TextUtils.isEmpty(passwordTest)) {
                    Password1.setError("Password is Required");
                    return;
                } else if (passwordTest.length() < 6) {
                    Password1.setError("Password must greater or equal to 6 charchters");
                    return;
                }
                // authenticattion
                Auth1.signInWithEmailAndPassword(emailTest, passwordTest).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Succefully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        CreateBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText text = new EditText(v.getContext());
                AlertDialog.Builder resetPass= new AlertDialog.Builder(v.getContext()); // construct an aler dialog to ask for the user's new password.
                resetPass.setTitle("Reset Password");
                resetPass.setMessage("Please Enter Your Email To Receive The Reset Link.");
                resetPass.setView(text);
                resetPass.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = text.getText().toString(); // get the email
                       Auth1.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {  // use the updatepaswword method from firebase
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {  //if the task is succefull
                                    Toast.makeText(Login.this, "A password reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                                } else if (!task.isSuccessful()) {  // if the task fails
                                    Toast.makeText(Login.this, "Something went wrong :(, failed to send the link", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                resetPass.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                resetPass.show();
            }
        });


    }
}