package com.jx.intelligent.ui.activitise.personalcenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.listener.OnRecycleScrollListener;
import com.jx.intelligent.result.ServicesResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 净水机绑定消息
 */
public class MessageBindedActivity extends RHBaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private TitleBarHelper titleBarHelper;
    private int page = 1;
    private boolean isFresh;
    private CommunityService communityService;
    private List<ServicesResult.Data> datas = new ArrayList<ServicesResult.Data>();
    private List<ServicesResult.Data> moreDatas = new ArrayList<ServicesResult.Data>();
    private ProgressWheelDialog dialog;
    private String userId;

    @Override
    protected void init() {
        SesSharedReferences sp = new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());
        getServices(page);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_message_binded;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(MessageBindedActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);
        titleBarHelper.setMiddleClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        titleBarHelper.setMiddleTitleText("净水机绑定消息");
        communityService = new CommunityService();
        dialog = new ProgressWheelDialog(MessageBindedActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    private void initData() {
        mAdapter = new RecycleAdapter<ServicesResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_message_binded_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final ServicesResult.Data data) {
                holder.setImageByUrl(R.id.img_product, data.getUrl(), R.id.loading_anim_iv, R.mipmap.small_empty_img);
                holder.setText(R.id.txt_date, "2016-12-14 16:06");
                holder.setText(R.id.txt_title, "亲~您购买的宝贝已支付完成啦！");
                holder.setText(R.id.txt_product_name, "挂壁式净水机（中国红）");
                holder.setText(R.id.txt_product_price, "包年费用1000.00元");
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                    }
                });
            }

            @Override
            protected void opHeader(RecycleCommonViewHolder holder) {
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了头");
                    }
                });
            }
        };

        if(datas.size()<10)
        {
            mAdapter.setHavefooter(false);
            mAdapter.setmLoadFinish(true);
        }
        else
        {
            mAdapter.setHavefooter(true);
            mAdapter.setmLoadFinish(false);
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnRecycleScrollListener(mAdapter, (LinearLayoutManager) mManager) {
            @Override
            protected void loadMore() {
                if (!mAdapter.isLoadFinish()) { //有更多数据
                    mSwipeRefreshLayout.setEnabled(false); //正在加载更多,不可下拉刷新
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFresh = false;
                                    page += 1;
                                    getServices(page);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mAdapter != null)
                                {
                                    mAdapter.setHavefooter(true);
                                    mAdapter.setmLoadFinish(false);
                                }
                                isFresh = true;
                                page = 1;
                                getServices(page);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    void getServices(final int page)
    {
        dialog.show();
        communityService.customServicesTask(SesSharedReferences.getPhoneNum(MessageBindedActivity.this), page+"",userId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ServicesResult servicesResult = (ServicesResult)object;
                if(servicesResult != null)
                {
                    moreDatas = servicesResult.getData();
                    if(datas.size() <=0 && moreDatas.size() <= 0)
                    {
                        mDiffViewHelper.showEmptyView();
                    }
                    else {
                        mDiffViewHelper.showDataView();
                        if (page == 1) {
                            if (isFresh) {
                                datas.clear();
                                mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                                datas.addAll(moreDatas);
                                mAdapter.notifyDataSetChanged();
                                if(moreDatas.size()<10)
                                {
                                    mAdapter.setHavefooter(false);
                                    mAdapter.setmLoadFinish(true);
                                }
                                else
                                {
                                    mAdapter.setHavefooter(true);
                                    mAdapter.setmLoadFinish(false);
                                }
                            } else {
                                datas = moreDatas;
                                initData();
                            }
                        } else {
                            mAdapter.loadMore(moreDatas); //数据长度为0,表示没有更多数据
                            mAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setEnabled(true);
                            if(moreDatas.size()<10)
                            {
                                mAdapter.setHavefooter(false);
                                mAdapter.setmLoadFinish(true);
                            }
                            else
                            {
                                mAdapter.setHavefooter(true);
                                mAdapter.setmLoadFinish(false);
                            }
                        }
                    }
                }
                else
                {
                    if(datas.size() <=0 && moreDatas.size() <= 0)
                    {
                        mDiffViewHelper.showEmptyView();
                    }
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
                if(datas.size() <=0 || moreDatas.size() <= 0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else
                {
                    mDiffViewHelper.showDataView();
                }
            }
        });
    }
    @Override
    public void emptyRetryRefreshListener() {
        super.emptyRetryRefreshListener();
        dialog.show();
        page = 1;
        getServices(page);
    }
}