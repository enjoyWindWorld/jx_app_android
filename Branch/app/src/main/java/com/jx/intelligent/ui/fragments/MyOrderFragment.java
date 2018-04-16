package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.MyOrderAdapter;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetOrderListResult;
import com.jx.intelligent.ui.activitise.payment.DetailsOrderActivity;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/17.
 */

public class MyOrderFragment extends RHBaseFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyOrderAdapter mMenuAdapter;
    private List<GetOrderListResult.Data> orderBeans;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private DBManager dbManager;

    private int page = 1;
    int my_adapterPosition, my_menuPosition;
    UserCenter userCenterDao;
    private String orderId;
    ProgressWheelDialog dialog;
    NormalAlertDialog normalAlertDialog;
    int preAmout = 0;
    private String state;

    @Override
    protected void init() {
        userCenterDao = new UserCenter();
        dialog = new ProgressWheelDialog(getActivity());
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        getMyOrderList();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_inner_my_order;
    }

    @Override
    protected void initTitle(View titleView) {
        new TitleBarHelper(titleView).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        orderBeans = new ArrayList<GetOrderListResult.Data>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) contentView.findViewById(R.id.recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//        mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        // 添加滚动监听。
        mSwipeMenuRecyclerView.addOnScrollListener(mOnScrollListener);

        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单创建器。
        mSwipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        // 设置菜单Item点击监听。

        mSwipeMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        initDialog();
    }

    /**
     * 获取我的订单列表
     */
    public void getMyOrderList() {
        getPreOrderData();
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            if (orderBeans.size() <= 0) {
                mDiffViewHelper.showEmptyView();
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            return;
        }

        dialog.show();
        userCenterDao.getMyOrderListTask(SesSharedReferences.getUserId(getActivity()), page + "", state + "", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                GetOrderListResult getOrderListResult = (GetOrderListResult) object;
                showOrderData(getOrderListResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                if (orderBeans.size() <= 0) {
                    mDiffViewHelper.showEmptyView();
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取缓存下来的我的订单的数据
     */
    void getPreOrderData() {
        if (page == 1) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", SesSharedReferences.getUserId(getActivity()));
            map.put("page", "1");

            String js = dbManager.getUrlJsonData(Constant.USER_GET_MY_ORD_WP + StringUtil.obj2JsonStr(map));
            if (!StringUtil.isEmpty(js)) {
                GetOrderListResult getOrderListResult = new Gson().fromJson(js, GetOrderListResult.class);
                showOrderData(getOrderListResult);
            }
        }
    }

    /**
     * 显示订单列表数据
     *
     * @param getOrderListResult
     */
    void showOrderData(GetOrderListResult getOrderListResult) {
        if (getOrderListResult != null) {
            if (page == 1 && mMenuAdapter != null)//移除界面本地缓存的
            {
                mMenuAdapter.clear();
                orderBeans.clear();
                mMenuAdapter.notifyDataSetChanged();
            }

            preAmout = getOrderListResult.getData().size();
            if (getOrderListResult.getData().size() > 0) {
                mDiffViewHelper.showDataView();
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                orderBeans.addAll(getOrderListResult.getData());
                initData();
            }
            else
            {
                mDiffViewHelper.showEmptyView();
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        } else {
            if (orderBeans.size() <= 0) {
                mDiffViewHelper.showEmptyView();
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

    void initData() {
        if (mMenuAdapter == null) {
            mMenuAdapter = new MyOrderAdapter(orderBeans);
            mSwipeMenuRecyclerView.setAdapter(mMenuAdapter);
            mMenuAdapter.setOnItemClickListener(onItemClickListener);
        } else {
            mMenuAdapter.add(orderBeans);
            mMenuAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 跳转到订单详情
     */
    private void goDetailsOrder(String orderId) {
        Intent intent = new Intent(getActivity(), DetailsOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        intent.putExtra("obj", bundle);
        intent.putExtra("orderId", orderId);
        startActivityForResult(intent, 100);
    }

    /**
     * 刷新监听。
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mSwipeMenuRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    orderBeans.clear();
                    if (mMenuAdapter != null) {
                        mMenuAdapter.clear();
                    }
                    page = 1;
                    getMyOrderList();
                }
            }, 2000);
        }
    };

    /**
     * 加载更多
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                if (preAmout >= 10) {
                    page += 1;
                    getMyOrderList();
                }
            }
        }
    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_65);
            int textSize = DensityUtil.px2sp(34);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
//            {
//                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
//                        .setImage(R.mipmap.ic_action_add) // 图标。
//                        .setWidth(width) // 宽度。
//                        .setHeight(height); // 高度。
//                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。
//
//                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)
//                        .setImage(R.mipmap.ic_action_add)
//                        .setWidth(width)
//                        .setHeight(height);
//
//                swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
//            }

            if (viewType == 1) {// 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
                // Do nothing.
            } else if (viewType == 0) {// 需要添加单个菜单的Item。
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.selector_green)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(textSize)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

            }

//            // 添加右侧的，如果不添加，则右侧不会出现菜单。
//            {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)
//                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
//                        .setTextColor(Color.WHITE)
//                        .setTextSize(textSize)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
//
//                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)
//                        .setImage(R.mipmap.ic_action_add)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

//                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_green)
//                        .setText("删除")
//                        .setTextColor(Color.WHITE)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
//            }
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            orderId = mMenuAdapter.getItem(position).getOrdno();
            goDetailsOrder(orderId);
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                orderId = mMenuAdapter.getItem(adapterPosition).getOrdno();
                my_adapterPosition = adapterPosition;
                my_menuPosition = menuPosition;
                normalAlertDialog.show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void initDialog() {
        normalAlertDialog = new NormalAlertDialog.Builder(getActivity())
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否要删除订单")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        deleteMyOrder(orderId);
                    }
                })
                .build();
    }

    /**
     * 删除我的订单
     */
    public void deleteMyOrder(String orderId) {
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
            return;
        }

        dialog.show();
        userCenterDao.myOrderDeleteTask(orderId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("删除我的订单列表成功");
                // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
                if (my_menuPosition == 0) {// 删除按钮被点击。
                    orderBeans.remove(my_adapterPosition);
                    mMenuAdapter.notifyItemRemoved(my_adapterPosition);
                    if (orderBeans.size() <= 0) {
                        mDiffViewHelper.showEmptyView();
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    @Override
    public void emptyRetryRefreshListener() {
        super.emptyRetryRefreshListener();
        orderBeans.clear();
        if (mMenuAdapter != null) {
            mMenuAdapter.clear();
        }
        page = 1;
        getMyOrderList();
    }

    public void freshView()
    {
        LogUtil.e("刷新界面");
        mSwipeRefreshLayout.setRefreshing(false);
        orderBeans.clear();
        if (mMenuAdapter != null) {
            mMenuAdapter.clear();
        }
        page = 1;
        getMyOrderList();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
