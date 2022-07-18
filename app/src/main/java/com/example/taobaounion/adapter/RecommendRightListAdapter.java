package com.example.taobaounion.adapter;

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
import com.example.taobaounion.model.domain.RecommendContent;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class RecommendRightListAdapter extends RecyclerView.Adapter<RecommendRightListAdapter.InnerHolder> {

    List<RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> mDataList = new ArrayList<>();
    private OnRightListItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_right_list, parent, false);
        return new InnerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendRightListAdapter.InnerHolder holder, int position) {
        View itemView = holder.itemView;

        ImageView coverIV = itemView.findViewById(R.id.recommend_right_list_cover);
        TextView offPriceTV = itemView.findViewById(R.id.recommend_right_list_off_price);
        TextView titleTV = itemView.findViewById(R.id.recommend_right_list_title);
        TextView buyBtn = itemView.findViewById(R.id.recommend_right_list_btn);
        TextView originPriceTV = itemView.findViewById(R.id.recommend_right_list_origin_price);

        RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO data = mDataList.get(position);
        if (data != null) {
            String url = data.getPict_url();
            String coverPath = UrlUtils.getCoverPath(url, 200);
            Glide.with(itemView).load(coverPath).into(coverIV);

            if (TextUtils.isEmpty(data.getCoupon_click_url())) {
                originPriceTV.setText("晚啦，没有优惠券了");
                buyBtn.setVisibility(View.GONE);
            }else{
                originPriceTV.setText("原价：" + data.getZk_final_price() + "元");
                buyBtn.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(data.getCoupon_info())) {
                offPriceTV.setVisibility(View.GONE);
            }else{
                offPriceTV.setVisibility(View.VISIBLE);
                offPriceTV.setText(data.getCoupon_info());
            }
            titleTV.setText(data.getTitle());

        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onRightListItemClick(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(List<RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnRightListItemClickListener(OnRightListItemClickListener listener){
        mItemClickListener = listener;
    }

    public interface OnRightListItemClickListener{
        void onRightListItemClick(RecommendContent.DataDTO.TbkDgOptimusMaterialResponseDTO.ResultListDTO.MapDataDTO item);
    }
}
