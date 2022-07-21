package com.example.taobaounion.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.HomePagerContent;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.ViewHolder> {

    private List<HomePagerContent.DataDTO> mDataList = new ArrayList<>();
    private OnListItemClickListener mItemClickListener;

    public HomePagerContentAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePagerContentAdapter.ViewHolder holder, int position) {
        View itemView = holder.itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onListItemClick(mDataList.get(position));
                }
            }
        });

        ImageView goodsCoverIV = itemView.findViewById(R.id.goods_cover);
        TextView goodsTitleIV = itemView.findViewById(R.id.goods_title);
        TextView goodsOffPriceTV = itemView.findViewById(R.id.goods_price_off);
        TextView goodsAfterOffPriceTV = itemView.findViewById(R.id.goods_after_off_price);
        TextView goodsOriginalPriceTV = itemView.findViewById(R.id.goods_original_price);
        TextView goodsSellCountTV = itemView.findViewById(R.id.goods_sell_count);

        HomePagerContent.DataDTO data = mDataList.get(position);

        ViewGroup.LayoutParams layoutParams = goodsCoverIV.getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;
        int coverSize = (Math.max(width, height)) / 2;

        Glide.with(itemView.getContext()).load(UrlUtils.getCoverPath(data.getPict_url(), coverSize)).into(goodsCoverIV);
        goodsTitleIV.setText(data.getTitle());

        long couponAmount = data.getCoupon_amount();
        float finalPrice = Float.parseFloat(data.getZk_final_price());
        float resultPrice = finalPrice - couponAmount;
        goodsOffPriceTV.setText(String.format(itemView.getContext().getString(R.string.text_off_price), couponAmount));
        goodsAfterOffPriceTV.setText(String.format("%.2f", resultPrice));
        goodsOriginalPriceTV.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodsOriginalPriceTV.setText("￥" + finalPrice);
        goodsSellCountTV.setText(String.format(itemView.getContext().getString(R.string.text_sell_count), data.getVolume()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addData(List<HomePagerContent.DataDTO> contents) {
        int oldSize = mDataList.size();
        mDataList.addAll(contents);
        notifyItemRangeChanged(oldSize, contents.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setDataList(List<HomePagerContent.DataDTO> dataList) {
        mDataList.clear();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnListItemClickListener listener){
        mItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(IBaseInfo item);
    }
}
