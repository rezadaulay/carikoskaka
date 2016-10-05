package com.carikoskaka.carikos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class DetailHouseActivity extends AppCompatActivity {
    private static final String TAG = "DetailHouseActivity";
    private String Id = null;
    private ImageView imgDetail;
    private JSONObject specs,location,owner;
    private JSONArray images,facilities;
    private SliderLayout sliderShow;
    private TextView txtPrice,txtDimention,txtStreet,txtProvince,txtCity,txtDescription,txtOwnerName,txtOwnerPhone,txtFacilities;
    private Button btnCallPhone;
    private Button btnCallMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_house);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ambil id dari list
        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");

        //slider show
        sliderShow = (SliderLayout) findViewById(R.id.detail_image_slider);
        sliderShow.stopAutoCycle();

        //single image
        imgDetail = (ImageView)findViewById(R.id.detail_image_single);

        new GetHouse().execute();

        btnCallPhone = (Button) findViewById(R.id.bb_menu_call);
        btnCallMap = (Button) findViewById(R.id.bb_menu_map);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetHouse extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DetailHouseActivity.this);
            pDialog.setMessage("Memuat Data...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            String jsonStr = webreq.makeWebServiceCall("houses/" + Id, WebRequest.GET);

            JSONObject myJson = null;
            try {
                myJson = new JSONObject(jsonStr);

                //data spek rumah
                specs = myJson.getJSONObject("spec");
                //data gambar rumah
                images = specs.getJSONArray("images");
                //data lokasi rumah
                location = specs.getJSONObject("location");
                //data fasilitas rumah
                facilities = specs.getJSONArray("facilities");

                //data pemilik rumah
                owner = myJson.getJSONObject("owner");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                getSupportActionBar().setTitle(specs.getString("name"));

                if( images.length() > 1 ){
                    //buat image slider
                    imgDetail.setVisibility(View.GONE);
                    for (int i = 0; i < images.length(); i++){
                        JSONObject c = images.getJSONObject(i);

                        DefaultSliderView textSliderView = new DefaultSliderView(DetailHouseActivity.this);
                        textSliderView
                                //.description("Game of Thrones")
                                .image(c.getString("url") );
                        sliderShow.addSlider(textSliderView);

                    }


                }
                else{
                    //hanya satu gambar
                    sliderShow.setVisibility(View.GONE);
                    JSONObject c = images.getJSONObject(0);
                    Picasso.with(DetailHouseActivity.this).load( c.getString("url") ).into(imgDetail);
                }

                txtPrice = (TextView)findViewById(R.id.detail_price);
                txtPrice.setText(Html.fromHtml("<strong>Harga</strong> "+specs.getString("price")));

                txtDimention = (TextView)findViewById(R.id.detail_dimention);
                txtDimention.setText(Html.fromHtml("<strong>Luas</strong> "+specs.getString("dimention")));

                txtFacilities = (TextView)findViewById(R.id.detail_facilities);

                String myFacilities = "<strong>Fasilitas</strong> <br/>";
                if( facilities.length() > 0 ){
                    for (int i = 0; i < facilities.length(); i++){
                        myFacilities += "&#8226; "+facilities.get(i).toString();
                        if( i < ( facilities.length() - 1 ) ) {
                            myFacilities += "<br/>";
                        }
                    }
                }
                txtFacilities.setText(Html.fromHtml(myFacilities));

                txtDescription = (TextView)findViewById(R.id.detail_description);
                txtDescription.setText(Html.fromHtml(specs.getString("description")));

                //lokasi kost
                txtStreet = (TextView)findViewById(R.id.detail_street);
                txtStreet.setText(Html.fromHtml("<strong>Lokasi</strong> "+location.getString("street")));

                txtCity = (TextView)findViewById(R.id.detail_city);
                txtCity.setText(Html.fromHtml("<strong>Kota</strong> "+location.getString("city")));

                txtProvince = (TextView)findViewById(R.id.detail_province);
                txtProvince.setText(Html.fromHtml("<strong>Provinsi</strong> "+location.getString("province")));

                //data pemilik kost
                txtOwnerName = (TextView)findViewById(R.id.owner_name);
                txtOwnerName.setText(Html.fromHtml("<strong>Nama</strong> "+owner.getString("name")));

                txtOwnerPhone = (TextView)findViewById(R.id.owner_phone);
                txtOwnerPhone.setText(Html.fromHtml("<strong>Telp</strong> "+owner.getString("phone")));

                //footer button click event listener
                btnCallPhone.setOnClickListener(new OnCallPhone(owner.getString("phone")));
                btnCallMap.setOnClickListener(new onCallMap(location.getString("lat"), location.getString("lng")));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //adapter.notifyDataSetChanged();

        }

    }


    public class OnCallPhone implements View.OnClickListener {

        String myPhoneNumber;
        public OnCallPhone(String myPhoneNumber) {
            this.myPhoneNumber = myPhoneNumber;
        }

        @Override
        public void onClick(View v) {
            Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + this.myPhoneNumber));
            startActivity(i);
        }

    }


    public class onCallMap implements View.OnClickListener {

        String myLat,myLng;
        public onCallMap(String myLat,String myLng) {
            this.myLat = myLat;
            this.myLng = myLng;
        }

        @Override
        public void onClick(View v) {
            /* final Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http:/a/maps.google.com/maps?"
                            + "daddr="+ mylatLng ));

            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");

            startActivity(intent); */
            Log.e("Anto", "geo:"+this.myLat+","+this.myLng);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+this.myLat+","+this.myLng+"?q="+this.myLat+","+this.myLng));
            startActivity(intent);

        }

    }

    //masukkan menu_detail_house.xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_house, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed:
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            share();
            return true;
        }
 
        if (id == android.R.id.home){
            finish();
        }
 
        return super.onOptionsItemSelected(item);
    }
	/* 
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	} */
 
    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, item.getTitle() + " " + item.getHarga() + " " + item.getLokasi());
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Jual Housee Murah");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}