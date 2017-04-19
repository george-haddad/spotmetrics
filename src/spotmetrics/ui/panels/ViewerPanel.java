package spotmetrics.ui.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import layout.TableLayout;
import spotmetrics.data.save.SavablePanel;
import spotmetrics.data.save.Savables;
import spotmetrics.ui.comp.AbstractActionListenerPanel;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 17, 2015
 *
 */
public class ViewerPanel extends AbstractActionListenerPanel implements SavablePanel {

        private static final long serialVersionUID = 2404277645392796694L;

        private JLabel flashFrameLabel = null;
        private JTextField flashFrameField = null;
        private JLabel videoSelectLabel = null;
        private JTextField videoSelectField = null;
        private JLabel cropVideoLabel = null;
        private JButton cropVideoButton = null;

        public ViewerPanel() {
                flashFrameLabel = new JLabel("Flash Frame:");
                flashFrameLabel.setHorizontalAlignment(JLabel.RIGHT);
                flashFrameLabel.setPreferredSize(new Dimension(120, 22));

                flashFrameField = new JTextField("", 5);
                flashFrameField.setEditable(false);

                //-------------------------------------------------------------------------

                videoSelectLabel = new JLabel("Video Selection:");
                videoSelectLabel.setHorizontalAlignment(JLabel.RIGHT);
                videoSelectLabel.setPreferredSize(new Dimension(120, 22));

                videoSelectField = new JTextField("", 5);
                videoSelectField.setEditable(false);
                
                //-------------------------------------------------------------------------
                
                cropVideoLabel = new JLabel("Crop Video");
                cropVideoLabel.setHorizontalAlignment(JLabel.RIGHT);
                cropVideoButton = new JButton("Crop");
                cropVideoButton.setEnabled(false);
                cropVideoButton.setToolTipText("Crop video for selected ROI");
                cropVideoButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                cropVideoButton_actionPerformed();
                        }
                });
                
                //-------------------------------------------------------------------------

                double spacer = 5;
                double[][] layoutSize1 = {
                                //                   0,      1,                2
                                { TableLayout.PREFERRED, spacer, TableLayout.FILL },
                                  {
                                  TableLayout.PREFERRED, //0
                                  spacer,
                                  TableLayout.PREFERRED, //2
                                  spacer,
                                  TableLayout.PREFERRED  //4
                                  }
                                };

                setLayout(new TableLayout(layoutSize1));
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                add(flashFrameLabel,  "0, 0");
                add(flashFrameField,  "2, 0");
                add(videoSelectLabel, "0, 2");
                add(videoSelectField, "2, 2");
                add(cropVideoLabel,   "0, 4");
                add(cropVideoButton,  "2, 4");
        }
        
        private final void cropVideoButton_actionPerformed() {
                actionPerformed(new ActionEvent(this, getEventID(), "CROP"));
        }
        
        public final void setFlashFrame(String frameNumber) {
                flashFrameField.setText(frameNumber);
        }

        public final void setVideoSelectRange(String range) {
                videoSelectField.setText(range);
        }
        
        public final void setCropButtonEnabled(boolean enabled) {
                cropVideoButton.setEnabled(enabled);
        }

        @Override
        public Map<Savables,String> getSavableData() {
                Map<Savables,String> savableData = new HashMap<Savables,String>();
                savableData.put(Savables.VIEWER_FLASH_FRAME, flashFrameField.getText());
                savableData.put(Savables.VIEWER_VIDEO_SELECTION, videoSelectField.getText());
                return savableData;
        }
}
