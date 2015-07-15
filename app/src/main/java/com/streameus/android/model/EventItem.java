package com.streameus.android.model;

import android.text.format.Time;

import com.streameus.android.dataProvider.RESTClient;
import com.streameus.android.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Pol on 09/07/14.
 */
public class EventItem {
    private int Id;
    private long Type; // (string, optional) = ['BilanCreateConfMonth' or 'BilanCreateConfWeek' or 'BilanFollowers' or 'BilanFollowingMonth' or 'BilanFollowingWeek' or 'BilanParticipateConfMonth' or 'BilanParticipateConfWeek' or 'BilanReputation' or 'CreateConf' or 'FirstFollower' or 'ParticipateConf' or 'RateConf' or 'StartFollow' or 'SuscribeConf' or 'UpdateMsgPerso' or 'ConferenceStart' or 'WillParticipateConf' or 'ConferenceEnd' or 'UserInvitation' or 'Recommand' or 'ShareConf' or 'ConfTitle' or 'ConfDate' or 'ConfTime' or 'ChangePhoto' or 'ConfDescription']: Type of event,
    private String Date;
    private int AuthorId;
    private String Content;
    private List<EventInfo> Items;
    private long timestamp = 0;

    private String secondaryText = null;

    public static class EventItemComparator implements Comparator<EventItem> {
        @Override
        public int compare(EventItem o1, EventItem o2) {
            long out = -(o1.getTimestamp() - o2.getTimestamp());
            if (out < 0)
                return -1;
            if (out == 0)
                return 0;
            return 1;
        }
    }

    public long getTimestamp() {
        if (timestamp == 0) {
            Time t = new Time();
            t.parse3339(Date);
            timestamp = t.toMillis(false);
        }
        return timestamp;
    }

    public String getFormatedContent() {
        String out = new String(Content);
        for (int i = 0; i < this.Items.size(); ++i) {
            out = out.replaceFirst("\\{" + i + "\\}", this.Items.get(i).getContent());
        }
        return out;
    }



    public int getId() { return Id;}

    public void setId(int id) {
        Id = id;
    }

    public long getType() {
        return Type;
    }

    public void setType(long type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(int authorId) {
        AuthorId = authorId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<EventInfo> getItems() {
        return Items;
    }

    public void setItems(List<EventInfo> items) {
        Items = items;
    }


    public String getPrimaryText() {
        return Items.get(0).getContent();
    }

    public String getSecondaryText() {
        if (secondaryText == null) {
            String originalContent = getContent();
            int posMarker1 = originalContent.indexOf("{1}");
            if (posMarker1 != -1) {
                originalContent = originalContent.substring(0, posMarker1);
            }
            originalContent = originalContent.replace("{0}", "");
            if (originalContent.charAt(0) == ' ') {
                originalContent = originalContent.substring(1);
            }
            secondaryText = originalContent.substring(0,1).toUpperCase() + originalContent.substring(1);
        }

        return secondaryText;
    }


    public String getTimeString() {

//        String dtStart = "2010-10-15T09:27:37Z";
        String s = "error";
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            java.util.Date date = format.parse(this.Date);
            s = DateUtils.diffTime(date, new java.util.Date());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            s = "error";
        }

        return s;
    }

    public String getBannerImageURI() {
        return RESTClient.API_URL + "/Picture/" +  (Items.get(1).getTargetType() == EventInfo.TYPE_USER ? "User" : "Conference") + "/" + Items.get(1).getTargetId();
    }

    public String getBannerLegend() {
        return Items.get(1).getContent();
    }

}
