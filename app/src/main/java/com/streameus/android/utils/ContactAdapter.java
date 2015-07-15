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
import com.streameus.android.model.User;

import java.util.List;

public class ContactAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;

    private List<User> userCollection;

    public static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
    }

    public ContactAdapter(Context context, LayoutInflater inflater,
                          List<User> UserCollection) {
        userCollection = UserCollection;
        mInflater = inflater;
        mContext = context;
    }

    @Override
    public int getCount() {
        return userCollection.size();
    }

    @Override
    public User getItem(int arg0) {
        return userCollection.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return userCollection.get(arg0).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.listitem_contact, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            holder.titleTextView = (TextView) convertView
                    .findViewById(R.id.textView1);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just getConferenceList the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        // Get the current book's data in JSON form
        User user = getItem(position);

        // See if there is a cover ID in the Object

        // Use Picasso to load the image
        // Temporarily have a placeholder in case it's slow to load
        // Picasso.with(mContext).load(imageURL)
        // .placeholder(R.drawable.ic_books)
        // .into(holder.UserPicture);
        holder.titleTextView.setText(user.getPseudo());
        Picasso.with(mContext)
                .load(RESTClient.API_URL + "/Picture/User/" + user.getId())
                .transform(new CircleTransform())
                .placeholder(R.drawable.default_avatar)
                .into(holder.thumbnailImageView);

        return convertView;
    }


}
