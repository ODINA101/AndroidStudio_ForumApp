package app.iraklisamniashvilii.com.geoforum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import app.iraklisamniashvilii.com.geoforum.models.search;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        FirebaseRecyclerOptions<search> options =
                new FirebaseRecyclerOptions.Builder<search>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"),search.class)
                        .build();


        FirebaseRecyclerAdapter<search,searchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<search, searchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull searchViewHolder holder, int position, @NonNull search model) {

            }

            @NonNull
            @Override
            public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };





    }
    public static class searchViewHolder extends RecyclerView.ViewHolder{
         public TextView name;
        public CircleImageView thumb;
        public searchViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            name = itemView.findViewById(R.id.name);
        }





        public void setThumb(String data) {

        }



    }
}
