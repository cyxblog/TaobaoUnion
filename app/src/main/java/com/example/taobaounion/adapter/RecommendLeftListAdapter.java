package com.example.taobaounion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.model.domain.RecommendCategories;

import java.util.ArrayList;
import java.util.List;

public class RecommendLeftListAdapter extends RecyclerView.Adapter<RecommendLeftListAdapter.HolderView> {

    List<RecommendCategories.DataDTO> mDataList = new ArrayList<>();

    private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener mItemClickListener;

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_left_list, parent, false);
        return new HolderView(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendLeftListAdapter.HolderView holder, int position) {
        TextView favoriteTitleTV = holder.itemView.findViewById(R.id.recommend_favorite_title);
        if (mCurrentSelectedPosition == position) {
            favoriteTitleTV.setBackgroundColor(favoriteTitleTV.getResources().getColor(R.color.colorEFEEEE, null));
        } else {
            favoriteTitleTV.setBackgroundColor(favoriteTitleTV.getResources().getColor(R.color.white, null));
        }
        RecommendCategories.DataDTO dataDTO = mDataList.get(position);
        favoriteTitleTV.setText(dataDTO.getFavorites_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && mCurrentSelectedPosition != position) {
                    mCurrentSelectedPosition = position;
                    mItemClickListener.onLeftListItemClick(dataDTO);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setData(List<RecommendCategories.DataDTO> result) {
        mDataList.clear();
        mDataList.addAll(result);
        notifyDataSetChanged();
    }

    public class HolderView extends RecyclerView.ViewHolder {
        public HolderView(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {
        mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftListItemClick(RecommendCategories.DataDTO dataDTO);
    }
}
