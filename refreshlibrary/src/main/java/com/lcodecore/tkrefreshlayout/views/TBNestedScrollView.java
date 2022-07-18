package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class TBNestedScrollView extends NestedScrollView {
    private int mHeaderHeight = 0;
    private int mOriginalScroll = 0;
    private RecyclerView mRecyclerView;

    public TBNestedScrollView(@NonNull  Context context) {
        super(context);
    }

    public TBNestedScrollView(@NonNull Context context, @Nullable  AttributeSet attrs) {
        super(context, attrs);
    }

    public TBNestedScrollView(@NonNull  Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight){
        mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) target;
        }
        if(mOriginalScroll < mHeaderHeight){
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOriginalScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类已经华东道底部
     * @return
     */
    public boolean isToBottom() {
        if (mRecyclerView != null) {
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
            return isBottom;
        }
        return false;
    }
}
