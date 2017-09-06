package com.priyo.go.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.makeramen.roundedimageview.RoundedImageView;
import com.priyo.go.R;
import com.priyo.go.Utilities.CircleTransform;


public class ProfileImageView extends FrameLayout {
    private Context context;

    private RoundedImageView mProfilePictureView;
    private Drawable placeHolderImage;

    public ProfileImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ProfileImageView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;

        View v = inflate(context, R.layout.profile_image_view, null);

        mProfilePictureView = (RoundedImageView) v.findViewById(R.id.portrait);
        mProfilePictureView.setOval(true);
        placeHolderImage = getResources().getDrawable(R.mipmap.ic_profile);
        addView(v);
    }

    public void setOval(boolean isOval) {
        mProfilePictureView.setOval(isOval);
        mProfilePictureView.setCornerRadius(2);
    }

    public void setProfilePicture(int photoResourceId) {
        Drawable drawable = context.getResources().getDrawable(photoResourceId);
        mProfilePictureView.setImageDrawable(drawable);
    }

    public void setPlaceHolder(int resourceId) {
        placeHolderImage = getResources().getDrawable(resourceId);
    }

    public void setProfilePicture(String photoUri, boolean forceLoad) {
        try {
            final DrawableTypeRequest<String> glide = Glide.with(context).load(photoUri);

            glide
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            if (forceLoad) {
                glide
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())));
            }

            glide
                    .placeholder(placeHolderImage)
                    .error(placeHolderImage)
                    .crossFade()
                    .dontAnimate();
            if (mProfilePictureView.isOval()) {
                glide.transform(new CircleTransform(context));
            }
            glide.into(mProfilePictureView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setProfilePicture(Bitmap tempBitmap) {
        mProfilePictureView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

}
