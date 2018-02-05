package me.zeroandone.technology.rushupdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.HistoryObject;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    List<DeliveryRequest> result;
    Context context;
    RushUpDeliverySettings rushUpDeliverySettings;


    public HistoryAdapter(List<DeliveryRequest> result, Context context, RushUpDeliverySettings rushUpDeliverySettings) {
        this.result = result;
        this.context = context;
        this.rushUpDeliverySettings=rushUpDeliverySettings;
    }


    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new HistoryAdapter.HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryViewHolder holder, int position) {
        DeliveryRequest deliveryRequest=result.get(position);
        if(deliveryRequest!=null){
            if(deliveryRequest.getPickupLocation()!=null && deliveryRequest.getPickupLocation().getName()!=null){
                holder.pickup_point.setText(deliveryRequest.getPickupLocation().getName());
            }
            if(deliveryRequest.getDropoffLocation()!=null && deliveryRequest.getDropoffLocation().getName()!=null){
                holder.delivery_point.setText(deliveryRequest.getDropoffLocation().getName());
            }
            if(deliveryRequest.getDeliveryDate()!=null) {
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm:ss aaa");
                Date dateD=new Date(deliveryRequest.getDeliveryDate()*1000);
                String date=dateFormat.format(dateD);
                String time=timeFormat.format(dateD);
                String completeDate=date+" \n "+time;
                holder.otherifo.setText(completeDate);
            }
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        View View;
        TextView pickup_point,delivery_point,otherifo;
        public HistoryViewHolder(View view) {
            super(view);
            pickup_point=(TextView) view.findViewById(R.id.pickup_point);
            delivery_point=(TextView) view.findViewById(R.id.delivery_point);
            otherifo=(TextView) view.findViewById(R.id.otherinfo);
            View = view;
        }
    }


    public void RefreshItems(List<DeliveryRequest> deliveryRequests){
        this.result=deliveryRequests;
        notifyDataSetChanged();
    }
}
