package app.iraklisamniashvilii.com.geoforum.fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.iraklisamniashvilii.com.geoforum.CategoryFull;
import app.iraklisamniashvilii.com.geoforum.R;

/**
 * Created by irakli on 1/12/2018.
 */

public class homeFragment extends Fragment  {

    String[] names = {"მუსიკა","სპორტი","კულტურა და ხელოვნება","დასაქმება","ბიზნესი და ეკონომიკა","ფსიქოლოგია","პროგრამირება"
            ,"ანდროიდი","თამაშები"};
    int[]  icons = {R.drawable.musicicon,R.drawable.sporticon,R.drawable.articon,R.drawable.jobs,R.drawable.business,R.drawable.psychology,
            R.drawable.programming,R.drawable.android,R.drawable.games};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.home_fragment, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        ListView listView = view.findViewById( R.id.fragment_listView );
        listView.setAdapter( new CustomListviewAdapter(getContext(),names,icons) );
       listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //Toast.makeText( getContext(),names[position],Toast.LENGTH_LONG ).show();
               Intent fullTheme = new Intent(getContext(), CategoryFull.class);
               fullTheme.putExtra( "title",names[position] );
                startActivity( fullTheme );



           }
       } );
    }

    public void onResume() {
        super.onResume();

        // Set title bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( "ფორუმი" );

    }
}


