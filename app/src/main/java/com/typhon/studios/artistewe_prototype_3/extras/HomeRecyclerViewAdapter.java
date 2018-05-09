package com.typhon.studios.artistewe_prototype_3.extras;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.typhon.studios.artistewe_prototype_3.R;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

private ArrayList<String> values;

        RecyclerViewAdapter(ArrayList<String> values) {
            this.values = values;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_single_item,parent,false));
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(values.get(position));
        //test
        }

@Override
public int getItemCount() {
        return values.size();
        }

class ViewHolder extends RecyclerView.ViewHolder {
    private TextView name;
    private TextView body;
    ViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.cardNameTextview);
        body = (TextView) itemView.findViewById(R.id.cardBodyTextview);
    }
}
}
