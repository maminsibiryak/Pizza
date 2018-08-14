package com.example.andrej.pizza;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "my_log";
    private ArrayList<String> strings = new ArrayList<String>();
    private Product product;

    private HashMap<String, ArrayList<Product>> stringProductHashMap = new HashMap<>();

    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),
                     "Подключаемся",
                    Toast.LENGTH_LONG).show();
        }


        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                //URL url = new URL("https://api.androidhive.info/contacts/");
                URL url = new URL("http://amopizza.ru/goods2.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                inputStream.close();
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);

            try {
                JSONArray jsonObject = new JSONArray(strJson);


                for (int i = 0; i < jsonObject.length(); i++) {
                    ArrayList<Product> productArrayList = new ArrayList<>();

                    JSONObject object = jsonObject.getJSONObject(i);
                    String name = object.getString("gr_name");
                    Log.d(LOG_TAG, name + " ");
                    strings.add(name);


                    JSONArray products = object.getJSONArray("products");


                    for (int j = 0; j < products.length(); j++) {

                        JSONObject productsJSONObject = products.getJSONObject(j);
                        String productName = productsJSONObject.getString("name");
                        String productId = productsJSONObject.getString("id");
                        String img_url = productsJSONObject.getString("img_url");
                        String desc = productsJSONObject.getString("desc");

                        Log.d(LOG_TAG, productName + " " + productId + " " + img_url + " " + desc);

                        HashMap<String, String> productSize = new HashMap<>();
                        JSONArray size = productsJSONObject.getJSONArray("size_price");
                        for (int k = 0; k < size.length(); k++) {
                            JSONObject size_price = size.getJSONObject(k);
                            String s = size_price.getString("size");
                            String p = size_price.getString("price");
                            if (!s.equals("null")){
                            productSize.put(s, p+" руб.");}
                            else {
                                productSize.put("Цена за 1 ед.",p+" руб.");
                            }
                            Log.d(LOG_TAG, "size: " + s + " " + "price:  " + p);
                        }
                        product = new Product(productId, productName, img_url, desc, productSize);
                        productArrayList.add(product);
                    }

                    stringProductHashMap.put(name, productArrayList);


                }


                expListView = findViewById(R.id.expListView);


                final List<String> title = new ArrayList<>(stringProductHashMap.keySet());
                expListAdapter = new MyListAdapter(MainActivity.this, title, stringProductHashMap);

                expListView.setAdapter(expListAdapter);

                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                title.get(groupPosition) + " Список раскрыт.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                title.get(groupPosition) + " Список скрыт.",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                       Toast.makeText(getApplicationContext(),
                               title.get(groupPosition)
                                        + " : " + stringProductHashMap.get(title.get(groupPosition))
                                        .get(childPosition), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });


                /*ListView listView = findViewById(R.id.listView);
                MyListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strings);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        for (Map.Entry<String, ArrayList<Product>> p : stringProductHashMap.entrySet()
                                ) {
                            if (strings.get(i).equals(p.getKey())) {
                                for (int j = 0; j < p.getValue().size(); j++) {
                                    System.out.println(p.getValue().get(j));

                                }
                            }
                        } //не удалять!

                    }

                });*/


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

