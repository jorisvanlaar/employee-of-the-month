package com.jorisvanlaar.employeeofthemonth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import java.io.File;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private File[] mImagesFolder;
    private static int mImageWidth, mImageHeight;

    public ImageAdapter(File[] imagesFolder, int imageWidth, int imageHeight) {
        mImagesFolder = imagesFolder;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ImageView imageView = new ImageView(viewGroup.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImageWidth, mImageHeight);
        imageView.setLayoutParams(params);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder imageViewHolder, int i) {
        final File imageFile = mImagesFolder[i];

        Glide.with(imageViewHolder.getImageView().getContext())
                .load(imageFile)
                .into(imageViewHolder.getImageView());

        imageViewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openFullScreenIntent = new Intent(imageViewHolder.getImageView().getContext(), FullScreenImageActivity.class);
                openFullScreenIntent.putExtra("image",imageFile.getAbsolutePath());
                imageViewHolder.getImageView().getContext()
                        .startActivity(openFullScreenIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImagesFolder.length;
    }
}
