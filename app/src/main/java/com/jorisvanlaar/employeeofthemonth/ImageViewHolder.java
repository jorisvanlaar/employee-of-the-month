package com.jorisvanlaar.employeeofthemonth;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class ImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public ImageViewHolder(View view) {
        super(view);

        imageView = (ImageView) view;
    }

    public ImageView getImageView() {
        return imageView;
    }
}

