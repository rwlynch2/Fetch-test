package com.example.fetchtest;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private RecyclerView recyclerView;
    private GroupedItemAdapter groupedItemAdapter;
    private List<RecyclerViewItem> recyclerViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems = new ArrayList<>();
        groupedItemAdapter = new GroupedItemAdapter(recyclerViewItems);
        recyclerView.setAdapter(groupedItemAdapter);

        fetchData();
    }

    private void fetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Item>>() {}.getType();
                        List<Item> items = gson.fromJson(response.toString(), listType);

                        // Filter and sort the items
                        List<Item> filteredItems = new ArrayList<>();
                        for (Item item : items) {
                            if (item.getName() != null && !item.getName().isEmpty()) {
                                filteredItems.add(item);
                            }
                        }

                        Collections.sort(filteredItems, new Comparator<Item>() {
                            @Override
                            public int compare(Item o1, Item o2) {
                                int listIdComparison = Integer.compare(o1.getListId(), o2.getListId());
                                if (listIdComparison == 0) {
                                    return extractNumber(o1.getName()) - extractNumber(o2.getName());
                                }
                                return listIdComparison;
                            }
                        });

                        recyclerViewItems.clear();
                        int currentListId = -1;
                        for (Item item : filteredItems) {
                            if (item.getListId() != currentListId) {
                                currentListId = item.getListId();
                                recyclerViewItems.add(new ListIdHeader(currentListId));
                            }
                            recyclerViewItems.add((RecyclerViewItem) item);
                        }

                        groupedItemAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "Request Failed: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private int extractNumber(String name) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return 0;
    }
}

