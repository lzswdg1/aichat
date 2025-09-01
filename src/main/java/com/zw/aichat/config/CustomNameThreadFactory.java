package com.zw.aichat.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomNameThreadFactory  implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public CustomNameThreadFactory(String name) {

        //不再使用securityManager
        this.group = Thread.currentThread().getThreadGroup();

        String basename = (name == null || name.isBlank()) ? "pool" : name;
        this.namePrefix = basename + "-" + poolNumber.getAndIncrement() + "-thread-";

    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
