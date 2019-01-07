package com.dankdevsstudio.osclearance;

import android.os.AsyncTask;
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
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import com.twigsntwines.daterangepicker.DatePickerDialog;
import com.twigsntwines.daterangepicker.DateRangePickedListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private Date startDate, endDate;
    private int difference;
    private String path;

    // Initialise Dropbox API
    private static final String ACCESS_TOKEN = "C2J6RumI-TAAAAAAAAAEfptj92rX3bOqVMTWm8TBG5__GRg1MpZyGUVLBrL0g0gx";
    DbxRequestConfig config;
    DbxClientV2 client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise Dropbox client
        initClient();

        //Initialise Date button
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
                        String sDate = DateFormat.format("dd/MM/yyyy", fromDate.getTime()).toString();
                        String eDate = DateFormat.format("dd/MM/yyyy", toDate.getTime()).toString();
                        TextView date_textview = findViewById(R.id.date_textview);
                        date_textview.setText(sDate + " to " + eDate);
                        startDate = fromDate.getTime();
                        endDate = toDate.getTime();
                        difference = ((int)((endDate.getTime()/(24*60*60*1000)) - (int)(startDate.getTime()/(24*60*60*1000)))) + 1;
                    }
                });
            }
        });

        Button submitForm = findViewById(R.id.submit_button);
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateExcel(difference);
                new testDropbox().execute();
            }
        });
    }
    // Generate xls file using APACHE POI
    private void generateExcel(int noOfRows){
        Log.i("DebugLog: ", "test");
        try {
            InputStream myInput;
            File myFile;
            FileOutputStream fileOut;

            //  Open xls file from Assets folder
            myInput = getApplicationContext().getAssets().open("template.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            //iterating r number of rows
            for (int r=2;r < 5; r++ )
            {
                HSSFRow row = mySheet.createRow(r);

                //iterating c number of columns
                for (int c=0;c < 5; c++ )
                {
                    HSSFCell cell = row.createCell(c);

                    cell.setCellValue("Cell "+r+" "+c);
                }
            }

            myFile = new File(getApplicationContext().getFilesDir(),"random.xls");
            fileOut = new FileOutputStream(myFile);
            path = myFile.getAbsolutePath();

            Log.i("DebugLog: ", path);
            //write this workbook to an Outputstream.
            myWorkBook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            Log.i("DebugLog: ", "test");

        } catch (Exception e) {
            Log.e("DebugLog: ", e.getMessage());
            e.printStackTrace();
        }
        return;
    }

    public class testDropbox extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... params) {

            try {
                InputStream in = new FileInputStream(path);
                FileMetadata metadata = client.files().uploadBuilder("/random.xls")
                        .uploadAndFinish(in);
                Log.e("DebugLog: ", metadata.toString());
                return null;
            }
            catch(Exception e) {
                Log.e("DebugLog: ", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

        }
    }
    // Initialise Dropbox client
    private void initClient(){
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }
}
