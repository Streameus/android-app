package com.streameus.android.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.streameus.android.R;

public class MenuAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;

    private int[][] menuItems;

    public static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
    }

    public MenuAdapter(Context context, LayoutInflater inflater,
                       int[][] menuItems) {
        this.menuItems = menuItems;
        mInflater = inflater;
        mContext = context;
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public int[] getItem(int arg0) {
        return menuItems[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        return menuItems[arg0][2];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.menu_list_item, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView
                    .findViewById(R.id.drawer_menuItem_image);
            holder.titleTextView = (TextView) convertView
                    .findViewById(R.id.drawer_menuItem_texte);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just getConferenceList the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        // Get the current book's data in JSON form
        int[] menuItem = getItem(position);

        // See if there is a cover ID in the Object

        // Use Picasso to load the image
        // Temporarily have a placeholder in case it's slow to load
        // Picasso.with(mContext).load(imageURL)
        // .placeholder(R.drawable.ic_books)
        // .into(holder.UserPicture);
        holder.titleTextView.setText(mContext.getString(menuItem[1]));
        holder.thumbnailImageView.setImageResource(menuItem[0]);

        return convertView;
    }


}
