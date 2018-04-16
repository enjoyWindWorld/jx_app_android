package com.jx.intelligent.ui.activitise.personalcenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.adapter.jxAdapter.MessageAdapter;
import com.jx.intelligent.result.MessageResult;
import com.jx.intelligent.ui.activitise.MainActivity;
import com.jx.intelligent.ui.activitise.payment.DetailsOrderActivity;
import com.jx.intelligent.util.DensityUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
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
 * Created by Administrator on 2016/12/16 0016.
 * 消息中心
 */

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MessageAdapter messageAdapter;
    private List<MessageResult.Data> messages;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private LinearLayout refresh_empty_view_ll;
    private Activity mContext;

    NormalAlertDialog normalAlertDialog;
    ImageView titlebar_left_iv;
    RelativeLayout titlebar_left_rl;
    UserCenter userCenterDao;
    ProgressWheelDialog dialog;
    MessageResult.Data data;
    int page = 1;
    int preAmout = 0;
    int my_adapterPosition, my_menuPosition;
    private String messageId = "";
    private DBManager dbManager;
    private boolean isComeFromUserCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);
        isComeFromUserCenter = getIntent().getBooleanExtra("isComeFromUserCenter", false);
        messages = new ArrayList<MessageResult.Data>();
        ((TextView) findViewById(R.id.titlebar_center_tv)).setText("消息中心");
        titlebar_left_iv = (ImageView) findViewById(R.id.titlebar_left_iv);
        titlebar_left_iv.setVisibility(View.VISIBLE);
        refresh_empty_view_ll = (LinearLayout) findViewById(R.id.refresh_empty_view_ll);
        refresh_empty_view_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messages.clear();
                if(messageAdapter != null)
                {
                    messageAdapter.clear();
                }
                getMessageList();
            }
        });
        titlebar_left_rl = (RelativeLayout) findViewById(R.id.titlebar_left_rl);
        titlebar_left_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isComeFromUserCenter)
                {
                    Intent intent_main = new Intent(MessageActivity.this, MainActivity.class);
                    startActivity(intent_main);
                    MessageActivity.this.finish();
                }
                else
                {
                    MessageActivity.this.finish();
                }
            }
        });
        mContext = this;
        userCenterDao = new UserCenter();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
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
        dialog = new ProgressWheelDialog(MessageActivity.this);
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        getMessageList();
        initDialog();
    }

    @Override
    public void onBackPressed() {
        if(isComeFromUserCenter)
        {
            Intent intent_main = new Intent(MessageActivity.this, MainActivity.class);
            startActivity(intent_main);
            MessageActivity.this.finish();
        }
        else
        {
            MessageActivity.this.finish();
        }
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
                    page = 1;
                    mSwipeRefreshLayout.setRefreshing(false);
                    messages.clear();
                    if(messageAdapter != null)
                    {
                        messageAdapter.clear();
                    }
                    getMessageList();
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
                if(preAmout >= 10) {
                    page += 1;
                    getMessageList();
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
            data = messageAdapter.getItem(position);
            if(!StringUtil.isEmpty(data.getIsread()) && data.getIsread().equals("0"))
            {
                getReadMsg(data.getId());
            }
            else
            {
                goDetailInfo();
            }
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
                messageId = messageAdapter.getItem(adapterPosition).getId();
                my_adapterPosition = adapterPosition;
                my_menuPosition = menuPosition;
                normalAlertDialog.show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };


    /**
     * 获取我的订单列表
     */
    public void getMessageList() {
        getPreMsgData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(messageAdapter == null || messageAdapter.getItemCount()<=0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            return;
        }


        dialog.show();
        userCenterDao.getMSGListTask(SesSharedReferences.getUserId(MessageActivity.this), page+"",  new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                MessageResult messageResult = (MessageResult) object;
                initData(messageResult);
            }

            @Override
            public void resFailure(String message) {
                if(messageAdapter == null || messageAdapter.getItemCount()<=0)
                {
                    refresh_empty_view_ll.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的服务类型的数据
     */
    void getPreMsgData()
    {
        if(page == 1)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("page", "1");
            map.put("userid", SesSharedReferences.getUserId(RHBaseApplication.getContext()));
            String js = dbManager.getUrlJsonData(Constant.USER_GET_MSG_LIST + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                MessageResult messageResult = new Gson().fromJson(js, MessageResult.class);
                initData(messageResult);
            }
        }
    }

    /**
     * 阅读消息
     */
    public void getReadMsg(String id) {

        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenterDao.readedMsgTask(id,  new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                data.setIsread("1");
                messageAdapter.notifyDataSetChanged();
                goDetailInfo();
            }


            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 删除消息
     */
    public void deleteMsg(String id) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenterDao.deleteMsgTask(id,  new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("删除消息成功");
                // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
                if (my_menuPosition == 0) {// 删除按钮被点击。
                    messages.remove(my_adapterPosition);
                    messageAdapter.notifyItemRemoved(my_adapterPosition);
                    if(messages.size()<=0)
                    {
                        refresh_empty_view_ll.setVisibility(View.VISIBLE);
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

    /**
     * 跳转到订单详情
     */
    private void goDetailInfo() {
        //0代表订单到期,1代表交易,2代表绑定,3代表分享
        if(data != null)
        {
            if("0".equals(data.getType()))
            {
                Intent intent = new Intent(MessageActivity.this, ServicePriceActivity.class);
                intent.putExtra("pro_no", data.getNextparams());
                startActivity(intent);
            }
            else if("1".equals(data.getType()) ||  "2".equals(data.getType()))
            {
                Intent intent = new Intent(MessageActivity.this, DetailsOrderActivity.class);
                intent.putExtra("orderId", data.getNextparams());
                intent.putExtra("isComeFromMessage", true);
                startActivity(intent);
            }
        }

    }

    void initData( MessageResult messageResult)
    {
        if(messageResult != null && messageResult.getData().size()>0)
        {
            if(page == 1 && messageAdapter != null)//移除界面本地缓存的数据
            {
                messageAdapter.clear();
                messages.clear();
                messageAdapter.notifyDataSetChanged();
            }

            preAmout =  messageResult.getData().size();
            refresh_empty_view_ll.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            messages.addAll(messageResult.getData());
            if(messageAdapter == null)
            {
                messageAdapter = new MessageAdapter(messages, MessageActivity.this);
                mSwipeMenuRecyclerView.setAdapter(messageAdapter);
                messageAdapter.setOnItemClickListener(onItemClickListener);
            }
            else
            {
                messageAdapter.add(messages);
                messageAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            preAmout = 0;
            if(messageAdapter == null || messageAdapter.getItemCount()<=0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

    private void initDialog()
    {
        normalAlertDialog = new NormalAlertDialog.Builder(MessageActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否要删除消息")
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
                        deleteMsg(messageId);
                    }
                })
                .build();
    }
}
