package com.example.kalk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	Button btnViewProducts;
    Button btnNewProduct;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
        btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
		 btnViewProducts.setOnClickListener(new View.OnClickListener() {
			 
	            @Override
	            public void onClick(View view) {
	                // Запускаем Activity вывода всех продуктов
	                Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
	                startActivity(i);
	 
	            }
	        });
	 
	        // обработчик на нажатия кнопки Add New Products
	        btnNewProduct.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View view) {
	                // Запускаем Activity создания нового продукта
	                Intent i = new Intent(getApplicationContext(), NewProductActivity.class);
	                startActivity(i);
	 
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
