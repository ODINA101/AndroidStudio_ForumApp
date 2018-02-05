package app.iraklisamniashvilii.com.geoforum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
  public Button disfollow;
public ProgressBar progressBar2;
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


      progressBar2 = findViewById(R.id.progressBar4);


 progressBar2.animate();

        progressBar2.setVisibility(View.VISIBLE);

         name.setText( getIntent().getExtras().getString( "name" ) );
        follow = findViewById( R.id.follow );
        disfollow = findViewById( R.id.follow2 );

        follow.setVisibility( View.GONE );
        disfollow.setVisibility( View.GONE );








        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        final DatabaseReference me = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
 disfollow.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         progressBar2.setVisibility(View.VISIBLE);
         follow.setVisibility(View.GONE);
         disfollow.setVisibility(View.GONE);
//         me.child("following").child(uid).getRef().removeValue();
//         db.child("followers").child(FirebaseAuth.getInstance().getUid()).getRef().removeValue();
         follow.setVisibility(View.VISIBLE);



     }
 });


//        db.child("followers").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(FirebaseAuth.getInstance().getUid()).exists()) {
//                                       disfollow.setVisibility(View.VISIBLE);
//                    progressBar2.setVisibility(View.GONE);
//                    follow.setVisibility(View.GONE);
//
//
//                    Log.d("kvanwebii", String.valueOf(dataSnapshot.child(FirebaseAuth.getInstance().getUid())));
//                }else{
//                                    follow.setVisibility(View.VISIBLE);
//                                    disfollow.setVisibility(View.GONE);
//                    progressBar2.setVisibility(View.GONE);
//
//                }
//
//
//                }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        me.child("following").child(uid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                Log.d("aleehop", String.valueOf(dataSnapshot));
//                if (dataSnapshot.child("ktx") != null) {
//                    if (dataSnapshot.child("ktx").getValue().equals("Youfollowing")) {
//
//                    } else {
//
//                    }
//                }
//            }
//
//
//
//
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });




        if(uid.equals(FirebaseAuth.getInstance().getUid())) {
    follow.setVisibility( View.GONE );
}

        follow.setOnClickListener( new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 HashMap<String,String> mMap1 = new HashMap<>();
                 mMap1.put("ktx","isFollowing");
                 db.child("followers").child(FirebaseAuth.getInstance().getUid()).setValue(mMap1);
             HashMap<String,String> mMap = new HashMap<>();
                 mMap.put("ktx","Youfollowing");
                 me.child("following").child(uid).setValue(mMap);

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
