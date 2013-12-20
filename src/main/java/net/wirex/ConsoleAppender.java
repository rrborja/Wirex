package net.wirex;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TextColor;
import org.apache.log4j.Level;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author Ritchie Borja
 */
public class ConsoleAppender extends WriterAppender {

    private static final Screen screen = ConsoleProcess.getScreen();

    @Override
    public void append(LoggingEvent event) {
        if (screen.getTerminalSize().getRows() < ConsoleProcess.ycursor) {
            ConsoleProcess.ycursor = 0;
            screen.getTerminal().clearScreen();
        }
        
        final String message = this.layout.format(event);
        ScreenWriter writer = new ScreenWriter(screen);
        Level level = event.getLevel();
        if (level.equals(Level.ERROR)) {
            writer.setForegroundColor(TextColor.ANSI.RED);
            writer.drawString(0, ConsoleProcess.ycursor++, message, ScreenCharacterStyle.Bold, ScreenCharacterStyle.Blinking);
            screen.refresh();
            return;
        } else if (level.equals(Level.WARN)) {
            writer.setForegroundColor(TextColor.ANSI.YELLOW);
        } else if (level.equals(Level.INFO)) {
            writer.setForegroundColor(TextColor.ANSI.WHITE);
        } else if (level.equals(Level.DEBUG)) {
            writer.setForegroundColor(TextColor.ANSI.GREEN);
        } else {
            writer.setForegroundColor(TextColor.ANSI.DEFAULT);
        }
        writer.drawString(0, ConsoleProcess.ycursor++, message);
//        screen.getTerminal().moveCursor(0, ConsoleProcess.ycursor);
        screen.refresh();
    }


}
