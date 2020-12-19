package com.example.teamc.friendfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HobbyCodeActivity extends ListActivity {

    public static String RESULT_HOBBYCODE;
    public static Drawable RESULT_ICON;
    public String[] hobbynames, hobbycodes;
    private TypedArray imgs;
    private List<Hobbies> hobbiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateHobbyList();
        ArrayAdapter<Hobbies> adapter = new HobbyListArrayAdapter(this, hobbiesList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Hobbies h = hobbiesList.get(position);
                RESULT_ICON = hobbiesList.get(position).getIcon();
                RESULT_HOBBYCODE = hobbiesList.get(position).getName();

                Bitmap bitmap = null;

                if (RESULT_ICON instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) RESULT_ICON;
                    if(bitmapDrawable.getBitmap() != null) {
                        bitmapDrawable.getBitmap();
                    }
                }

                if(RESULT_ICON.getIntrinsicWidth() <= 0 || RESULT_ICON.getIntrinsicHeight() <= 0) {
                    bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
                } else {
                    bitmap = Bitmap.createBitmap(RESULT_ICON.getIntrinsicWidth(), RESULT_ICON.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                }

                Canvas canvas = new Canvas(bitmap);
                RESULT_ICON.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                RESULT_ICON.draw(canvas);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();



                Intent returnIntent = new Intent();
                returnIntent.putExtra("picture", b);
                returnIntent.putExtra("name", RESULT_HOBBYCODE);
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populateHobbyList() {
        hobbiesList = new ArrayList<Hobbies>();
        hobbynames = getResources().getStringArray(R.array.hobby_names);
        hobbycodes = getResources().getStringArray(R.array.hobby_codes);
        imgs = getResources().obtainTypedArray(R.array.hobby_icons);
        for(int i = 0; i < hobbycodes.length; i++){
            hobbiesList.add(new Hobbies(hobbynames[i], hobbycodes[i], imgs.getDrawable(i)));
        }
    }

    public class Hobbies {
        private String name;
        private String code;
        private Drawable icon;
        public Hobbies(String name, String code, Drawable icon){
            this.name = name;
            this.code = code;
            this.icon = icon;
        }
        public String getName() {
            return name;
        }
        public Drawable getIcon() {
            return icon;
        }
        public String getCode() {
            return code;
        }
    }
}
