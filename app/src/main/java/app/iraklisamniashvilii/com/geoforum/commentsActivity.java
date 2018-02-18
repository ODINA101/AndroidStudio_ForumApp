package app.iraklisamniashvilii.com.geoforum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import org.w3c.dom.Text;

import java.util.HashMap;

import app.iraklisamniashvilii.com.geoforum.models.ReplycommentsModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class commentsActivity extends AppCompatActivity {


   private DatabaseReference mDatabase;
   private RecyclerView recyclerView;
 private ImageButton imageButton;
private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);



          getSupportActionBar().setTitle("კომენტარები");

        imageButton = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("replycomments").child(getIntent().getExtras().getString("ref"));



        imageButton.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                 HashMap<String,String> mMap = new HashMap<>();
                 mMap.put("uid", FirebaseAuth.getInstance().getUid());
                 mMap.put("content",editText.getText().toString());
                String key = mDatabase.push().getKey();

               mDatabase.child(key).setValue(mMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                        editText.setText(null);
                    }
                });

                mDatabase.child(key).child("date").setValue(ServerValue.TIMESTAMP);

           }
        });


            recyclerView =  findViewById(R.id.comments_recycler);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        final FirebaseRecyclerAdapter<ReplycommentsModel,commentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReplycommentsModel, commentViewHolder>(
                ReplycommentsModel.class,
                R.layout.single_reply_comment,
                commentViewHolder.class,
                mDatabase


        ) {
            @Override
            protected void populateViewHolder(final commentViewHolder viewHolder, ReplycommentsModel model, int position) {

                  viewHolder.setDate(model.getDate());
                    viewHolder.setContent(model.getContent());

                       FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.print(dataSnapshot.child("name").getValue());

                             System.out.print(dataSnapshot);
                             System.out.print(dataSnapshot);
                              System.out.print(dataSnapshot);
                             System.out.print(dataSnapshot);

                           viewHolder.uname.setText(dataSnapshot.child("name").getValue().toString());
                              viewHolder.setPhoto(dataSnapshot.child("thumb_image").getValue().toString());

                          }

                        @Override
                           public void onCancelled(DatabaseError databaseError) {

                          }
                      });


            }
        };

recyclerView.setAdapter(firebaseRecyclerAdapter);

mDatabase.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        linearLayoutManager.scrollToPositionWithOffset(firebaseRecyclerAdapter.getItemCount() -1,0);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});



    }


    public static class commentViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView uname;
        public TextView time;
        public CircleImageView circleImageView;
        public View mView;


        public commentViewHolder(View itemView) {
            super(itemView);
             mView = itemView;
            content = mView.findViewById(R.id.user_single_comment);
             time = mView.findViewById(R.id.time);
             circleImageView = mView.findViewById(R.id.user_single_image);
             uname = mView.findViewById(R.id.user_single_name);

        }


        public void setDate(Long data)  {
            if(data != null ){

            time.setText( new timeago().gettimeago(data,itemView.getContext()) );
            }


        }
        public void setContent(String comment)  {
            content.setText(comment);

        }
        public void setPhoto(String url)  {
            Picasso.with(mView.getContext()).load(url).into(circleImageView, new Callback() {
                @Override
                public void onSuccess() {



                }

                @Override
                public void onError() {

                }
            });
        }














    }

}
