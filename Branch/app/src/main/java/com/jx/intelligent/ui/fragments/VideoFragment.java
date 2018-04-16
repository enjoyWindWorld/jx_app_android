package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.VideoDao;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.VideoResult;
import com.jx.intelligent.ui.activitise.personalcenter.VideoPlayActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.video.SuperVideoAdapter;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.superplayer.library.OnItemClickListener;
import com.superplayer.library.SuperPlayer;
import com.superplayer.library.SuperPlayerManage;
import com.superplayer.library.mediaplayer.IjkVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 王云 on 2017/6/29 0029.
 */

public class VideoFragment extends RHBaseFragment {
    TitleBarHelper titleBarHelper;
    private SuperPlayer player;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView superRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private int postion = -1;
    private int lastPostion = -1;
    private RecyclerViewHeader header;
    private RelativeLayout fullScreen;
    private int currentPosition;
    private List<VideoResult.DataBean> mMoreVideDatas = new ArrayList<>();
    private ProgressWheelDialog dialog;
    private int page = 1;
    private SuperVideoAdapter mAdapter;
    private VideoDao dao;
    private boolean isFresh;


    @Override
    protected void init() {
        LoadDataFromNet(page);
        initAdapter(mMoreVideDatas);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.video_fragment;
    }

    @Override
    protected void initTitle(View titleView) {
        titleBarHelper = new TitleBarHelper(titleView);
        titleBarHelper.setMiddleTitleText("视频娱乐");
        titleBarHelper.setHideTitleBar();
    }



    @Override
    protected void findView(View contentView) {
        dao = new VideoDao();
        dialog =  new ProgressWheelDialog(getActivity());
        fullScreen = (RelativeLayout) contentView.findViewById(R.id.full_screen);
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        superRecyclerView = (RecyclerView) contentView.findViewById(R.id.act_recycle_super_video_recycleview);
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        header = RecyclerViewHeader.fromXml(UIUtil.getContext(), R.layout.video_recycleheader);
        mLayoutManager = new LinearLayoutManager(UIUtil.getContext());
        superRecyclerView.setLayoutManager(mLayoutManager);
        superRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        superRecyclerView.setHasFixedSize(true);
        header.attachTo(superRecyclerView);
        player = SuperPlayerManage.getSuperManage().initialize(getActivity());
        player.setShowTopControl(false).setSupportGesture(false);
        player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
        superRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //recyclerView.canScrollVertically(1) 用来判断 列表是否滚动到了底部 fase表示 滚动到了底部  true表示没有滚动到底部
                //上啦加载更多
                if(!recyclerView.canScrollVertically(1)){
                    isFresh = false;
                    page+=1;
                    LoadDataFromNet(page);
                    ShowDialog();

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);






                }

        });

        superRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int index = superRecyclerView.getChildAdapterPosition(view);
                View controlview = view.findViewById(R.id.adapter_player_control);
                if (controlview == null) {
                    return;
                }
                view.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                if (index == postion) {
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                    frameLayout.removeAllViews();
                    if (player != null &&
                            ((player.isPlaying()) || player.getVideoStatus() == IjkVideoView.STATE_PAUSED)) {
                        view.findViewById(R.id.adapter_player_control).setVisibility(View.GONE);
                    }
                    if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                        if (player.getParent() != null)
                            ((ViewGroup) player.getParent()).removeAllViews();
                        frameLayout.addView(player);
                        return;
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int index = superRecyclerView.getChildAdapterPosition(view);
                if ((index) == postion) {

                    if(true){
                        if (player != null) {
                            player.stop();
                            player.release();
                            player.showView(R.id.adapter_player_control);
                        }
                    }
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFresh = true;
                                page=1;
                                LoadDataFromNet(page);
                                ShowDialog();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * 网络加载数据
     */
    private void LoadDataFromNet(int page) {

        player.stopPlayVideo();
        player.release();
        player.showView(R.id.adapter_player_control);

        dao.getVideoTask(0 + "", page + "", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                VideoResult result = (VideoResult) object;
                if(result != null && result.getData().size() > 0)
                {
                    if(isFresh)
                    {
                        mMoreVideDatas.clear();
                    }
                    for (int i=0;i<result.getData().size();i++){
                        VideoResult.DataBean dataBean = result.getData().get(i);
                        mMoreVideDatas.add(dataBean);
                    }
                    mAdapter.notifyDataSetChanged();
                    DismissDialog();
                }
                else
                {
                    ToastUtil.showToast(R.string.not_new_datas);
                }

                if(isFresh)
                {
                    mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                }
                else
                {
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(R.string.download_error_server);
                if(isFresh)
                {
                    mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                }
                else
                {
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        });
    }

    /**
     * 初始化播放器
     */
    private void initAdapter(final List<VideoResult.DataBean> mMoreVideDatas) {
        mAdapter = new SuperVideoAdapter(getActivity(), mMoreVideDatas);
        superRecyclerView.setAdapter(mAdapter);
        mAdapter.setPlayClick(new SuperVideoAdapter.onPlayClick() {
            @Override
            public void onPlayclick(int position, RelativeLayout image) {
                image.setVisibility(View.GONE);
                if (player.isPlaying() && lastPostion == position) {
                    return;
                }
                postion = position;
                if (player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    if (position != lastPostion) {
                        player.stopPlayVideo();
                        player.release();
                    }
                }
                if (lastPostion != -1) {
                    player.showView(R.id.adapter_player_control);
                }
                View view = superRecyclerView.findViewHolderForAdapterPosition(position).itemView;
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                frameLayout.removeAllViews();
                player.showView(R.id.adapter_player_control);
                frameLayout.addView(player);
                player.setTitle(mMoreVideDatas.get(position).getTitle());
                player.play(mMoreVideDatas.get(position).getVideo());
                lastPostion = position;
            }
        });




        /**
         * 播放完设置还原播放界面
         */
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
                if (last != null && last.getChildCount() > 0) {
                    last.removeAllViews();
                    View itemView = (View) last.getParent();
                    if (itemView != null) {
                        itemView.findViewById(R.id.adapter_player_control).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        /**
         * 跳转全屏播放
         */
        player.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (player != null) {
                    currentPosition = player.getCurrentPosition();
                }
                String play_url = mMoreVideDatas.get(postion).getVideo();
                String title = mMoreVideDatas.get(postion).getTitle();
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                LogUtil.e(title);
                intent.putExtra("title", title);
                intent.putExtra("play_url", play_url);
                intent.putExtra("currentPosition", currentPosition);
                startActivityForResult(intent, Constant.VIDEO_POSITION);
            }
        });
    }

    /**
     * 监测屏幕变化
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                fullScreen.setVisibility(View.GONE);
                fullScreen.removeAllViews();
                superRecyclerView.setVisibility(View.VISIBLE);
                if (postion <= mLayoutManager.findLastVisibleItemPosition()
                        && postion >= mLayoutManager.findFirstVisibleItemPosition()) {
                    View view = superRecyclerView.findViewHolderForAdapterPosition(postion).itemView;
                    FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.adapter_super_video);
                    frameLayout.removeAllViews();
                    ViewGroup last = (ViewGroup) player.getParent();
                    //找到videoitemview的父类，然后remove
                    if (last != null) {
                        last.removeAllViews();
                    }
                    frameLayout.addView(player);
                }
                int mShowFlags =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                fullScreen.setSystemUiVisibility(mShowFlags);
            } else {
                ViewGroup viewGroup = (ViewGroup) player.getParent();
                if (viewGroup == null)
                    return;
                viewGroup.removeAllViews();
                fullScreen.addView(player);
                fullScreen.setVisibility(View.VISIBLE);
                int mHideFlags =
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                fullScreen.setSystemUiVisibility(mHideFlags);
            }
        } else {
            fullScreen.setVisibility(View.GONE);
        }
    }

    void ShowDialog(){
        dialog.show();
    }

    void DismissDialog() {
        if (dialog.isShowing()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    public void onPause() {

        super.onPause();
        if (player != null) {
            player.onPause();
            player.stopPlayVideo();
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (player != null) {
            player.onResume();
            player.stopPlayVideo();
        }
    }


    @Override
    public void onDestroy() {

        super.onDestroy();

        if (player != null) {
            player.onDestroy();
            player.stopPlayVideo();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        if (player != null) {

            player.stopPlayVideo();
            player.release();
        }
    }



    public void  stopVideo(){
        if(player!=null){
            player.stopPlayVideo();
            player.release();
            player.showView(R.id.adapter_player_control);
        }

    }

}
