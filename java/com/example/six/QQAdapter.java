package com.example.six;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QQAdapter extends RecyclerView.Adapter<QQAdapter.ViewHolder> {

    private ArrayList<QQData> dataList;
    public QQAdapter(ArrayList<QQData> dataList){
        this.dataList = dataList;
    }

    public interface OnItemClickListener{
        void OnItemClick(QQData data);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView gName;
        TextView uName;
        TextView content;
        ImageView icon;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gName = itemView.findViewById(R.id.g_name);
            uName = itemView.findViewById(R.id.u_name);
            content = itemView.findViewById(R.id.ct);
            icon = itemView.findViewById(R.id.icon);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int pos = getAdapterPosition();
                   QQData data = dataList.get(pos);

                   listener.OnItemClick(data);
               }
           });
        }

        public void bind(QQData data){
            gName.setText(data.getgName());
            uName.setText(data.getuName());
            content.setText(data.getContent());
            icon.setImageResource(data.getIcon());

        }
    }
}
