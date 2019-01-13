package com.dankdevsstudio.osclearance;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.twigsntwines.daterangepicker.DatePickerDialog;
import com.twigsntwines.daterangepicker.DateRangePickedListener;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private Date startDate = null;
    private Date endDate = null;
    private EditText eNric, eName, eFmn, eUnit, eCompany, eHpNum, eOfficeNum, eRemarks, eVehicleNum;
    private Button showCalendar;
    private CheckBox checkVehicle;
    public static Clearance newClearance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show instructions in form
        Toast.makeText(getApplicationContext(),"Please fill up your particulars for clearance", Toast.LENGTH_LONG).show();

        //Initialise Date button
        showCalendar = findViewById(R.id.datePicker_button);

        // Initialise Form component's variable
        eNric = findViewById(R.id.nric_edittext);
        eName = findViewById(R.id.name_edittext);
        eFmn = findViewById(R.id.fmn_edittext);
        eUnit = findViewById(R.id.unit_edittext);
        eCompany = findViewById(R.id.company_edittext);
        eHpNum = findViewById(R.id.hp_num_edittext);
        eOfficeNum = findViewById(R.id.office_num_edittext);
        eRemarks = findViewById(R.id.remarks_edittext);
        checkVehicle = findViewById(R.id.checkCar);
        eVehicleNum = findViewById(R.id.vehicle_num_edittext);

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
                R.array.poc_corresponding_rank, android.R.layout.simple_spinner_item);
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
        poc_rank_spinner.setEnabled(false);
        eVehicleNum.setEnabled(false);
        vehicle_spinner.setEnabled(false);


        // Spinner activity when item is selected
        rank_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItem().toString().matches("Mr|Mrs|Miss|Ms|Dr|Prof")){
                    eFmn.setText("");
                    eUnit.setText("");
                    eFmn.setEnabled(false);
                    eUnit.setEnabled(false);
                    eCompany.setEnabled(true);
                }
                else {
                    eCompany.setText("");
                    eCompany.setEnabled(false);
                    eFmn.setEnabled(true);
                    eUnit.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        poc_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                poc_rank_spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        checkVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    eVehicleNum.setEnabled(true);
                    vehicle_spinner.setEnabled(true);
                }
                else {
                    eVehicleNum.setText("");
                    eVehicleNum.setEnabled(false);
                    vehicle_spinner.setEnabled(false);
                }
            }
        });

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
                        String sDate = DateFormat.format("dd/MM/yyyy", fromDate.getTime()).toString();
                        String eDate = DateFormat.format("dd/MM/yyyy", toDate.getTime()).toString();
                        TextView date_textview = findViewById(R.id.date_textview);
                        date_textview.setText(sDate + " to " + eDate);
                        startDate = fromDate.getTime();
                        endDate = toDate.getTime();
                    }
                });
            }
        });

        Button submitForm = findViewById(R.id.submit_button);
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateClearanceDetails()){
                    String vehicleType = "";
                    String vehicleNum = "";
                    if (checkVehicle.isChecked()){
                        vehicleType = vehicle_spinner.getSelectedItem().toString();
                        vehicleNum = eVehicleNum.getText().toString();
                    }
                    newClearance = new Clearance(
                            startDate,
                            endDate,
                            eName.getText().toString(),
                            eNric.getText().toString(),
                            rank_spinner.getSelectedItem().toString(),
                            eFmn.getText().toString(),
                            eUnit.getText().toString(),
                            eCompany.getText().toString(),
                            eHpNum.getText().toString(),
                            eOfficeNum.getText().toString(),
                            purpose_spinner.getSelectedItem().toString(),
                            eRemarks.getText().toString(),
                            poc_rank_spinner.getSelectedItem().toString(),
                            poc_name_spinner.getSelectedItem().toString(),
                            vehicleType,
                            vehicleNum
                    );
                    Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateClearanceDetails (){
        String missingFields = "";
        if ((startDate == null) || (endDate == null)){
            missingFields += "Date(s) Of Visit";
        }
        if (isEmpty(eNric)){
            eNric.setError("Required");
            if (!missingFields.equals("")){
                missingFields += ", ";
            }
            missingFields += "NRIC";
        }
        if (isEmpty(eName)){
            eName.setError("Required");
            if (!missingFields.equals("")){
                missingFields += ", ";
            }
            missingFields += "Full Name (as in NRIC)";
        }
        if (isEmpty(eHpNum)){
            eHpNum.setError("Required");
            if (!missingFields.equals("")){
                missingFields += ", ";
            }
            missingFields += "HP Number";
        }
        if (!missingFields.equals("")){
            Toast.makeText(getApplicationContext(),"Missing Fieids: " + missingFields, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
