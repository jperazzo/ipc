package com.bahackaton.changuito;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuActivity extends Activity implements LocationListener{
	 
 	 private LocationManager locationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		ImageButton b1=(ImageButton) findViewById(R.id.imageButton2);
		ImageButton b2=(ImageButton) findViewById(R.id.imageButton3);
		ImageButton b3=(ImageButton) findViewById(R.id.imageButton4);
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(getApplicationContext(),ElegirProducto.class);
				startActivity(i1);
				
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent i2 = new Intent(getApplicationContext(),VerEstadisticas.class);
						startActivity(i2);
						
					}
				});

		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i3 = new Intent(getApplicationContext(),ArmarLista.class);
				startActivity(i3);
				
			}
		});
		inicializaGPS();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		  Location ubicacionActual=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		  MyProperties.getInstance().ubicacionactual=ubicacionActual; 
		  this.getAddressFromLocation(ubicacionActual, getApplicationContext(), null);
		   
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		  Location ubicacionActual=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    MyProperties.getInstance().ubicacionactual=ubicacionActual;
		   this.getAddressFromLocation(ubicacionActual, getApplicationContext(), null);
		   
	}

	
	private void inicializaGPS(){
        
	     locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
    if(locationManager != null)
    {
        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
         
        if(gpsIsEnabled)
        {
            locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria, true), 1000, 2F , this);
        }
        else if(networkIsEnabled)
        {
            locationManager.requestLocationUpdates(	LocationManager.NETWORK_PROVIDER, 20000, 200F  , this);
            
            }
        else
        {
            //Show an error dialog that GPS is disabled...
         //   Toast.makeText(this, "Los servicios de geolocalizaci—n est‡n deshabilitados se tomar‡ la œltima ubicaci–on conocida", Toast.LENGTH_SHORT);
            
        	   //Location network=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        	  Location ubicacionActual=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
             
        }
    }
    else
    {
        //Show some generic error dialog because something must have gone wrong with location manager.
    
            Toast.makeText(this, "Falla en los servicios de geolocalización", Toast.LENGTH_SHORT);
        
    }
        
   
        
 }
	
	public static void getAddressFromLocation(
	        final Location location, final Context context, final Handler handler) {
	    Thread thread = new Thread() {
	        @Override public void run() {
	            Geocoder geocoder = new Geocoder(context, Locale.getDefault());   
	            String result = null;
	            try {
	                List<Address> list = geocoder.getFromLocation(
	                        location.getLatitude(), location.getLongitude(), 1);
	                if (list != null && list.size() > 0) {
	                    Address address = list.get(0);
	                    // sending back first address line and locality
	                    result = address.getAddressLine(2) ;
	                }
	            } catch (IOException e) {
	                Log.e("app", "Impossible to connect to Geocoder", e);
	            } finally {
	             /*   Message msg = Message.obtain();
	                msg.setTarget(handler);
	                if (result != null) {
	                    msg.what = 1;
	                    Bundle bundle = new Bundle();
	                    bundle.putString("address", result);
	                    msg.setData(bundle);
	                } else 
	                    msg.what = 0;
	                msg.sendToTarget();*/
	            	
	            	MyProperties.getInstance().ciudad=   result;
	     	       
	            }
	        }
	    };
	    thread.start();
	}
	
}
