package net.wirex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public class PrepareSemaphoreNode {

    private static final Logger LOG = LoggerFactory.getLogger(WirexLock.class.getSimpleName());

    private int children;
    private final PrepareSemaphoreNode node;

    public PrepareSemaphoreNode(int permits, PrepareSemaphoreNode node) {
        this.children = permits;
        this.node = node;
    }

    public void acquire() {
        synchronized (this) {
            while (children > 0) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    LOG.error("Prematurely interrupted MVP Preparation lock", ex);
                }
            }
        }
    }

    public void releaseFinally() {
        synchronized (this) {
            notify();
        }
    }

    public void release() {
        synchronized (this) {
            children--;
            if (node != null && children <= 0) {
                node.release();
                notify();
            } else {
                notify();
            }
        }
    }

    public void setPrepares(int numOfPrepares) {
        children = numOfPrepares;
    }

}
