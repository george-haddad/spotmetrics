package spotmetrics.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import layout.TableLayout;
import spotmetrics.analyzer.AnalysisOptions;
import spotmetrics.analyzer.TrackAnalyzer;
import spotmetrics.analyzer.export.ExcelExporter;
import spotmetrics.analyzer.export.NotInitializedException;
import spotmetrics.analyzer.export.OverlayVideoExporter;
import spotmetrics.data.MySpot;
import spotmetrics.data.MyTrack;
import spotmetrics.data.save.Panels;
import spotmetrics.data.save.Savables;
import spotmetrics.ui.SpotMetricsFrame;
import spotmetrics.ui.UITool;
import spotmetrics.ui.file.FileSave;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Dec 18, 2015
 *
 */
public class SpotsPanel extends JPanel {

        private static final long serialVersionUID = -3870213724431674065L;

        private SpotMetricsFrame parentFrame = null;
        private JLabel spotTreeLabel = null;
        private JTree spotTree = null;
        private JScrollPane treeScrollPane = null;
        private DefaultMutableTreeNode rootNode = null;
        private JButton excelButton = null;
        private JButton overlayButton = null;
        private JButton saveButton = null;
        private JButton openButton = null;

        /**
         * Key: "Track_+trackId"<br>
         * Value: MyTrack
         */
        private HashMap<String, MyTrack> tracksMap = null;

        private boolean inspectingTrack = false;
        private ImagePlus imagePlus = null;
        private ImagePlus imagePlusColor = null;
        private AnalysisOptions analysisOptions = null;

        public SpotsPanel() {
                spotTreeLabel = new JLabel("Tracked Spots");
                spotTreeLabel.setHorizontalAlignment(JLabel.CENTER);
                spotTreeLabel.setHorizontalTextPosition(JLabel.CENTER);

                rootNode = new DefaultMutableTreeNode("root");
                spotTree = new JTree(rootNode);
                spotTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                treeScrollPane = new JScrollPane(spotTree);
                spotTree.addTreeSelectionListener(new TreeSelectionListener() {
                        @Override
                        public void valueChanged(TreeSelectionEvent e) {
                                EventQueue.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                spotTree_valueChanged();
                                        }
                                });
                        }
                });

                spotTree.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                                if (SwingUtilities.isRightMouseButton(e)) {
                                        spotTree_rightClickMouseReleased(e);
                                }
                        }
                });

                excelButton = new JButton("Export Excel");
                excelButton.setEnabled(false);
                excelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                File videoFile = parentFrame.getVideoFile();
                                final String videoName = videoFile.getName().substring(0, videoFile.getName().lastIndexOf('.'));
                                final File excelFile = FileSave.saveFile("Save Excel as ...", new File(System.getProperty("user.home")), "Excel Results", videoName + ".xlsx");
                                if (excelFile != null) {
                                        Thread t = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                        excelButton_actionPerformed(excelFile);
                                                }
                                        });
                                        t.start();
                                }

                        }
                });
                
                overlayButton = new JButton("Export Overlay");
                overlayButton.setEnabled(false);
                overlayButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                Thread t = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                                overlayButton_actionPerformed();

                                        }
                                });
                                t.start();
                        }
                });
                
                saveButton = new JButton("Save");
                saveButton.setEnabled(false);
                saveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                saveButton_actionPerformed();
                        }
                });
                
                openButton = new JButton("Open");
                openButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                openButton_actionPerformed();
                        }
                });
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                buttonPanel.add(openButton);
                buttonPanel.add(saveButton);
                buttonPanel.add(excelButton);
                buttonPanel.add(overlayButton);

                //-----------------------------------------------------------------

                setLayout(new BorderLayout(5, 5));
                add(treeScrollPane, BorderLayout.CENTER);
                add(buttonPanel, BorderLayout.SOUTH);
        }

        public SpotsPanel(SpotMetricsFrame parentFrame) {
                this();
                setParentFrame(parentFrame);
        }
        
        private final void saveButton_actionPerformed() {
                File saveLocation = FileSave.saveFile("Save Analysis", new File(System.getProperty("user.home")), "Project Folder", "", true);
                if(saveLocation != null) {
                        System.out.println("Save location: "+saveLocation.getAbsolutePath());
                        saveLocation.mkdirs();
                        
                        Properties props = new Properties();
                        
                        Map<Savables,String> saveData = parentFrame.getSavableDataPanel(Panels.SPOT_METRICS_FRAME);
                        props.put(Savables.MAIN_VIDEO_FILE.getKey(), saveData.get(Savables.MAIN_VIDEO_FILE));
                        
                        saveData = parentFrame.getSavableDataPanel(Panels.VIEWER_PANEL);
                        props.put(Savables.VIEWER_FLASH_FRAME.getKey(), saveData.get(Savables.VIEWER_FLASH_FRAME));
                        props.put(Savables.VIEWER_VIDEO_SELECTION.getKey(), saveData.get(Savables.VIEWER_VIDEO_SELECTION));
                        
                        saveData = parentFrame.getSavableDataPanel(Panels.PROCESSING_PANEL);
                        props.put(Savables.PROCESSING_SUBTRACT_BACKGROUND.getKey(), saveData.get(Savables.PROCESSING_SUBTRACT_BACKGROUND));
                        props.put(Savables.PROCESSING_DARK_BACKGROUND.getKey(), saveData.get(Savables.PROCESSING_DARK_BACKGROUND));
                        props.put(Savables.PROCESSING_THRESHOLD_METHOD.getKey(), saveData.get(Savables.PROCESSING_THRESHOLD_METHOD));
                        
                        saveData = parentFrame.getSavableDataPanel(Panels.FLASH_PANEL);
                        props.put(Savables.FLASH_DETECT_MODE.getKey(), saveData.get(Savables.FLASH_DETECT_MODE));
                        props.put(Savables.FLASH_OFFSET_BEFORE.getKey(), saveData.get(Savables.FLASH_OFFSET_BEFORE));
                        props.put(Savables.FLASH_OFFSET_AFTER.getKey(), saveData.get(Savables.FLASH_OFFSET_AFTER));
                        props.put(Savables.FLASH_DETECT.getKey(), saveData.get(Savables.FLASH_DETECT));
                        props.put(Savables.FLASH_DELETE_ONLY.getKey(), saveData.get(Savables.FLASH_DELETE_ONLY));
                        
                        saveData = parentFrame.getSavableDataPanel(Panels.TRACKING_PANEL);
                        props.put(Savables.TRACK_BLOB_DIAMETER.getKey(), saveData.get(Savables.TRACK_BLOB_DIAMETER));
                        props.put(Savables.TRACK_BLOB_THRESHOLD.getKey(), saveData.get(Savables.TRACK_BLOB_THRESHOLD));
                        props.put(Savables.TRACK_LINKING_MAX_DISTANCE.getKey(), saveData.get(Savables.TRACK_LINKING_MAX_DISTANCE));
                        props.put(Savables.TRACK_GAP_CLOSING_MAX_DISTANCE.getKey(), saveData.get(Savables.TRACK_GAP_CLOSING_MAX_DISTANCE));
                        props.put(Savables.TRACK_GAP_CLOSING_MAX_FRAME_GAP.getKey(), saveData.get(Savables.TRACK_GAP_CLOSING_MAX_FRAME_GAP));
                        props.put(Savables.TRACK_INITIAL_SPOT_FILTER_VALUE.getKey(), saveData.get(Savables.TRACK_INITIAL_SPOT_FILTER_VALUE));

                        saveData = parentFrame.getSavableDataPanel(Panels.ANALYSIS_PANEL);
                        props.put(Savables.ANALYSIS_X_OFFSET.getKey(), saveData.get(Savables.ANALYSIS_X_OFFSET));
                        props.put(Savables.ANALYSIS_Y_OFFSET.getKey(), saveData.get(Savables.ANALYSIS_Y_OFFSET));
                        props.put(Savables.ANALYSIS_W_OFFSET.getKey(), saveData.get(Savables.ANALYSIS_W_OFFSET));
                        props.put(Savables.ANALYSIS_H_OFFSET.getKey(), saveData.get(Savables.ANALYSIS_H_OFFSET));
                        props.put(Savables.ANALYSIS_MIN_SIZE.getKey(), saveData.get(Savables.ANALYSIS_MIN_SIZE));
                        props.put(Savables.ANALYSIS_MAX_SIZE.getKey(), saveData.get(Savables.ANALYSIS_MAX_SIZE));
                        props.put(Savables.ANALYSIS_MIN_CIRCULARITY.getKey(), saveData.get(Savables.ANALYSIS_MIN_CIRCULARITY));
                        props.put(Savables.ANALYSIS_MAX_CIRCULARITY.getKey(), saveData.get(Savables.ANALYSIS_MAX_CIRCULARITY));
                        props.put(Savables.ANALYSIS_INFINITY.getKey(), saveData.get(Savables.ANALYSIS_INFINITY));
                        
                        FileOutputStream fos = null;
                        ObjectOutputStream oos = null;
                        try {
                                fos = new FileOutputStream(new File(saveLocation.getAbsolutePath()+File.separator+"config.xml"));
                                props.storeToXML(fos, "SpotMetrics Panel Configuraitons", "UTF-8");
                                fos.flush();
                                
                                oos = new ObjectOutputStream(new FileOutputStream(new File(saveLocation.getAbsolutePath()+File.separator+"tracksMap.bin")));
                                oos.writeObject(tracksMap);
                                oos.flush();
                                
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                String jsonStr = gson.toJson(tracksMap);
                                FileWriter jfos = new FileWriter(new File(saveLocation.getAbsolutePath()+File.separator+"tracksMap.json"));
                                jfos.write(jsonStr);
                                jfos.flush();
                                jfos.close();
                                
                                XStream xstream = new XStream(new StaxDriver());
                                String xmlStr = xstream.toXML(tracksMap);
                                FileWriter xfos = new FileWriter(new File(saveLocation.getAbsolutePath()+File.separator+"tracksMap.xml"));
                                xfos.write(xmlStr);
                                xfos.flush();
                                xfos.close();
                                
                                IJ.save(imagePlus, saveLocation.getAbsolutePath()+File.separator+"orig_"+parentFrame.getVideoFile().getName());
                                IJ.save(imagePlusColor, saveLocation.getAbsolutePath()+File.separator+"orig_color_"+parentFrame.getVideoFile().getName());
                        }
                        catch(FileNotFoundException fnne) {
                                fnne.printStackTrace();
                        }
                        catch(IOException ioe) {
                                ioe.printStackTrace();
                        }
                        finally {
                                if(fos != null) {
                                        try {
                                                fos.close();
                                        }
                                        catch(IOException e) {}
                                        finally {
                                                fos = null;
                                        }
                                }
                                
                                if(oos != null) {
                                        try {
                                                oos.close();
                                        }
                                        catch(IOException e) {}
                                        finally {
                                                oos = null;
                                        }
                                }
                                
                                JOptionPane.showMessageDialog(this, "State saved");
                        }
                }
        }
        
        private final void openButton_actionPerformed() {
                
        }

        private final void spotTree_rightClickMouseReleased(MouseEvent me) {
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) spotTree.getLastSelectedPathComponent();
                if (node != null) {
                        if (node.isLeaf() && !node.getAllowsChildren()) {
                                final MyTrack track = (MyTrack) node.getUserObject();

                                JMenuItem item1 = new JMenuItem();
                                item1.setText("Edit Track");
                                item1.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/edit_track_22.png"));
                                item1.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                if (!inspectingTrack) {
                                                        editTrack(track);
                                                }
                                                else {
                                                        JOptionPane.showMessageDialog(null, "Already editing this track", "Edit Track", JOptionPane.INFORMATION_MESSAGE);
                                                }
                                        }
                                });

                                JMenuItem item2 = new JMenuItem();
                                item2.setText("Delete Track");
                                item2.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/delete_22.png"));
                                item2.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                if(spotTree.getSelectionCount() > 1) {
                                                        int choice = JOptionPane.showConfirmDialog(null, "Really delete all "+spotTree.getSelectionCount()+" tracks ?", "Confirm Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                                                        if(JOptionPane.YES_OPTION == choice) {
                                                                TreePath[] treePaths = spotTree.getSelectionPaths();
                                                                for (int i = 0; i < treePaths.length; i++) {
                                                                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)treePaths[i].getLastPathComponent();
                                                                        if (node != null && node.isLeaf() && !node.getAllowsChildren()) {
                                                                                MyTrack track = (MyTrack) node.getUserObject();
                                                                                tracksMap.remove("Track_" + track.getTrackId());
                                                                                DefaultTreeModel treeModel = (DefaultTreeModel) spotTree.getModel();
                                                                                treeModel.removeNodeFromParent(node);
                                                                        }
                                                                }
                                                                
                                                                clearSpotTreeSelection();
                                                        }
                                                }
                                                else {
                                                        int choice = JOptionPane.showConfirmDialog(null, "Really delete Track " + track.getTrackId() + "?", "Confirm Delete", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                                                        if (JOptionPane.YES_OPTION == choice) {
                                                                tracksMap.remove("Track_" + track.getTrackId());
                                                                DefaultTreeModel treeModel = (DefaultTreeModel) spotTree.getModel();
                                                                treeModel.removeNodeFromParent(node);
                                                                clearSpotTreeSelection();
                                                        }   
                                                }
                                        }
                                });

                                JMenuItem item3 = new JMenuItem();
                                item3.setText("Analyze - Show Results");
                                item3.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/analyze_22.png"));
                                item3.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                analyzeTrack(track, true, false);
                                        }
                                });

                                JMenuItem item4 = new JMenuItem();
                                item4.setText("Analyze - Show Results & ROI Manager");
                                item4.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/analyze_results_22.png"));
                                item4.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                analyzeTrack(track, true, true);
                                        }
                                });

                                JMenuItem item5 = new JMenuItem();
                                item5.setText("Diagnose All Tracks");
                                item5.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/diagnostics_22.png"));
                                item5.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                Thread t1 = new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                                diagnosticsScan();
                                                        }
                                                });
                                                t1.start();
                                        }
                                });

                                JMenuItem item6 = new JMenuItem();
                                item6.setText("Measure RGB");
                                item6.setIcon(UITool.getImageIcon("/spotmetrics/ui/icons/rgb_22.png"));
                                item6.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                                measureRgb(track, true);
                                        }
                                });

                                JPopupMenu spotPopup = new JPopupMenu();
                                spotPopup.add(new JMenuItem(track.getLabel()));
                                spotPopup.addSeparator();
                                spotPopup.add(item1);
                                spotPopup.add(item2);
                                spotPopup.add(item3);
                                spotPopup.add(item4);
                                spotPopup.add(item5);
                                spotPopup.add(item6);
                                spotPopup.show(spotTree, me.getX(), me.getY());
                        }
                }
        }

        private int endSlice = 0;
        private int slice = 0;
        private JTextField roiXField = null;
        private JTextField roiYField = null;
        private JTextField roiHField = null;
        private JTextField roiWField = null;
        private JLabel sliceLabel = null;

        private final void editTrack(final MyTrack track) {
                JLabel trackLabel = new JLabel("Track: " + track.getTrackId());
                JLabel roiXLabel = new JLabel("X:");
                JLabel roiYLabel = new JLabel("Y:");
                JLabel roiHLabel = new JLabel("H:");
                JLabel roiWLabel = new JLabel("W:");

                slice = 0;
                endSlice = track.getTrackDuration();

                //Pre-fill the first spot
                MySpot spot = track.getSpotAt(slice);
                roiXField = new JTextField("" + (spot.getX() - spot.getXOffset()), 5);
                roiYField = new JTextField("" + (spot.getY() - spot.getYOffset()), 5);
                roiWField = new JTextField("" + spot.getW(), 5);
                roiHField = new JTextField("" + spot.getH(), 5);

                Roi roi = new Roi(spot.getX() - spot.getXOffset(), spot.getY() - spot.getYOffset(), spot.getW(), spot.getH());
                roi.setStrokeColor(Color.BLUE);
                roi.setStrokeWidth(1.0);
                roi.setPosition(spot.getFrame() + 1);
                imagePlus.setRoi(roi);

                sliceLabel = new JLabel("Slice " + (slice + 1) + " of " + endSlice);
                sliceLabel.setHorizontalAlignment(JLabel.CENTER);

                double spacer = 5;
                double[][] layoutSize = {
                                //                   0,      1,                      2,      3,                     4,      5,                     6          
                                { TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED, spacer, TableLayout.PREFERRED }, { TableLayout.PREFERRED, //0
                                                spacer, TableLayout.PREFERRED  //2
                                } };

                JPanel roiPanel = new JPanel(new TableLayout(layoutSize));
                roiPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), BorderFactory.createTitledBorder("ROI Coords")));
                roiPanel.add(roiXLabel, "0, 0");
                roiPanel.add(roiXField, "2, 0");
                roiPanel.add(roiWLabel, "4, 0");
                roiPanel.add(roiWField, "6, 0");
                roiPanel.add(roiYLabel, "0, 2");
                roiPanel.add(roiYField, "2, 2");
                roiPanel.add(roiHLabel, "4, 2");
                roiPanel.add(roiHField, "6, 2");

                JButton updateRoiButton = new JButton("Update ROI");
                updateRoiButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                Roi roi = imagePlus.getRoi();
                                if (roi != null) {
                                        MySpot spot = track.getSpotAt(slice);
                                        spot.setXOffset(Math.abs(spot.getX() - roi.getXBase()));
                                        spot.setYOffset(Math.abs(spot.getY() - roi.getYBase()));
                                        spot.setW(roi.getFloatWidth());
                                        spot.setH(roi.getFloatHeight());

                                        roiXField.setText("" + (spot.getX() - spot.getXOffset()));
                                        roiYField.setText("" + (spot.getY() - spot.getYOffset()));
                                        roiWField.setText("" + spot.getW());
                                        roiHField.setText("" + spot.getH());
                                }
                        }
                });

                JPanel parentRoiPanel = new JPanel(new BorderLayout(5, 5));
                parentRoiPanel.add(roiPanel, BorderLayout.CENTER);
                parentRoiPanel.add(updateRoiButton, BorderLayout.EAST);

                JButton prevButton = new JButton("Prev");
                prevButton.addMouseListener(new MouseAdapter() {
                        private Timer mTimer = null;
                        private boolean first = false;

                        @Override
                        public void mouseReleased(MouseEvent e) {
                                if (mTimer != null) {
                                        mTimer.cancel();
                                        mTimer = null;
                                        first = false;
                                }

                                if (!first) {
                                        prevSlice(track);
                                }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                                if (mTimer == null) {
                                        mTimer = new java.util.Timer();
                                }

                                mTimer.schedule(new TimerTask() {
                                        public void run() {
                                                prevSlice(track);
                                                first = true;
                                        }
                                }, 1000, 150);
                        }
                });

                JButton nextButton = new JButton("Next");
                nextButton.addMouseListener(new MouseAdapter() {
                        private Timer mTimer = null;
                        private boolean first = false;

                        @Override
                        public void mouseReleased(MouseEvent e) {
                                if (mTimer != null) {
                                        mTimer.cancel();
                                        mTimer = null;
                                        first = false;
                                }

                                if (!first) {
                                        nextSlice(track);
                                }
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                                if (mTimer == null) {
                                        mTimer = new java.util.Timer();
                                }

                                mTimer.schedule(new TimerTask() {
                                        public void run() {
                                                nextSlice(track);
                                                first = true;
                                        }
                                }, 1000, 150);
                        }
                });

                JPanel navPanel = new JPanel(new BorderLayout(5, 5));
                navPanel.add(prevButton, BorderLayout.WEST);
                navPanel.add(sliceLabel, BorderLayout.CENTER);
                navPanel.add(nextButton, BorderLayout.EAST);

                JPanel inspectPanel = new JPanel(new BorderLayout(5, 5));
                inspectPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                inspectPanel.add(trackLabel, BorderLayout.NORTH);
                inspectPanel.add(parentRoiPanel, BorderLayout.CENTER);
                inspectPanel.add(navPanel, BorderLayout.SOUTH);

                final JFrame frame = new JFrame("Edit Track");
                frame.getContentPane().setLayout(new BorderLayout(5, 5));
                frame.getContentPane().add(inspectPanel, BorderLayout.CENTER);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                frame.setVisible(false);
                                frame.dispose();
                                inspectingTrack = false;
                                imagePlus.deleteRoi();
                                spotTree_valueChanged();
                        }
                });

                frame.setSize(280, 180);
                UITool.center(frame);
                frame.setVisible(true);
                inspectingTrack = true;
        }

        private final void nextSlice(MyTrack track) {
                if ((slice + 1) + 1 <= endSlice) {
                        //Move video 1 frame forwards
                        slice++;
                        imagePlus.setSlice(slice + 1);

                        //Get spot and draw ROI
                        //MySpot spot = track.getSpots()[slice];
                        MySpot spot = track.getSpotAt(slice);

                        roiXField.setText("" + (spot.getX() - spot.getXOffset()));
                        roiYField.setText("" + (spot.getY() - spot.getYOffset()));
                        roiHField.setText("" + spot.getH());
                        roiWField.setText("" + spot.getW());

                        Roi roi = new Roi(spot.getX() - spot.getXOffset(), spot.getY() - spot.getYOffset(), spot.getW(), spot.getH());
                        roi.setStrokeColor(Color.BLUE);
                        roi.setStrokeWidth(1.0);
                        roi.setPosition(spot.getFrame() + 1);
                        imagePlus.setRoi(roi);

                        sliceLabel.setText("Slice " + (slice + 1) + " of " + endSlice);
                }
        }

        private final void prevSlice(MyTrack track) {
                if ((slice + 1) - 1 > 0) {
                        //Move video 1 frame backwards
                        slice--;
                        imagePlus.setSlice(slice + 1);

                        //Get spot and draw ROI
                        //MySpot spot = track.getSpots()[slice];
                        MySpot spot = track.getSpotAt(slice);

                        roiXField.setText("" + (spot.getX() - spot.getXOffset()));
                        roiYField.setText("" + (spot.getY() - spot.getYOffset()));
                        roiWField.setText("" + spot.getW());
                        roiHField.setText("" + spot.getH());

                        Roi roi = new Roi(spot.getX() - spot.getXOffset(), spot.getY() - spot.getYOffset(), spot.getW(), spot.getH());
                        roi.setStrokeColor(Color.BLUE);
                        roi.setStrokeWidth(1.0);
                        roi.setPosition(spot.getFrame() + 1);
                        imagePlus.setRoi(roi);

                        sliceLabel.setText("Slice " + (slice + 1) + " of " + endSlice);
                }
        }

        private final void spotTree_valueChanged() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) spotTree.getLastSelectedPathComponent();
                if (node != null) {
                        if (node.isLeaf() && !node.getAllowsChildren()) {
                                MyTrack track = (MyTrack) node.getUserObject();
                                Overlay overlay = new Overlay();
                                MySpot[] spots = track.getSpots();
                                for (int i = 0; i < spots.length; i++) {
                                        MySpot spot = spots[i];
                                        Roi spotRoi = new Roi(spot.getX() - spot.getXOffset(), spot.getY() - spot.getYOffset(), spot.getW(), spot.getH());
                                        spotRoi.setStrokeColor(Color.MAGENTA);
                                        spotRoi.setStrokeWidth(1.0);
                                        spotRoi.setPosition(spot.getFrame() + 1);
                                        overlay.add(spotRoi);
                                }

                                imagePlus.setOverlay(overlay);
                        }
                }
        }

        public final void setSpotTracks(HashMap<String, MyTrack> tracksMap, int xOffset, int yOffset, int w, int h) {
                this.tracksMap = tracksMap;

                normalizeTracks(this.tracksMap, xOffset, yOffset, w, h);

                Iterator<MyTrack> iter = tracksMap.values().iterator();
                while (iter.hasNext()) {
                        MyTrack track = iter.next();
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode(track);
                        node.setAllowsChildren(false);
                        rootNode.add(node);
                }

                spotTree.expandPath(new TreePath(rootNode.getPath()));
                spotTree.validate();
                spotTree.repaint();

                excelButton.setEnabled(true);
                overlayButton.setEnabled(true);
                saveButton.setEnabled(true);
        }

        private final void normalizeTracks(HashMap<String, MyTrack> tracksMap, int xOffset, int yOffset, int w, int h) {
                Iterator<MyTrack> iter = tracksMap.values().iterator();
                while (iter.hasNext()) {
                        MyTrack track = iter.next();
                        MySpot[] spots = track.getSpots();
                        for (int i = 0; i < spots.length; i++) {
                                MySpot spot = spots[i];
                                spot.setXOffset(xOffset);
                                spot.setYOffset(yOffset);
                                spot.setW(w);
                                spot.setH(h);
                        }
                }
        }

        private final void analyzeTrack(final MyTrack track, final boolean showResults, final boolean showRoiManager) {
                Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                EventQueue.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                saveButton.setEnabled(false);
                                                excelButton.setEnabled(false);
                                                overlayButton.setEnabled(false);
                                                parentFrame.setProcessButtonEnabled(false);
                                                parentFrame.setProgressBarIndeterminate(true);
                                                parentFrame.updateProgressBar("Analyzing ...");
                                        }
                                });

                                TrackAnalyzer.analyzeParticles(track, imagePlus, analysisOptions, showResults, showRoiManager);

                                EventQueue.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                saveButton.setEnabled(true);
                                                excelButton.setEnabled(true);
                                                overlayButton.setEnabled(true);
                                                parentFrame.setProcessButtonEnabled(true);
                                                parentFrame.setProgressBarIndeterminate(false);
                                                parentFrame.updateProgressBar("Ready");
                                        }
                                });
                        }
                });
                t1.start();
        }
        
        private final void overlayButton_actionPerformed() {
                OverlayVideoExporter videoExporter = new OverlayVideoExporter();
                videoExporter.setTracks(tracksMap);
                videoExporter.setAnalysisOptions(analysisOptions);
                videoExporter.setImagePlus(imagePlus);
                videoExporter.setImagePlusColor(imagePlusColor);
                
                if(videoExporter.isInit()) {
                        EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                        saveButton.setEnabled(false);
                                        excelButton.setEnabled(false);
                                        overlayButton.setEnabled(false);
                                        parentFrame.setProcessButtonEnabled(false);
                                        parentFrame.setProgressBarIndeterminate(true);
                                        parentFrame.updateProgressBar("Exporting to overlay video ...");
                                }
                        });
                        
                        try {
                                videoExporter.export();
                        }
                        catch (NotInitializedException e) {
                                IJ.error("Overlay Video Initialization Error", "Overlay video exporter not properly initialized");
                                e.printStackTrace();
                        }
                        finally {
                                EventQueue.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                saveButton.setEnabled(true);
                                                excelButton.setEnabled(true);
                                                overlayButton.setEnabled(true);
                                                parentFrame.setProcessButtonEnabled(true);
                                                parentFrame.setProgressBarIndeterminate(false);
                                                parentFrame.updateProgressBar("Ready");
                                        }
                                });
                        }
                }
        }

        private final void excelButton_actionPerformed(File excelFile) {
                ExcelExporter excelExporter = new ExcelExporter();
                excelExporter.setTracks(tracksMap);
                excelExporter.setAnalysisOptions(analysisOptions);
                excelExporter.setImagePlus(imagePlus);
                excelExporter.setImagePlusColor(imagePlusColor);
                if (excelExporter.isInit()) {
                        EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                        saveButton.setEnabled(false);
                                        excelButton.setEnabled(false);
                                        overlayButton.setEnabled(false);
                                        parentFrame.setProcessButtonEnabled(false);
                                        parentFrame.setProgressBarIndeterminate(true);
                                        parentFrame.updateProgressBar("Exporting to excel ...");
                                }
                        });

                        try {
                                excelExporter.export(excelFile);
                                excelExporter.close();
                                IJ.showMessage("Excel File Created: \"" + excelFile.getName() + "\"");
                        }
                        catch (NotInitializedException e) {
                                IJ.error("Excel Initialization Error", "Excel exporter not properly initialized");
                                e.printStackTrace();
                        }
                        catch (IOException e) {
                                IJ.error("Excel Error", "Error creating excel file");
                                e.printStackTrace();
                        }
                        finally {
                                try {
                                        excelExporter.close();
                                }
                                catch (IOException e) {
                                        IJ.error("Excel Close Error", "Error closing excel file");
                                        e.printStackTrace();
                                }
                                finally {
                                        EventQueue.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        saveButton.setEnabled(true);
                                                        excelButton.setEnabled(true);
                                                        overlayButton.setEnabled(true);
                                                        parentFrame.setProcessButtonEnabled(true);
                                                        parentFrame.setProgressBarIndeterminate(false);
                                                        parentFrame.updateProgressBar("Ready");
                                                }
                                        });
                                }
                        }
                }
        }

        private final void diagnosticsScan() {
                EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                saveButton.setEnabled(false);
                                excelButton.setEnabled(false);
                                overlayButton.setEnabled(false);
                                parentFrame.setProcessButtonEnabled(false);
                                parentFrame.setProgressBarIndeterminate(true);
                                parentFrame.updateProgressBar("Diagnosing Tracks ...");
                        }
                });

                spotTree.clearSelection();
                Iterator<MyTrack> iter = tracksMap.values().iterator();
                StringBuilder sb = new StringBuilder();
                int problems = 0;
                int trackDuration = 0;
                while (iter.hasNext()) {
                        MyTrack track = iter.next();
                        ResultsTable rt = TrackAnalyzer.analyzeParticles(track, imagePlus, analysisOptions, false, false);
                        trackDuration = track.getTrackDuration();
                        if (trackDuration > rt.size()) {
                                sb.append(track.getLabel());
                                sb.append(": ");
                                sb.append(rt.size());
                                sb.append(" / ");
                                sb.append(track.getTrackDuration());
                                sb.append("\n");
                                problems++;
                        }

                        rt = null;
                }

                EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                saveButton.setEnabled(true);
                                excelButton.setEnabled(true);
                                overlayButton.setEnabled(true);
                                parentFrame.setProcessButtonEnabled(true);
                                parentFrame.setProgressBarIndeterminate(false);
                                parentFrame.updateProgressBar("Ready");
                        }
                });

                if (problems > 0) {
                        String msg = "The video selection has total of " + trackDuration + " frames\nwhere each frame should have a spot.\nThere are " + problems + " tracks that have missing spots.\n\n";
                        JTextArea msgArea = new JTextArea();
                        msgArea.setText(msg + sb.toString());
                        JScrollPane scrollPane = new JScrollPane(msgArea);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        msg = null;
                        sb = null;

                        JFrame msgFrame = new JFrame("Scan Success");
                        msgFrame.setSize(400, 300);
                        msgFrame.getContentPane().add(scrollPane);
                        msgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        msgFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                        UITool.center(msgFrame);
                        msgFrame.setVisible(true);
                }
                else {
                        JOptionPane.showMessageDialog(this, "Diagnostic scan complete, all tracks are fine.", "Scan Success", JOptionPane.PLAIN_MESSAGE);
                }
        }

        private final void measureRgb(MyTrack track, boolean showResults) {
                TrackAnalyzer.measureColors(track, imagePlusColor);
                if (showResults) {
                        MySpot[] spots = track.getSpots();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < spots.length; i++) {
                                MySpot mySpot = spots[i];
                                sb.append(mySpot.getRgbString()).append('\n');
                        }

                        JTextArea msgArea = new JTextArea();
                        msgArea.setText(sb.toString());
                        msgArea.setFont(new Font("monospaced", Font.PLAIN, 12));
                        JScrollPane scrollPane = new JScrollPane(msgArea);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        sb = null;

                        JFrame msgFrame = new JFrame("Track " + track.getTrackId() + " RGB");
                        msgFrame.setSize(400, 300);
                        msgFrame.getContentPane().add(scrollPane);
                        msgFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        msgFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                        UITool.center(msgFrame);
                        msgFrame.setVisible(true);
                }
        }

        public final void clearSpotTreeSelection() {
                spotTree.clearSelection();
        }

        public final HashMap<String, MyTrack> getSpotTracks() {
                return tracksMap;
        }

        public final void clearSpotTracks() {
                if (tracksMap != null) {
                        tracksMap.clear();
                        tracksMap = null;
                }

                rootNode.removeAllChildren();
                DefaultTreeModel treeModel = (DefaultTreeModel) spotTree.getModel();
                treeModel.reload();
        }

        public final void setImagePlus(ImagePlus imagePlus) {
                this.imagePlus = imagePlus;
        }

        public final void setImagePlusColor(ImagePlus imagePlusColor) {
                this.imagePlusColor = imagePlusColor;
        }

        public final void setAnalysisOptions(AnalysisOptions analysisOptions) {
                this.analysisOptions = analysisOptions;
        }

        public final void setParentFrame(SpotMetricsFrame parentFrame) {
                this.parentFrame = parentFrame;
        }
}
