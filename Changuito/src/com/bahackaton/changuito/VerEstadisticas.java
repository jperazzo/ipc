package com.bahackaton.changuito;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class VerEstadisticas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ver_estadisticas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ver_estadisticas, menu);
		return true;
	}

}
