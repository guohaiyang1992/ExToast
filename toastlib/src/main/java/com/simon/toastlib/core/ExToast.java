package com.simon.toastlib.core;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.toastlib.BuildConfig;
import com.simon.toastlib.utils.RomUtils;

/**
 * 拓展版的Toast
 * 1.仿照原生Toast，自定义消息队列
 * 2.支持miui、flyme 、emui、锤子 等国产rom
 * 3.支持android 7.0 以上的系统
 *
 * @author Simon
 * @date 2018/3/11
 */
public class ExToast {
    /**
     * TAG
     */
    private static final String TAG = "ExToast";

    /**
     * 默认的Y的高度 64dp
     */
    private static final int FINAL_Y = 64;

    /**
     * 显示 短时间
     */
    public static final int LENGTH_SHORT = 1500;

    /**
     * 显示 长时间
     */
    public static final int LENGTH_LONG = 2500;

    /**
     * 上下文
     */
    final Context mContext;
    /**
     * Toast的显示隐藏的代理类
     */
    final AgentOperation mTN;

    /**
     * 显示时间
     */
    int mDuration;
    /**
     * 显示的View
     */
    View mNextView;

    /**
     * Toast 管理类
     */
    ExToastManager manager;

    /**
     * 构造函数
     *
     * @param context
     */
    public ExToast(Context context) {
        mContext = context;
        mTN = new AgentOperation();
        //默认距离底部64dp
        mTN.mY = dip2px(context, 64);
        //默认对齐方式 底部居中
        mTN.mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        //创建manager
        manager = ExToastManager.getInstance();
    }

    /**
     * 显示Toast
     */
    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }

        AgentOperation tn = mTN;
        tn.mNextView = mNextView;

        manager.enqueueToast(tn, mDuration);
    }

    /**
     * 取消显示Toast
     */
    public void cancel() {
        mTN.hide();

        manager.cancelToast(mTN);

    }

    /**
     * 设置View
     *
     * @param view 待显示的View
     */
    public void setView(View view) {
        mNextView = view;
    }

    /**
     * 获取需要显示的View
     *
     * @return 返回当前显示的View
     */
    public View getView() {
        return mNextView;
    }

    /**
     * 设置显示的时间
     *
     * @param duration 显示时间单位ms
     */
    public void setDuration(int duration) {
        mDuration = calculationDuration(duration);
        mTN.mDuration = duration;
    }

    /**
     * 获取显示时间
     *
     * @return 显示时间 单位ms
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * 设置横向纵向的margin
     *
     * @param horizontalMargin
     * @param verticalMargin
     */
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mTN.mHorizontalMargin = horizontalMargin;
        mTN.mVerticalMargin = verticalMargin;
    }

    /**
     * 获取横向的margin
     *
     * @return 横向margin 值
     */
    public float getHorizontalMargin() {
        return mTN.mHorizontalMargin;
    }

    /**
     * 获取纵向margin值
     *
     * @return 纵向margin值
     */
    public float getVerticalMargin() {
        return mTN.mVerticalMargin;
    }

    /**
     * 设置对齐方式和偏移量
     *
     * @param gravity 对其方式
     * @param xOffset x偏移
     * @param yOffset y偏移
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mTN.mGravity = gravity;
        mTN.mX = xOffset;
        mTN.mY = yOffset;
    }

    /**
     * 获取对齐方式
     *
     * @return 对齐方式
     */
    public int getGravity() {
        return mTN.mGravity;
    }

    /**
     * 获取x的偏移
     */
    public int getXOffset() {
        return mTN.mX;
    }

    /**
     * 获取y偏移
     */
    public int getYOffset() {
        return mTN.mY;
    }

    /**
     * 获取window的params
     */
    public WindowManager.LayoutParams getWindowParams() {
        return mTN.mParams;
    }

    /**
     * 创建一个Toast
     */
    public static ExToast makeText(Context context, CharSequence text, int duration) {
        ExToast result = new ExToast(context);

        View view = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
        checkViewNotNullThrowExpection(view);

        TextView tv = (TextView) view.findViewById(android.R.id.message);
        tv.setText(text);

        result.mNextView = view;
        result.mDuration = calculationDuration(duration);

        return result;
    }

    /**
     * 计算时间兼容Toast 的时间设定
     */
    private static int calculationDuration(int duration) {
        if (duration == Toast.LENGTH_SHORT) {
            duration = 1500;
        } else if (duration == Toast.LENGTH_LONG) {
            duration = 2500;
        } else if (duration < 0) {
            duration = 1500;
        }
        return duration;
    }

    /**
     * 检测View 内容，如果为null 则抛出异常
     *
     * @param view
     */
    private static void checkViewNotNullThrowExpection(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View can't be null!");
        }
    }

    /**
     * 创建一个Toast
     */
    public static ExToast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * 更新Toast 文字，在此之前确保Toast 使用{@link #makeText(Context, int, int)} 或者{@link #makeText(Context, CharSequence, int)}
     * 创建成功过
     *
     * @param resId 文字资源id
     */
    public void setText(int resId) {
        setText(mContext.getText(resId));
    }

    /**
     * 更新Toast 文字，在此之前确保Toast 使用{@link #makeText(Context, int, int)} 或者{@link #makeText(Context, CharSequence, int)}
     * 创建成功过
     *
     * @param s 文字内容
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(android.R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    /**
     * dp 转px
     */
    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 具体的操作代理类
     */
    private static class AgentOperation implements IAgentOperation {
        final Runnable mShow = new Runnable() {
            @Override
            public void run() {
                handleShow();
            }
        };

        final Runnable mHide = new Runnable() {
            @Override
            public void run() {
                handleHide();
                // Don't do this in handleHide() because it is also invoked by handleShow()
                mNextView = null;
            }
        };

        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        final Handler mHandler = new Handler();

        int mGravity;
        int mX, mY;
        float mHorizontalMargin;
        float mVerticalMargin;


        View mView;
        View mNextView;
        int mDuration;

        WindowManager mWM;


        AgentOperation() {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            //config window type
            configWindowType(params);

        }

        private void configWindowType(WindowManager.LayoutParams params) {
            if (params == null) {
                return;
            }
            //fix flyme os
            if (RomUtils.isFlyme()) {
                params.type = WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 37;
                return;
            }
            //fix if sdk>=7.1.1
            if (Build.VERSION.SDK_INT >= 25) {
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                params.type = WindowManager.LayoutParams.TYPE_TOAST;
            }

        }

        /**
         * schedule handleShow into the right thread
         */
        @Override
        public void show() {
            mHandler.post(mShow);
        }

        /**
         * schedule handleHide into the right thread
         */
        @Override
        public void hide() {
            mHandler.post(mHide);
        }

        /**
         * 添加window 显示Toast
         * （相比原有的Toast 修改Context 来源， 去除ToastType）
         */
        public void handleShow() {

            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                //原Toast 默认使用Application的Context 部分机型导致报错
                Context context = mView.getContext();
                mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                // We can resolve the Gravity here by using the Locale for getting
                // the layout direction
                final Configuration config = mView.getContext().getResources().getConfiguration();
                final int gravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mWM.addView(mView, mParams);
            }
        }

        /**
         * 隐藏window
         */
        public void handleHide() {
            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }

                mView = null;
            }
        }
    }

    /**
     * Toast 操作的代理接口类
     */
    interface IAgentOperation {
        /**
         * 显示
         */
        void show();

        /**
         * 隐藏
         */
        void hide();
    }
}
