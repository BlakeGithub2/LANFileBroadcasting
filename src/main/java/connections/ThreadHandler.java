package connections;

public abstract class ThreadHandler {
    private Thread thread;
    private boolean active;

    public void toggle() {
        if (!active) {
            thread = createThread();
            thread.start();
        } else {
            thread.interrupt();
        }
        active = !active;
    }

    public abstract Thread createThread();
    public boolean isActive() {
        return active;
    }
}
