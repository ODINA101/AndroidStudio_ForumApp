package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegActivity extends AppCompatActivity {
 public Button back_btn_reg;
 public EditText name;
 public EditText email;
 public EditText password;
 public Button regBtn;
 public FirebaseAuth mAuth;
 public DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reg );
        mAuth = FirebaseAuth.getInstance();
        back_btn_reg = findViewById( R.id.back_btn_reg );
        back_btn_reg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        regBtn = findViewById( R.id.reg_btn );
        name = findViewById( R.id.reg_nick );
        email = findViewById( R.id.reg_email );
        password = findViewById( R.id.reg_password );


       regBtn.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String nm = name.getText().toString();
               String ps = password.getText().toString();
               String em = email.getText().toString();
                Log.d("dundulebi",nm);
                signup(em,ps,nm);
           }
       } );





    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    public void signup(String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser current_user = mAuth.getCurrentUser();
                            String uid = current_user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> userMap = new HashMap<>();
                           userMap.put("name",name);
                           userMap.put("status","");
                           userMap.put("image","default");
                            userMap.put("cover","default");
                            userMap.put("thumb_image","default");
                           mDatabase.setValue(userMap);
                            Intent home = new Intent(RegActivity.this,HomeActivity.class);
                            home.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity( home );
                           finish();

                        } else {
                            Toast.makeText(RegActivity.this,"Cannot Sign In. Please check form and try again",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }


}
