package com.user.studentapi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {
    EditText id;
    Button resetpassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password);
        firebaseAuth=FirebaseAuth.getInstance();
        id=(EditText)findViewById(R.id.email);
        id.addTextChangedListener(new MaskWatcher("#######@#########"));
        resetpassword=(Button)findViewById(R.id.reset);
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail=id.getText().toString().trim();
                if (useremail.isEmpty())
                {
                    id.setError("Email required");
                    id.requestFocus();
                    return;
                }
                char[] chars = useremail.toCharArray();
                System.out.println(chars.length);

                /*for (int i = 0; i < chars.length; i++) {
                    if (chars[i] == '@') {
                        if (chars[i + 1] == 'n' && chars[i + 2] == 'u' && chars[i + 3] == '.' && chars[i + 4] == 'e' && chars[i + 5] == 'd' && chars[i + 6] == 'u' && chars[i + 7] == '.' && chars[i + 8] == 'p' && chars[i + 9] == 'k') {
                        } else {
                            id.setError("Invalid Domain");
                            id.requestFocus();
                            return;
                        }
                    }
                }*/
                firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(ResetPassword.this,"Password reset email sent",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ResetPassword.this,StudentsLogin.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ResetPassword.this,"Error in sending password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=firebaseAuth.getCurrentUser();
        //String id=user.getUid();
        //final String driveremail=user.getEmail();
    }
}
