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
