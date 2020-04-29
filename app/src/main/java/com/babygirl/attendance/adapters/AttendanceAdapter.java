package com.babygirl.attendance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.babygirl.attendance.objects.Attendance;
import com.babygirl.attendance.R;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{
    private ArrayList<Attendance> attendances;
    private Context context;

    public AttendanceAdapter(Context context, ArrayList<Attendance> attendances){
        this.context = context;
        this.attendances = attendances;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row_attendace,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.user_mail_textView.setText(attendances.get(position).getUser_mail());
        holder.date_textView.setText(attendances.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView user_mail_textView;
        TextView date_textView;
        ConstraintLayout mainLayout;

        MyViewHolder(@NonNull View itemView){
            super(itemView);
            user_mail_textView = itemView.findViewById(R.id.user_mail_textView);
            date_textView = itemView.findViewById(R.id.date_textView);
            mainLayout = itemView.findViewById(R.id.mainLayoutRowAttendance);
        }
    }

}
