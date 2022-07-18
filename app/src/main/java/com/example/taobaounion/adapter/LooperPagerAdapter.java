package com.example.taobaounion.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataDTO> mDataList = new ArrayList<>();
    private OnLooperPagerItemClickListener mLooperPagerItemClickListener;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mDataList.size();
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        int ivSize = (Math.max(measuredHeight, measuredWidth)) / 2;
        HomePagerContent.DataDTO data = mDataList.get(realPosition);
        String imgUrl = UrlUtils.getCoverPath(data.getPict_url(), ivSize);
        ImageView imageView = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(imgUrl).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLooperPagerItemClickListener != null) {
                    mLooperPagerItemClickListener.onLooperItemClick(mDataList.get(realPosition));
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataDTO> contents) {
        mDataList.clear();
        mDataList.addAll(contents);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return mDataList.size();
    }

    public void setOnLooperPagerItemClickListener(OnLooperPagerItemClickListener looperPagerItemClickListener){
        mLooperPagerItemClickListener = looperPagerItemClickListener;
    }

    public interface OnLooperPagerItemClickListener{
        void onLooperItemClick(HomePagerContent.DataDTO item);
    }
}
