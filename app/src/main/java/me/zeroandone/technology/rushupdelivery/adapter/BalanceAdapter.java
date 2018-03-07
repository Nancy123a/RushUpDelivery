package me.zeroandone.technology.rushupdelivery.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.objects.BalanceObject;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;

public class BalanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<DeliveryRequest> result;
    Context context;
    RushUpDeliverySettings rushUpDeliverySettings;

    public BalanceAdapter(List<DeliveryRequest> result, Context context, RushUpDeliverySettings rushUpDeliverySettings) {
        this.result = result;
        this.context = context;
        this.rushUpDeliverySettings=rushUpDeliverySettings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.balance_row, parent, false);
        return new BalanceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
      DeliveryRequest deliveryRequest=result.get(position);
      String pickup_location=deliveryRequest.getPickupLocation().getName()+" \n delivered";
        ((ViewHolder)holder).pickup_point.setText(pickup_location);
        ((ViewHolder) holder).go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rushUpDeliverySettings!=null){
                    rushUpDeliverySettings.onBalanceHistoryRowClicked(result.get(position),false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View View;
        TextView pickup_point,dollars;
        RelativeLayout go;
        public ViewHolder(View view) {
            super(view);
            pickup_point=(TextView) view.findViewById(R.id.pickup_point);
            dollars=(TextView) view.findViewById(R.id.dollars);
            go=(RelativeLayout) view.findViewById(R.id.go);
            View = view;
        }
    }

    public void RefreshItems(List<DeliveryRequest> deliveryRequests){
        this.result=deliveryRequests;
        notifyDataSetChanged();
    }
}
