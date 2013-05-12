package com.bahackaton.changuito;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class ElegirProducto extends Activity {
	 AlertDialog.Builder categorias;
	 AlertDialog alert;
	 EditText producto;
	 private ProgressDialog progress;
		private static final String ACTION_CONSULTA = "com.bahackaton.changuito.CONSULTA";
		private static final String  URL_CONSULTA = "http://www.idforideas.com/ipc/ver/productos";

		
		List<Producto> lista;
		
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_elegir_producto);
	    categorias = new AlertDialog.Builder(this);
		
	    
	    lista=new ArrayList<Producto>();
	    
	    
	  ImageButton btnbuscar=(ImageButton) findViewById(R.id.imageButton4);
	
		producto=(EditText) findViewById(R.id.editProducto);
		
		btnbuscar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
				final CharSequence[] items=new CharSequence[lista.size()] ;
				
				for (int i=0; i<lista.size();i++){
					items[i]= lista.get(i).producto;
				}
				
				
				categorias.setTitle("Seleccione el producto");				
				categorias.setItems(items,  new DialogInterface.OnClickListener()
				{
	   	            public void onClick(DialogInterface dialog, int item)
	   	            {
	   	            	
	   	            	MyProperties.getInstance().codigoProducto=lista.get(item).id;
	   	            	MyProperties.getInstance().productoseleccionado=String.valueOf(items[item]);
	   	            	 producto.setText(items[item]);
	   	            	 alert.dismiss();
	   	            	
	   	            	if (item==0){
	   	            	 	
	   	            		MyProperties.getInstance().productoseleccionado="";
	   	            			   	           
	   	            		Intent intent=new Intent(getApplicationContext(),MainActivity.class);
	   	            		startActivity(intent);
	   	            	}
	   	            	//Intent map = new Intent(getApplicationContext(),Mapa.class);
	   					//startActivity(map);
	   						
	   	                //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
	   	            }
				});
				alert = categorias.create();
        	    alert.show(); 
				
			}
		});
		
		
		ImageButton b=(ImageButton )findViewById(R.id.imageButton2);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MyProperties.getInstance().productoseleccionado=producto.getText().toString();
				//MyProperties.getInstance().productoseleccionado=producto.getText().toString();
				
				// TODO tomar datos del producto ingresado y pasar a la carga
				// la segunda vez tiene que aparecer este producto y pasar a la pantalla siguiente
				Intent intent=new Intent(getApplicationContext(),MainActivity.class);
           		startActivity(intent);
				
			}
		});
		
		cargarProductos();
		
		ImageButton b2=(ImageButton) findViewById(R.id.imageButton3);
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				cerrarVentana();
			}
		});
	}

	 void cerrarVentana(){
		 this.finish();
	 }
	 void cargarProductos(){
		 // lee los productos del servicio
			final String url = URL_CONSULTA;
		  	 HttpPost httpPost = new HttpPost(url);
				
		  	try{
		  		     
		  	  		EditText txtCodigo=(EditText) findViewById(R.id.editCodigo);
		  	 	
		   	  	 	List<NameValuePair> parameters = new ArrayList<NameValuePair>(); 
		   	  	 	//parameters.add(new BasicNameValuePair("codigo",txtCodigo.getText().toString())); 
		   	  	 	httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		      
					RestTask task = new RestTask(this,ACTION_CONSULTA );
					 progress = ProgressDialog.show(this, "Changuito", "Buscando productos ...",
					    	 true);
					task.execute(httpPost);
				} catch (Exception ex) {
					ex.printStackTrace();
					}
	 }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.elegir_producto, menu);
		return true;
	}
	@Override
	public void onResume() {
	super.onResume();
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
	 	 	if (intent.getAction().equalsIgnoreCase(ACTION_CONSULTA)){
				JSONArray productos;
				try {
					productos = new JSONArray(response);
				
				
				for (int s = 0;s<productos.length();s++) 
				{
					Producto p= new Producto();
					JSONObject jo=productos.getJSONObject(s);
					
					p.producto=jo.getString("producto");
					p.id=jo.getString("id");
					p.precio=jo.getString("precio");
					 
					
					lista.add(p);
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
	};

	
	
}
