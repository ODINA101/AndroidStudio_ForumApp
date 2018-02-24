package app.iraklisamniashvilii.com.geoforum;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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


        FirebaseRecyclerAdapter<NotiModel,NotiViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NotiModel, NotiViewHolder>(
                NotiModel.class,
                R.layout.single_notification,
                NotiViewHolder.class,
                databaseReference



        ) {
            @Override
            protected void populateViewHolder(NotiViewHolder viewHolder, NotiModel model, int position) {
                  viewHolder.seAction(model.getContent());
                  viewHolder.seTime(model.getDate());

            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
recyclerView.setAdapter(firebaseRecyclerAdapter);
recyclerView.setLayoutManager(linearLayoutManager);
    }


    public static class NotiViewHolder extends RecyclerView.ViewHolder {
private TextView content;
private TextView dattime;


        public NotiViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.content);
            dattime = itemView.findViewById(R.id.dattime);

        }






        public void seAction(String con) {

            content.setText(con);



        }
        public void seTime(Long dat) {
            dattime.setText(new timeago().gettimeago(dat,itemView.getContext()));



        }

    }
}
