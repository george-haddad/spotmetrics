package spotmetrics.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import spotmetrics.data.save.SavablePanel;
import spotmetrics.data.save.Savables;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 17, 2015
 *
 */
public class TrackingPanel extends JPanel implements SavablePanel {

        private static final long serialVersionUID = 8039875070656802888L;

        private JLabel blobDiameterLabel = null;
        private JSlider blobDiameterSlider = null;
        private JTextField blobDiameterValue = null;

        private JLabel blobThresholdLabel = null;
        private JSlider blobThresholdSlider = null;
        private JTextField blobThresholdValue = null;

        private JLabel linkingMaxDistanceLabel = null;
        private JSpinner linkingMaxDistanceSpinner = null;

        private JLabel gapClosingMaxDistanceLabel = null;
        private JSpinner gapClosingMaxDistanceSpinner = null;

        private JLabel gapClosingMaxFrameGapLabel = null;
        private JSpinner gapClosingMaxFrameGapSpinner = null;

        private JLabel initialSpotFilterValueLabel = null;
        private JSpinner initialSpotFilterValueSpinner = null;

        public TrackingPanel() {
                blobDiameterLabel = new JLabel("Blob Diameter:");
                blobDiameterLabel.setHorizontalAlignment(JLabel.RIGHT);
                blobDiameterLabel.setPreferredSize(new Dimension(120, 22));

                blobDiameterSlider = new JSlider(5, 100, 10);
                blobDiameterSlider.setMajorTickSpacing(50);
                blobDiameterSlider.setMinorTickSpacing(10);
                blobDiameterSlider.setPaintTicks(true);
                blobDiameterSlider.setSnapToTicks(false);
                blobDiameterSlider.setPaintLabels(false);
                blobDiameterSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                blobDiameterValue.setText("" + blobDiameterSlider.getValue());

                        }
                });

                blobDiameterValue = new JTextField("10", 3);
                blobDiameterValue.setBackground(Color.WHITE);
                blobDiameterValue.setEditable(false);

                JPanel blobDiameterPanel = new JPanel(new BorderLayout(5, 5));
                blobDiameterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                blobDiameterPanel.add(blobDiameterLabel, BorderLayout.WEST);
                blobDiameterPanel.add(blobDiameterSlider, BorderLayout.CENTER);
                blobDiameterPanel.add(blobDiameterValue, BorderLayout.EAST);

                //-----------------------------------------------------------------

                blobThresholdLabel = new JLabel("Blob Threshold:");
                blobThresholdLabel.setHorizontalAlignment(JLabel.RIGHT);
                blobThresholdLabel.setPreferredSize(new Dimension(120, 22));

                blobThresholdSlider = new JSlider(0, 20, 5);
                blobThresholdSlider.setMajorTickSpacing(10);
                blobThresholdSlider.setMinorTickSpacing(5);
                blobThresholdSlider.setPaintTicks(true);
                blobThresholdSlider.setSnapToTicks(false);
                blobThresholdSlider.setPaintLabels(false);
                blobThresholdSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                blobThresholdValue.setText("" + blobThresholdSlider.getValue());

                        }
                });

                blobThresholdValue = new JTextField("5", 3);
                blobThresholdValue.setBackground(Color.WHITE);
                blobThresholdValue.setEditable(false);

                //-----------------------------------------------------------------

                linkingMaxDistanceLabel = new JLabel("Linking Max Distance:");
                linkingMaxDistanceLabel.setHorizontalAlignment(JLabel.RIGHT);
                linkingMaxDistanceLabel.setPreferredSize(new Dimension(180, 22));

                linkingMaxDistanceSpinner = new JSpinner(new SpinnerNumberModel(15, 0, 100, 1));
                linkingMaxDistanceSpinner.setPreferredSize(new Dimension(40, 20));

                JPanel linkingMaxDistancePanel = new JPanel(new BorderLayout(5, 5));
                linkingMaxDistancePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
                linkingMaxDistancePanel.add(linkingMaxDistanceLabel, BorderLayout.WEST);
                linkingMaxDistancePanel.add(linkingMaxDistanceSpinner, BorderLayout.CENTER);

                //-----------------------------------------------------------------

                gapClosingMaxDistanceLabel = new JLabel("Gap Closing Max Distance:");
                gapClosingMaxDistanceLabel.setHorizontalAlignment(JLabel.RIGHT);
                gapClosingMaxDistanceLabel.setPreferredSize(new Dimension(180, 22));

                gapClosingMaxDistanceSpinner = new JSpinner(new SpinnerNumberModel(15, 0, 100, 1));
                gapClosingMaxDistanceSpinner.setPreferredSize(new Dimension(40, 20));

                JPanel gapClosingMaxDistancePanel = new JPanel(new BorderLayout(5, 5));
                gapClosingMaxDistancePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
                gapClosingMaxDistancePanel.add(gapClosingMaxDistanceLabel, BorderLayout.WEST);
                gapClosingMaxDistancePanel.add(gapClosingMaxDistanceSpinner, BorderLayout.CENTER);

                //-----------------------------------------------------------------

                gapClosingMaxFrameGapLabel = new JLabel("Gap Closing Max Fram Gap:");
                gapClosingMaxFrameGapLabel.setHorizontalAlignment(JLabel.RIGHT);
                gapClosingMaxFrameGapLabel.setPreferredSize(new Dimension(180, 22));

                gapClosingMaxFrameGapSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 20, 1));
                gapClosingMaxFrameGapSpinner.setPreferredSize(new Dimension(40, 20));

                JPanel gapClosingMaxFrameGapPanel = new JPanel(new BorderLayout(5, 5));
                gapClosingMaxFrameGapPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
                gapClosingMaxFrameGapPanel.add(gapClosingMaxFrameGapLabel, BorderLayout.WEST);
                gapClosingMaxFrameGapPanel.add(gapClosingMaxFrameGapSpinner, BorderLayout.CENTER);

                //-----------------------------------------------------------------

                initialSpotFilterValueLabel = new JLabel("Initial Spot Filter Value:");
                initialSpotFilterValueLabel.setHorizontalAlignment(JLabel.RIGHT);
                initialSpotFilterValueLabel.setPreferredSize(new Dimension(180, 22));

                initialSpotFilterValueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
                initialSpotFilterValueSpinner.setPreferredSize(new Dimension(40, 20));

                JPanel initialSpotFilterValuePanel = new JPanel(new BorderLayout(5, 5));
                initialSpotFilterValuePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
                initialSpotFilterValuePanel.add(initialSpotFilterValueLabel, BorderLayout.WEST);
                initialSpotFilterValuePanel.add(initialSpotFilterValueSpinner, BorderLayout.CENTER);

                //-----------------------------------------------------------------

                JPanel blobThresholdPanel = new JPanel(new BorderLayout(5, 5));
                blobThresholdPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                blobThresholdPanel.add(blobThresholdLabel, BorderLayout.WEST);
                blobThresholdPanel.add(blobThresholdSlider, BorderLayout.CENTER);
                blobThresholdPanel.add(blobThresholdValue, BorderLayout.EAST);

                Box trackingTabBox = Box.createVerticalBox();
                trackingTabBox.add(blobDiameterPanel);
                trackingTabBox.add(Box.createVerticalStrut(5));
                trackingTabBox.add(blobThresholdPanel);
                trackingTabBox.add(Box.createVerticalStrut(5));
                trackingTabBox.add(linkingMaxDistancePanel);
                trackingTabBox.add(Box.createVerticalStrut(5));
                trackingTabBox.add(gapClosingMaxDistancePanel);
                trackingTabBox.add(Box.createVerticalStrut(5));
                trackingTabBox.add(gapClosingMaxFrameGapPanel);
                trackingTabBox.add(Box.createVerticalStrut(5));
                trackingTabBox.add(initialSpotFilterValuePanel);

                //-----------------------------------------------------------------

                setLayout(new BorderLayout(5, 5));
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                add(trackingTabBox, BorderLayout.NORTH);
        }

        public final int getBlobDiameter() {
                return blobDiameterSlider.getValue();
        }

        public final int getBlobThreshold() {
                return blobThresholdSlider.getValue();
        }

        public final int getLinkingMaxDistance() {
                return ((Integer) linkingMaxDistanceSpinner.getModel().getValue()).intValue();
        }

        public final int getGapClosingMaxDistance() {
                return ((Integer) gapClosingMaxDistanceSpinner.getModel().getValue()).intValue();
        }

        public final int getGapClosingMaxFrameGap() {
                return ((Integer) gapClosingMaxFrameGapSpinner.getModel().getValue()).intValue();
        }

        public final int getInitialSpotFilterValue() {
                return ((Integer) initialSpotFilterValueSpinner.getModel().getValue()).intValue();
        }

        @Override
        public Map<Savables, String> getSavableData() {
                Map<Savables,String> savableData = new HashMap<Savables,String>();
                savableData.put(Savables.TRACK_BLOB_DIAMETER, String.valueOf(blobDiameterSlider.getValue()));
                savableData.put(Savables.TRACK_BLOB_THRESHOLD, String.valueOf(blobThresholdSlider.getValue()));
                savableData.put(Savables.TRACK_LINKING_MAX_DISTANCE, String.valueOf(linkingMaxDistanceSpinner.getModel().getValue()));
                savableData.put(Savables.TRACK_GAP_CLOSING_MAX_DISTANCE, String.valueOf(gapClosingMaxDistanceSpinner.getModel().getValue()));
                savableData.put(Savables.TRACK_GAP_CLOSING_MAX_FRAME_GAP, String.valueOf(gapClosingMaxFrameGapSpinner.getModel().getValue()));
                savableData.put(Savables.TRACK_INITIAL_SPOT_FILTER_VALUE, String.valueOf(initialSpotFilterValueSpinner.getModel().getValue()));
                return savableData;
        }
}
