package spotmetrics.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import spotmetrics.data.save.SavablePanel;
import spotmetrics.data.save.Savables;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 17, 2015
 *
 */
public class ProcessingPanel extends JPanel implements SavablePanel {

        private static final long serialVersionUID = -7762522817194499710L;

        private JLabel subBackgroundLabel = null;
        private JSpinner subBackgroundSpinner = null;
        private JCheckBox darkBbgCheckBox = null;

        private JLabel thresholdLabel = null;
        private JComboBox<String> thresholdComboBox = null;

        public ProcessingPanel() {
                subBackgroundLabel = new JLabel("Subtract Background:");
                subBackgroundLabel.setHorizontalAlignment(JLabel.RIGHT);
                subBackgroundLabel.setPreferredSize(new Dimension(130, 22));

                subBackgroundSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 90, 1));
                subBackgroundSpinner.setPreferredSize(new Dimension(60, 20));
                darkBbgCheckBox = new JCheckBox("Dark Background", false);

                JPanel subtractBgPanel = new JPanel(new BorderLayout(5, 5));
                subtractBgPanel.add(subBackgroundLabel, BorderLayout.WEST);
                subtractBgPanel.add(subBackgroundSpinner, BorderLayout.CENTER);
                subtractBgPanel.add(darkBbgCheckBox, BorderLayout.EAST);

                //-------------------------------------------------------------------------

                thresholdLabel = new JLabel("Threshold Method:");
                thresholdLabel.setHorizontalAlignment(JLabel.RIGHT);
                thresholdLabel.setPreferredSize(new Dimension(130, 22));

                thresholdComboBox = new JComboBox<String>(new String[] { "Default", "Huang", "Intermodes", "IsoData", "IJ_IsoData", "Li", "MaxEntropy", "Mean", "MinError", "Minimum", "Moments", "Otsu", "Percentile", "RenyiEntropy", "Shanbhag", "Triangle", "Yen" });

                JPanel thresholdPanel = new JPanel(new BorderLayout(5, 5));
                thresholdPanel.add(thresholdLabel, BorderLayout.WEST);
                thresholdPanel.add(thresholdComboBox, BorderLayout.CENTER);

                Box processingBox = Box.createVerticalBox();
                processingBox.add(subtractBgPanel);
                processingBox.add(Box.createVerticalStrut(5));
                processingBox.add(thresholdPanel);

                setLayout(new BorderLayout(5, 5));
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                add(processingBox, BorderLayout.NORTH);
        }

        public final int getRollingValue() {
                return ((Integer) subBackgroundSpinner.getModel().getValue()).intValue();
        }

        public final String getThresholdName() {
                return (String) thresholdComboBox.getSelectedItem();
        }

        public final boolean isDarkBackground() {
                return darkBbgCheckBox.isSelected();
        }

        @Override
        public Map<Savables,String> getSavableData() {
                Map<Savables,String> savableData = new HashMap<Savables,String>();
                savableData.put(Savables.PROCESSING_SUBTRACT_BACKGROUND, String.valueOf(subBackgroundSpinner.getModel().getValue()));
                savableData.put(Savables.PROCESSING_DARK_BACKGROUND, String.valueOf(darkBbgCheckBox.isSelected()));
                savableData.put(Savables.PROCESSING_THRESHOLD_METHOD, String.valueOf(thresholdComboBox.getSelectedItem()));
                return savableData;
        }

        @Override
        public void setSavableData(Map<Savables, String> savableData) {
                if(savableData.containsKey(Savables.PROCESSING_SUBTRACT_BACKGROUND)) {
                        String value = savableData.get(Savables.PROCESSING_SUBTRACT_BACKGROUND);
                        subBackgroundSpinner.getModel().setValue(Integer.valueOf(value));
                }
                
                if(savableData.containsKey(Savables.PROCESSING_DARK_BACKGROUND)) {
                        String value = savableData.get(Savables.PROCESSING_DARK_BACKGROUND);
                        darkBbgCheckBox.setSelected(Boolean.parseBoolean(value));
                }
                
                if(savableData.containsKey(Savables.PROCESSING_THRESHOLD_METHOD)) {
                        String value = savableData.get(Savables.PROCESSING_THRESHOLD_METHOD);
                        thresholdComboBox.setSelectedItem(value);
                }
        }
}
