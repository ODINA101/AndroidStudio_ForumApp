package app.iraklisamniashvilii.com.geoforum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private static final int gallery_pick = 1;
    public Button back_btn_reg;
    public Button profile_photo;
    private StorageReference mImageStorage;
    private CircleImageView mDisplayImage;
    private ProgressDialog mProgressDialog;
    private String currentUid;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mUserData;
    private ProgressBar progressBar;
    private TextView settings_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );
        back_btn_reg = findViewById( R.id.back_btn_reg );
        back_btn_reg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        mImageStorage = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById( R.id.setting_progressbar );
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();
        mUserData = FirebaseDatabase.getInstance().getReference("Users").child(currentUid);
        progressBar.animate();
        settings_name = findViewById( R.id.settings_name );
        mDisplayImage = findViewById(R.id.settings_user);




 mUserData.addValueEventListener( new ValueEventListener() {
     @Override
     public void onDataChange(DataSnapshot dataSnapshot) {
         if(dataSnapshot.child("name").exists()) {
             settings_name.setText(dataSnapshot.child("name").getValue().toString());
         }

         if(dataSnapshot.child("thumb_image").exists()) {
             if (!dataSnapshot.child("thumb_image").getValue().equals("default")) {

                 Picasso.with(SettingsActivity.this).load(dataSnapshot.child("thumb_image").getValue().toString()).placeholder(R.drawable.white).into(mDisplayImage, new com.squareup.picasso.Callback() {
                     @Override
                     public void onSuccess() {
                         progressBar.setVisibility(View.GONE);
                     }

                     @Override
                     public void onError() {
                         progressBar.setVisibility(View.GONE);

                     }
                 });
             } else {
                 Picasso.with(SettingsActivity.this).load(R.drawable.user).placeholder(R.drawable.white).into(mDisplayImage, new Callback() {
                     @Override
                     public void onSuccess() {
                         progressBar.setVisibility(View.GONE);
                     }

                     @Override
                     public void onError() {
                         progressBar.setVisibility(View.GONE);
                     }
                 });
             }
         }

     }

     @Override
     public void onCancelled(DatabaseError databaseError) {

     }
 } );



 Button changename = findViewById(R.id.settings_change_name);
changename.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        View view2 = getLayoutInflater().inflate(R.layout.reply_dialogbox3, null);
        final EditText comment = view2.findViewById(R.id.commentTxt);
        Button publish = view2.findViewById(R.id.publish);
        Button close = view2.findViewById(R.id.close);


        builder.setView(view2);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String come = comment.getText().toString();
                final String uid = FirebaseAuth.getInstance().getUid();
                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("name").setValue(come);
                dialog.dismiss();


            }
        });

    }
});

                profile_photo = findViewById( R.id.setting_change_profile);
                profile_photo.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), gallery_pick);

                    }
                } );





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallery_pick && resultCode == RESULT_OK) {


            Uri imageUrl = data.getData();
            CropImage.activity(imageUrl)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog = new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("იტვირთება...");
                mProgressDialog.setMessage("გთხოვთ დაელოდოთ სანამ ფოტო დამუშავდება..");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                String current_user_id = mCurrentUser.getUid();
                Uri resultUri = result.getUri();
                final File thumb_filePath = new File(resultUri.getPath());
                Bitmap thumb_bitmap = new Compressor( this ).setMaxWidth( 200 )
                        .setMaxHeight( 200 )
                        .setQuality( 10 )
                        .compressToBitmap( thumb_filePath );

                ByteArrayOutputStream baos = new ByteArrayOutputStream(  );
                thumb_bitmap.compress( Bitmap.CompressFormat.JPEG,100,baos );
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference thumb_filepath = mImageStorage.child("profile_image").child("thumbs").child(current_user_id + ".jpg");

                UploadTask uploadTask = thumb_filepath.putBytes( thumb_byte );
                uploadTask.addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            String thumb_downloadurl = task.getResult().getDownloadUrl().toString();
                            mUserData.child("thumb_image").setValue( thumb_downloadurl ).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "პროფილის ფოტო შეცვლილია", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } );
                            }else{
                            Toast.makeText(SettingsActivity.this, "მოხდა შეცდომა", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                } );




            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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
