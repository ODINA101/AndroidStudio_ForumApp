package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import app.iraklisamniashvilii.com.geoforum.models.Chatusers;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by irakli on 1/20/2018.
 */

public class Chat extends Fragment {
    private RecyclerView rec;
    private LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.chat_fragment,container,false );
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
         rec = view.findViewById(R.id.rec);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout( true );
        linearLayoutManager.setStackFromEnd( true );
         rec.setLayoutManager(linearLayoutManager);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( "ჩათი" );
        FirebaseRecyclerOptions<Chatusers> options = new FirebaseRecyclerOptions.Builder<Chatusers>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chats"),Chatusers.class)
                .build();


        FirebaseRecyclerAdapter<Chatusers,ChatusersHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chatusers, ChatusersHolder>(
                 options
        ) {
            @NonNull
            @Override
            public ChatusersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = getLayoutInflater().inflate(R.layout.single_user_chat, parent,false);
               return new ChatusersHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ChatusersHolder viewHolder, int position, @NonNull final Chatusers model) {

                viewHolder.setDet(model.getUid());
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent chatroom = new Intent(getContext(),ChatRoomActivity.class);

                        chatroom.putExtra("uid",model.getUid());
                        startActivity(chatroom);
                    }
                });
            }


        };

        rec.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.startListening();

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
    public static class ChatusersHolder extends RecyclerView.ViewHolder {
TextView name;
View mview;
ProgressBar progressBari;
public CardView cardView;
CircleImageView circleImageView;
        public ChatusersHolder(View itemView) {
            super(itemView);
        mview = itemView;
            name=itemView.findViewById(R.id.name);
            circleImageView = itemView.findViewById(R.id.thumb);
            progressBari = itemView.findViewById(R.id.progressBari);
            progressBari.animate();
            cardView = mview.findViewById(R.id.mCard);

        }



        public void setDet(String uid) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name.setText(dataSnapshot.child("name").getValue().toString());

                    if(!dataSnapshot.child("thumb_image").getValue().toString().equals("default")) {
                    Picasso.with(mview.getContext()).load(dataSnapshot.child("thumb_image").getValue().toString()).into(circleImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBari.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    }else{
                        Picasso.with(mview.getContext()).load(R.drawable.user).into(circleImageView);
                        progressBari.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }
}


