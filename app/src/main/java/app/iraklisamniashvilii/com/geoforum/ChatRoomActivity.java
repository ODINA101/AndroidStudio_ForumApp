package app.iraklisamniashvilii.com.geoforum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.zip.Inflater;

import app.iraklisamniashvilii.com.geoforum.models.MessagesModel;

import static app.iraklisamniashvilii.com.geoforum.R.drawable.rounded_rectangle_purple;
import static app.iraklisamniashvilii.com.geoforum.R.id.mybubble;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView messages_recycler;
   private EditText editText;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar mChatToolbar = (Toolbar) findViewById(R.id.cbr1);
      setSupportActionBar(mChatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getExtras().getString("uid")).child("Messages").child(FirebaseAuth.getInstance().getUid());
        final DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Messages").child(getIntent().getExtras().getString("uid"));

        getSupportActionBar().setTitle("testia");
        editText = findViewById(R.id.editText);
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            ////////////////////////MESSAGE SENDING SECTION/////////////////////////

            @Override
            public void onClick(View v) {
                HashMap<String,String> mapi = new HashMap<>();
                mapi.put("uid",FirebaseAuth.getInstance().getUid());
                mapi.put("txt",editText.getText().toString());
            myRef.push().setValue(mapi);
                myRef2.push().setValue(mapi);
                editText.setText(null);


            }
        });
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);


        final View mCustomView = mInflater.inflate(R.layout.custom_chat_bar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        messages_recycler = findViewById(R.id.messages_recycler);
        messages_recycler.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        final TextView onlineornot = findViewById(R.id.onlineornot);
        messages_recycler.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getExtras().getString("uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("isOnline").getValue().equals(true)) {
                    onlineornot.setText("შემოსულია");
                }else{
                    onlineornot.setText("გასულია");
                }




                TextView titl = mCustomView.findViewById(R.id.userTitle);
                titl.setText(dataSnapshot.child("name").getValue().toString());
                if(!dataSnapshot.child("thumb_image").getValue().toString().equals("default")) {
                    Picasso.with(ChatRoomActivity.this).load(dataSnapshot.child("thumb_image").getValue().toString()).placeholder(R.drawable.white).into((ImageView) mCustomView.findViewById(R.id.mImg), new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

                }else{
                                Picasso.with(ChatRoomActivity.this).load(R.drawable.user).placeholder(R.drawable.white).into((ImageView) mCustomView.findViewById(R.id.mImg));

                }
            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).
               child("Messages")
               .child(getIntent().getExtras().getString("uid"));


        FirebaseRecyclerOptions<MessagesModel> options =
                new FirebaseRecyclerOptions.Builder<MessagesModel>()
                .setQuery(msgRef,MessagesModel.class).build();



       final FirebaseRecyclerAdapter<MessagesModel,MessagesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MessagesModel, MessagesViewHolder>(

              options

        ) {
           @NonNull
           @Override
           public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

              View view = getLayoutInflater().inflate(R.layout.msg_bubble,parent,false);

               return new MessagesViewHolder(view);
           }

           @Override
           protected void onBindViewHolder(@NonNull MessagesViewHolder viewHolder, int position, @NonNull MessagesModel model) {
               viewHolder.setMsg(model.getTxt());


               if(model.getUid().toString().equals(FirebaseAuth.getInstance().getUid())) {
                   viewHolder.gra.setGravity(Gravity.RIGHT);
                   viewHolder.bubble.setBackground(getDrawable(R.drawable.rounded_rectangle_orange));


               }else{
                   viewHolder.gra.setGravity(Gravity.LEFT);
                   viewHolder.bubble.setBackground(getDrawable(R.drawable.rounded_rectangle_purple));


               }
           }






        };

        messages_recycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

msgRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        linearLayoutManager.scrollToPositionWithOffset(firebaseRecyclerAdapter.getItemCount() - 1, 0);





    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
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






    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
private TextView bubble;
private LinearLayout gra;
public View view;
        public MessagesViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            bubble = itemView.findViewById(mybubble);
            gra = itemView.findViewById(R.id.gra);
        }


        public void setMsg(String txt) {

            bubble.setText(txt);
        }
    }








    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:



                finish();
                break;

        }
        return true;

    }


}


