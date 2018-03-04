package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import app.iraklisamniashvilii.com.geoforum.models.search;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {
RecyclerView recyclerView;
LinearLayoutManager linearLayoutManager;
AVLoadingIndicatorView progressBar4;
ImageButton searchbtn;
EditText searchtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar4 = findViewById(R.id.progressBar4);
searchbtn = findViewById(R.id.searchbtn);
searchtxt = findViewById(R.id.searchtxt);

searchbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loadData(searchtxt.getText().toString());
    }
});

        linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                loadData(null);

                            }
                        }
                ,1000);
    }


    public void loadData(final String searchtxt) {
        FirebaseRecyclerOptions<search> options;
if(searchtxt != null) {
         options =
                new FirebaseRecyclerOptions.Builder<search>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), search.class)
                        .build();
    FirebaseRecyclerAdapter<search, searchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<search, searchViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull final searchViewHolder holder, final int position, @NonNull final search model) {

            progressBar4.hide();
if(model.getName().contains(searchtxt)) {
    holder.setName(model.getName());
    holder.setThumb(model.getThumb_image());

    holder.post_card.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent profileInfo = new Intent(SearchActivity.this, ProfileInfoActivity.class);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this, holder.thumb, "ProfilePhoto");

            profileInfo.putExtra("name", model.getName());
            profileInfo.putExtra("uid", getRef(position).getKey().toString());

            startActivity(profileInfo, optionsCompat.toBundle());
        }
    });

}else{
    holder.post_card.setVisibility(View.GONE);
}

        }

        @NonNull
        @Override
        public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.single_user_card, parent, false);
            return new searchViewHolder(view);


        }

    };
    recyclerView.setAdapter(firebaseRecyclerAdapter);
    recyclerView.setLayoutManager(linearLayoutManager);
    firebaseRecyclerAdapter.startListening();

}else{
  /*  options = new FirebaseRecyclerOptions.Builder<search>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").startAt(searchtxt).endAt(searchtxt+"\uf8ff"),search.class)
                    .build();*/
    options =
            new FirebaseRecyclerOptions.Builder<search>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), search.class)
                    .build();
    FirebaseRecyclerAdapter<search, searchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<search, searchViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull final searchViewHolder holder, final int position, @NonNull final search model) {

            progressBar4.hide();

            holder.setName(model.getName());
            holder.setThumb(model.getThumb_image());

            holder.post_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileInfo = new Intent(SearchActivity.this, ProfileInfoActivity.class);
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this, holder.thumb, "ProfilePhoto");

                    profileInfo.putExtra("name", model.getName());
                    profileInfo.putExtra("uid", getRef(position).getKey().toString());

                    startActivity(profileInfo, optionsCompat.toBundle());
                }
            });


        }

        @NonNull
        @Override
        public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = getLayoutInflater().inflate(R.layout.single_user_card, parent, false);
            return new searchViewHolder(view);


        }
    };
    recyclerView.setAdapter(firebaseRecyclerAdapter);
    recyclerView.setLayoutManager(linearLayoutManager);
    firebaseRecyclerAdapter.startListening();

}




    }


    public static class searchViewHolder extends RecyclerView.ViewHolder{
         public TextView name;
        public CircleImageView thumb;
        public ProgressBar progressBar2;
        public CardView post_card;
        View mview;
        public searchViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.post_title_oncard);
           mview = itemView;
           progressBar2 = itemView.findViewById(R.id.progressBar2);
            post_card = itemView.findViewById(R.id.post_card);

        }



         public void setName(String nm) {
            if(nm != null) {
             name.setText(nm);
        }

        }

       void setThumb(String data) {
               if(data.equals("default")) {
                   Picasso.with(mview.getContext()).load(R.drawable.user).into(thumb, new Callback() {
                       @Override
                        public void onSuccess() {
                            progressBar2.setVisibility(View.GONE);
                        }

                       @Override
                        public void onError() {

                       }
                    });
                }else{
                    if(!data.isEmpty() || data.equals(null)) {
                        Picasso.with(mview.getContext()).load(data).into(thumb, new Callback() {
                           @Override
                            public void onSuccess() {
                                progressBar2.setVisibility(View.GONE);
                            }

                            @Override
                           public void onError() {

                            }
                        });
                    }
                }

        }



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
}
