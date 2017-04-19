package spotmetrics.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import layout.TableLayout;
import spotmetrics.analyzer.AnalysisOptions;
import spotmetrics.data.save.SavablePanel;
import spotmetrics.data.save.Savables;
import spotmetrics.ui.comp.RegexDocumentFilter;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 17, 2015
 *
 */
public class AnalysisPanel extends JPanel implements SavablePanel {

        private static final long serialVersionUID = 8797948870110036499L;
        private static final Pattern DECIMAL_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

        private JTextField minSizeField = null;
        private JTextField maxSizeField = null;
        private JCheckBox infinityCheckBox = null;
        private JTextField circMinField = null;
        private JTextField circMaxField = null;
        private JTextField xOffsetField = null;
        private JTextField yOffsetField = null;
        private JTextField wOffsetField = null;
        private JTextField hOffsetField = null;

        public AnalysisPanel() {
                JLabel sizeMinLabel = new JLabel("Size (pixel^2):");
                sizeMinLabel.setHorizontalAlignment(JLabel.RIGHT);
                minSizeField = new JTextField(3);
                minSizeField.setText("10");
                ((AbstractDocument) minSizeField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                JLabel sizeToLabel = new JLabel("to");
                sizeToLabel.setHorizontalAlignment(JLabel.CENTER);

                maxSizeField = new JTextField(3);
                ((AbstractDocument) maxSizeField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                infinityCheckBox = new JCheckBox("Infinity", true);
                infinityCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                infinityCheckBox_actionPerformed();
                        }
                });

                infinityCheckBox_actionPerformed();

                //-----------------------------------------------------------------

                JLabel circLabel = new JLabel("Circularity:");
                circLabel.setHorizontalAlignment(JLabel.RIGHT);

                circMinField = new JTextField(6);
                circMinField.setText("0.10");
                ((AbstractDocument) circMinField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d|\\.", 10));
                circMinField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                                validateDecimal_focusLost(e);
                        }
                });

                JLabel circToLabel = new JLabel("to");
                circToLabel.setHorizontalAlignment(JLabel.CENTER);

                circMaxField = new JTextField(6);
                circMaxField.setText("1.0");
                ((AbstractDocument) circMaxField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d|\\.", 10));
                circMaxField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                                validateDecimal_focusLost(e);
                        }
                });

                double spacer = 5;
                double[][] layoutSize1 = {
                                //                   0,      1,                      2,      3,                     4,      5,                     6,      7,               8
                                { TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.FILL }, { TableLayout.PREFERRED, //0
                                                spacer, TableLayout.PREFERRED  //2
                                } };

                JPanel analysisPanel = new JPanel(new TableLayout(layoutSize1));
                analysisPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder("Particle Analyzer")));
                analysisPanel.add(sizeMinLabel, "0, 0");
                analysisPanel.add(minSizeField, "2, 0");
                analysisPanel.add(sizeToLabel, "4, 0");
                analysisPanel.add(maxSizeField, "6, 0");
                analysisPanel.add(infinityCheckBox, "8, 0");
                analysisPanel.add(circLabel, "0, 2");
                analysisPanel.add(circMinField, "2, 2");
                analysisPanel.add(circToLabel, "4, 2");
                analysisPanel.add(circMaxField, "6, 2");

                //-----------------------------------------------------------------

                JLabel xOffsetLabel = new JLabel("X Offset:");
                xOffsetLabel.setHorizontalAlignment(JLabel.RIGHT);

                JLabel yOffsetLabel = new JLabel("Y Offset:");
                yOffsetLabel.setHorizontalAlignment(JLabel.RIGHT);

                JLabel wOffsetLabel = new JLabel("W:");
                wOffsetLabel.setHorizontalAlignment(JLabel.LEFT);

                JLabel hOffsetLabel = new JLabel("H:");
                hOffsetLabel.setHorizontalAlignment(JLabel.LEFT);

                xOffsetField = new JTextField(6);
                xOffsetField.setText("8");
                ((AbstractDocument) xOffsetField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                yOffsetField = new JTextField(6);
                yOffsetField.setText("8");
                ((AbstractDocument) yOffsetField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                wOffsetField = new JTextField(6);
                wOffsetField.setText("18");
                ((AbstractDocument) wOffsetField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                hOffsetField = new JTextField(6);
                hOffsetField.setText("18");
                ((AbstractDocument) hOffsetField.getDocument()).setDocumentFilter(new RegexDocumentFilter("\\d", 10));

                double[][] layoutSize2 = {
                                //                   0,       1,                     2,      3,                     4,      5,                     6
                                { TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED }, { TableLayout.PREFERRED, //0
                                                spacer, TableLayout.PREFERRED  //2
                                } };

                JPanel offsetPanel = new JPanel(new TableLayout(layoutSize2));
                offsetPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder("Initial ROI Offset")));
                offsetPanel.add(xOffsetLabel, "0, 0");
                offsetPanel.add(xOffsetField, "2, 0");
                offsetPanel.add(wOffsetLabel, "4, 0");
                offsetPanel.add(wOffsetField, "6, 0");
                offsetPanel.add(yOffsetLabel, "0, 2");
                offsetPanel.add(yOffsetField, "2, 2");
                offsetPanel.add(hOffsetLabel, "4, 2");
                offsetPanel.add(hOffsetField, "6, 2");

                //-----------------------------------------------------------------

                setLayout(new BorderLayout(5, 5));
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                add(analysisPanel, BorderLayout.NORTH);
                add(offsetPanel, BorderLayout.CENTER);
        }

        private final void validateDecimal_focusLost(FocusEvent e) {
                JTextField source = (JTextField) e.getSource();
                String text = source.getText();

                if (!DECIMAL_PATTERN.matcher(text).matches()) {
                        JOptionPane.showMessageDialog(this, "Circularity value must be a decimal to 2 decimal places");
                }
        }

        private final void infinityCheckBox_actionPerformed() {
                if (infinityCheckBox.isSelected()) {
                        RegexDocumentFilter regexDocFilter = (RegexDocumentFilter) ((AbstractDocument) maxSizeField.getDocument()).getDocumentFilter();
                        regexDocFilter.setRegex("Infinity");
                        maxSizeField.setText("Infinity");
                        maxSizeField.setEditable(false);
                        maxSizeField.setBackground(Color.WHITE);
                }
                else {
                        RegexDocumentFilter regexDocFilter = (RegexDocumentFilter) ((AbstractDocument) maxSizeField.getDocument()).getDocumentFilter();
                        regexDocFilter.setRegex("\\d{1,10}");
                        maxSizeField.setText("");
                        maxSizeField.setEditable(true);
                }
        }

        public final int getXoffset() {
                return Integer.parseInt(xOffsetField.getText());
        }

        public final int getYoffset() {
                return Integer.parseInt(yOffsetField.getText());
        }

        public final int getWoffset() {
                return Integer.parseInt(wOffsetField.getText());
        }

        public final int getHoffset() {
                return Integer.parseInt(hOffsetField.getText());
        }

        public final double getMinSize() {
                return Double.parseDouble(minSizeField.getText());
        }

        public final double getMaxSize() {
                return Double.parseDouble(maxSizeField.getText());
        }

        public final double getMinCircularity() {
                return Double.parseDouble(circMinField.getText());
        }

        public final double getMaxCircularity() {
                return Double.parseDouble(circMaxField.getText());
        }

        public final boolean isInfinity() {
                return infinityCheckBox.isSelected();
        }

        public final AnalysisOptions getAnalysisOptions() {
                AnalysisOptions aopt = new AnalysisOptions();
                aopt.setHOffset(getHoffset());
                aopt.setWOffset(getWoffset());
                aopt.setXOffset(getXoffset());
                aopt.setYOffset(getYoffset());
                aopt.setMinSize(getMinSize());
                aopt.setMaxSize(getMaxSize());
                aopt.setMinCircularity(getMinCircularity());
                aopt.setMaxCircularity(getMaxCircularity());
                aopt.setInfinity(isInfinity());
                return aopt;
        }

        @Override
        public Map<Savables, String> getSavableData() {
                Map<Savables,String> savableData = new HashMap<Savables,String>();
                savableData.put(Savables.ANALYSIS_X_OFFSET, xOffsetField.getText());
                savableData.put(Savables.ANALYSIS_Y_OFFSET, yOffsetField.getText());
                savableData.put(Savables.ANALYSIS_W_OFFSET, wOffsetField.getText());
                savableData.put(Savables.ANALYSIS_H_OFFSET, hOffsetField.getText());
                savableData.put(Savables.ANALYSIS_MIN_SIZE, minSizeField.getText());
                savableData.put(Savables.ANALYSIS_MAX_SIZE, maxSizeField.getText());
                savableData.put(Savables.ANALYSIS_MIN_CIRCULARITY, circMinField.getText());
                savableData.put(Savables.ANALYSIS_MAX_CIRCULARITY, circMaxField.getText());
                savableData.put(Savables.ANALYSIS_INFINITY, String.valueOf(infinityCheckBox.isSelected()));
                return savableData;
        }
}
