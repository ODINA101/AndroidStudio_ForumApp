package app.iraklisamniashvilii.com.geoforum;

        import android.annotation.SuppressLint;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Debug;
        import android.os.Parcelable;
        import android.support.annotation.NonNull;
        import android.support.design.widget.AppBarLayout;
        import android.support.design.widget.CollapsingToolbarLayout;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityOptionsCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.support.v7.widget.Toolbar;

        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
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
        import com.like.LikeButton;
        import com.like.OnLikeListener;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;

        import org.json.JSONException;
        import org.json.JSONObject;
        import org.w3c.dom.Text;

        import java.lang.reflect.Array;
        import java.net.URISyntaxException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
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
        setContentView(R.layout.forum_layout2);
     
        recyclerView = findViewById(R.id.myr);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        final ProgressBar progressBar;
        progressBar = findViewById(R.id.pprgrs);
        progressBar.animate();

       final LikeButton like_btn2 = findViewById(R.id.like_btn2);
        FirebaseDatabase.getInstance().getReference().child("likes").
                child(getIntent().getExtras().getString("category")).
                child(getIntent().getExtras().getString("postTitle"))
                .child("likes")
                .orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    like_btn2.setLiked(true);
                }else{
                    like_btn2.setLiked(false);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       final TextView postLikesNum = findViewById(R.id.postLikesNum);


        FirebaseDatabase.getInstance().getReference().child("likes").child(getIntent().getExtras().getString("category"))
                .child(getIntent().getExtras().getString("postTitle"))
                .child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    postLikesNum.setText(Long.toString(dataSnapshot.getChildrenCount()));
                }else{
                    postLikesNum.setText("");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        like_btn2.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                HashMap<String,String> mmmap2 = new HashMap<>();
                mmmap2.put("uid",FirebaseAuth.getInstance().getUid());
                FirebaseDatabase.getInstance().getReference().child("likes").
                        child(getIntent().getExtras().getString("category")).
                        child(getIntent().getExtras().getString("postTitle")).child("likes")
                        .child(FirebaseAuth.getInstance().getUid()).setValue(mmmap2);
                System.out.print("likedddddddxd");
                final String uid = FirebaseAuth.getInstance().getUid();

                if(!getIntent().getExtras().getString("postUser").equals(uid)) {


                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String ke = mDatabase.push().getKey();


                            HashMap<String, String> mymap = new HashMap<>();
                            mymap.put("content", dataSnapshot.child("name").getValue() + "_მ მოიწონა თქვენი პოსტი");
                            mymap.put("seen","false");
                            mymap.put("uid",FirebaseAuth.getInstance().getUid());


                            FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).setValue(mymap);

                            FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).child("date").setValue(ServerValue.TIMESTAMP);

                            try {
                                websockets socket = new websockets();

                                JSONObject mmm2 = new JSONObject();
                                mmm2.putOpt("content",dataSnapshot.child("name").getValue() + "_მ მოიწონა თქვენი პოსტი" );
                                mmm2.putOpt("usertoken", getIntent().getExtras().getString("postUser"));
                                socket.mSocket.emit("notification",mmm2);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                FirebaseDatabase.getInstance().getReference().child("likes").
                        child(getIntent().getExtras().getString("category")).
                        child(getIntent().getExtras().getString("postTitle")).child("likes").child(FirebaseAuth.getInstance().getUid()).removeValue();

            }
        });

        final CircleImageView mUserPhoto = findViewById(R.id.profile_image);

        final TextView post_author = findViewById(R.id.post_author);
        TextView jurika = findViewById(R.id.user_author_content);

        jurika.setText(getIntent().getExtras().getString("postContent"));


        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getExtras().getString("postUser")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                post_author.setText(dataSnapshot.child("name").getValue().toString());

                if (!dataSnapshot.child("thumb_image").getValue().toString().equals("default")) {

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

                    Picasso.with(forumActivity.this).load(R.drawable.user).into(mUserPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {

                        }
                    });



                }

                mUserPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileInfo = new Intent(forumActivity.this, ProfileInfoActivity.class);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(forumActivity.this, mUserPhoto, "ProfilePhoto");

                        profileInfo.putExtra("name", dataSnapshot.child("name").getValue().toString());
                        profileInfo.putExtra("uid", getIntent().getExtras().getString("postUser"));
                        profileInfo.putExtra("psUser", getIntent().getExtras().getString("postUser"));

                        startActivity(profileInfo, optionsCompat.toBundle());
                    }
                });

            }







            @Override

            public void onCancelled(DatabaseError databaseError) {
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("comments").child(getIntent().getExtras().getString("category")).child(getIntent().getExtras().getString("postTitle"));

        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabCom = findViewById(R.id.commentBTN);
        fabCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(forumActivity.this);
                View view2 = getLayoutInflater().inflate(R.layout.reply_dialogbox, null);
                final EditText comment = view2.findViewById(R.id.commentTxt);
                Button publish = view2.findViewById(R.id.publish);
                Button close = view2.findViewById(R.id.close);


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

                                if(!getIntent().getExtras().getString("postUser").equals(uid)) {


                                    HashMap<String, String> mymap = new HashMap<>();
                                    mymap.put("content", dataSnapshot.child("name").getValue().toString() + "_მ დააკომენტარა თქვენს პოსტზე");
                                    mymap.put("seen", "false");
                                    mymap.put("uid",FirebaseAuth.getInstance().getUid());
                                    try {
                                        websockets socket = new websockets();

        JSONObject mb = new JSONObject();

           mb.putOpt("content",dataSnapshot.child("name").getValue().toString() + "_მ დააკომენტარა თქვენს პოსტზე");
           mb.putOpt("usertoken",getIntent().getExtras().getString("postUser"));
                                        socket.mSocket.emit("notification",mb);
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).setValue(mymap);

                                    FirebaseDatabase.getInstance().getReference().child("notifications").child(getIntent().getExtras().getString("postUser")).child(ke).child("date").setValue(ServerValue.TIMESTAMP);



                                    mDatabase.child(ke).setValue(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });


                                    mDatabase.child(ke).child("date").setValue(ServerValue.TIMESTAMP);
                                    dialog.dismiss();
                                }else{
                                    mDatabase.child(ke).setValue(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });


                                    mDatabase.child(ke).child("date").setValue(ServerValue.TIMESTAMP);
                                }
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

        FirebaseRecyclerOptions<postReplyModel> options
                = new FirebaseRecyclerOptions.Builder<postReplyModel>()

                .setQuery(
                        FirebaseDatabase.getInstance().getReference().child("comments").child(getIntent().getExtras().getString("category")).child(getIntent().getExtras().getString("postTitle"))
, postReplyModel.class

                )
                .build();
        FirebaseRecyclerAdapter<postReplyModel, replyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<postReplyModel, replyViewHolder>(
               options
        ) {

            @NonNull
            @Override
            public replyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.single_post_replies,parent,false);

                return new replyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final replyViewHolder viewHolder, @SuppressLint("RecyclerView") final int position, @NonNull final postReplyModel model) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setContent(model.getContent());



                FirebaseDatabase.getInstance().getReference().child("likes").child(getIntent().getExtras().getString("category"))
                        .child(getIntent().getExtras().getString("postTitle"))
                        .child(getRef(position).getKey()).child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            viewHolder.LikeNum(Long.toString(dataSnapshot.getChildrenCount()));
                        }else{
                            viewHolder.LikeNum("");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                FirebaseDatabase.getInstance().getReference().child("replycomments").child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            viewHolder.SetcommentsNum(Long.toString(dataSnapshot.getChildrenCount()));
                        }else{
                            viewHolder.SetcommentsNum("");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                FirebaseDatabase.getInstance().getReference().child("likes").
                        child(getIntent().getExtras().getString("category")).
                        child(getIntent().getExtras().getString("postTitle"))
                        .child(getRef(position).getKey()).child("likes")
                        .orderByChild("uid").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() > 0) {
                            viewHolder.LikedOrNot(true);
                        }else{
                            viewHolder.LikedOrNot(false);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                final String rf = getRef(position).getKey();

                viewHolder.like_btn.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        HashMap<String,String> mmmap = new HashMap<>();
                        mmmap.put("uid",FirebaseAuth.getInstance().getUid());
                        FirebaseDatabase.getInstance().getReference().child("likes").
                                child(getIntent().getExtras().getString("category")).
                                child(getIntent().getExtras().getString("postTitle")).child(rf).child("likes").child(FirebaseAuth.getInstance().getUid()).setValue(mmmap);
                        System.out.print("likedddddddxd");
                        final String uid = FirebaseAuth.getInstance().getUid();

                        if(!model.getUid().equals(uid)) {


                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String ke = mDatabase.push().getKey();


                                    HashMap<String, String> mymap = new HashMap<>();
                                    mymap.put("content", dataSnapshot.child("name").getValue() + "_მ მოიწონა თქვენი კომენტარი");
                                    mymap.put("seen","false");
                                    mymap.put("uid",FirebaseAuth.getInstance().getUid());


                                    FirebaseDatabase.getInstance().getReference().child("notifications").child(model.getUid()).child(ke).setValue(mymap);

                                    FirebaseDatabase.getInstance().getReference().child("notifications").child(model.getUid()).child(ke).child("date").setValue(ServerValue.TIMESTAMP);

                                    try {
                                        websockets socket = new websockets();

                                        JSONObject mmm2 = new JSONObject();
                                        mmm2.putOpt("content",dataSnapshot.child("name").getValue() + "_მ მოიწონა თქვენი კომენტარი" );
                                        mmm2.putOpt("usertoken", getIntent().getExtras().getString("postUser"));
                                        socket.mSocket.emit("notification",mmm2);
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        }


                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {
                        FirebaseDatabase.getInstance().getReference().child("likes").
                                child(getIntent().getExtras().getString("category")).
                                child(getIntent().getExtras().getString("postTitle")).child(rf).child("likes").child(FirebaseAuth.getInstance().getUid()).removeValue();


                    }
                });


                if(model.getDate() != null) {
                    viewHolder.setDate(model.getDate());
                }else{
                    ///////////////
                }
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

                viewHolder.replyBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent commentSection = new Intent(forumActivity.this,commentsActivity.class);

                        commentSection.putExtra("ref", getRef(position).getKey());
                        commentSection.putExtra("posterUid",model.getUid());
                        startActivity(commentSection);
                    }
                });

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
firebaseRecyclerAdapter.startListening();

    }


    public static class replyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView photo;
        public TextView username;
        public ProgressBar progressBar;
        public TextView date;
        public TextView content;
        public View mView;
        public Button replyBTN;
        public LikeButton like_btn;
        public TextView likesNum;
        public TextView commentsNum;
        public replyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            photo = mView.findViewById(R.id.profile_image);
            username = mView.findViewById(R.id.post_reply_name);
            progressBar = mView.findViewById(R.id.progressBar3);
            date = mView.findViewById(R.id.post_reply_time);
            content = mView.findViewById(R.id.post_reply_content);
            replyBTN = mView.findViewById(R.id.replyBTN);
            like_btn = mView.findViewById(R.id.like_btn);
            likesNum = mView.findViewById(R.id.likesNum);
            commentsNum = mView.findViewById(R.id.commentsNum);



        }

        public void setDate(Long dat) {



            date.setText(new timeago().gettimeago(dat,itemView.getContext()));



        }



        public void setContent(String cont) {
            content.setText(cont);
        }
         public void SetcommentsNum(String num) {
             commentsNum.setText(num);
         }

        public void LikedOrNot(Boolean like) {
            like_btn.setLiked(like);
        }
        public void LikeNum(String nm) {
            likesNum.setText(nm);
        }

        public void setUsername(String name) {
        }

        public void setPhoto(String id) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("thumb_image").getValue().equals("default")) {
                        Picasso.with(mView.getContext()).load(dataSnapshot.child("thumb_image").getValue().toString()).placeholder(R.drawable.white).into(photo, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }else{
                        Picasso.with(mView.getContext()).load(R.drawable.user).placeholder(R.drawable.white).into(photo);
                        progressBar.setVisibility(View.GONE);

                    }

                        username.setText(dataSnapshot.child("name").getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });


        }
    }
}
