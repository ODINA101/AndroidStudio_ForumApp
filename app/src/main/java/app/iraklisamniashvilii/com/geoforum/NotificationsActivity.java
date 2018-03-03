package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.iraklisamniashvilii.com.geoforum.models.NotiModel;

public class NotificationsActivity extends Fragment{

    private RecyclerView recyclerView;
   private DatabaseReference databaseReference;
   private Button cleanNoti;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_notifications,container,false );
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.mRecy);
          databaseReference = FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance().getUid());
        cleanNoti = view.findViewById(R.id.cleanNoti);
        cleanNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.getRef().removeValue();
            }
        });





        FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance().getUid()).orderByChild("seen").equalTo("false").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                  FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance().getUid())
                            .child(snapshot.getKey()).child("seen").setValue("true");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        super.onViewCreated( view, savedInstanceState );
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( "შეტყობინებები" );

        FirebaseRecyclerOptions<NotiModel> options =
                new FirebaseRecyclerOptions.Builder<NotiModel>()
                        .setQuery(databaseReference, NotiModel.class)
                        .build();



        FirebaseRecyclerAdapter<NotiModel,NotiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NotiModel, NotiViewHolder>(
               options


        ) {
            @NonNull
            @Override
            public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NotiViewHolder(getLayoutInflater().inflate(R.layout.single_notification,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull NotiViewHolder viewHolder, int position, @NonNull final NotiModel model) {
                viewHolder.seAction(model.getContent());
                viewHolder.seTime(model.getDate());
                if (model.getUid()!= null) {
                    viewHolder.crd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent profileInfo = new Intent(view.getContext(), ProfileInfoActivity.class);

                            FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    profileInfo.putExtra("name", dataSnapshot.child("name").getValue().toString());
                                    profileInfo.putExtra("uid", model.getUid());
                                    profileInfo.putExtra("psUser", FirebaseAuth.getInstance().getUid());
                                    startActivity(profileInfo);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    });
                }

            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
recyclerView.setAdapter(firebaseRecyclerAdapter);
recyclerView.setLayoutManager(linearLayoutManager);
        firebaseRecyclerAdapter.startListening();

    }


    public static class NotiViewHolder extends RecyclerView.ViewHolder {
private TextView content;
private TextView dattime;
private CardView crd;

        public NotiViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            dattime = itemView.findViewById(R.id.dattime);
            crd = itemView.findViewById(R.id.mCrd);

        }






        public void seAction(String con) {

            content.setText(con);



        }
        public void seTime(Long dat) {
            dattime.setText(new timeago().gettimeago(dat,itemView.getContext()));



        }

    }
}
