package com.herokuapp.ezhao.codepathinstagram;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emily on 2/7/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvRelativeCreatedTime;
        TextView tvLikes;
        TextView tvCaption;
        TextView tvViewComments;
        TextView tvLastComments;
        ImageView ivPhoto;
        RoundedImageView rivUserPhoto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto photo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvRelativeCreatedTime = (TextView) convertView.findViewById(R.id.tvRelativeCreatedTime);
            viewHolder.tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvViewComments = (TextView) convertView.findViewById(R.id.tvViewComments);
            viewHolder.tvLastComments = (TextView) convertView.findViewById(R.id.tvLastComments);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.rivUserPhoto = (RoundedImageView) convertView.findViewById(R.id.rivUserPhoto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set username and relative created time
        viewHolder.tvUsername.setText(photo.getUsername());
        viewHolder.tvRelativeCreatedTime.setText(DateUtils.getRelativeTimeSpanString(photo.getCreatedTime() * 1000));

        // Set caption
        if (photo.getCaption() != null) {
            viewHolder.tvCaption.setText(Html.fromHtml(String.format("<b>%s</b> %s", photo.getUsername(), photo.getCaption())));
        } else {
            viewHolder.tvCaption.setVisibility(View.GONE);
        }

        // Set likes
        viewHolder.tvLikes.setText(Html.fromHtml(String.format(getContext().getResources().getString(R.string.placeholder_likes), NumberFormat.getIntegerInstance().format(photo.getLikeCount()))));

        // Set comments and comment count
        ArrayList<InstagramPhotoComment> photoComments = photo.getComments();
        viewHolder.tvLastComments.setText("");
        boolean firstComment = true;
        for (int i = Math.max(0, photoComments.size()-2); i < photoComments.size(); i++) {
            InstagramPhotoComment currentComment = photoComments.get(i);
            if (!firstComment) {
                viewHolder.tvLastComments.append(Html.fromHtml("<br/>"));
            } else {
                firstComment = false;
            }
            viewHolder.tvLastComments.append(Html.fromHtml(String.format("<b>%s</b> %s", currentComment.getUsername(), currentComment.getText())));
        }
        if (photo.getCommentCount() > 2) {
            viewHolder.tvViewComments.setText(String.format(getContext().getResources().getString(R.string.view_more_comments_placeholder),
                    photo.getCommentCount()));
        } else {
            viewHolder.tvViewComments.setVisibility(View.GONE);
        }

        // Load photos async using picasso "with load into"
        viewHolder.ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl()).placeholder(R.drawable.loading).into(viewHolder.ivPhoto);
        viewHolder.rivUserPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getUserImageUrl()).into(viewHolder.rivUserPhoto);

        return convertView;
    }
}
