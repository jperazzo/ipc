package com.bahackaton.changuito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

// pantalla de carga:
// Agregar geolocalizacion
// agregar fecha actual LISTO
// agregar seleccion de fecha usando el componenteº
// agregar lugar de la compra 
// agregar tipo de lugar de compra

//menu principal
//menu de opciones  cargar un producto / ver historial 
//(de un producto, de todos en un periodo de tiempo / mostrar producto en el mapa

// ver historial
// presentar datos en un webview

// mapa
// mostrar productos y precios en el mapa

//settings
//nombre apellido y mail del usuario

public class MainActivity extends Activity {
	private static final String ACTION_SUBIR = "com.bahackaton.changuito.SUBIR";
	private static final String ACTION_CONSULTA = "com.bahackaton.changuito.CONSULTA";
	private static final String  URL_SUBIR = "http://www.idforideas.com/ipc/subir/producto";
	private static final String  URL_CONSULTA = "http://www.idforideas.com/ipc/chequear/porCodigo";
	private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		EditText txtFecha=(EditText) findViewById(R.id.editFecha);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = sdf.format(new Date());
		txtFecha.setText(currentDate);
		ImageButton agregar=(ImageButton) findViewById(R.id.btnAgregar);
		
		
		EditText txtDescripcion=(EditText) findViewById(R.id.editDescripcion);
		txtDescripcion.setText( 	MyProperties.getInstance().productoseleccionado);
		EditText txtCodigo=(EditText) findViewById(R.id.editCodigo);
	  	 
		txtCodigo.setText(MyProperties.getInstance().codigoProducto);
	        
		agregar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		  		
				insertarDato();
				
			}
		});
		
		ImageButton b=(ImageButton) findViewById(R.id.imageButton2);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
				integrator.initiateScan();	
			}
		});
		//consultarCodigoBarra();
	/*	Button bt=(Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				consultarCodigoBarra();
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void insertarDato(){
		final String url = URL_SUBIR;
  	  HttpPost httpPost = new HttpPost(url);
		
  	try{
  		     
  	 	//httpPost.setHeader("Content-type", "application/json");
  	      
  	 	EditText txtFecha=(EditText) findViewById(R.id.editFecha);
  	 	EditText txtCodigo=(EditText) findViewById(R.id.editCodigo);
  	 	EditText txtDescripcion=(EditText) findViewById(R.id.editDescripcion);
  	 	EditText txtPrecio=(EditText) findViewById(R.id.editPrecio);
  	 	
  	 	float lat, lon;
  	 	
  	 	lat=0.0f;
  	 	lon=0.0f;
  	 	
  	 	
  	 	List<NameValuePair> parameters = new ArrayList<NameValuePair>(); 
  		parameters.add(new BasicNameValuePair("codigo",txtCodigo.getText().toString())); 
  		parameters.add(new BasicNameValuePair("descripcion",txtDescripcion.getText().toString()));
  		parameters.add(new BasicNameValuePair("precio",txtPrecio.getText().toString()));
  		parameters.add(new BasicNameValuePair("fecha",txtFecha.getText().toString()));
  		parameters.add(new BasicNameValuePair("lat","0.0"));
  		parameters.add(new BasicNameValuePair("lng","0.0"));
  		
  		parameters.add(new BasicNameValuePair("usuario_id",this.getPhoneId()));
  		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
      
			RestTask task = new RestTask(this,ACTION_SUBIR );
			 progress = ProgressDialog.show(this, "Changuito", "Enviando datos...",
			    	 true);
			task.execute(httpPost);
		} catch (Exception ex) {
			ex.printStackTrace();
			}
	}
	
	
	
	public void consultarCodigoBarra(){
		final String url = URL_CONSULTA;
  	  HttpPost httpPost = new HttpPost(url);
		
  	try{
  		     
  	 	//httpPost.setHeader("Content-type", "application/json");
  	      
  	 		EditText txtCodigo=(EditText) findViewById(R.id.editCodigo);
  	 	
  	 
		 /* JSONObject jsonObject = new JSONObject();
          jsonObject.put("codigo",txtCodigo.toSg);
          jsonObject.put("descripcion",txtDescripcion);
          jsonObject.put("precio",txtPrecio);
          jsonObject.put("fecha",txtFecha);
          jsonObject.put("lat",lat);
          jsonObject.put("lng",lon);
            
          httpPost.setEntity(new StringEntity(jsonObject.toString())); */
  	 	
  	 	List<NameValuePair> parameters = new ArrayList<NameValuePair>(); 
  		parameters.add(new BasicNameValuePair("codigo",txtCodigo.getText().toString())); 
  		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
      
			RestTask task = new RestTask(this,ACTION_CONSULTA );
			 progress = ProgressDialog.show(this, "Changuito", "Consultando datos...",
			    	 true);
			task.execute(httpPost);
		} catch (Exception ex) {
			ex.printStackTrace();
			}
	}
	@Override
	public void onResume() {
	super.onResume();
	registerReceiver(receiver, new IntentFilter(ACTION_SUBIR));
	registerReceiver(receiver, new IntentFilter(ACTION_CONSULTA));
	}
	@Override
	public void onPause() {
	super.onPause();
	unregisterReceiver(receiver);
	 
	
	}
	
	@Override
	public void onStop(){
		super.onStop();
	
		
		
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() { 
		@Override
		public void onReceive(Context context, Intent intent) { 
			if(progress != null) {
				progress.dismiss();
				}
			String response = intent.getStringExtra(RestTask.HTTP_RESPONSE); 
			//Process the response data (here we just display it)
			if (intent.getAction().equalsIgnoreCase(ACTION_SUBIR)){
			
				  Intent intent2 = new Intent(context, MenuActivity.class);
	                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// This flag ensures all activities on top of the CloseAllViewsDemo are cleared.
	                context.startActivity(intent2);
	 			
			}
			
		
			//Process the response data (here we just display it)
			if (intent.getAction().equalsIgnoreCase(ACTION_CONSULTA)){
			
	 			
			}
		} 
	};

	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
		    // handle scan result
			  EditText txtCodigo=(EditText) findViewById(R.id.editCodigo);
		  	  txtCodigo.setText(scanResult.getContents().toString());
			  
		  }
		  // else continue with any other code you need in the method
		
		}
	
	
	public String getPhoneId(){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String deviceId = deviceUuid.toString();
	    
	    return  deviceId;
	}
}
