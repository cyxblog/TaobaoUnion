package com.example.taobaounion;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaounion.base.BaseActivity;
import com.example.taobaounion.model.domain.TicketResult;
import com.example.taobaounion.presenter.ITicketPresenter;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.StringUtils;
import com.example.taobaounion.utils.ToastUtil;
import com.example.taobaounion.utils.UrlUtils;
import com.example.taobaounion.view.ITicketCallback;
import com.example.taobaounion.view.custom.LoadingView;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketCallback {


    private ITicketPresenter mTicketPresenter;

    private boolean mHasTaobaoApp = false;

    @BindView(R.id.ticket_back_btn)
    public ImageView mBackBtn;

    @BindView(R.id.ticket_cover)
    public ImageView mCoverIV;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_and_open_btn)
    public TextView mTicketBtn;

    @BindView(R.id.ticket_cover_loading)
    public LoadingView mTicketCoverLoadingView;

    @BindView(R.id.ticket_load_retry)
    public TextView mTicketLoadRetryTV;

    @Override
    protected void initView() {

    }

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        mTicketPresenter.registerCallback(this);

        // 判断是否安装淘宝
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }
        LogUtils.d(this, "mHasTaobaoApp == > " + mHasTaobaoApp);
        // 根据这个值修改UI
        mTicketBtn.setText(mHasTaobaoApp ? "打开淘宝领券" : "复制淘口令");
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterCallback(this);
        }
    }

    @Override
    protected void initListener() {
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 复制淘口令
                String code = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.this, "code === > " + code);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("taobao_ticket_code", code);
                cm.setPrimaryClip(clipData);
                // 判断有没有淘宝
                if (mHasTaobaoApp) {
                    Intent taobaoIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }else{
                    ToastUtil.showToast("已经复制，粘贴分享，或打开淘宝");
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        //设置商品封面
        if (mCoverIV != null && !TextUtils.isEmpty(cover)) {
            Glide.with(this).load(UrlUtils.getCoverPath(cover, 300)).into(mCoverIV);
        }

        if(TextUtils.isEmpty(cover)){
            mCoverIV.setVisibility(View.GONE);
        }
        // 设置code
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String ticketModel = result.getData().getTbk_tpwd_create_response().getData().getModel();
            String ticketCodeText = StringUtils.getTicketCode(ticketModel);
            mTicketCode.setText(ticketCodeText);
        }

        if (mTicketCoverLoadingView != null) {
            mTicketCoverLoadingView.setVisibility(View.GONE);
        }
        if (mTicketLoadRetryTV != null) {
            mTicketLoadRetryTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (mTicketCoverLoadingView != null) {
            mTicketCoverLoadingView.setVisibility(View.GONE);
        }
        if (mTicketLoadRetryTV != null) {
            mTicketLoadRetryTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (mTicketCoverLoadingView != null) {
            mTicketCoverLoadingView.setVisibility(View.VISIBLE);
        }
        if (mTicketLoadRetryTV != null) {
            mTicketLoadRetryTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {
        if (mTicketCoverLoadingView != null) {
            mTicketCoverLoadingView.setVisibility(View.GONE);
        }
    }
}
