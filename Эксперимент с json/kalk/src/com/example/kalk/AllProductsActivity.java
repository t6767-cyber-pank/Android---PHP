package com.example.kalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.kalk.JSONParser;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class AllProductsActivity extends ListActivity {
	 
    private ProgressDialog pDialog;
 
    // ������� JSON ������
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> productsList;
 
    // url ��������� ������ ���� ���������
    private static String url_all_products = "http://poks.org.kg/exp/get_all_products.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
 
    // ��� ����� �������� ������ ���������
    JSONArray products = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_products);
 
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // ��������� �������� � ������� ������
        new LoadAllProducts().execute();
 
        // �������� ListView
        ListView lv = getListView();
 
        // �� ����� ������ ��������
        // ����������� Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
 
                // ��������� ����� intent ������� ������� ��� Activity
                Intent in = new Intent(getApplicationContext(), EditProductActivity.class);
                // ���������� pid � ��������� activity
                in.putExtra(TAG_PID, pid);
                // �������� ����� Activity ������� ����� �������
                startActivityForResult(in, 100);
            }
        });
 
    }
 
    // ����� �� Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ���� ������������ ��� ����� 100
        if (resultCode == 100) {
            // ���� ���������� ��� ���������� ����� 100
            // ������ ������������ ����������� ��� ������ �������
            // ����� �� ������������� ���� �����
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * ������� Async Task ��� �������� ���� ��������� �� HTTP �������
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
 
        /**
         * ����� ������� �������� ������ Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllProductsActivity.this);
            pDialog.setMessage("�������� ���������. ���������...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * �������� ��� ������� �� url
         * */
        protected String doInBackground(String... args) {
            // ����� ������� ���������
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // �������� JSON ����� � URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
 
            Log.d("All Products: ", json.toString());
 
            try {
                // �������� SUCCESS ��� ��� �������� ������� ������ �������
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // ������� ������
                    // �������� ����� �� ���������
                    products = json.getJSONArray(TAG_PRODUCTS);
 
                    // ������� ���� ���������
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
 
                        // ��������� ������ json ������� � ����������
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
 
                        // ������� ����� HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // ��������� ������ ������� � HashMap ���� => ��������
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
 
                        // ��������� HashList � ArrayList
                        productsList.add(map);
                    }
                } else {
                    // ������� �� ������
                    // ��������� Add New Product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // �������� ���� ���������� activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * ����� ���������� ������� ������ ��������� ������� ������
         * **/
        protected void onPostExecute(String file_url) {
            // ��������� �������� ������ ����� ��������� ��� ���������
            pDialog.dismiss();
            // ��������� UI ����� � ������� ������
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * ��������� ������������ JSON ������ � ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // ��������� listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
 
}