
package com.example.teamc.friendfinder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private FirebaseAuth mAuth;
    private DatabaseReference userDbReference;
    private FirebaseUser user;
    myUser user1 = new myUser();

    TextToSpeech tts;
    int result;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            String text;


            switch (item.getItemId()) {
                case R.id.navigation_my_profile:

                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    {
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in your Device",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        text = item.toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }


                    MyProfile myProfile = new MyProfile();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, myProfile, "Fragment Name");

                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_browse_user_profiles:

                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    {
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in your Device",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        text = item.toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }

                    BrowseProfiles browseProfiles = new BrowseProfiles();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, browseProfiles, "Fragment Name");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.navigaion_chat:

                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    {
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in your Device",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        text = item.toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    Chat chat = new Chat();
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, chat, "Fragment Name");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.navigation_notifications:

                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    {
                        Toast.makeText(getApplicationContext(), "Feature Not Supported in your Device",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        text = item.toString();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    Notifications notifications = new Notifications();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, notifications, "Fragment Name");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.action_signOut) {


            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        FirebaseAuth.getInstance().getCurrentUser();

        userDbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userDbReference.child("onlineStatus").setValue(false);
                userDbReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        //Put this in onCreate method instead
//
//
////            user1.setOnlineStatusTrue();
////
////            if(user1.getOnlineStatus() == true){
////                Toast.makeText(this, "You are online!", Toast.LENGTH_SHORT).show();
////            }
//
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        tts = new TextToSpeech(MainActivity.this, MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {

            final String online_user_id = user.getUid();

            userDbReference = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(online_user_id);

            userDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userDbReference.child("onlineStatus").setValue(true);
                    userDbReference.child("onlineStatus").onDisconnect().setValue(false);
                    userDbReference.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    String text;

    @Override
    public void onInit(int status) {
        result = tts.setLanguage(Locale.US);

        if (status == TextToSpeech.SUCCESS) {

            tts.setLanguage(Locale.ENGLISH);

        } else {
            Toast.makeText(getApplicationContext(), "Feature Not Supported in your Device",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
