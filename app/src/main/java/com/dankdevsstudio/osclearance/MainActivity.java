package com.dankdevsstudio.osclearance;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.twigsntwines.daterangepicker.DatePickerDialog;
import com.twigsntwines.daterangepicker.DateRangePickedListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private String startDate, endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button showCalendar = findViewById(R.id.datePicker_button);

        //Initialise drop-down list
        Spinner rank_spinner = findViewById(R.id.rank_title_spinner);
        Spinner purpose_spinner = findViewById(R.id.purpose_spinner);
        Spinner poc_name_spinner = findViewById(R.id.poc_name_spinner);
        Spinner poc_rank_spinner = findViewById(R.id.poc_rank_spinner);
        Spinner vehicle_spinner = findViewById(R.id.vehicle_spinner);
        ArrayAdapter<CharSequence> rank_adapter = ArrayAdapter.createFromResource(this,
                R.array.rank_title_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> purpose_adapter = ArrayAdapter.createFromResource(this,
                R.array.purpose_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> poc_name_adapter = ArrayAdapter.createFromResource(this,
                R.array.poc_name_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> poc_rank_adapter = ArrayAdapter.createFromResource(this,
                R.array.rank_title_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> vehicle_adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_type_array, android.R.layout.simple_spinner_item);
        rank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purpose_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        poc_name_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        poc_rank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicle_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rank_spinner.setAdapter(rank_adapter);
        purpose_spinner.setAdapter(purpose_adapter);
        poc_name_spinner.setAdapter(poc_name_adapter);
        poc_rank_spinner.setAdapter(poc_rank_adapter);
        vehicle_spinner.setAdapter(vehicle_adapter);

        //Date Picker Code
        showCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager(); //Initialize fragment manager
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(); // Create datePickerDialog Instance
                datePickerDialog.show(fragmentManager,"Date Picker"); // Show DatePicker Dialog
                datePickerDialog.setOnDateRangePickedListener(new DateRangePickedListener() {
                    @Override
                    public void OnDateRangePicked(Calendar fromDate, Calendar toDate) {
                        startDate = DateFormat.format("dd/MM/yyyy", fromDate.getTime()).toString();
                        endDate = DateFormat.format("dd/MM/yyyy", toDate.getTime()).toString();
                        TextView date_textview = findViewById(R.id.date_textview);
                        date_textview.setText(startDate + " to " + endDate);
                    }});
            }

        });


    }
}
