package com.jx.intelligent.intf;

import android.content.Context;
import android.view.View;

/**
 * 切换页面的接口
 */

public interface ISwitchView {
    /**
     * 获取上下文
     * @return
     */
    Context getContext();

    /**
     * 获取显示数据的View
     * @return
     */
    View getDataView();

    /**
     * 获取当前正在显示的View
     * @return
     */
    View getCurrentView();

    /**
     * 切换View
     * @param view 需要显示的View
     */
    void showSwitchLayout(View view);

    /**
     * 切换View
     * @param layoutId 需要显示布局id
     */
    void showSwitchLayout(int layoutId);

    /**
     * 恢复显示数据的View<
     */
    void restoreLayout();

    /**
     * 实例化布局
     * @param layoutId 需要实例化的布局id
     * @return View
     */
    View inflate(int layoutId);
}
