package com.dankdevsstudio.osclearance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ConfirmationActivity extends AppCompatActivity {

    ListView listView;

    // Initialise Dropbox API V2
    private static final String ACCESS_TOKEN = "Ocph8LRnf-AAAAAAAAAACoqmAUuRSf1H4ommBgvjjHBWOKIf8AlpK5Co9mIu6ARM";
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private String path;
    private String fileName;

    // Declare variables for clearance details
    private String nric;
    private String name;
    private String rank;
    private String formation;
    private String unit;
    private String company;
    private String hpNum;
    private String officeNum;
    private String purpose;
    private String remarks;
    private String pocRank;
    private String pocName;
    private String vehicleType;
    private String vehicleNum;
    private Date startDate;
    private Date endDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        // Toast to display confirmation instructions
        Toast.makeText(getApplicationContext(),"Please check and confirm your clearance details", Toast.LENGTH_LONG).show();

        // Initialise variables for clearance details
        startDate = MainActivity.newClearance.getStartDate();
        endDate = MainActivity.newClearance.getEndDate();
        rank = MainActivity.newClearance.getRank();
        name = MainActivity.newClearance.getName();
        nric = MainActivity.newClearance.getNric();
        formation = MainActivity.newClearance.getFormation();
        unit = MainActivity.newClearance.getUnit();
        company = MainActivity.newClearance.getCompany();
        hpNum = MainActivity.newClearance.getHpNum();
        officeNum = MainActivity.newClearance.getOfficeNum();
        purpose = MainActivity.newClearance.getPurpose();
        remarks = MainActivity.newClearance.getRemarks();
        pocName = MainActivity.newClearance.getPocName();
        pocRank = MainActivity.newClearance.getPocRank();
        vehicleType = MainActivity.newClearance.getVehicleType();
        vehicleNum = MainActivity.newClearance.getVehicleNum();

        // Add clearance details into ListView
        listView = findViewById(R.id.confirmListView);
        LinkedHashMap<String, String> displayList = new LinkedHashMap<>();
        displayList.put(getString(R.string.start_date), DateFormat.format("dd/MM/yyyy", startDate.getTime()).toString());
        displayList.put(getString(R.string.end_date), DateFormat.format("dd/MM/yyyy", endDate.getTime()).toString());
        displayList.put(getString(R.string.rank), rank);
        displayList.put(getString(R.string.name), name);
        displayList.put(getString(R.string.nric), nric);
        displayList.put(getString(R.string.fmn), formation);
        displayList.put(getString(R.string.unit), unit);
        displayList.put(getString(R.string.company), company);
        displayList.put(getString(R.string.hp_num), hpNum);
        displayList.put(getString(R.string.office_num), officeNum);
        displayList.put(getString(R.string.purpose), purpose);
        displayList.put(getString(R.string.remarks), remarks);
        displayList.put(getString(R.string.poc_name), pocName);
        displayList.put(getString(R.string.poc_rank), pocRank);
        displayList.put(getString(R.string.vehicle_type), vehicleType);
        displayList.put(getString(R.string.vehicle_num), vehicleNum);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        Iterator it = displayList.entrySet().iterator();
        while (it.hasNext())
        {
            String value = "NIL";
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultsMap.put("First Line", pair.getKey().toString() + ": ");
            if (!pair.getValue().toString().equals(""))
                value = pair.getValue().toString();
            resultsMap.put("Second Line", value);
            listItems.add(resultsMap);
        }

        listView.setAdapter(adapter);

        final Button amendButton = findViewById(R.id.amendButton);
        final Button confirmButton = findViewById(R.id.confirmButton);
        amendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new testDropbox().execute();
            }
        });
    }

    // Generate xls file using APACHE POI
    private void generateExcel(){
        try {
            // Declare/Initialise variables
            InputStream myInput;
            File myFile;
            FileOutputStream fileOut;
            String currentDateTime = DateFormat.format("ddMMyy_HHmmss", Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"))).toString();
            String currentRank = rank;
            String currentName = name.replace(" ", "_");
            fileName = currentDateTime + "_" + currentRank + "_" + currentName + ".xls";
            int noOfRows = ((int)((endDate.getTime()/(24*60*60*1000)) - (int)(startDate.getTime()/(24*60*60*1000)))) + 2;
            Date incrementDate = startDate;
            Calendar calendar = Calendar.getInstance();

            //  Open xls file from Assets folder
            myInput = getApplicationContext().getAssets().open("template.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            //iterating r number of rows
            for (int r = 2; r < noOfRows; r++)
            {
                HSSFRow row = mySheet.createRow(r);
                row.createCell(0).setCellValue(incrementDate);
                row.createCell(1).setCellValue(nric);
                row.createCell(2).setCellValue(rank);
                row.createCell(3).setCellValue(name);
                row.createCell(4).setCellValue(formation);
                row.createCell(5).setCellValue(unit);
                row.createCell(6).setCellValue(company);
                row.createCell(7).setCellValue(hpNum);
                row.createCell(8).setCellValue(officeNum);
                row.createCell(9).setCellValue(purpose);
                row.createCell(10).setCellValue(remarks);
                row.createCell(11).setCellValue(pocRank);
                row.createCell(12).setCellValue(pocName);
                row.createCell(13).setCellValue(vehicleType);
                row.createCell(14).setCellValue(vehicleNum);

                // Date increment for the next row
                calendar.setTime(incrementDate);
                calendar.add(Calendar.DATE, 1);
                incrementDate = calendar.getTime();
            }

            // Write this workbook to internal storage.
            myFile = new File(getApplicationContext().getFilesDir(),fileName);
            fileOut = new FileOutputStream(myFile);
            path = myFile.getAbsolutePath();
            myWorkBook.write(fileOut);
            fileOut.flush();
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class testDropbox extends AsyncTask<Void, Void, String> {
        private ProgressDialog mDialog;

        protected void onPreExecute() {
            // Initialise Dropbox client
            initClient();

            mDialog = new ProgressDialog(ConfirmationActivity.this);
            mDialog.setMessage("Please wait...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        protected String doInBackground(Void... params) {
            if (isOnline()){
                generateExcel();
                try {
                    InputStream in = new FileInputStream(path);
                    FileMetadata metadata = client.files().uploadBuilder("/" + fileName)
                            .uploadAndFinish(in);
                    File file = new File(getFilesDir(), fileName);
                    file.delete();
                    Log.i("DebugLog: ", metadata.toString());
                    return null;
                }
                catch(Exception e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }
            else {
                return "No Internet Connection";
            }
        }

        protected void onPostExecute(String error) {
            mDialog.dismiss();
            if (error == null) {
                finishAffinity();
                Intent intent = new Intent(ConfirmationActivity.this, SuccessActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    public boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    // Initialise Dropbox client
    private void initClient(){
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }
}
