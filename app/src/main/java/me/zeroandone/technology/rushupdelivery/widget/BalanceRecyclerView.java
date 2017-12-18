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
import me.zeroandone.technology.rushupdelivery.adapter.BalanceAdapter;
import me.zeroandone.technology.rushupdelivery.objects.BalanceObject;


public class BalanceRecyclerView extends RecyclerView {

    Context context;

    public BalanceRecyclerView(Context context) {
        super(context);
        this.context=context;
    }

    public BalanceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public BalanceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    public void setAdapter(BalanceRecyclerView RV){
        if(RV!=null && context!=null) {
            List<BalanceObject> Balancelist=new ArrayList<>();
            BalanceObject obj=new BalanceObject("Aley","15$");
            Balancelist.add(obj);
            BalanceAdapter menu_adapter = new BalanceAdapter(Balancelist, context);
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
