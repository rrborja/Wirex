package net.wirex;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public class WirexLock {

    private static final Logger LOG = LoggerFactory.getLogger(WirexLock.class.getSimpleName());

    private WirexLock() {
        this.sendingThroughput = 0;
        this.receivingThroughput = 0;
    }

    private static class SingletonHolder {

        public static final WirexLock INSTANCE = new WirexLock();
    }

    public static WirexLock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final Semaphore receivingLock = new Semaphore(10);

    private final Semaphore sendingLock = new Semaphore(2);

    private int sendingThroughput;

    private int receivingThroughput;

    private final int DOWNTHROUGHPUT = 2097152;

    private final int UPTHROUGHPUT = 524288;

    public void lockReceiving(int bytes) {
        receivingThroughput += bytes;
        try {
            receivingLock.acquire();
            synchronized (this) {
                while (receivingThroughput >= DOWNTHROUGHPUT) {
                    wait();
                }
            }
        } catch (InterruptedException ex) {
            LOG.error("Incoming connection Lock failed.", ex);
        }
    }

    public void unlockReceiving(int bytes) {
        receivingThroughput -= bytes;
        receivingLock.release();
        synchronized (this) {
            notifyAll();
        }
    }

    public void lockSending(int bytes) {
        sendingThroughput += bytes;
        try {
            sendingLock.acquire();
            synchronized (this) {
                while (sendingThroughput >= UPTHROUGHPUT) {
                    wait();
                }
            }
        } catch (InterruptedException ex) {
            LOG.error("Uploading connection Lock failed.", ex);
        }
    }

    public void unlockSending(int bytes) {
        sendingThroughput -= bytes;
        sendingLock.release();
        synchronized (this) {
            notifyAll();
        }
    }
}
