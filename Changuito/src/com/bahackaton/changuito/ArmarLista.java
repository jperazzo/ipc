package com.bahackaton.changuito;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ArmarLista extends Activity {
	 MyCustomAdapter dataAdapter = null;
	 
	 private ProgressDialog progress;
		private static final String ACTION_CONSULTA = "com.bahackaton.changuito.CONSULTA";
		private static final String  URL_CONSULTA = "http://www.idforideas.com/ipc/ver/productos";

		
		List<Producto> lista;
		ListView lv;
		EditText et;
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_armar_lista);
		 lista=new ArrayList<Producto>();
		 //Generate list View from ArrayList
		
		  
		  cargarProductos();
		   lv = (ListView) findViewById(R.id.listView1);
		  lv.setTextFilterEnabled(true);
		  
		 
		  EditText myFilter = (EditText) findViewById(R.id.editText1);
		  myFilter.addTextChangedListener(new TextWatcher() {
		 
		  public void afterTextChanged(Editable s) {
		  }
		 
		  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		  }
		 
		  public void onTextChanged(CharSequence s, int start, int before, int count) {
		   dataAdapter.getFilter().filter(s.toString());
		  }
		  });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.armar_lista, menu);
		return true;
	}


	 private void displayListView() {
	 
	   
		  //create an ArrayAdaptar from the String Array
	  dataAdapter = new MyCustomAdapter(this,
	    R.layout.lista, (ArrayList<Producto>) lista);
	  ListView listView = (ListView) findViewById(R.id.listView1);
	  // Assign adapter to ListView
	  listView.setAdapter(dataAdapter);
	 
	 
	  listView.setOnItemClickListener(new OnItemClickListener() {
	   public void onItemClick(AdapterView<?> parent, View view,
	     int position, long id) {
	    // When clicked, show a toast with the TextView text
	    Producto country = (Producto) parent.getItemAtPosition(position);
	  //  Toast.makeText(getApplicationContext(),
	   //   "Clicked on Row: " + country.getName(), 
	    //  Toast.LENGTH_LONG).show();
	   }
	  });
	 
	 }
	 

	 private class MyCustomAdapter extends ArrayAdapter<Producto> {
	 
	  private ArrayList<Producto> countryList;
	 
	  public MyCustomAdapter(Context context, int textViewResourceId, 
	    ArrayList<Producto> countryList) {
	   super(context, textViewResourceId, countryList);
	   this.countryList = new ArrayList<Producto>();
	   this.countryList.addAll(countryList);
	  }
	 
	  private class ViewHolder {
	   TextView code;
	   CheckBox name;
	  }
	 
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	 
	   ViewHolder holder = null;
	 //  Log.v("ConvertView", String.valueOf(position));
	 
	   if (convertView == null) {
	   LayoutInflater vi = (LayoutInflater)getSystemService(
	     Context.LAYOUT_INFLATER_SERVICE);
	   convertView = vi.inflate(R.layout.lista, null);
	 
	   holder = new ViewHolder();
	   holder.code = (TextView) convertView.findViewById(R.id.code);
	   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
	   convertView.setTag(holder);
	 
	    holder.name.setOnClickListener( new View.OnClickListener() {  
	     public void onClick(View v) {  
	      CheckBox cb = (CheckBox) v ;  
	      Producto producto = (Producto) cb.getTag();  
	      //Toast.makeText(getApplicationContext(),
	       //"Clicked on Checkbox: " + cb.getText() +
	       //" is " + cb.isChecked(), 
	       //Toast.LENGTH_LONG).show();
	      producto.isSelected=cb.isChecked();
	     
	      calcularPrecio();
	      
	     }  
	    });  
	   } 
	   else {
	    holder = (ViewHolder) convertView.getTag();
	   }
	 
	   Producto prod = countryList.get(position);
	   holder.code.setText("$ "+	prod.precio  );
	   holder.name.setText(	   prod.producto );
	   holder.name.setChecked(false);
	   holder.name.setTag(prod);
	 
	   return convertView;
	 
	  }
	 
	 }
	 
	 
	 void calcularPrecio(){
		 TextView precio=(TextView) findViewById(R.id.textView1);
	      precio.setText("");
	      float pre=0f;
	      for (int i=0;i<lista.size();i++){
	    	  
	    	  Producto p=lista.get(i);
	    	  
	    	  
	    	  if(p.isSelected!=null){
	    		  if(p.isSelected){
	    		  pre=pre+Float.parseFloat(p.precio);
	    		  }
	    	  }
	    	  
	      }
	      
	      precio.setText("$ "+String.valueOf(pre));
		 
	 }
	 
	 
	 private void checkButtonClick() {
	 
	 
	/*  Button myButton = (Button) findViewById(R.id.findSelected);
	  myButton.setOnClickListener(new OnClickListener() {
	 
	   @Override
	   public void onClick(View v) {*/
	 
	    StringBuffer responseText = new StringBuffer();
	    responseText.append("The following were selected...\n");
	 
	    ArrayList<Producto> countryList = dataAdapter.countryList;
	    for(int i=0;i<countryList.size();i++){
	    	Producto producto = countryList.get(i);
	     if(producto.isSelected ){
	      responseText.append(  producto.producto);
	     }
	    }
	 
	    Toast.makeText(getApplicationContext(),
	      responseText, Toast.LENGTH_LONG).show();
	 
	   }
	  //});
	 

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
					
					  displayListView();
					  
					  //checkButtonClick();
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			} 
		};
		
		
}
