package com.jx.intelligent.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.intelligent.R;
import com.jx.intelligent.constant.ToastType;
import com.jx.intelligent.view.OptAnimationLoader;
import com.jx.intelligent.view.SuccessTickView;


/**
 *Toast工具类
 */

public class ToastUtil {
    private static Toast toast;
    /**
     * 字体大小
     */
    private final static int FONT_SIZE = 16;

    private final static Context context = UIUtil.getContext();

    public static void showToast(String text) {
        showToast(text, ToastType.NULL, ToastType.SHORT);
    }

    public static void showToast(int textId) {
        showToast(textId, ToastType.NULL, ToastType.SHORT);
    }

    public static void showToast(int textId, int LONG) {
        showToast(textId, ToastType.NULL, LONG);
    }

    /**
     * @param textId
     * @param imgType {@link com.jx.intelligent.constant.ToastType}
     * @param LONG    0默认2s，1持续时间3.5s
     */
    public static void showToast(int textId, int imgType,
                                 int LONG) {
        String text = context.getResources().getString(textId);
        showToast(text, imgType, LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
    }

    /**
     * @param imgType {@link com.jx.intelligent.constant.ToastType}
     * @param LONG    0默认2s，1持续时间3.5s
     */
    public static void showToast(String text, int imgType,
                                 int LONG) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        show(text, imgType, LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
    }

    private static void show(String text, int imgType, int LONG) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.customtoast, null);
        ImageView image = (ImageView) layout.findViewById(R.id.warningToast_iv);
        if (imgType == ToastType.RIGHT) {
            image.setImageResource(R.mipmap.icon_right_normal);
        } else if (imgType == ToastType.ERROR) {
            image.setImageResource(R.mipmap.icon_error_normal);
        } else if (imgType == ToastType.WAIT) {

        } else if (imgType == ToastType.NULL) {
            image.setVisibility(View.GONE);
        }
        TextView tView = (TextView) layout.findViewById(R.id.textToast_tv);
        tView.setText(text);
        tView.setTextSize(FONT_SIZE);
        //        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     * @param textId
     * @param imgType {@link com.jx.intelligent.constant.ToastType}
     * @param LONG    0默认2s，1持续时间3.5s
     */
    public static void showToast(Handler mHandler,
                                 int textId, final int imgType, final int LONG) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        final String text = context.getResources().getString(textId);
        if (mHandler != null) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    showToast(text, imgType, LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * @param text
     * @param imgType {@link com.jx.intelligent.constant.ToastType}
     * @param LONG    0默认2s，1持续时间3.5s
     */
    public static void showToast(Handler mHandler,
                                 final String text, final int imgType, final int LONG) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(text, imgType, LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
                }
            });
        }
    }

    private static void showToast(TextView textView, int LONG) {
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setView(textView);
        toast.setDuration(LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 返回的是完整的toast提示，比如提示是"请删除[content]设备"
     *
     * @param context
     * @param id
     * @param content 动态显示的内容
     * @return
     */
    public static String getToastStr(Context context, int id, String content) {
        String str = context.getResources().getString(id);
        return String.format(str, content);
    }

    /**
     * 加载完成的结果弹窗动画,成功或失败,成功失败后的提示信息设置,二者选其一
     * @param type
     * @param LONG
     * @param sucMessage
     * @param failMessage
     */
    public static void showAnimationToast(int type, int LONG, final String sucMessage, final String failMessage){
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = new Toast(context);
        }
        AnimationSet animationSet = (AnimationSet) OptAnimationLoader.loadAnimation(context, R.anim.success_mask_layout);
        Animation animation = OptAnimationLoader.loadAnimation(context, R.anim.success_bow_roate);
        View promptView = LayoutUtil.inflate(context, R.layout.prompt_toast);
        final View maskLeft =promptView.findViewById(R.id.mask_left);
        final View maskRight =  promptView.findViewById(R.id.mask_right);
        final ImageView errorIv = (ImageView) promptView.findViewById(R.id.error);
        final TextView msg = (TextView) promptView.findViewById(R.id.msg_tv);
        final SuccessTickView successTickView = (SuccessTickView) promptView.findViewById(R.id.success_tick);
        maskLeft.startAnimation(animationSet.getAnimations().get(0));
        maskRight.startAnimation(animationSet.getAnimations().get(1));
        if(type == ToastType.SUCCESS){
            successTickView.setVisibility(View.VISIBLE);
            errorIv.setVisibility(View.GONE);
            successTickView.startTickAnim();
            maskRight.startAnimation(animation);
            successTickView.setOnAnimationEndListener(new SuccessTickView.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    msg.setText(sucMessage);
                    successTickView.clearAnimation();
                    maskRight.clearAnimation();
                    maskLeft.clearAnimation();
                }
            });
        }else if(type == ToastType.FAILURE){
            successTickView.setVisibility(View.GONE);
            maskRight.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    errorIv.setVisibility(View.VISIBLE);
                    msg.setText(failMessage);
                    maskRight.clearAnimation();
                    maskLeft.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        toast.setView(promptView);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(LONG == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 消失提示
     */
    public static void dismissToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * 在UI线程中消失
     *
     * @param mHandler
     */
    public static void dismiss(Handler mHandler) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (toast != null) {
                        toast.cancel();
                    }
                }
            });
        }
    }
}
