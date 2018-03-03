package app.iraklisamniashvilii.com.geoforum.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.iraklisamniashvilii.com.geoforum.R;

/**
 * Created by irakli on 1/17/2018.
 */


public class CustomListviewAdapter  extends BaseAdapter {
    private Context context;
    String[] names;
    int[]  icons;

    public CustomListviewAdapter(Context context,String[] names,int[] icons) {
        this.icons = icons;
        this.names = names;
        this.context = context;
    }



    static class ViewHolder {
        public TextView Tname;
        public ImageView img;
    }


    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();


      if(convertView == null) {
          convertView = LayoutInflater.from(context).
                  inflate(R.layout.single_theme_layout, parent, false);
      }
        TextView Tname = convertView.findViewById( R.id.tname );
        ImageView img = convertView.findViewById( R.id.img_icon );
        convertView.setTag(holder);
        final TextView postnum;
        postnum = convertView.findViewById(R.id.postnum);

        holder = (ViewHolder) convertView.getTag();

        Tname.setText( names[position] );
        img.setImageResource( icons[position] );

        FirebaseDatabase.getInstance().getReference().child("Posts").child(names[position]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()) {
                    if(dataSnapshot.getChildrenCount() > 0) {
                          postnum.setText(Long.toString(dataSnapshot.getChildrenCount()));

                    }else{
                        postnum.setText("");
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return convertView;
    }





}