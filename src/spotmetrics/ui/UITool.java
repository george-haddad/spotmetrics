package spotmetrics.ui;

import java.awt.Toolkit;
import java.awt.Window;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 9:26:05 AM - Sep 19, 2014
 * 
 */
public final class UITool {

        private UITool() {

        }

        /**
         * <p>Centers an given Window to the user's screen.</p>
         * 
         * @param w
         * 	- the window to center
         */
        public static void center(Window w) {
                int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

                int windowWidth = w.getWidth();
                int windowHeight = w.getHeight();

                if (windowHeight > screenHeight) {
                        return;
                }

                if (windowWidth > screenWidth) {
                        return;
                }

                int x = (screenWidth - windowWidth) / 2;
                int y = (screenHeight - windowHeight) / 2;

                w.setLocation(x, y);
        }

        /**
         * 
         * @param path - the path and name of the image
         * @return the icon as an {@link ImageIcon} object
         */
        public static ImageIcon getImageIcon(String path) {
                ImageIcon imageIcon = null;
                URL iconURL = getIconPath(path);

                if (iconURL != null) {
                        imageIcon = new ImageIcon(iconURL);
                }

                return imageIcon;
        }

        /**
         * <p>Returns the URL of an ImageIcon given its path and name.</p>
         * 
         * @param iconPath - the path and name of the icon
         * @return the {@link URL} of the icon
         */
        public static URL getIconPath(String iconPath) {
                URL iconURL = null;
                iconURL = UITool.class.getResource(iconPath);
                return iconURL;
        }
}
