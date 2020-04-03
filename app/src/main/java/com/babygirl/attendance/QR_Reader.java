package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.babygirl.attendance.HelpFuncs;

public class QR_Reader extends AppCompatActivity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;


    private String extract_name_curr_user(){
        // get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            return email.replaceAll("((@.*)|[^a-zA-Z])+", " ").trim();
        }
        return "";
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__reader);

        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        textView = (TextView) findViewById(R.id.textView_qr);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try{
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size()!=0){

                    textView.post(new Runnable() {
                        @Override
                        public void run() {

                            //--------aici e problema--------------------------------------------

                            final String result_code = qrCodes.valueAt(0).displayValue; //value of qr code
                            //getting only the part until "_"
                            int iend = result_code.indexOf("_");
                            String subString="";
                            if(iend != -1) {
                                subString = result_code.substring(0, iend);
                            }

                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(70);

                            //verifying if the code is valid
                            if(subString!="")
                            {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("Courses/");
                                final String finalSubString = subString;
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                                            if(finalSubString!="" && child.getKey().equals(finalSubString))
                                            {
                                                // Log.d("debug_baby", child.getKey().toString());
                                                // Log.d("debug_baby", result_code);
                                                // Log.d("debug_baby", child.child("DQRC").getValue().toString());

                                                if(result_code.equals(child.child("DQRC").getValue().toString())) //check if DQRC is the same as in the db

                                                {

                                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                    Date date = new Date();
                                                    String strDate = dateFormat.format(date).toString();
                                                    Log.d("debug_baby", strDate);

                                                    //DatabaseReference myRef = database.getReference("Courses/"+child.getKey()+"/attendances/"+strDate+"_"+extract_name_curr_user());
                                                    //myRef.setValue(true);
                                                    DatabaseReference myRef = database.getReference("Courses/"+child.getKey()+"/attendances/"+HelpFuncs.getAlphaNumeric(6)+"/date"+strDate);
                                                    myRef.setValue(true);
                                                    myRef = database.getReference("Courses/"+child.getKey()+"/attendances/"+HelpFuncs.getAlphaNumeric(6)+"/user_mail"+HelpFuncs.extract_curr_user_mail());
                                                    myRef.setValue(true);

                                                    textView.setText("Success");
                                                    finish();
                                                }

                                                else
                                                {
                                                    textView.setText("Not a valid code.");
                                                }

                                            }

                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.d("debug_baby", "Failed to read value.", error.toException());
                                    }
                                });
                            }
                            else
                            {
                                textView.setText("Not a valid code.");
                            }

                        }

                    });

                }

            }
        });

    }
}
