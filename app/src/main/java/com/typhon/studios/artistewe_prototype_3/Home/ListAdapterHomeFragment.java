package com.typhon.studios.artistewe_prototype_3.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.typhon.studios.artistewe_prototype_3.extras.*;
import com.typhon.studios.artistewe_prototype_3.R;

public class ListAdapterHomeFragment extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_single_item,parent,false);
        return new HomeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HomeListViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return randomData.names.length;
    }

    private class HomeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView myNameTextView;
        private TextView myBodyTextView;

        public HomeListViewHolder(View itemView) {
            super(itemView);
            myNameTextView = (TextView)itemView.findViewById(R.id.cardNameTextview);
            myBodyTextView = (TextView)itemView.findViewById(R.id.cardBodyTextview);
            itemView.setOnClickListener(this);

        }

        public void bindView(int position){
            myNameTextView.setText(randomData.names[position]);
            myBodyTextView.setText(randomData.body[position]);
        }

        public void onClick(View view){

        }
    }
}
