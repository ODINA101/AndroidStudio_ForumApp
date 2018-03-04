package app.iraklisamniashvilii.com.geoforum;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddNewActivity extends AppCompatActivity {
public ImageButton backbtn;
public Button addbtn;
public EditText postTitle;
public DatabaseReference mDatabase;
public FirebaseUser user;
public EditText shortDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new );
            user = FirebaseAuth.getInstance().getCurrentUser();
           final String uid = user.getUid();
        backbtn = findViewById( R.id.cat_back2 );
        backbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        } );
    mDatabase = FirebaseDatabase.getInstance().getReference("Posts").child(getIntent().getExtras().getString( "title" ) );
     addbtn = findViewById( R.id.add_new_postbtn );
        postTitle = findViewById( R.id.new_post_title2 );
        shortDes = findViewById( R.id.new_post_shortDes );
        final AVLoadingIndicatorView avLoadingIndicatorView;
        avLoadingIndicatorView = findViewById( R.id.adnewload );
        avLoadingIndicatorView.setVisibility( View.GONE );
        addbtn.setEnabled( false );

     addbtn.setOnClickListener( new View.OnClickListener() {
         @RequiresApi(api = Build.VERSION_CODES.O)
         @Override
         public void onClick(View view) {
             avLoadingIndicatorView.animate();
             avLoadingIndicatorView.setVisibility( View.VISIBLE);


             final HashMap<String,String> map = new HashMap<>();
             map.put( "title",postTitle.getText().toString());
             map.put("uid", uid );

             FirebaseDatabase.getInstance().getReference().child( "Users" ).child( uid ).addListenerForSingleValueEvent( new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
String ke = mDatabase.push().getKey();


                     map.put("name",dataSnapshot.child("name").getValue().toString());
                     avLoadingIndicatorView.setVisibility( View.GONE );
                     map.put("des",shortDes.getText().toString());
                     mDatabase.child(ke).setValue( map );
                     mDatabase.child(ke).child("date").setValue(ServerValue.TIMESTAMP);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {
                     avLoadingIndicatorView.setVisibility( View.GONE );

                 }
             } );


               onBackPressed();

         }
     } );

     postTitle.addTextChangedListener( new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             if(postTitle.getText().toString().isEmpty()) {
                 addbtn.setEnabled( false );
             }else{
                 addbtn.setEnabled( true );

             }
         }

         @Override
         public void afterTextChanged(Editable editable) {

         }
     } );




}
    @Override
    public void onResume() {

        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("isOnline").setValue(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(FirebaseAuth.getInstance().getUid() != null) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("isOnline").setValue("false");
        }
    }

}
