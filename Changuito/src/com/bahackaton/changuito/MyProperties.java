package com.bahackaton.changuito;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public class MyProperties {

	private static MyProperties mInstance=null;
	
	protected MyProperties(){}
	
	public ArrayList<Producto > listaProductos;
	
	public String idsesion;
	
	public String productoseleccionado;
	public String codigoProducto;
	public Location ubicacionactual;
	public String ciudad;
	
	public static synchronized MyProperties getInstance(){
		
		if (null==mInstance){
			
			mInstance=new MyProperties();
			
		}
		return mInstance;
		
	}
}
