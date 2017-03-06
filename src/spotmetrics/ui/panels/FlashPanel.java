package spotmetrics.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import spotmetrics.analyzer.FlashDetect;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 17, 2015
 *
 */
public class FlashPanel extends JPanel {

        private static final long serialVersionUID = 6682806437593675558L;

        private JCheckBox detectFlashCheckBox = null;
        private JLabel flashDetectModeLabel = null;
        private JComboBox<FlashDetect> flashDetectModeComboBox = null;
        
        private JCheckBox deleteFlashOnlyCheckBox = null;
        
        private JLabel offsetBeforeLabel = null;
        private JSlider offsetBeforeSlider = null;
        private JTextField offsetBeforeValue = null;

        private JLabel offsetAfterLabel = null;
        private JSlider offsetAfterSlider = null;
        private JTextField offsetAfterValue = null;

        public FlashPanel() {
                detectFlashCheckBox = new JCheckBox("Detect Flash", true);
                detectFlashCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                detectFlashCheckBox_actionPerformed();
                        }
                });

                JPanel detectFlashCheckBoxPanel = new JPanel(new BorderLayout(5, 5));
                detectFlashCheckBoxPanel.add(detectFlashCheckBox, BorderLayout.WEST);
                
                //-------------------------------------------------------------------------
                
                deleteFlashOnlyCheckBox = new JCheckBox("Delete Flash Only", false);
                deleteFlashOnlyCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                deleteFlashOnlyCheckBox_actionPerformed();
                        }
                });
                
                JPanel deleteFlashOnlyCheckBoxPanel = new JPanel(new BorderLayout(5,5));
                deleteFlashOnlyCheckBoxPanel.add(deleteFlashOnlyCheckBox, BorderLayout.WEST);
                
                //-------------------------------------------------------------------------

                flashDetectModeLabel = new JLabel("Detection Mode:");
                flashDetectModeLabel.setHorizontalAlignment(JLabel.RIGHT);
                flashDetectModeLabel.setPreferredSize(new Dimension(125, 22));

                flashDetectModeComboBox = new JComboBox<FlashDetect>(FlashDetect.values());
                flashDetectModeComboBox.setSelectedIndex(0);
                flashDetectModeComboBox.setToolTipText(((FlashDetect) flashDetectModeComboBox.getSelectedItem()).getDesc());
                flashDetectModeComboBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                if (flashDetectModeComboBox.getSelectedItem() == null) {
                                        flashDetectModeComboBox.setToolTipText("");
                                }
                                else {
                                        FlashDetect fd = (FlashDetect) flashDetectModeComboBox.getSelectedItem();
                                        flashDetectModeComboBox.setToolTipText(fd.getDesc());
                                }
                        }
                });

                JPanel detectFlashModePanel = new JPanel(new BorderLayout(5, 5));
                detectFlashModePanel.add(flashDetectModeLabel, BorderLayout.WEST);
                detectFlashModePanel.add(flashDetectModeComboBox, BorderLayout.CENTER);

                //-------------------------------------------------------------------------

                offsetBeforeLabel = new JLabel("Before Flash Offset:");
                offsetBeforeLabel.setHorizontalAlignment(JLabel.RIGHT);
                offsetBeforeLabel.setPreferredSize(new Dimension(120, 22));

                offsetBeforeSlider = new JSlider(0, 240, 60);
                offsetBeforeSlider.setMajorTickSpacing(60);
                offsetBeforeSlider.setMinorTickSpacing(10);
                offsetBeforeSlider.setPaintTicks(true);
                offsetBeforeSlider.setSnapToTicks(true);
                offsetBeforeSlider.setPaintLabels(false);
                offsetBeforeSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                offsetBeforeValue.setText("" + offsetBeforeSlider.getValue());
                        }
                });

                offsetBeforeValue = new JTextField("60", 3);
                offsetBeforeValue.setBackground(Color.WHITE);
                offsetBeforeValue.setEditable(false);

                JPanel offsetBeforePanel = new JPanel(new BorderLayout(5, 5));
                offsetBeforePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                offsetBeforePanel.add(offsetBeforeLabel, BorderLayout.WEST);
                offsetBeforePanel.add(offsetBeforeSlider, BorderLayout.CENTER);
                offsetBeforePanel.add(offsetBeforeValue, BorderLayout.EAST);

                //-------------------------------------------------------------------------

                offsetAfterLabel = new JLabel("After Flash Offset:");
                offsetAfterLabel.setHorizontalAlignment(JLabel.RIGHT);
                offsetAfterLabel.setPreferredSize(new Dimension(120, 22));

                offsetAfterSlider = new JSlider(0, 240, 60);
                offsetAfterSlider.setMajorTickSpacing(60);
                offsetAfterSlider.setMinorTickSpacing(10);
                offsetAfterSlider.setPaintTicks(true);
                offsetAfterSlider.setSnapToTicks(true);
                offsetAfterSlider.setPaintLabels(false);
                offsetAfterSlider.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                                offsetAfterValue.setText("" + offsetAfterSlider.getValue());

                        }
                });

                offsetAfterValue = new JTextField("60", 3);
                offsetAfterValue.setBackground(Color.WHITE);
                offsetAfterValue.setEditable(false);

                JPanel offsetAfterPanel = new JPanel(new BorderLayout(5, 5));
                offsetAfterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                offsetAfterPanel.add(offsetAfterLabel, BorderLayout.WEST);
                offsetAfterPanel.add(offsetAfterSlider, BorderLayout.CENTER);
                offsetAfterPanel.add(offsetAfterValue, BorderLayout.EAST);

                Box flashTabBox = Box.createVerticalBox();
                flashTabBox.add(Box.createVerticalStrut(5));
                flashTabBox.add(detectFlashCheckBoxPanel);
                flashTabBox.add(Box.createVerticalStrut(5));
                flashTabBox.add(detectFlashModePanel);
                flashTabBox.add(Box.createVerticalStrut(5));
                flashTabBox.add(deleteFlashOnlyCheckBoxPanel);
                flashTabBox.add(Box.createVerticalStrut(5));
                flashTabBox.add(offsetBeforePanel);
                flashTabBox.add(Box.createVerticalStrut(5));
                flashTabBox.add(offsetAfterPanel);

                setLayout(new BorderLayout(5, 5));
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                add(flashTabBox, BorderLayout.NORTH);
        }
        
        private final void detectFlashCheckBox_actionPerformed() {
                boolean enabled = detectFlashCheckBox.isSelected();
                flashDetectModeLabel.setEnabled(enabled);
                flashDetectModeComboBox.setEnabled(enabled);
                deleteFlashOnlyCheckBox.setEnabled(enabled);
                
                if(enabled) {
                        deleteFlashOnlyCheckBox_actionPerformed();
                }
                else {
                        offsetBeforeLabel.setEnabled(enabled);
                        offsetBeforeSlider.setEnabled(enabled);
                        offsetBeforeValue.setEnabled(enabled);
                        offsetAfterLabel.setEnabled(enabled);
                        offsetAfterSlider.setEnabled(enabled);
                        offsetAfterValue.setEnabled(enabled);
                }
        }
        
        private final void deleteFlashOnlyCheckBox_actionPerformed() {
                if(deleteFlashOnlyCheckBox.isSelected()) {
                        offsetBeforeLabel.setEnabled(false);
                        offsetBeforeSlider.setEnabled(false);
                        offsetBeforeValue.setEnabled(false);
                        offsetAfterLabel.setEnabled(false);
                        offsetAfterSlider.setEnabled(false);
                        offsetAfterValue.setEnabled(false);
                }
                else {
                        offsetBeforeLabel.setEnabled(true);
                        offsetBeforeSlider.setEnabled(true);
                        offsetBeforeValue.setEnabled(true);
                        offsetAfterLabel.setEnabled(true);
                        offsetAfterSlider.setEnabled(true);
                        offsetAfterValue.setEnabled(true);
                }
        }

        public final FlashDetect getFlashDetectMode() {
                return (FlashDetect) flashDetectModeComboBox.getSelectedItem();
        }

        public final int getOffsetBefore() {
                return offsetBeforeSlider.getValue();
        }

        public final int getOffsetAfter() {
                return offsetAfterSlider.getValue();
        }

        public final boolean isDetectFlash() {
                return detectFlashCheckBox.isSelected();
        }
        
        public final boolean isDeleteFlashOnly() {
                return deleteFlashOnlyCheckBox.isSelected();
        }
}
