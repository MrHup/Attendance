package com.babygirl.attendance.ui.presence_card;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.babygirl.attendance.AttendanceListStudent;
import com.babygirl.attendance.HelpFuncs;
import com.babygirl.attendance.QR_Reader;
import com.babygirl.attendance.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PresenceCardFragment extends Fragment {
    SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private PresenceCardViewModel presenceCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        presenceCardViewModel =
                ViewModelProviders.of(this).get(PresenceCardViewModel.class);
        View root = inflater.inflate(R.layout.activity_qr__reader, container, false);

        /*Intent i = new Intent(getContext(), QR_Reader.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);*/

        /// IMPLEMENTATION OF ACTUAL QR READER
        surfaceView = (SurfaceView) root.findViewById(R.id.camerapreview);
        textView = (TextView) root.findViewById(R.id.textView_qr);

        barcodeDetector = new BarcodeDetector.Builder(root.getContext())
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(root.getContext(),barcodeDetector)
                .setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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

                            Vibrator vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
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

                                                    String identifier = HelpFuncs.md5(HelpFuncs.extract_curr_user_mail() + strDate); // hash the value to avoid data dupes
                                                    DatabaseReference myRef = database.getReference("Courses/"+child.getKey()+"/attendances/"+identifier+"/date");
                                                    myRef.setValue(strDate);
                                                    myRef = database.getReference("Courses/"+child.getKey()+"/attendances/"+identifier+"/user_mail");
                                                    myRef.setValue(HelpFuncs.extract_curr_user_mail());

                                                    textView.setText("Success");
                                                    // get out of fragment
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

        return root;
    }
}