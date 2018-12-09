package com.uki121.hguidetemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TosAdapter extends RecyclerView.Adapter<TosAdapter.ViewHolder> {
private Context mContext;
private List<String> mDataSet;

public TosAdapter(Context context, List<String> list){
        mContext = context;
        mDataSet = list;
        }

public static class ViewHolder extends RecyclerView.ViewHolder{
    public TextView mContentOfTos;

    public ViewHolder(View v){
        super(v);
        // Get the widget reference from the custom layout
        mContentOfTos = (TextView) v.findViewById(R.id.content_tos);
    }
}

    @Override
    public TosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.content_scroll_item,parent,false);

        // Get the TextView reference from RecyclerView current item
        final TextView titleOfTos = (TextView) v.findViewById(R.id.content_tos);

        // Set a click listener for the current item of RecyclerView
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the RecyclerView current item text
                final String label = titleOfTos.getText().toString();

                // Display the RecyclerView clicked item label
                Toast.makeText(
                        mContext,
                        "Clicked : " + label,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        // Get the current animal from the data set
        String title = mDataSet.get(position);

        // Set the TextView widgets text
        holder.mContentOfTos.setText(title);
    }

    @Override
    public int getItemCount(){
        // Count the items
        return mDataSet.size();
    }
}