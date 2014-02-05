package net.wirex;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Timer;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.CustomBalloonTip;

/**
 * 
 * @author Tim Molderez
 */
class ToolTipController extends MouseAdapter implements MouseMotionListener {

    private final BalloonTip balloonTip;
    private final Timer initialTimer;
    private final Timer showTimer;

    public ToolTipController(final BalloonTip balloonTip, int initialDelay, int showDelay) {
        super();
        this.balloonTip = balloonTip;
        initialTimer = new Timer(initialDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                balloonTip.setVisible(true);
                showTimer.start();
            }
        });
        initialTimer.setRepeats(false);

        showTimer = new Timer(showDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                balloonTip.setVisible(false);
            }
        });
        showTimer.setRepeats(false);

//        Component[] components = this.balloonTip.getContents().getComponents();
//        for (Component component : components) {
//            component.addMouseListener(new MouseListener() {
//
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    balloonTip.setVisible(true);
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    balloonTip.setVisible(true);
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                }
//                
//            });
//        }
        
        this.balloonTip.getContents().addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                    balloonTip.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //balloonTip.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                int x = balloonTip.getAttachedRectangle().x;
                int y = balloonTip.getAttachedRectangle().y;
                int height = balloonTip.getAttachedRectangle().height;
                int width = balloonTip.getAttachedRectangle().width;
                int currentX = e.getX();
                int currentY = e.getY();
                if (x < currentX && currentX < x + height) {
                    //balloonTip.setVisible(true);
                } else if (y < currentY && currentY < y + width) {
                    //balloonTip.setVisible(true);
                } else {
                    balloonTip.setVisible(false);
                }
            }

        });
//        this.balloonTip.addMouseListener(new MouseListener() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                balloonTip.setVisible(true);
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                balloonTip.setVisible(false);
//            }
//
//        });
    }

    public void mouseEntered(MouseEvent e) {
        initialTimer.start();
    }

    public void mouseMoved(MouseEvent e) {
        if (balloonTip instanceof CustomBalloonTip) {
            // If the mouse is within the balloon tip's attached rectangle
            if (((CustomBalloonTip) balloonTip).getOffset().contains(e.getPoint())) {
                if (!balloonTip.isVisible() && !initialTimer.isRunning()) {
                    initialTimer.start();
                }
            } else {
                stopTimers();
                balloonTip.setVisible(false);
            }
        }
    }

    public void mouseExited(MouseEvent e) {
        stopTimers();
        balloonTip.setVisible(false);
    }

    public void mousePressed(MouseEvent e) {
        stopTimers();
        balloonTip.setVisible(false);
    }

    /*
     * Stops all timers related to this tool tip
     */
    private void stopTimers() {
        initialTimer.stop();
        showTimer.stop();
    }

}
