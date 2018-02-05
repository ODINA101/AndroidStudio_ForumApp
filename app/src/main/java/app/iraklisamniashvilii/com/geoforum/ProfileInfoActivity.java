package app.iraklisamniashvilii.com.geoforum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileInfoActivity extends AppCompatActivity {
  public TextView name;
  public CircleImageView circleImageView;
  public Button follow;
  public FirebaseDatabase firebaseDatabase;
  public DatabaseReference db;
public String uid;
public ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile_info );
       circleImageView = findViewById( R.id.circleImageView );
         name = findViewById( R.id.name );
         uid = getIntent().getExtras().getString( "uid" );
         name.setText( getIntent().getExtras().getString( "name" ) );
        follow = findViewById( R.id.follow );
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        final DatabaseReference me = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());

        if(uid.equals(FirebaseAuth.getInstance().getUid())) {
    follow.setVisibility( View.GONE );
}
        follow.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 HashMap<String,String> uid2 = new HashMap<>();
                 uid2.put("uid",FirebaseAuth.getInstance().getUid());
                 db.child("followers").push().setValue( uid2 );

                 HashMap<String,String> uid3 = new HashMap<>();
                 uid2.put("uid",uid);
                 me.child("following").push().setValue( uid3 );

             }
         } );
    mProgress = findViewById( R.id.mPRO );
    mProgress.setVisibility( View.VISIBLE );
        db.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("image").getValue().toString() != "default") {
                    Picasso.with( ProfileInfoActivity.this ).load( dataSnapshot.child("image").getValue().toString() ).placeholder( R.drawable.white ).into( circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgress.setVisibility( View.GONE );


                        }

                        @Override
                        public void onError() {

                        }
                    } );
                }else{
                    Picasso.with( ProfileInfoActivity.this ).load( R.drawable.user ).into( circleImageView );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }
}
