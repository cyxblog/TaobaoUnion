package com.example.taobaounion.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.service.autofill.TextValueSanitizer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaounion.R;
import com.example.taobaounion.utils.LogUtils;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    private static final float DEFAULT_SPACE = 10f;

    private List<String> mTextList = new ArrayList<>();
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mChildMeasureHeight;
    private OnFlowTextItemClickListener mClickListener;

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private float mItemVerticalSpace = DEFAULT_SPACE;

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = ta.getDimension(R.styleable.TextFlowLayout_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.TextFlowLayout_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
//        LogUtils.d(this, "mItemHorizontalSpace ==> " + mItemHorizontalSpace);
//        LogUtils.d(this, "mItemVerticalSpace ==> " + mItemVerticalSpace);
    }

    public int getContentSize(){
        return mTextList.size();
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        mTextList.clear();
        mTextList.addAll(textList);
        Collections.reverse(mTextList);
        // 遍历内容
        for (String text : mTextList) {
            // 添加子view
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onFlowTextItemClick(text);
                    }
                }
            });
            item.setText(text);
            addView(item);
        }
    }

    // 所有行的子view
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingStart() - getPaddingEnd();
        // 测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                continue;
            }
            // 测量前
//            LogUtils.d(this, "height --- > " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            // 测量后
//            LogUtils.d(this, "height --- > " + itemView.getMeasuredHeight());
            if (line == null) {
                // 当前行为空，可以添加
                line = createNewLine(itemView);
                lines.add(line);
            } else {
                if (canBeAdd(itemView, line)) {
                    // 可以添加
                    line.add(itemView);
                }else{
                    // 新建一行
                    line = createNewLine(itemView);
                    lines.add(line);
                }
            }
        }
        mChildMeasureHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mChildMeasureHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        // 测量自己
        setMeasuredDimension(mSelfWidth, selfHeight);

    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        return line;
    }

    private boolean canBeAdd(View itemView, List<View> line) {
        // 所有的已添加的子view宽度和间距相加 + itemView的宽度
        // 如果小于/等于当前空间的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();

        for (View view : line) {
            totalWidth += view.getMeasuredWidth();
        }

        // 水平间距
        totalWidth += mItemHorizontalSpace * (line.size() + 1);

        if (totalWidth <= mSelfWidth) {
            return true;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 摆放
        int topOffset = (int) mItemVerticalSpace;
        for (List<View> views : lines) {
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mChildMeasureHeight + mItemVerticalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener){
        mClickListener = listener;
    }

    public interface OnFlowTextItemClickListener{
        void onFlowTextItemClick(String text);
    }
}
