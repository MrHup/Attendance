package com.babygirl.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.glxn.qrgen.android.QRCode;

public class QR_Generator extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__generator);

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            String course_id =(String) b.get("COURSE_ID");
            // find DQRC from course_id
            final String path = "Courses/" + course_id + "/DQRC";
            DatabaseReference myRef = database.getReference(path);
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d("debug_firebase", "Value of new DQRC for path:'" + path + "' is: " + value);

                    // update image
                    Bitmap myBitmap = QRCode.from(value).bitmap();
                    ImageView myImage = (ImageView) findViewById(R.id.qr_image);
                    myImage.setImageBitmap(myBitmap);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("debug_firebase", "Failed to read value.", error.toException());
                }
            });


        }else{
            Log.d("debug_baby","Issue on receive course_id through intent [QR Generator]");
        }
    }
}
