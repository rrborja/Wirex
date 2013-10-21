package net.visp.mvpshortcut;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.visp.wirex.annotations.Event;
import net.visp.wirex.annotations.Data;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    
}
