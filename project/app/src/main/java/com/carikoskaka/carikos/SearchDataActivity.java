package com.carikoskaka.carikos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.apptik.widget.MultiSlider;

public class SearchDataActivity extends AppCompatActivity {
    Context context;

    //Declaration Button
    Button btnSubmit;
    MultiSelectSpinner mySpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        getSupportActionBar().setTitle("Pencarian Rumah Kos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView min1 = (TextView) findViewById(R.id.search_form_min_price);
        final TextView max1 = (TextView) findViewById(R.id.search_form_max_price);

        MultiSlider multiSlider1 = (MultiSlider)findViewById(R.id.search_form_range_price_slider);


        min1.setText(String.valueOf(multiSlider1.getThumb(0).getValue()));
        max1.setText(String.valueOf(multiSlider1.getThumb(1).getValue()));

        multiSlider1.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    min1.setText(String.valueOf(value));
                } else {
                    max1.setText(String.valueOf(value));
                }
            }
        });

        final TextView min2 = (TextView) findViewById(R.id.search_form_min_dimention_room);
        final TextView max2 = (TextView) findViewById(R.id.search_form_max_dimention_room);

        MultiSlider multiSlider2 = (MultiSlider)findViewById(R.id.search_form_range_room_dimention_slider);


        min2.setText(String.valueOf(multiSlider2.getThumb(0).getValue()));
        max2.setText(String.valueOf(multiSlider2.getThumb(1).getValue()));

        multiSlider2.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    min2.setText(String.valueOf(value));
                } else {
                    max2.setText(String.valueOf(value));
                }
            }
        });

        String[] strings = { "Oven", "Fire Extinguisher", "Gordyn", "AC", "Swimming Pool", "Carport", "Garden", "Garasi", "Telephone", "PAM", "Water Heater", "Refrigerator", "Stove", "Microwave", "Satu orang satu kamar", "Satu kamar untuk pria", "Satu kamar untuk wanita"};
        List<Long> longs = Arrays.asList(  Long.valueOf(1), Long.valueOf(3), Long.valueOf(4), Long.valueOf(5), Long.valueOf(6), Long.valueOf(7), Long.valueOf(8), Long.valueOf(9), Long.valueOf(10), Long.valueOf(11), Long.valueOf(12), Long.valueOf(13), Long.valueOf(14), Long.valueOf(15), Long.valueOf(16), Long.valueOf(17), Long.valueOf(18));

        mySpin = (MultiSelectSpinner)findViewById(R.id.multi_spinner);
        mySpin.setItems(strings);
        mySpin.setIds(longs);
        //List<Long> selected = mySpin.getSelectedIds();
        //Log.d("anto", selected.toString());

        //Intialization Button

        btnSubmit = (Button) findViewById(R.id.search_form_submit);
        btnSubmit.setOnClickListener(new OnSubmit());

    }

    public class OnSubmit implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            List<Long> facilility_val = mySpin.getSelectedIds();
            EditText location_name_val   = (EditText)findViewById(R.id.search_form_name_location);
            TextView price_range_min_val   = (TextView)findViewById(R.id.search_form_min_price);
            TextView price_range_max_val   = (TextView)findViewById(R.id.search_form_max_price);
            TextView room_dimention_range_min_val   = (TextView)findViewById(R.id.search_form_min_dimention_room);
            TextView room_dimention_range_max_val   = (TextView)findViewById(R.id.search_form_max_dimention_room);

            if( facilility_val.size() > 0 ) {
                Log.d("anto : fasilitas = ", facilility_val.toString());
            }

            /*Intent intent = new Intent(SearchDataActivity.this, SearchResultActivity.class);
            intent.putExtra("KEYWORD_VALUE",
                    "&location_name="+ ( (location_name_val.getText().toString() == "") ? "null" : location_name_val.getText().toString() ) +
                            "&price_range="+ price_range_min_val.getText().toString() +","+ price_range_max_val.getText().toString()+
                            "&room_dimention="+ room_dimention_range_min_val.getText().toString() +","+ room_dimention_range_max_val.getText().toString()+
                            "&facilities="+ ( (facilility_val.size()) > 0 ? join(facilility_val, ",") : "" )
            );
            startActivity(intent);*/

            Intent intent = new Intent(SearchDataActivity.this, SearchResultActivity.class);
            intent.putExtra("KEYWORD_VALUE",
                    "&location_name="+ ( (location_name_val.getText().toString() == "") ? "null" : location_name_val.getText().toString() ) +
                            "&price_range="+ price_range_min_val.getText().toString() +","+ price_range_max_val.getText().toString()+
                            "&room_dimention="+ room_dimention_range_min_val.getText().toString() +","+ room_dimention_range_max_val.getText().toString()+
                            "&facilities="+ ( (facilility_val.size()) > 0 ? join(facilility_val, ",") : "" )
            );
            startActivity(intent);

            /*Intent resultIntent = new Intent(SearchDataActivity.this, SearchResultActivity.class);
            resultIntent.putExtra(SearchResultActivity.KEYWORD_VALUE,
                    "&location_name="+ ( (location_name_val.getText().toString() == "") ? "null" : location_name_val.getText().toString() ) +
                    "&price_range="+ price_range_min_val.getText().toString() +","+ price_range_max_val.getText().toString()+
                    "&room_dimention="+ room_dimention_range_min_val.getText().toString() +","+ room_dimention_range_max_val.getText().toString()+
                    "&facilities="+ ( (facilility_val.size()) > 0 ? join(facilility_val, ",") : "" )
            );
            setResult(Activity.RESULT_OK, resultIntent);

            //finish();

            startActivityForResult(resultIntent, 1);
            //return true;
            */


        }

    }

    private static String join(List<Long> list, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(Long s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }

        return sb.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
