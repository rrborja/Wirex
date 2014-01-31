package net.wirex;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public class WirexLock {

    private static final Logger LOG = LoggerFactory.getLogger(WirexLock.class.getSimpleName());

    private WirexLock() {

    }

    private static class SingletonHolder {

        public static final WirexLock INSTANCE = new WirexLock();
    }

    public static WirexLock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final Semaphore receivingLock = new Semaphore(10);

    private final Semaphore sendingLock = new Semaphore(2);

    private final Lock presenterLock = new ReentrantLock();
    
    private final Lock clearLock = new ReentrantLock();

    public void lockReceiving() {
        try {
            receivingLock.acquire();
        } catch (InterruptedException ex) {
            LOG.error("Incoming connection Lock failed.", ex);
        }
    }

    public void unlockReceiving() {
        receivingLock.release();
    }

    public void lockSending() {
        try {
            sendingLock.acquire();
        } catch (InterruptedException ex) {
            LOG.error("Uploading connection Lock failed.", ex);
        }
    }

    public void unlockSending() {
        sendingLock.release();
    }

    public void lockPresenter() {
        presenterLock.lock();
    }

    public void unlockPresenter() {
        presenterLock.unlock();
    }

    public void lockClear() {
        clearLock.lock();
    }

    public void unlockClear() {
        clearLock.unlock();
    }
}
