package net.wirex;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Ritchie Borja
 */
public class WirexLock {

    private WirexLock() {

    }
    
    private static class SingletonHolder {
        public static final WirexLock INSTANCE = new WirexLock();
    }
    
    public static WirexLock getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    private final Semaphore prepareLock = new Semaphore(5);
    
    private final Semaphore receivingLock = new Semaphore(10);
    
    private final Semaphore sendingLock = new Semaphore(1);
    
    public void lockPrepare() throws InterruptedException {
        prepareLock.acquire();
    }
    
    public void unlockPrepare() {
        prepareLock.release();
    }
    
    public void lockReceiving() throws InterruptedException {
        receivingLock.acquire();
    }
    
    public void unlockReceiving() {
        receivingLock.release();
    }
    
    public void lockSending() throws InterruptedException {
        sendingLock.acquire();
    }
    
    public void unlockSending() {
        sendingLock.release();
    }
}
