package app.student.forum.util;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicCounter {

    private final AtomicLong counter = new AtomicLong();

    public void increment() {
        counter.incrementAndGet();
    }

    public Long get() {
        return counter.get();
    }
}
