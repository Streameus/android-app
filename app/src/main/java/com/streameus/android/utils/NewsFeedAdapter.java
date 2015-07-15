package com.streameus.android.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paging.listview.PagingBaseAdapter;
import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.EventItem;

import java.util.List;

public class NewsFeedAdapter extends PagingBaseAdapter<EventItem> {

    Context mContext;
    LayoutInflater mInflater;

    private List<EventItem> eventList;

    public static class ViewHolder {
        public ImageView userPicture;
        public TextView PrimaryText;
        public TextView secondaryText;
        public TextView timeText;
        public TextView cardBanner_legend;
        public ImageView coverPicture;

    }

    public NewsFeedAdapter(Context context, LayoutInflater inflater,
                           List<EventItem> itemList) {
        eventList = itemList;
        mInflater = inflater;
        mContext = context;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public EventItem getItem(int arg0) {
        return eventList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return eventList.get(arg0).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.newsfeed_listitem_material, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();

            holder.userPicture = (ImageView) convertView.findViewById(R.id.UserPicture);
            holder.PrimaryText = (TextView) convertView.findViewById(R.id.primaryText);
            holder.secondaryText = (TextView) convertView.findViewById(R.id.secondaryText);
            holder.timeText = (TextView) convertView.findViewById(R.id.timeText);
            holder.cardBanner_legend = (TextView) convertView.findViewById(R.id.cardBanner_legend);
            holder.coverPicture = (ImageView) convertView.findViewById(R.id.cardBannerImage);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just getConferenceList the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        EventItem event = getItem(position);


        // .placeholder(R.drawable.ic_books)
//        holder.textViewTitle.setText(event.getFormatedContent());
//        holder.textViewDate.setText(Dates.parse3339ToReadable(event.getDate()));

        holder.PrimaryText.setText(event.getPrimaryText());

        holder.secondaryText.setText(event.getSecondaryText());
        holder.timeText.setText(event.getTimeString());
        holder.cardBanner_legend.setText(event.getBannerLegend());


        Picasso.with(mContext)
                .load(RESTClient.API_URL + "/Picture/User/" + event.getAuthorId())
                .transform(new CircleTransform())
                .placeholder(R.drawable.default_avatar)
                .into(holder.userPicture);

        Picasso.with(mContext)
                .load(event.getBannerImageURI())
                .placeholder(R.drawable.image_loading)
                .into(holder.coverPicture);

        return convertView;
    }
}
