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
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.OnSellContent;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class OnSellContentListAdapter extends RecyclerView.Adapter<OnSellContentListAdapter.InnerHolder> {

    private List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mDataList = new ArrayList<>();
    private OnSellPageItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_list, parent, false);
        return new InnerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnSellContentListAdapter.InnerHolder holder, int position) {
        View itemView = holder.itemView;

        OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO data = mDataList.get(position);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onSellItemClick(data);
                }
            }
        });

        ImageView goodsCoverIV = itemView.findViewById(R.id.on_sell_item_cover);
        TextView goodsTitleTV = itemView.findViewById(R.id.on_sell_item_title);
        TextView goodsOriginPriceTV = itemView.findViewById(R.id.on_sell_origin_price_tv);
        TextView goodsOffPriceTV = itemView.findViewById(R.id.on_sell_off_price_tv);

        Glide.with(itemView).load(UrlUtils.getCoverPath(data.getPict_url(), 200)).into(goodsCoverIV);
        goodsTitleTV.setText(data.getTitle());
        String originPrice = data.getZk_final_price();
        Integer couponAmount = data.getCoupon_amount();
        float originPriceFloat = Float.parseFloat(originPrice);
        float finalPrice = originPriceFloat - couponAmount;
        goodsOriginPriceTV.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodsOriginPriceTV.setText("￥"+ originPrice);
        goodsOffPriceTV.setText("特惠价：" + String.format("%.2f", finalPrice));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     * @param dataList
     */
    public void onMoreLoaded(List<OnSellContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> dataList) {
        int oldDataSize = mDataList.size();
        mDataList.addAll(dataList);
        notifyItemRangeChanged(oldDataSize - 1, dataList.size());
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener){
        mItemClickListener = listener;
    }

    public interface OnSellPageItemClickListener{
        void onSellItemClick(IBaseInfo data);
    }
}
