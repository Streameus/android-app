package com.streameus.android.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.streameus.android.R;
import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.model.IConference;

import java.util.List;

public class IConferenceAdaptater extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;

    private List<? extends IConference> conferenceCollection;

    public static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView sousTitreTextview;
    }

    public IConferenceAdaptater(Context context, LayoutInflater inflater,
                                List<? extends IConference> conferenceList) {
        conferenceCollection = conferenceList;
        mInflater = inflater;
        mContext = context;
    }

    @Override
    public int getCount() {
        return conferenceCollection.size();
    }

    @Override
    public IConference getItem(int arg0) {
        return conferenceCollection.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return conferenceCollection.get(arg0).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.listitem_conference, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            holder.titleTextView = (TextView) convertView
                    .findViewById(R.id.TextViewContent);
            holder.sousTitreTextview = (TextView) convertView.findViewById(R.id.TextViewDate);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just getConferenceList the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        IConference conference = getItem(position);

        // See if there is a cover ID in the Object

        // Use Picasso to load the image
        // Temporarily have a placeholder in case it's slow to load
        // Picasso.with(mContext).load(imageURL)
        // .placeholder(R.drawable.ic_books)
        // .into(holder.UserPicture);
        holder.titleTextView.setText(conference.getName());
        holder.sousTitreTextview.setText(Dates.parse3339ToReadable(conference.getDate()));
        Picasso.with(mContext)
                .load(RESTClient.API_URL + "/Picture/Conference/" + conference.getId())
                .placeholder(R.drawable.default_avatar)
                .into(holder.thumbnailImageView);

        return convertView;
    }
}
