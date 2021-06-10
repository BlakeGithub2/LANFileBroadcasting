package connections;

public abstract class ConnectionThread extends Thread {
    protected boolean shouldStop;

    public ConnectionThread(String name) {
        super(name);
    }

    public void stopConnection() {
        shouldStop = true;
        interrupt();
    }
}
