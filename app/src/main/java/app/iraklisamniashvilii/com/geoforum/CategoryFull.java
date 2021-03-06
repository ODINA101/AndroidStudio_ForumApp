package app.iraklisamniashvilii.com.geoforum;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import app.iraklisamniashvilii.com.geoforum.models.NotiModel;
import app.iraklisamniashvilii.com.geoforum.models.postModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryFull extends AppCompatActivity {
    public ImageButton backBtn;
    public TextView title;
    public DatabaseReference mDatabase;
    public RecyclerView recyclerView;
    public LinearLayoutManager mLayout;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_full);
        backBtn = findViewById(R.id.cat_back);
        recyclerView = findViewById(R.id.mrecycler);
        mLayout = new LinearLayoutManager(this);
        mLayout.setStackFromEnd(true);
        mLayout.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayout);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadmore();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Addintent = new Intent(CategoryFull.this, AddNewActivity.class);
                Addintent.putExtra("title", getIntent().getExtras().getString("title"));
                startActivity(Addintent);


            }
        });
        title = findViewById(R.id.cat_full_title);
        title.setText(getIntent().getExtras().getString("title"));


        /////////////////////////

    }

    /////////////////////////created//////////////////////
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
    public void loadmore() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(getIntent().getExtras().getString("title"));

        FirebaseRecyclerOptions<postModel> options =
                new FirebaseRecyclerOptions.Builder<postModel>()
                        .setQuery(mDatabase,postModel.class)
                        .build();

        FirebaseRecyclerAdapter<postModel, postviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<postModel, postviewholder>(
              options
        ) {


            @NonNull
            @Override
            public postviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                     View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_post_layout, parent, false);
                   return new postviewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final postviewholder viewHolder, final int position, @NonNull final postModel model) {
                viewHolder.settitle(model.getTitle());
                viewHolder.setdate(model.getDate());

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent postInfo = new Intent(getApplicationContext(), forumActivity.class);
                        postInfo.putExtra("postTitle", getRef(position).getKey());
                        postInfo.putExtra("postUser", model.getUid());
                        postInfo.putExtra("postContent", model.getDes());

                        postInfo.putExtra("postUsername", model.getName());
                        postInfo.putExtra("category", getIntent().getExtras().getString("title"));
                        startActivity(postInfo);


                    }
                });



                FirebaseDatabase.getInstance().getReference().child("likes").child(getIntent().getExtras().getString("title"))
                        .child(getRef(position).getKey())
                        .child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            viewHolder.postlikenm.setText(Long.toString(dataSnapshot.getChildrenCount()));
                        }else{
                            viewHolder.postlikenm.setText("");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.child("thumb_image").getValue().toString().equals("default")) {
                            viewHolder.setProfile_image(dataSnapshot.child("thumb_image").getValue().toString());
                            findViewById(R.id.aviLoader).setVisibility(View.GONE);


                        } else {

                            viewHolder.setProfile_image("R.drawable.user");
                            findViewById(R.id.aviLoader).setVisibility(View.GONE);


                        }
                    }

                    @Override

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }


        };


        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();





    }



    public static class postviewholder extends RecyclerView.ViewHolder {
        private CircleImageView profile_image;
        private TextView posttitle;
        private TextView date;
        private ProgressBar progressBar;
        private CardView cardView;
        private TextView postlikenm;

        View mView;

        public postviewholder(View itemView) {
            super( itemView );
            mView = itemView;
            postlikenm = mView.findViewById(R.id.postlikenm);
            profile_image = mView.findViewById( R.id.profile_image );
            posttitle = mView.findViewById( R.id.post_title_oncard );
            date = mView.findViewById( R.id.post_time );
            progressBar = mView.findViewById( R.id.progressBar2 );
            cardView = mView.findViewById( R.id.post_card );

        }


        void setlikesnum(String nm) {
            postlikenm.setText(nm);

        }

        void settitle(String title) {
            posttitle.setText( title );

        }

        void setdate(Long data) {
            date.setText( new timeago().gettimeago(data,itemView.getContext()) );
        }

        void setProfile_image(String profile) {

            if (!profile.equals( "R.drawable.user" )) {

                Picasso.with( mView.getContext() ).load( profile ).placeholder( R.drawable.white ).into( profile_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility( View.GONE );
                    }

                    @Override
                    public void onError() {

                    }
                } );


            }else{

                Picasso.with( mView.getContext() ).load( R.drawable.user ).placeholder( R.drawable.white ).into( profile_image, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility( View.GONE );

                    }

                    @Override
                    public void onError() {

                    }
                } );
            }


        }

    }
}
