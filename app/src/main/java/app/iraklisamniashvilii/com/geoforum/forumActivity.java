package app.iraklisamniashvilii.com.geoforum;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Debug;
        import android.support.annotation.NonNull;
        import android.support.design.widget.CollapsingToolbarLayout;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityOptionsCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ServerValue;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;

        import org.w3c.dom.Text;

        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Locale;
        import java.util.Map;

        import app.iraklisamniashvilii.com.geoforum.models.postReplyModel;
        import de.hdodenhof.circleimageview.CircleImageView;

public class forumActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.myr);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        final ProgressBar progressBar;
        progressBar = findViewById(R.id.pprgrs);
        progressBar.animate();


        final CircleImageView mUserPhoto = findViewById(R.id.profile_image);

        TextView post_author = findViewById(R.id.post_author);
        TextView jurika = findViewById(R.id.user_author_content);

        post_author.setText(getIntent().getExtras().getString("postUsername"));
        jurika.setText(getIntent().getExtras().getString("postContent"));


        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getExtras().getString("postUser")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("thumb_image").equals("default")) {

                    progressBar.setVisibility(View.GONE);
                    Picasso.with(forumActivity.this).load(dataSnapshot.child("thumb_image").getValue().toString()).placeholder(R.drawable.white).into(mUserPhoto, new Callback() {


                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {

                        }
                    });


                } else {

                    Picasso.with(forumActivity.this).load(R.drawable.user).into(mUserPhoto);
                    findViewById(R.id.aviLoader).setVisibility(View.GONE);


                }
            }

            @Override

            public void onCancelled(DatabaseError databaseError) {
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("comments").child(getIntent().getExtras().getString("category")).child(getIntent().getExtras().getString("postTitle"));

        recyclerView.setHasFixedSize(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(getIntent().getExtras().getString("postUsername") + "_ს პოსტი");
        FloatingActionButton fabCom = findViewById(R.id.commentBTN);
        fabCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(forumActivity.this);
                View view2 = getLayoutInflater().inflate(R.layout.reply_dialogbox, null);
                final EditText comment = view2.findViewById(R.id.commentTxt);
                Button publish = view2.findViewById(R.id.publish);
                Button close = view2.findViewById(R.id.close);
                TextView name = view2.findViewById(R.id.username);
                name.setText(getIntent().getExtras().getString("postUsername") + "_ის პოსტზე პასუხი");
                builder.setView(view2);
                final AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                publish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String come = comment.getText().toString();
                        final HashMap<String, String> mMap = new HashMap<>();
                        final String uid = FirebaseAuth.getInstance().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mMap.put("username", dataSnapshot.child("name").getValue().toString());
                                mMap.put("content", come);
                                mMap.put("uid", uid);




                               String ke = mDatabase.push().getKey();


                                        mDatabase.child(ke).setValue(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });

                                        mDatabase.child(ke).child("date").setValue(ServerValue.TIMESTAMP);

                                dialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        FirebaseRecyclerAdapter<postReplyModel, replyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<postReplyModel, replyViewHolder>(
                postReplyModel.class,
                R.layout.single_post_replies,
                replyViewHolder.class,
                FirebaseDatabase.getInstance().getReference().child("comments").child(getIntent().getExtras().getString("category")).child(getIntent().getExtras().getString("postTitle"))

        ) {
            @Override
            protected void populateViewHolder(final replyViewHolder viewHolder, final postReplyModel model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setContent(model.getContent());
                viewHolder.setDate(model.getDate());
                viewHolder.setPhoto(model.getUid());

                viewHolder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileInfo = new Intent(forumActivity.this, ProfileInfoActivity.class);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(forumActivity.this, viewHolder.photo, "ProfilePhoto");

                        profileInfo.putExtra("name", model.getUsername());
                        profileInfo.putExtra("uid", model.getUid());

                        startActivity(profileInfo, optionsCompat.toBundle());
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();


        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "ახლახანს";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "რამდენიმე წუთის წინ";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " წუთის წინ";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " საათის წინ";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "გუშინ";
        } else {
            return diff / DAY_MILLIS + " დღის წინ";
        }
    }
    public static class replyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView photo;
        public TextView username;
        public ProgressBar progressBar;
        public TextView date;
        public TextView content;
        public View mView;

        public replyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            photo = mView.findViewById(R.id.profile_image);
            username = mView.findViewById(R.id.post_reply_name);
            progressBar = mView.findViewById(R.id.progressBar3);
            date = mView.findViewById(R.id.post_reply_time);
            content = mView.findViewById(R.id.post_reply_content);


        }

        public void setDate(Long dat) {



            date.setText(getTimeAgo(dat,itemView.getContext()));



        }

        public void setContent(String cont) {
            content.setText(cont);
        }


        public void setUsername(String name) {
            username.setText(name);
        }

        public void setPhoto(String id) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                           Picasso.with(mView.getContext()).load(dataSnapshot.child("thumb_image").getValue().toString()).placeholder(R.drawable.white).into(photo);
                           progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });


        }
    }
}
