package me.zeroandone.technology.rushupdelivery.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.Settings;
import me.zeroandone.technology.rushupdelivery.utils.NotificationSound;

public class SettingsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Settings> settings;
    Context _context;
    NotificationSound notificationSound;

    public SettingsAdapter(Context context, List<Settings> settings) {
        this.settings = settings;
        this._context = context;
        notificationSound=new NotificationSound(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (settings != null) {
            Settings setting = settings.get(position);
            if (setting != null) {
                return setting.getType();
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Settings.HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            return new HeaderView(view);
        } else if (viewType == Settings.OPTION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option, parent, false);
            return new OptionView(view);
        } else if (viewType == Settings.Notification) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
            return new NotificationView(view);
        }

        return null; // This shouldn't return null in any case or error
        // will be thrown.
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Settings setting = settings.get(position);
        if (setting != null) {
            if (setting.getType() == Settings.HEADER) {
                configureHeaderView((HeaderView) viewHolder, position);
            } else if (setting.getType() == Settings.OPTION) {
                configureOptionView((OptionView) viewHolder, position);
            } else if (setting.getType() == Settings.Notification) {
                configureNotificationView((NotificationView) viewHolder, position);
            }
        }
    }

    private void configureOptionView(OptionView vh1, final int position) {
        vh1.option.setText(settings.get(position).getOptions());
        vh1.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = settings.get(position).getOptions();
                if(option.equalsIgnoreCase(_context.getResources().getString(R.string.first_name))){

                }
            }
        });
    }

    private void configureHeaderView(HeaderView vh1, int position) {
        vh1.header.setText(settings.get(position).getTitle());
    }

    private void configureNotificationView(NotificationView vh1, int position) {
        vh1.notification.setText(settings.get(position).getOptions());
        if(notificationSound.getNotificationsound()){
            vh1.onoff.setChecked(true);
        }
        else{
            vh1.onoff.setChecked(false);
        }
        vh1.onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notificationSound.saveNotificationsound(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    public static class HeaderView extends RecyclerView.ViewHolder {
        TextView header;

        public HeaderView(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.headerText);
        }
    }

    public static class OptionView extends RecyclerView.ViewHolder {
        TextView option;
        ImageView optionclick;
        View view;

        public OptionView(View itemView) {
            super(itemView);
            view = itemView;
            option = (TextView) itemView.findViewById(R.id.option);
            optionclick = (ImageView) itemView.findViewById(R.id.optionclick);
        }
    }

    public static class NotificationView extends RecyclerView.ViewHolder {
        TextView notification;
        SwitchCompat onoff;
        View view;

        public NotificationView(View itemView) {
            super(itemView);
            view = itemView;
            notification = (TextView) itemView.findViewById(R.id.notification);
            onoff = (SwitchCompat) itemView.findViewById(R.id.on_off);

        }
    }



}
