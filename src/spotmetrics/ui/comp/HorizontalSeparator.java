package spotmetrics.ui.comp;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 10:15:43 AM - Sep 19, 2014
 * 
 */
public class HorizontalSeparator extends JPanel {

        private static final long serialVersionUID = 8083453959932842575L;

        /**
         * So simple
         */
        public HorizontalSeparator() {
                super();
                setForeground(Color.BLACK);
        }

        public HorizontalSeparator(Color color) {
                super();
                setForeground(color);
        }

        @Override
        public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int mid = getSize().height / 2;
                int width = getSize().width;
                g.setColor(Color.BLACK);
                g.drawLine(0, mid, width, mid);
        }
}
