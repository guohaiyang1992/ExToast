package com.simon.toastlib.core;

/**
 * 用于记录Toast相关信息
 *
 * @author Simon
 * @version v1.0
 * @date 2018/3/11
 */

public class ExToastRecord {
    /**
     * 操作的代理类
     */
    final ExToast.IAgentOperation callback;
    /**
     * 时间 单位ms
     */
    int duration;

    ExToastRecord(ExToast.IAgentOperation callback, int duration) {
        this.callback = callback;
        this.duration = duration;
    }

    void update(int duration) {
        this.duration = duration;
    }


    @Override
    public String toString() {
        return "ExToastRecord{" +
                "callback=" + callback +
                ", duration=" + duration +
                '}';
    }
}
