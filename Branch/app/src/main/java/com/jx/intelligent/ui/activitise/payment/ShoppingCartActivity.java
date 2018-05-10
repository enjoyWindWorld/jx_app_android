package com.jx.intelligent.ui.activitise.payment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ShopPingCartOutSideRecycleAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.DeleteToShoppingCartDao;
import com.jx.intelligent.dao.ShoppingCartDao;
import com.jx.intelligent.dao.UpdaterShopDao;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.AddButtonClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.intf.SubButtonClickListener;
import com.jx.intelligent.result.ShopPingCartListBeanResult;
import com.jx.intelligent.result.ShopPingCartResult;
import com.jx.intelligent.util.MsgIntEvent;
import com.jx.intelligent.util.MsgScidEvent;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.SharedPreferencesUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.NumberAddSubView;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jx.intelligent.constant.Constant.CART_JIESUAN;

/**
 * Created by 王云 on 2017/6/16 0016.
 * 购物车页面
 */

public class ShoppingCartActivity extends RHBaseActivity {
    TitleBarHelper titleBarHelper;
    private static boolean flag = true;
    private RecyclerView mRecycle_outside;
    private String mUserId;
    private LinearLayoutManager manager;
    private CheckBox mAll_checkBox;
    private List<ShopPingCartResult.DataBean> mOutside_datas;
    private List<ShopPingCartResult.DataBean> mUpdaterOutside_datas;
    private ShopPingCartOutSideRecycleAdapter adapter;
    private Button mCart_Btn_Detele;
    private Button mCart_btn_pay;
    private TextView mCart_tv_all;
    private float mPrice;
    private ArrayList<ShopPingCartListBeanResult> mDatas;
    private ShopPingCartListBeanResult resultDatas;
    private String mSc_id_all;
    private String mAll_sc_id;
    private NumberAddSubView mShop_addSubView;

    private int shopcart_number=1;
    private int mUpDate_number;
    private int mUpDate_scid;

    private NumberAddSubView mSub_addView;
    private SharedPreferencesUtil sp2;
    private String mDetailsMsg;
    private ProgressWheelDialog dialog;
    @Override
    protected void init() {

        SesSharedReferences sp = new SesSharedReferences();
        mUserId = sp.getUserId(UIUtil.getContext());

        LoadDataFromNet(mUserId);

        EventBus.getDefault().register(this);
        sp2 = new SharedPreferencesUtil();
        sp2.putBoolean(ShoppingCartActivity.this,Constant.FIRST_SHOW,true);



    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_shopping_cart;
    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgIntEvent event){
        //加减过后的商品数量
        mUpDate_number = event.getIntMsg();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgScidEvent event){
        // 加减的那一个 购物车的  Scid
        mUpDate_scid = event.getScid();
    }

    @Override
    protected void initTitle() {

        titleBarHelper = new TitleBarHelper(ShoppingCartActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setMiddleTitleText("购物车");
        titleBarHelper.setRightText("编辑");

        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShoppingCartActivity.this, NewProductDetailActivity.class);

                setResult(Constant.SHOPPINGCART_NUMBER, intent);
                finish();
            }
        });
        /**
         * 右边编辑按钮的点击事件
         */

        titleBarHelper.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag) {
                    GoneAddSubView();
                    flag = false;

                    sp2.putBoolean(ShoppingCartActivity.this,Constant.FIRST_SHOW,false);
                    titleBarHelper.setRightText("完成");
                    mCart_Btn_Detele.setVisibility(View.VISIBLE);
                    mCart_btn_pay.setVisibility(View.GONE);
                    mCart_tv_all.setText("已选" + "0" + "项");

//                    ShowAddSubView();
                    AllSelectedPrice();

                } else {
                    flag = true;

                    sp2.putBoolean(ShoppingCartActivity.this,Constant.FIRST_SHOW,true);
                    titleBarHelper.setRightText("编辑");
                    mCart_Btn_Detele.setVisibility(View.GONE);
                    mCart_btn_pay.setVisibility(View.VISIBLE);
                    mCart_tv_all.setText("总计¥：0.0元");
//                    GoneAddSubView();
                    AllSelectedPrice();

                }
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        dialog =  new ProgressWheelDialog(ShoppingCartActivity.this);
        mRecycle_outside = (RecyclerView) contentView.findViewById(R.id.cart_recycle_outside);
        manager = new LinearLayoutManager(this);
        mRecycle_outside.setLayoutManager(manager);
        //优化性能
        mRecycle_outside.setHasFixedSize(true);
        //删除
        mCart_Btn_Detele = (Button) contentView.findViewById(R.id.cart_btn_delete);
        //结算
        mCart_btn_pay = (Button) contentView.findViewById(R.id.cart_btn_pay);
        //总计：
        mCart_tv_all = (TextView) contentView.findViewById(R.id.cart_tv_all);
        mCart_tv_all.setText("总计¥："+"0.0"+"元");

        //全选的 CheckBox
        mAll_checkBox = (CheckBox) contentView.findViewById(R.id.shop_all_checkbox);
        //全选 CheckBox监听
        mAll_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){


                    for (int i = 0;i < mOutside_datas.size();i++){
                        //选择店铺
                        if (!mOutside_datas.get(i).ischeck()){
                            mOutside_datas.get(i).setIscheck(true);
                        }
                        for (int j = 0;j <  mOutside_datas.get(i).getList().size();j++){
                            //选择店铺的商品
                            if (!mOutside_datas.get(i).getList().get(j).ischeck()){
                                mOutside_datas.get(i).getList().get(j).setIscheck(true);
                            }
                        }
                    }
                }else {

                    //只有当点击全不选时才执行
                    // 解决点击取消选择店铺或商品时，
                    // 全选按钮取消选择状态，不会不变成全不选
                    if (allSelect() == mOutside_datas.size()){
                        for (int i = 0;i < mOutside_datas.size();i++){
                            if (mOutside_datas.get(i).ischeck()){
                                mOutside_datas.get(i).setIscheck(false);
                            }
                            for (int j = 0;j <  mOutside_datas.get(i).getList().size();j++){
                                if (mOutside_datas.get(i).getList().get(j).ischeck()){
                                    mOutside_datas.get(i).getList().get(j).setIscheck(false);
                                }
                            }
                        }
                    }
                }

                //更新
                UpdateRecyclerView();
                AllSelectedPrice();
            }
        });

        //各种点击事件
        mCart_Btn_Detele.setOnClickListener(this);
        mCart_btn_pay.setOnClickListener(this);
    }
   //----------------------------
    /**
     * 点击事件
     * @param view
     */
    public void onClick(View view){

        switch (view.getId()){
            //删除
            case R.id.cart_btn_delete:

                String mSc_id_all_Deleted =  AllSelectedScid();

                if(AllSelectedScid().length()!=0){
                    //删除的网络请求
                    ShowDialog();
                    DeleteShoppingCartDatas(mSc_id_all_Deleted);
                }else {
                    ToastUtil.showToast("请选择最少一件商品进行删除！");
                }
                break;

            //结算
            case R.id.cart_btn_pay:
                mSc_id_all = AllSelectedScid();
                ArrayList<ShopPingCartResult.DataBean.ListBean> listBeen = AllSelectedItem();
                if(listBeen.size()>0) {
                    Intent intent = new Intent(this, WyPaymentDetailActivity.class);
                    intent.putExtra("mSc_id_All", mSc_id_all);
                    intent.putExtra("listBean", listBeen);
                    startActivityForResult(intent, CART_JIESUAN);
                }else {
                    ToastUtil.showToast("请选择最少一件商品");
                }
            break;
        }
    }

    /*
      *解决Recycleyview刷新报错问题
     */
    private void UpdateRecyclerView() {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };
        handler.post(r);
    }
    //计算店铺的选择数量
    private int allSelect(){
        int sum = 0;
        for (int i = 0; i < mOutside_datas.size(); i++) {
            if (mOutside_datas.get(i).ischeck()){
                sum++;
            }
        }
        if(sum==0){
            if(flag){
                mCart_tv_all.setText("总计¥：" + 0.0 + "元");

            }else {
                mCart_tv_all.setText("已选" + 0 + "项");
            }

        }
        return sum;
    }
    //计算每个店铺商品的选择数量
    private int allChildSelect(int position){

//        AllSelectedPrice();
        int sum = 0;
            for (int i = 0; i < mOutside_datas.get(position).getList().size(); i++) {

                if (mOutside_datas.get(position).getList().get(i).ischeck()) {
                    sum++;
                }
            }
        if (sum==0){
            if(flag){
                mCart_tv_all.setText("总计¥：" + 0.0 + "元");
            }else {

                mCart_tv_all.setText("已选" + 0 + "项");
            }

        }
            return sum;
        }







    //计算全部选中的item 加入一个新的集合 传递给 填写支付信息页面
    private ArrayList<ShopPingCartResult.DataBean.ListBean> AllSelectedItem(){



       ArrayList<ShopPingCartResult.DataBean.ListBean> result = new ArrayList();

        for(int j=0;j<mOutside_datas.size();j++){
            for (int k=0;k<mOutside_datas.get(j).getList().size(); k++){
                if (mOutside_datas.get(j).getList().get(k).ischeck()) {
                  //这个 ShopPingCartResult.DataBean.ListBean 就是 购物车列表的bean
                    ShopPingCartResult.DataBean.ListBean listBean = mOutside_datas.get(j).getList().get(k);

                    result.add(listBean);

                }
            }
        }
       return result ;

    }





    /**
     * 网络加载购物车数据
     */
    public void LoadDataFromNet(String userid) {
        ShowDialog();
//        getPreCache(id);
        if (!Utils.isNetworkAvailable(UIUtil.getContext())) {
            return;
        }
        ShoppingCartDao dao = new ShoppingCartDao();
        dao.getShoppingCartTask(userid, new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                DismissDialog();
                ShopPingCartResult getshopPingCartResult = (ShopPingCartResult) object;
                mOutside_datas=getshopPingCartResult.getData();

                ShowDatasView(mOutside_datas);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void resFailure(String message) {

            }
        });

    }

    //显示数据
   public void ShowDatasView(final List<ShopPingCartResult.DataBean> mOutside_datas){

        adapter = new ShopPingCartOutSideRecycleAdapter(mOutside_datas);
        mRecycle_outside.setAdapter(adapter);

       adapter.setAddButtonClickListener(new AddButtonClickListener() {
           @Override
           public void AddButtonClickListener(View v, int value) {

               sp2.putBoolean(ShoppingCartActivity.this,Constant.FIRST_SHOW,false);


               ToUpDateShopCart(mUpDate_scid+"",mUpDate_number+"");

           }
       });
       adapter.setSubButtonClickListener(new SubButtonClickListener() {
           @Override
           public void SubButtonClickListener(View v, int value) {


               sp2.putBoolean(ShoppingCartActivity.this,Constant.FIRST_SHOW,false);

                   ToUpDateShopCart(mUpDate_scid+"",mUpDate_number+"");



           }
       });

       adapter.setCallBack(new ShopPingCartOutSideRecycleAdapter.allCheck() {

           @Override
           public void OnCheckListener(boolean isSelected, int position) {

               //保存店铺点击状态
               mOutside_datas.get(position).setIscheck(isSelected);
               //通知全选CheckBox的选择状态
               if (allSelect() == mOutside_datas.size()){
                   mAll_checkBox.setChecked(true);
               }else {
                   mAll_checkBox.setChecked(false);
               }

               if (isSelected){
                   for (int i = 0;i <  mOutside_datas.get(position).getList().size();i++){
                       if (!mOutside_datas.get(position).getList().get(i).ischeck()){
                           mOutside_datas.get(position).getList().get(i).setIscheck(true);
                       }
                   }
                   AllSelectedPrice();
               }else {
                   // 解决点击取消选择商品时，
                   // 店铺全选按钮取消选择状态，变成全不选
                   if (allChildSelect(position) == mOutside_datas.get(position).getList().size()){
                       for (int i = 0;i <  mOutside_datas.get(position).getList().size();i++){
                           if (mOutside_datas.get(position).getList().get(i).ischeck()){
                               mOutside_datas.get(position).getList().get(i).setIscheck(false);
                           }
                       }
                   }
                   AllSelectedPrice();
               }

               //更新
               UpdateRecyclerView();

           }

           @Override
           public void OnItemCheckListener(boolean isSelected, int parentposition, int chaildposition) {

               //保存商品点击状态
               mOutside_datas.get(parentposition).getList().get(chaildposition).setIscheck(isSelected);

               //通知店铺选择的状态
               if (allChildSelect(parentposition) == mOutside_datas.get(parentposition).getList().size()){
                   mOutside_datas.get(parentposition).setIscheck(true);
                   AllSelectedPrice();
               }else {
                   mOutside_datas.get(parentposition).setIscheck(false);
                   AllSelectedPrice();
               }
               UpdateRecyclerView();

           }
       });
   }
    //计算全部选中的item的价格总和 也就是总计价格
    public void   AllSelectedPrice(){
        float Price =0;
        int sum = 0;
        for(int j=0;j<mOutside_datas.size();j++){
            for (int k=0;k<mOutside_datas.get(j).getList().size(); k++){
                if (mOutside_datas.get(j).getList().get(k).ischeck()) {
                    double mTotalPrice = mOutside_datas.get(j).getList().get(k).getTotalPrice();
                    Price+=mTotalPrice;
                    float mPrice = Price;
                    String format = DecimalFormat.getInstance().format(mPrice);
                    sum++;
                    if(flag){
                        mCart_tv_all.setText("总计¥：" + format + "元");
                    }else {

                        mCart_tv_all.setText("已选" + sum + "项");
                    }
                }
            }
        }

        if (sum==0){
            if(flag){
                mCart_tv_all.setText("总计¥：" + 0.0 + "元");
            }else {

                mCart_tv_all.setText("已选" + 0 + "项");
            }
        }
    }

    /**
     * 删除购物车数据
     */

    public void DeleteShoppingCartDatas(String scid){

        DeleteToShoppingCartDao dao = new DeleteToShoppingCartDao();
        dao.getDeleteShoppingCartTask(scid, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                DismissDialog();
                mOutside_datas.clear();
                //删除数据以后及时更新UI
                LoadDataFromNet(mUserId);
                adapter.notifyDataSetChanged();
                AllSelectedPrice();

                mAll_checkBox.setChecked(false);
                if(flag){
                    mCart_tv_all.setText("总计¥：" + 0.0 + "元");
                }else {

                    mCart_tv_all.setText("已选" + 0 + "项");
                }

                Intent intent = new Intent();
                setResult(Constant.CART_NUMBER, intent);

            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 修改购物车数量
     * @param scid
     * @param number
     */
   public void ToUpDateShopCart(String scid,String number){
       UpdaterShopDao dao = new UpdaterShopDao();
       dao.getUpdaterCartTask(scid, number, new ResponseResult() {
           @Override
           public void resSuccess(Object object) {

               LoadDataFromNet(mUserId);

               ToastUtil.showToast("修改成功！");
           }

           @Override
           public void resFailure(String message) {

           }
       });

    }

    /**
     * 隐藏加减控件
     */
    public void GoneAddSubView(){
        for (int i = 0; i < mRecycle_outside.getChildCount(); i ++)
        {
            View parent_adapter = mRecycle_outside.getChildAt(i);
            RecyclerView recyclerView_adapter = (RecyclerView)parent_adapter.findViewById(R.id.cart_recycle_inside);
            for (int j = 0; j < recyclerView_adapter.getChildCount(); j ++)
            {
                View parent_inner_adapter = recyclerView_adapter.getChildAt(j);
                mSub_addView = (NumberAddSubView) parent_inner_adapter.findViewById(R.id.shop_addsubview);
                mSub_addView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示加减控件
     */
    public void ShowAddSubView(){
        for (int i = 0; i < mRecycle_outside.getChildCount(); i ++)
        {
            View parent_adapter = mRecycle_outside.getChildAt(i);
            RecyclerView recyclerView_adapter = (RecyclerView)parent_adapter.findViewById(R.id.cart_recycle_inside);
            for (int j = 0; j < recyclerView_adapter.getChildCount(); j ++)
            {
                View parent_inner_adapter = recyclerView_adapter.getChildAt(j);
                mSub_addView = (NumberAddSubView) parent_inner_adapter.findViewById(R.id.shop_addsubview);
                mSub_addView.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== CART_JIESUAN&& data!=null){

            String ZhiFu_msg = data.getStringExtra("quxiao");

            if(ZhiFu_msg!=null&&"确定取消支付".equals(ZhiFu_msg)){
                LoadDataFromNet(mUserId);
                mAll_checkBox.setChecked(false);
                if(flag){
                    mCart_tv_all.setText("总计¥：" + 0.0 + "元");
                }else {

                    mCart_tv_all.setText("已选" + 0 + "项");
                }
            }


        }
    }

    //计算全部选中的 item也就是 scid 购物车ID
    private String  AllSelectedScid(){
        mAll_sc_id = "";
        StringBuilder builder = new StringBuilder();
        for(int j=0;j<mOutside_datas.size();j++){
            for (int k=0;k<mOutside_datas.get(j).getList().size(); k++){
                if (mOutside_datas.get(j).getList().get(k).ischeck()) {
                    int mSc_id = mOutside_datas.get(j).getList().get(k).getSc_id();
                    builder.append(mSc_id).append(",");
                }
            }
        }

        String allSc_id = String.valueOf(builder);
        if(allSc_id.length()!=0) {
            //切割掉最后的"，"号
            mAll_sc_id = allSc_id.substring(0, allSc_id.length() - 1);
        }else {
            ToastUtil.showToast("请选择最少一件商品");
        }
        return mAll_sc_id;
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
                        Thread.sleep(10);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShoppingCartActivity.this, NewProductDetailActivity.class);
        setResult(Constant.SHOPPINGCART_NUMBER, intent);
        finish();
    }
}
