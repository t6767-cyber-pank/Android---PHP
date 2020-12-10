package com.example.kalk;

import java.util.ArrayList;
import java.util.List;

import com.example.kalk.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProductActivity extends Activity {

    EditText txtName;
    EditText txtPrice;
    EditText txtDesc;
    Button btnSave;
    Button btnDelete;

    String pid;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url ��� ��������� ������ ��������
    private static final String url_product_detials = "http://poks.org.kg/exp/get_product_details.php";

    // url ��� ���������� ��������
    private static final String url_update_product = "http://poks.org.kg/exp/update_product.php";

    // url ��� �������� ��������
    private static final String url_delete_product = "http://poks.org.kg/exp/delete_product.php";

    // JSON ���������
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        // ���������� ����� ��� ��������� ���������� � ��������
        Intent i = getIntent();

        // �������� id �������� (pid) � �����
        pid = i.getStringExtra(TAG_PID);

        // ��������� ������ ���������� � �������� � ������� ������
        new GetProductDetails().execute();

        // ��������� �� ������ ���������� ��������
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ��������� ���������� ������ �� ���������� ��������
                new SaveProductDetails().execute();
            }
        });

        // ���������� �� ������ �������� ��������
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ������ ������� � ������� ������
                new DeleteProduct().execute();
            }
        });

    }

    /**
     * ������� ����������� ������ ��� ��������� ������ ���������� � ��������
     **/
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * ����� ������� �������� � ������� ������ �������� ������
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();  
        }

        /**
         * ��������� ��������� ���������� � �������� � ������� ������
         **/
        protected String doInBackground(String... args) {
        	 int success;
        	 List<NameValuePair> params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("pid", pid));
             // �������� ������� �� HTTP �������
             JSONObject json = jsonParser.makeHttpRequest(url_product_detials, "GET", params);
             Log.d("Single Product Details", json.toString());      	 
             try {
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
                    // ������� ��������� ��������� ���������� � ��������
                    JSONArray productObj = json.getJSONArray(TAG_PRODUCT);

                    // �������� ������ ������ � JSON Array
                    JSONObject product = productObj.getJSONObject(0);

                    // ������� � pid ������
                    // Edit Text
                    txtName = (EditText) findViewById(R.id.inputName);
                    txtPrice = (EditText) findViewById(R.id.inputPrice);
                    txtDesc = (EditText) findViewById(R.id.inputDesc);

                    // ��������� ������ � �������� � EditText
                      txtName.setText(product.getString(TAG_NAME).toString());
                      txtPrice.setText(product.getString(TAG_PRICE).toString());
                      txtDesc.setText(product.getString(TAG_DESCRIPTION).toString());

                }else{
                    // ������� � pid �� ������
                }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 return null;
        }

        /**
         * ����� ���������� ������� ������ ��������� ������ ��������
         **/
        protected void onPostExecute(String file_url) {
            // ��������� ������ ��������
             pDialog.dismiss();
        }
    }

    /**
     * � ������� ������ ��������� ���������� ������ �� ���������� ��������
     **/
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * ����� ������� ���������� � ������� ������ �������� ������
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * ��������� �������
         **/
        protected String doInBackground(String[] args) {

            // �������� ����������� ������ � EditTexts
            String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();

            // ��������� ���������
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

            // ���������� ���������� ������ ����� http ������
            JSONObject json = jsonParser.makeHttpRequest(url_update_product, "POST", params);

            // ��������� json success ���
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // ������� ������ �������
                    Intent i = getIntent();
                    // ���������� �������������� ��� 100 ����� �������� �� ���������� ��������
                    setResult(100, i);
                    finish();
                } else {
                    // ������� �� ��������
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * ����� ��������� ��������� �������� ������
         **/
        protected void onPostExecute(String file_url) {
            // ��������� �������� ������
            pDialog.dismiss();
        }
    }

    /**
     * ������� ����������� ������ �� �������� ��������
     **/
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * �� ������ ���������� �������� ������
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("������ �������...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * �������� ��������
         **/
        protected String doInBackground(String[] args) {

            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // ��������� �������� ��������� HTTP ������
                JSONObject json = jsonParser.makeHttpRequest(url_delete_product, "POST", params);

                Log.d("Delete Product", json.toString());

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // ������� ������ ������
                    Intent i = getIntent();
                    // ���������� �������������� ��� 100 ��� ����������� �� �������� ��������
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * ����� ��������� �������� �������� ������
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

        }

    }

}
