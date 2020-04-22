package com.babygirl.attendance;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder>{
    private ArrayList<Course> courses;
    private Context context;
    private Boolean deals_with_generator;

    public CourseAdapter(Context context, ArrayList<Course> courses, Boolean deals_with_generator){
        this.context = context;
        this.courses = courses;
        this.deals_with_generator = deals_with_generator;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_course,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.course_title_textView.setText(courses.get(position).getCourse_name());
        holder.target_year_textView.setText("Target year: "+courses.get(position).getTarget_year());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deals_with_generator){
                    Intent i= new Intent(context, QR_Generator.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // get course_id from DQRC
                    String DQRC = courses.get(position).getDQRC();
                    int iend = DQRC.indexOf("_");
                    String subString = DQRC.substring(0 , iend);
                    i.putExtra("COURSE_ID", subString);
                    context.startActivity(i);
                }else{
                    // get all attendances, put them in a list
                    String DQRC = courses.get(position).getDQRC();
                    int iend = DQRC.indexOf("_");
                    String course_id = DQRC.substring(0 , iend); //id of course

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String user_mail = user.getEmail();

                    DatabaseReference userIdRef = FirebaseDatabase.getInstance().getReference("Courses").child(course_id);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot attendancesSnapshot = dataSnapshot.child("attendances");
                            Iterable<DataSnapshot> attendances_list = attendancesSnapshot.getChildren();
                            for(DataSnapshot attendance: attendances_list){
                                Attendance currentAttendance = attendance.getValue(Attendance.class);
                                if(currentAttendance.getUser_mail().equals(user_mail))
                                {
                                    Log.d("debug_querry", "Attendance: " + currentAttendance.getUser_mail() + " | " +currentAttendance.getDate());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    userIdRef.addListenerForSingleValueEvent(eventListener);


                }

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("debug_recylcer",String.valueOf(courses.size()));
        return courses.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder{
        TextView course_title_textView;
        TextView target_year_textView;
        ConstraintLayout mainLayout;

        MyViewHolder(@NonNull View itemView){
            super(itemView);
            course_title_textView = itemView.findViewById(R.id.course_title);
            target_year_textView = itemView.findViewById(R.id.target_year);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}
