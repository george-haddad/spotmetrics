package spotmetrics;

import javax.swing.UIManager;
import ij.IJ;
import ij.plugin.PlugIn;
import spotmetrics.ui.SpotMetricsFrame;
import spotmetrics.ui.UITool;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 5:47:09 PM - Sep 4, 2014
 */
public class SpotMetrics implements PlugIn {

        @Override
        public void run(String arg) {
                if (IJ.versionLessThan("1.51h")) {
                        IJ.showMessage("This plugin needs to run on ImageJ v1.51h and above");
                }
                else {
                        try {
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        }
                        catch (Exception e) {
                                //Will use the standard Look&Feel
                        }

                        SpotMetricsFrame frame = new SpotMetricsFrame();
                        UITool.center(frame);
                        frame.setVisible(true);
                }
        }
        
        //For stand alone testing
        public static void main(String ...args) {
                new SpotMetrics().run("");
        }
}
