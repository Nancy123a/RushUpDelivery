package me.zeroandone.technology.rushup.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.zeroandone.technology.rushup.R;
import me.zeroandone.technology.rushup.objects.BalanceObject;
import me.zeroandone.technology.rushup.objects.HistoryObject;

public class BalanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<BalanceObject> result;
    Context context;


    public BalanceAdapter(List<BalanceObject> result, Context context) {
        this.result = result;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_row, parent, false);
        return new BalanceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View View;
        TextView pickup_point,dollars;
        public ViewHolder(View view) {
            super(view);
            pickup_point=(TextView) view.findViewById(R.id.pickup_point);
            dollars=(TextView) view.findViewById(R.id.dollars);
            View = view;
        }
    }
}
