package com.carikoskaka.carikos;

import android.app.ProgressDialog;import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressBar progressBar;

    /** Called when the activity is first created. */
    private MainActivity activity;

    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } */

    private ListView lvItem;
    // Hashmap for ListView
    ArrayList<HouseModel> houseList;
    // Hashmap for ListView
    ArrayList<HouseModel> houselist = new ArrayList<>();

    com.carikoskaka.carikos.HouseAdapter adapter;
    int pageNumber = 1;
    public static String KEYWORD_VALUE = "";
    public static String keywordText= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting JSON from URL
        //WebRequest webreq = new WebRequest();
        // String jsonStr = webreq.makeWebServiceCall("house", WebRequest.GET);

        lvItem = (ListView)findViewById(R.id.lv_item);

        View footer = getLayoutInflater().inflate(R.layout.progress_bar_footer, null);
        progressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lvItem.addFooterView(footer);

        new GetHouses().execute();

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HouseModel mbl = houselist.get(position);
                //Log.d("Anto", String.valueOf(mbl.getId()) );

                Intent intent = new Intent(MainActivity.this, DetailHouseActivity.class);
                intent.putExtra("Id", String.valueOf(mbl.getId()) );
                startActivity(intent);

            }
        });

        lvItem.setOnScrollListener(new InfiniteScrollListener(2) {
            @Override
            public void loadMore(int page, int totalItemsCount) {
                //Log.d("anto", String.valueOf(pageNumber));
                //if( pageNumber == 1 ){
                //    this.currentPage = 0;
                //}
                pageNumber = page;
                new GetHouses().execute();
                //adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1 : {
                if (resultCode == Activity.RESULT_OK) {
                    keywordText = data.getStringExtra(KEYWORD_VALUE);

                    adapter.houseList.clear();
                    adapter.notifyDataSetChanged();

                    getSupportActionBar().setTitle("Hasil Pencarian");
                    //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    pageNumber = 1;
                    new GetHouses().execute();
                }
                break;
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetHouses extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if( pageNumber > 1 ){
                progressBar.setVisibility(View.VISIBLE);
            }
            else {
                // Showing progress dialog
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Memuat Data...");
                pDialog.setCancelable(false);
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            String jsonStr = webreq.makeWebServiceCall("houses?page="+pageNumber+keywordText, WebRequest.GET);

            Log.d("Response: ", "> " + jsonStr);

            houseList = ParseJSON(jsonStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if( pageNumber > 1 ) {
                progressBar.setVisibility(View.GONE);
            }
            else{
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
            if( houseList != null ) {
                if (adapter == null) {
                    adapter = new HouseAdapter(MainActivity.this, houseList);
                    lvItem.setAdapter(adapter);

                }
                else {
                    adapter.notifyDataSetChanged();
                }
            }
            else{
                if( pageNumber > 1 ) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Anda Tiba Diakhir Halaman", Toast.LENGTH_LONG).show();
                }
                else {
                    //Log.d("anto", "wacau");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    private ArrayList<HouseModel> ParseJSON(String json) {
        if (json != null) {
            try {

                JSONObject jsonObj = new JSONObject(json);

                // Getting JSON Array node
                JSONArray houses = jsonObj.getJSONArray("data");

                HouseModel house = null;
                Integer a = jsonObj.length();
                for (int i = 0; i < houses.length(); i++){
                    JSONObject c = houses.getJSONObject(i);

                    house = new HouseModel();
                    house.setId(c.getString("id"));
                    house.setTitle(c.getString("name"));
                    house.setImage(c.getString("image"));
                    house.setHarga(c.getString("price"));
                    house.setLokasi(c.getString("locationName"));
                    houselist.add(house);
                }

                return houselist;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.d("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
    }

    //masukkan main_menu.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //on click search button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(MainActivity.this, SearchDataActivity.class);
                startActivityForResult(intent, 1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void searchWord(String textString) {
        // TODO Auto-generated method stub

    }

	/* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */

}
