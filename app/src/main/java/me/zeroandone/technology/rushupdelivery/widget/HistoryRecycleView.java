package me.zeroandone.technology.rushupdelivery.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.adapter.HistoryAdapter;
import me.zeroandone.technology.rushupdelivery.objects.HistoryObject;

public class HistoryRecycleView extends RecyclerView {
    Context context;

    public HistoryRecycleView(Context context) {
        super(context);
        this.context=context;
    }

    public HistoryRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public HistoryRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }


    public void setAdapter(HistoryRecycleView RV){
        if(RV!=null && context!=null) {
            List<HistoryObject> Historylist=new ArrayList<>();
            HistoryObject obj=new HistoryObject("Aley","29-11-2017","11:00:59 AM","15$","Bchamoun");
            Historylist.add(obj);
            HistoryAdapter menu_adapter = new HistoryAdapter(Historylist, context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
            RV.setLayoutManager(linearLayoutManager);
            RV.setItemAnimator(new DefaultItemAnimator());
            DividerItemDecoration horizontalDecoration = new DividerItemDecoration(RV.getContext(), DividerItemDecoration.VERTICAL);
            Drawable horizontalDivider = ContextCompat.getDrawable(context, R.drawable.contactdivider);
            horizontalDecoration.setDrawable(horizontalDivider);
            RV.addItemDecoration(horizontalDecoration);
            RV.setAdapter(menu_adapter);
        }
    }
}
