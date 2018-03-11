package com.simon.toastlib.core;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于管理Toast
 *
 * @author Simon
 * @version v1.0
 * @date 2018/3/11
 */

final class ExToastManager {

    /**
     * ExToastManager实例
     */
    private static ExToastManager INSTANCE = null;

    /**
     * 消息超时标志
     */
    private static final int MESSAGE_TIMEOUT = 2;

    /**
     * ExToastRecord 队列
     */
    private final List<ExToastRecord> mToastQueue = new ArrayList<>();

    /**
     * 消息队列
     */
    private Handler mHandler;

    /**
     * 私有化构造函数
     */
    private ExToastManager() {
        mHandler = new WorkerHandler();
    }

    /**
     * 获取{@link ExToastManager} 实例
     */
    static ExToastManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ExToastManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExToastManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 将Toast 加入队列
     */
    void enqueueToast(ExToast.IAgentOperation callback, int duration) {
        if (callback == null) {
            return;
        }
        // 如果已经包含此Toast 则更新其时间
        synchronized (mToastQueue) {
            ExToastRecord record;
            int index = indexOfToastLocked(callback);
            // If it's already in the queue, we update it in place, we don't
            // move it to the end of the queue.
            if (index >= 0) {
                record = mToastQueue.get(index);
                record.update(duration);
            } else {
                // 如果不包含
                //添加进去
                record = new ExToastRecord(callback, duration);
                mToastQueue.add(record);
                index = mToastQueue.size() - 1;
            }
            if (index == 0) {
                showNextToastLocked();
            }
        }
    }

    /**
     * 显示下一个Toast
     */
    private void showNextToastLocked() {
        ExToastRecord record = mToastQueue.get(0);
        record.callback.show();
        scheduleTimeoutLocked(record);
    }

    /**
     * 调度显示超时的消息
     */
    private void scheduleTimeoutLocked(ExToastRecord r) {
        mHandler.removeCallbacksAndMessages(r);
        Message m = Message.obtain(mHandler, MESSAGE_TIMEOUT, r);
        long delay = r.duration;
        mHandler.sendMessageDelayed(m, delay);
    }

    /**
     * 获取Toast 所在位置 lock on mToastQueue
     */
    private int indexOfToastLocked(ExToast.IAgentOperation callback) {
        List<ExToastRecord> list = mToastQueue;
        int len = list.size();
        for (int i = 0; i < len; i++) {
            ExToastRecord r = list.get(i);
            if (r.callback == callback) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 取消Toast
     */
    void cancelToast(ExToast.IAgentOperation callback) {
        if (callback == null) {
            return;
        }

        synchronized (mToastQueue) {
            int index = indexOfToastLocked(callback);
            if (index >= 0) {
                cancelToastLocked(index);
            }
        }
    }

    final class WorkerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIMEOUT:
                    handleTimeout((ExToastRecord) msg.obj);
                    break;
                default:
                    break;

            }
        }

    }

    /**
     * 发射显示超时
     *
     * @param record ExToastRecord
     */
    private void handleTimeout(ExToastRecord record) {
        synchronized (mToastQueue) {
            int index = indexOfToastLocked(record.callback);
            if (index >= 0) {
                cancelToastLocked(index);
            }
        }
    }

    /**
     * 取消Toast
     */
    private void cancelToastLocked(int index) {
        ExToastRecord record = mToastQueue.get(index);
        record.callback.hide();

        mToastQueue.remove(index);

        if (mToastQueue.size() > 0) {
            // Show the next one. If the callback fails, this will remove
            // it from the list, so don't assume that the list hasn't changed
            // after this point.
            showNextToastLocked();
        }
    }
}
