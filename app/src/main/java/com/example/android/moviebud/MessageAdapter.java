package com.example.android.moviebud;

import android.app.Activity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Siddhesh on 11/23/2017.
 */

public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    public Activity activity;
    public List<ChatMessage> list;
    public MessageAdapter(Activity activity, int resource, List<ChatMessage> list){
        super(activity,resource,list);
        this.activity=activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int resource=0;
        ChatMessage message = getItem(position);
        int viewType=message.getId();
        if(viewType==1){
            resource = R.layout.my_message;
        }
        else if(viewType==2){
            resource = R.layout.their_message;
        }
        else if(viewType==4){
            resource = R.layout.video;
        }
        else{
            resource=R.layout.horizontal_list;
        }
        convertView=inflater.inflate(resource,parent,false);
        holder=new ViewHolder(convertView);
        convertView.setTag(holder);
        holder.msg.setText(message.getData());
        if(message.isURL()){
            Linkify.addLinks(holder.msg,Linkify.WEB_URLS);
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime. Value 2 is returned because of left and right views.
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        //list.get(position);
        return position%4;
    }

    public class ViewHolder {
        public TextView msg;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.message_body);
        }
    }
}
