package com.dankdevsstudio.osclearance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        // Toast to display confirmation instructions
        Toast.makeText(getApplicationContext(),"Please confirm your clearance details", Toast.LENGTH_LONG).show();

        listView = findViewById(R.id.confirmListView);
        HashMap<String, String> displayList = new HashMap<>();
        displayList.put(getString(R.string.name), MainActivity.newClearance.getName());
        displayList.put(getString(R.string.rank), MainActivity.newClearance.getRank());
        displayList.put(getString(R.string.nric), MainActivity.newClearance.getNric());

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});


        Iterator it = displayList.entrySet().iterator();
        while (it.hasNext())
        {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        listView.setAdapter(adapter);
    }
}
