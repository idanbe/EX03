package com.example.administrator.home_ex3;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener
{

    private Spinner spinner;
    private String loction_selcted ;
    private Context context;
    //location
    private LocationManager locationManager;
    private final long SECOND = 1000;
    private final long MIN_DISTANCE = 5;
    private String date;
    private String hour;
    private String temp;
    private String description;
    private String icon_name;
    private Picasso icon;
    private ListView list;
    private ImageView imageView;
    private String url_icon;

    private ListView ListView;
    private ListAdapter listAdapter;
    private ArrayList<Item> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        spinner = (Spinner) findViewById(R.id.spinner);
        ListView = (ListView) findViewById(R.id.item_list);

        //******************
        // idan ahomo NOTE !!
        // you need to insert the data to arrayList, any row is one item and array list is all items(row)
        // where you need to add row - then create item like : item = newItem() and save the parameters in row according to the function setDataInItem()
        //
        //******************

        // for test ... one row
        arrayList = new ArrayList<Item>();


        Item item = setDataInItem("May 26, 2013" , "13:35" , "10c" , "Light rain");
        arrayList.add(item); // any item is row
        arrayList.add(item);
        arrayList.add(item);

        // connect between row layout and
        listAdapter = new ListAdapter(this, arrayList);


        ListView.setAdapter(listAdapter);

        //Initialize ImageView
        //imageView = (ImageView) findViewById(R.id.imageView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {

                loction_selcted = parent.getItemAtPosition(pos).toString();

                Log.d("@@@", loction_selcted);

                if(loction_selcted.equals("Curent location"))
                {
                    //// TODO: get location
                }
                get_forecast(loction_selcted);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    private Item setDataInItem(String date , String time , String temp , String info) {

        Item newItem = new Item();

        newItem.setDate(date);
        newItem.setTime(time);
        newItem.setTemperature(temp);
        newItem.setInfo(info);

        // Add some more dummy data for testing
        return newItem;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }




    @Override
    protected void onStop() {
        super.onStop();
    }

    private void get_forecast(String location)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=metric"+"&appid=2de143494c0b295cca9337e1e96b00e0";


        Log.d("@@@222",location);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {

                    JSONArray list = response.getJSONArray("list");

                    if (list.length() > 0)
                    {
                        Log.d("@@@222","list length"+list.length()); //all data
                        for (int i =0; i<list.length();i++)
                        {

                            JSONObject json =list.getJSONObject(i);
                            JSONObject main = json.getJSONObject("main");
                            JSONArray weather = json.getJSONArray("weather");
                            JSONObject zero =weather.getJSONObject(0);

                            try {
                                temp = main.getString("temp");
                                date = json.getString("dt_txt");
                                hour = date.substring(11, 19);
                                date = date.substring(0, 10);
                                description = zero.getString("description");
                                icon_name = zero.getString("icon");

                                url_icon =" http://openweathermap.org/img/w/"+icon_name+".png";
                                //Loading image from below url into imageView
                                Picasso.with(MainActivity.this)
                                        .load(url_icon)
                                        .into(imageView);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Log.d("@@@222","-----------------------------------"); //all data
                            Log.d("@@@222","date:"+date); //all data
                            Log.d("@@@222","hour:"+hour); //all data
                            Log.d("@@@222","temp:"+temp); //all data
                            Log.d("@@@222","description:" + description); //all data
                            Log.d("@@@222","icon:"+icon_name); //all data
                            Log.d("@@@222","url_icon:"+url_icon); //all data


                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Utils.cancelProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.cancelProgressDialog();
                Toast.makeText(context, "Failed to get forecast", Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(request);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
