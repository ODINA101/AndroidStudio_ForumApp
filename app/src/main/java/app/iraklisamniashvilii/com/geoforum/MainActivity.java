package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
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


public class MainActivity extends AppCompatActivity {


public Button reg;
public Button login;
public EditText email;
public EditText password;
public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        mAuth = FirebaseAuth.getInstance();
           if(mAuth.getCurrentUser() != null) {
               sendToStart();
           }
        setContentView( R.layout.activity_main);

        reg = findViewById( R.id.login_reg);
        reg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this,RegActivity.class );
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation( MainActivity.this,findViewById( R.id.Login_email ),"nick" );

                 startActivity( reg,optionsCompat.toBundle());

            }
        } );

       login = findViewById( R.id.login_btn);
       email = findViewById( R.id.Login_email);
       password = findViewById( R.id.login_password);
             login.setOnClickListener( new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     SignIn(email.getText().toString(),password.getText().toString());
                 }


             } );

    }

    public  void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(startIntent);
        finish();
    }

    public void SignIn(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent home = new Intent( MainActivity.this,HomeActivity.class );
                           home.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity( home );
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "მოხდა შეცდომა, ელფოსტა ან პაროლი არასწორია",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }

}
