package app.iraklisamniashvilii.com.geoforum;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import app.iraklisamniashvilii.com.geoforum.fragments.Logout;
import app.iraklisamniashvilii.com.geoforum.fragments.homeFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public FirebaseAuth mAuth;
    private DatabaseReference mUserData;
    private FirebaseUser mCurrentUser;
    private CircleImageView mDisplayImage;
    public ProgressBar header_progress;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        Fragment mtavari = new homeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.drawer_content,mtavari);
        ft.commit();
        String currentUid = mCurrentUser.getUid();
        mUserData = FirebaseDatabase.getInstance().getReference("Users").child(currentUid);
        mDisplayImage = navigationView.getHeaderView(0).findViewById(R.id.drawer_profile_photo);
        header_progress = navigationView.getHeaderView(0).findViewById(R.id.header_progress);

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        mUserData.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.child( "image" ).getValue());

                System.out.println("aeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                if(dataSnapshot.child( "image" ).getValue().equals("default")) {
                    Picasso.with( HomeActivity.this ).load(R.drawable.user).into(  mDisplayImage);
                    header_progress.setVisibility(View.GONE);

                }else{

                    Picasso.with(HomeActivity.this).load(dataSnapshot.child( "image" ).getValue().toString()).placeholder(R.drawable.white).into(mDisplayImage,new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            header_progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.home, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }


    public void sendToStart() {
        Intent startpage = new Intent(HomeActivity.this,MainActivity.class);
        startActivity( startpage );
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new homeFragment();
            // Handle the camera action
        } else if (id == R.id.nav_notifications) {
            fragment = new NotificationsActivity();

        } else if (id == R.id.nav_chat) {
           fragment = new Chat();
        } else if (id == R.id.nav_logout) {
            mAuth.getInstance().signOut();
            fragment = new Logout();
            sendToStart();
        }  else if (id == R.id.nav_account) {
            fragment = new homeFragment();

            Intent accountSettings = new Intent( HomeActivity.this,SettingsActivity.class );
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation( HomeActivity.this, navigationView.getHeaderView(0).findViewById(R.id.drawer_profile_photo),"navProfile" );
            startActivity( accountSettings,optionsCompat.toBundle() );

        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace( R.id.drawer_content,fragment ).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
}
