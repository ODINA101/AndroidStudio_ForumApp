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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.iraklisamniashvilii.com.geoforum.models.NotiModel;

public class NotificationsActivity extends Fragment{

    private RecyclerView recyclerView;
   private DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_notifications,container,false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.mRecy);
          databaseReference = FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance().getUid());



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

            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
recyclerView.setAdapter(firebaseRecyclerAdapter);
recyclerView.setLayoutManager(linearLayoutManager);
    }


    public static class NotiViewHolder extends RecyclerView.ViewHolder {

        public NotiViewHolder(View itemView) {
            super(itemView);
        }

        public void seAction(String con) {

        }
        public void seTime(Long data) {

        }

    }
}
