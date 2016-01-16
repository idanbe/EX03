package com.example.administrator.home_ex3;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Network;
import android.preference.DialogPreference;
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
    private String url_icon;
    private double lat;
    private double lon;
    private String url;

    private ListView ListView;
    private ListAdapter listAdapter;
    private ArrayList<Item> arrayList;
    private  Item item;

    private ProgressDialog progressDialog;
    final String PROGRESS_TITTLE = "Loading" ;
    final String PROGRESS_MASSAGE = "please wait..." ;
    final String CURRENT_LOCATION = "Current location";

    final String JSON_LIST = "list";
    final String JSON_MAIN = "main";
    final String JSON_WEATHER = "weather";
    final int JSON_ZERO = 0 ;
    final int ZERO =0;
    final String TEMP = "temp";
    final String DT_TXT = "dt_txt";

    final int SUBSTRING_DATE_START = 0;
    final int SUBSTRING_DATE_END = 10;
    final int SUBSTRING_temp_START = 0;
    final char SUBSTRING_temp_END = '.';
    final int SUBSTRING_HOUR_START = 11;
    final int SUBSTRING_HOUR_END = 19;

    final String DESCRIPTION = "description" ;
    final String ICON = "icon" ;

    final String FAILED_LOCATION = "Failed to get location";
    final String NETWORK_TITTLE = "Network";
    final String NETWORK_MASSAGE = "please check the connection network";
    final String MASSAGE_BUTTON = "OK" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        spinner = (Spinner) findViewById(R.id.spinner);
        ListView = (ListView) findViewById(R.id.item_list);

        // show all inems
        listAdapter = new ListAdapter(MainActivity.this, arrayList);

        // location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {

                // Loading ...
                progressDialog = ProgressDialog.show(get_context(), PROGRESS_TITTLE, PROGRESS_MASSAGE, true);

                loction_selcted = parent.getItemAtPosition(pos).toString();

                if (loction_selcted.equals(CURRENT_LOCATION))
                {
                    //check GPS availability
                    boolean isGPSAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (isGPSAvailable) {
                        //get GPS updates
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, SECOND*10, MIN_DISTANCE, (LocationListener) context);
                    }
                }
                else // not current location
                {
                    // new list
                    arrayList = new ArrayList<Item>();
                    get_forecast(loction_selcted);
                    // show all items
                    listAdapter = new ListAdapter(MainActivity.this, arrayList);
                    ListView.setAdapter(listAdapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }

    //get context
    public Context get_context()
    {
        return context;
    }


    // set data in item
    private Item setDataInItem(String date , String time , String temp , String info,String url) {

        Item newItem = new Item();

        newItem.setDate(date);
        newItem.setTime(time);
        newItem.setTemperature(temp);
        newItem.setInfo(info);
        newItem.setUrl(url);

        return newItem;
    }

    // get forecast according to user selected
    private void get_forecast(String location)
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        if(location.equals(CURRENT_LOCATION))
        {
            url = "http://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&units=metric&appid=2de143494c0b295cca9337e1e96b00e0";
        }
        else
        {
            url = "http://api.openweathermap.org/data/2.5/forecast?q="+location+"&units=metric"+"&appid=2de143494c0b295cca9337e1e96b00e0";
        }

        // JSON
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray list = response.getJSONArray(JSON_LIST);
                    if (list.length() > ZERO) {
                        for (int i =ZERO; i<list.length();i++) {
                            JSONObject json =list.getJSONObject(i);
                            JSONObject main = json.getJSONObject(JSON_MAIN);
                            JSONArray weather = json.getJSONArray(JSON_WEATHER);
                            JSONObject zero =weather.getJSONObject(JSON_ZERO);

                            try {
                                temp = main.getString(TEMP);
                                temp = temp.substring(SUBSTRING_temp_START,temp.indexOf(SUBSTRING_temp_END));
                                date = json.getString(DT_TXT);
                                hour = date.substring(SUBSTRING_HOUR_START, SUBSTRING_HOUR_END);
                                date = date.substring(SUBSTRING_DATE_START, SUBSTRING_DATE_END);
                                description = zero.getString(DESCRIPTION);
                                icon_name = zero.getString(ICON);

                                url_icon =" http://openweathermap.org/img/w/"+icon_name+".png";

                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.cancel();


                            item = setDataInItem(date ,hour, temp ,description,url_icon);
                            arrayList.add(item);

                            ListView.setAdapter(listAdapter);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                massage(NETWORK_TITTLE ,NETWORK_MASSAGE );
                Toast.makeText(context, FAILED_LOCATION , Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        // new list
        arrayList = new ArrayList<Item>();
        get_forecast(loction_selcted);
        // show all items
        listAdapter = new ListAdapter(MainActivity.this, arrayList);
        ListView.setAdapter(listAdapter);

    }

    private void massage(String tittle , String text){

        final AlertDialog.Builder inputAlert = new AlertDialog.Builder(get_context());
        inputAlert.setTitle(tittle);
        inputAlert.setMessage(text);
        inputAlert.setPositiveButton(MASSAGE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.cancel();
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = inputAlert.create();
        alertDialog.show();
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
