package com.example.taobaounion.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
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
import com.example.taobaounion.model.domain.ILinearItemInfo;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    private List<ILinearItemInfo> mDataList = new ArrayList<>();
    private OnListItemClickListener mItemClickListener;

    public HomePagerContentAdapter() {
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
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

        ILinearItemInfo data = mDataList.get(position);

//        ViewGroup.LayoutParams layoutParams = goodsCoverIV.getLayoutParams();
//        int width = layoutParams.width;
//        int height = layoutParams.height;
//        int coverSize = (Math.max(width, height)) / 2;
        String cover = data.getCover();
        if (!TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getCoverPath(cover, 200);
            Glide.with(itemView.getContext()).load(coverPath).into(goodsCoverIV);
        }else{
            goodsCoverIV.setImageResource(R.mipmap.ic_launcher);
        }
        goodsTitleIV.setText(data.getTitle());

        Long couponAmount = data.getCouponAmount();
        if (couponAmount == null) {
            couponAmount = 0L;
        }

        float finalPrice = Float.parseFloat(data.getFinalPrice());
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

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setDataList(List<? extends ILinearItemInfo> dataList) {
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
