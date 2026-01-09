package com.example.six;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class vpAdapter extends RecyclerView.Adapter<vpAdapter.InnerViewHolder> {

    private ArrayList<String> data;

    public vpAdapter(ArrayList<String> data) {
        this.data = data;
    }




    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp2_item,parent,false);

        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {

        holder.bind(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class InnerViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.title);
        }
        public void bind(String data){
            text.setText(data);
        }
    }
}
