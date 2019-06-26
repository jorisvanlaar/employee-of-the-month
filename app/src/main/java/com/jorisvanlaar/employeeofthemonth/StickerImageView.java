package com.jorisvanlaar.employeeofthemonth;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class StickerImageView extends StickerView {

    private ImageView mImageView;

    public StickerImageView(Context context) {
        super(context);
    }

    public StickerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (this.mImageView == null) {
            this.mImageView = new ImageView(getContext());
            this.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return mImageView;
    }

    public void setImageDrawable(Drawable drawable) {
        this.mImageView.setImageDrawable(drawable);
    }
}
