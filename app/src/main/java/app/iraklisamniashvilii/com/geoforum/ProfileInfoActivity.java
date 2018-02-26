package app.iraklisamniashvilii.com.geoforum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
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
        disfollow = findViewById( R.id.follow2 );

        follow.setVisibility( View.GONE );
        disfollow.setVisibility( View.GONE );



        if(getIntent().getExtras().getString("uid").equals(FirebaseAuth.getInstance().getUid())) {
            follow.setVisibility( View.GONE );
        }





        db = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        final DatabaseReference me = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
 disfollow.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         follow.setVisibility(View.GONE);
         disfollow.setVisibility(View.GONE);
       me.child("following").child(uid).getRef().removeValue();
         db.child("followers").child(FirebaseAuth.getInstance().getUid()).getRef().removeValue();
         me.child("chats").child(uid).getRef().removeValue();
         db.child("chats").child(FirebaseAuth.getInstance().getUid()).getRef().removeValue();
         follow.setVisibility(View.VISIBLE);



     }
 });


       db.child("followers").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(FirebaseAuth.getInstance().getUid()).exists()) {
                                       disfollow.setVisibility(View.VISIBLE);
                    follow.setVisibility(View.GONE);


                   Log.d("kvanwebii", String.valueOf(dataSnapshot.child(FirebaseAuth.getInstance().getUid())));
               }else{
                    if(getIntent().getExtras().getString("uid").equals(FirebaseAuth.getInstance().getUid())) {
                        follow.setVisibility( View.GONE );
                    }else{
                       follow.setVisibility(View.VISIBLE);
                    }


                                   disfollow.setVisibility(View.GONE);
               }

                }

             @Override
           public void onCancelled(DatabaseError databaseError) {
             }
         });





        System.out.println("aeeeeeeeee" + getIntent().getExtras().getString("uid") + "sdaasdasdasd" + FirebaseAuth.getInstance().getUid());


        follow.setOnClickListener( new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 HashMap<String,String> mMap1 = new HashMap<>();
                 mMap1.put("uid",FirebaseAuth.getInstance().getUid());

                 db.child("followers").child(FirebaseAuth.getInstance().getUid()).setValue(mMap1);
                 db.child("chats").child(FirebaseAuth.getInstance().getUid()).setValue(mMap1);



             HashMap<String,String> mMap = new HashMap<>();
                 mMap.put("uid",uid);




                 me.child("following").child(uid).setValue(mMap);
                 me.child("chats").child(uid).setValue(mMap);

                 String ke =  FirebaseDatabase.getInstance().getReference().child("notifications").push().getKey();

                 HashMap<String,String> mymap = new HashMap<>();
                 mymap.put("content",getIntent().getExtras().getString( "name" ) + "_მ გამოიწერა თქვენი პროფილი");
                 mymap.put("seen","false");




                 FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).setValue(mymap);

                 FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).child("date").setValue(ServerValue.TIMESTAMP);








             }
         } );
    mProgress = findViewById( R.id.mPRO );
    mProgress.setVisibility( View.VISIBLE );
        db.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("thumb_image").getValue().toString().equals("default")) {
                    Picasso.with( ProfileInfoActivity.this ).load( dataSnapshot.child("thumb_image").getValue().toString() ).placeholder( R.drawable.white ).into( circleImageView, new Callback() {
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
