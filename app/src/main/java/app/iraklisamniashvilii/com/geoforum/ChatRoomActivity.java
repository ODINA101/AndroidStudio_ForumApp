package app.iraklisamniashvilii.com.geoforum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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

        messages_recycler.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        FirebaseDatabase.getInstance().getReference().child("Users").child(getIntent().getExtras().getString("uid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView titl = mCustomView.findViewById(R.id.userTitle);
                titl.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.with(ChatRoomActivity.this).load(dataSnapshot.child("image").getValue().toString()).into((ImageView) mCustomView.findViewById(R.id.mImg), new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).
               child("Messages")
               .child(getIntent().getExtras().getString("uid"));




       final FirebaseRecyclerAdapter<MessagesModel,MessagesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MessagesModel, MessagesViewHolder>(

                MessagesModel.class,
                R.layout.msg_bubble,
                MessagesViewHolder.class,
                msgRef
        ) {
            @Override
            protected void populateViewHolder(MessagesViewHolder viewHolder, MessagesModel model, int position) {



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


