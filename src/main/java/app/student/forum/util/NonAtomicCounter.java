package app.student.forum.util;

public class NonAtomicCounter {

    private int counter = 0;

    public void increment() {
        counter++;
    }

    public int get() {
        return counter;
    }
}
