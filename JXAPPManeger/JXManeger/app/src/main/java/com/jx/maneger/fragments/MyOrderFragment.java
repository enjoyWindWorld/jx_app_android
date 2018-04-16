package com.jx.maneger.fragments;

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
import com.jx.maneger.R;
import com.jx.maneger.activities.DetailsOrderActivity;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.activities.loginAndRegister.LoginActivity;
import com.jx.maneger.adapter.jxAdapter.MyOrderAdapter;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.base.RHBaseFragment;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.OrderDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetOrderListResult;
import com.jx.maneger.util.DensityUtil;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;
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
    OrderDao orderDao;
    private String orderId;
    ProgressWheelDialog dialog;
    int preAmout = 0;
    private String state;
    private String ord_managerno;

    @Override
    protected void init() {
        orderDao = new OrderDao();
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
    }

    /**
     * 获取我的订单列表
     */
    public void getMyOrderList() {
        getPreOrderData();
        if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(getActivity()))) {
            if (orderBeans.size() <= 0) {
                mDiffViewHelper.showEmptyView();
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            return;
        }

        dialog.show();
        orderDao.getMyOrderListTask(StringUtil.isEmpty(ord_managerno) ? SesSharedReferences.getPartnerNumber(getActivity()) : ord_managerno, page + "", state, SesSharedReferences.getSafetyMark(getActivity()), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                GetOrderListResult getOrderListResult = (GetOrderListResult) object;
                showOrderData(getOrderListResult);
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    if (orderBeans.size() <= 0) {
                        mDiffViewHelper.showEmptyView();
                        mSwipeRefreshLayout.setVisibility(View.GONE);
                    }
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
            map.put("ord_managerno", StringUtil.isEmpty(ord_managerno) ? SesSharedReferences.getPartnerNumber(getActivity()) : ord_managerno);
            map.put("page", "1");
            map.put("state", state);
            map.put("safetyMark", SesSharedReferences.getSafetyMark(getActivity()));

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
//            int width = getResources().getDimensionPixelSize(R.dimen.dp_65);
//            int textSize = DensityUtil.px2sp(34);
//
//            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//
//            if (viewType == 1) {// 根据Adapter的ViewType来决定菜单的样式、颜色等属性、或者是否添加菜单。
//                // Do nothing.
//            } else if (viewType == 0) {// 需要添加单个菜单的Item。
//                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
//                        .setBackgroundDrawable(R.drawable.selector_red)
//                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
//                        .setTextColor(Color.WHITE)
//                        .setTextSize(textSize)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
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
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

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

    public String getOrd_managerno() {
        return ord_managerno;
    }

    public void setOrd_managerno(String ord_managerno) {
        this.ord_managerno = ord_managerno;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            getMyOrderList();
        }
    }
}
