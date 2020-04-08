package spotmetrics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import ij.IJ;
import ij.ImagePlus;
import spotmetrics.SpotMetrics;
import spotmetrics.analyzer.AnalysisEngine;
import spotmetrics.analyzer.AnalysisOptions;
import spotmetrics.analyzer.FlashDetect;
import spotmetrics.analyzer.ProcessingOptions;
import spotmetrics.analyzer.TrackingOptions;
import spotmetrics.analyzer.VideoRange;
import spotmetrics.data.MyTrack;
import spotmetrics.data.save.Panels;
import spotmetrics.data.save.SavablePanel;
import spotmetrics.data.save.Savables;
import spotmetrics.ui.file.FileOpen;
import spotmetrics.ui.panels.AnalysisPanel;
import spotmetrics.ui.panels.FlashPanel;
import spotmetrics.ui.panels.ProcessingPanel;
import spotmetrics.ui.panels.SpotsPanel;
import spotmetrics.ui.panels.TrackingPanel;
import spotmetrics.ui.panels.ViewerPanel;
import spotmetrics.util.VideoUtil;


/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 4:33:56 PM - Sep 5, 2014
 * 
 */
public class SpotMetricsFrame extends JFrame implements ProgressUpdatableFrame, SavablePanel {

        private static final long serialVersionUID = -6163968461355644696L;

        private ViewerPanel viewerPanel = null;
        private FlashPanel flashPanel = null;
        private ProcessingPanel processingPanel = null;
        private TrackingPanel trackingPanel = null;
        private AnalysisPanel analysisPanel = null;
        private SpotsPanel spotsPanel = null;

        private JLabel videoLabel = null;
        private JTextField videoField = null;
        private JButton videoOpenButton = null;
        private File videoFile = null;
        private String recentVideoDir = null;
        
        private JButton loadVideoButton = null;
        private JButton startButton = null;

        private JProgressBar progressBar = null;

        private JTabbedPane tabs = null;
        private AnalysisEngine engine = null;

        public SpotMetricsFrame() {
                init();
        }

        private final void init() {
                videoLabel = new JLabel("Video:");
                videoLabel.setHorizontalAlignment(JLabel.RIGHT);
                videoLabel.setPreferredSize(new Dimension(60, 22));

                videoField = new JTextField(20);
                videoField.setEditable(false);
                videoField.setBackground(Color.WHITE);

                videoOpenButton = new JButton("...");
                videoOpenButton.setToolTipText("Selects the video that will be loaded");
                videoOpenButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                videoFile = FileOpen.getFile("Choose the video file", (recentVideoDir == null ? System.getProperty("user.dir") : recentVideoDir), JFileChooser.FILES_ONLY, "AVI Files", "avi");
                                if (videoFile != null) {
                                        recentVideoDir = videoFile.getParent();
                                        videoField.setText(videoFile.getAbsolutePath());
                                        loadVideoButton.setEnabled(true);
                                }
                        }
                });

                loadVideoButton = new JButton("Load");
                loadVideoButton.setToolTipText("Loads the video");
                loadVideoButton.setEnabled(false);
                loadVideoButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                if (checkOptions()) {
                                        loadVideoButton_actionPerformed();
                                }
                                else {
                                        JOptionPane.showMessageDialog(SpotMetricsFrame.this, "Please select a video file", "Missing Video", JOptionPane.INFORMATION_MESSAGE);
                                }
                        }
                });
                
                startButton = new JButton("Start");
                startButton.setToolTipText("Start video analysis");
                startButton.setEnabled(false);
                startButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                if (checkOptions()) {
                                        startButton_actionPerformed();
                                }
                                else {
                                        JOptionPane.showMessageDialog(SpotMetricsFrame.this, "Please select a video file", "Missing Video", JOptionPane.INFORMATION_MESSAGE);
                                }
                        }
                });

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
                buttonPanel.add(videoOpenButton);
                buttonPanel.add(loadVideoButton);
                buttonPanel.add(startButton);

                JPanel videoFilePanel = new JPanel(new BorderLayout(5, 5));
                videoFilePanel.add(videoLabel, BorderLayout.WEST);
                videoFilePanel.add(videoField, BorderLayout.CENTER);
                videoFilePanel.add(buttonPanel, BorderLayout.EAST);

                //-------------------------------------------------------------------------

                progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
                progressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                progressBar.setStringPainted(true);
                progressBar.setString("Ready");

                //-------------------------------------------------------------------------

                viewerPanel = new ViewerPanel();
                viewerPanel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                viwerPanel_actionPerformed(e);
                        }
                });
                
                flashPanel = new FlashPanel();
                processingPanel = new ProcessingPanel();
                trackingPanel = new TrackingPanel();
                analysisPanel = new AnalysisPanel();

                tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
                tabs.addTab("Viewer", viewerPanel);
                tabs.addTab("Flash", flashPanel);
                tabs.addTab("Processing", processingPanel);
                tabs.addTab("Tracking", trackingPanel);
                tabs.addTab("Analysis", analysisPanel);

                //-------------------------------------------------------------------------

                spotsPanel = new SpotsPanel();
                spotsPanel.setParentFrame(this);

                Box mainPanel = Box.createVerticalBox();
                mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                mainPanel.add(videoFilePanel);
                mainPanel.add(Box.createVerticalStrut(5));
                mainPanel.add(tabs);
                mainPanel.add(Box.createVerticalStrut(5));
                mainPanel.add(spotsPanel);
                mainPanel.add(Box.createVerticalStrut(5));
                mainPanel.add(progressBar);

                setTitle("SpotMetrics : "+SpotMetrics.VERSION);
                setLayout(new BorderLayout(5, 5));
                add(mainPanel, BorderLayout.NORTH);
                pack();
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                                exit();
                        }
                });
        }

        private final void exit() {
                int choice = JOptionPane.showConfirmDialog(this, "Do you wish to exit the plugin ?", "Exit Plugin ?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (JOptionPane.YES_OPTION == choice) {
                        if (engine != null) {
                                engine.close();
                                engine = null;
                        }

                        setVisible(false);
                        dispose();
                        System.gc();
                }
        }

        private final boolean checkOptions() {
                return videoFile != null;
        }
        
        private final void loadVideoButton_actionPerformed() {
                loadVideoButton.setEnabled(false);
                progressBar.setIndeterminate(true);
                
                engine = new AnalysisEngine();
                engine.setParentFrame(this);
                
                Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                updateProgressBar("Loading video file ...");
                                ImagePlus imagePlus = VideoUtil.loadVideo(videoFile);
                                engine.setImagePlus(imagePlus);
                                imagePlus.show();
                                
                                EventQueue.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                                updateProgressBar("Ready for Crop or Start");
                                                progressBar.setIndeterminate(false);
                                                startButton.setEnabled(true);
                                                viewerPanel.setCropButtonEnabled(true);
                                        }
                                });
                        }
                });
                t.start();
        }
        
        private final void viwerPanel_actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if("CROP".equals(cmd)) {
                        progressBar.setIndeterminate(true);
                        updateProgressBar("Cropping video ...");
                        viewerPanel.setCropButtonEnabled(false);
                        loadVideoButton.setEnabled(false);
                        startButton.setEnabled(false);
                        
                        Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                        engine.cropVideo();
                                        
                                        EventQueue.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        updateProgressBar("Ready for Start");
                                                        progressBar.setIndeterminate(false);
                                                        startButton.setEnabled(true);
                                                }
                                        });
                                }
                        });
                        t.start();
                }
        }
        
        private final void startButton_actionPerformed() {
                startButton.setEnabled(false);
                loadVideoButton.setEnabled(false);
                viewerPanel.setCropButtonEnabled(false);
                progressBar.setIndeterminate(true);

                FlashDetect flashDetectMode = flashPanel.getFlashDetectMode();
                int offsetBefore = flashPanel.getOffsetBefore();
                int offsetAfter = flashPanel.getOffsetAfter();
                boolean detectFlash = flashPanel.isDetectFlash();
                boolean deleteFlashOnly = flashPanel.isDeleteFlashOnly();

                int rolling = processingPanel.getRollingValue();
                String threshold = processingPanel.getThresholdName();
                boolean darkBg = processingPanel.isDarkBackground();

                int blobDiameter = trackingPanel.getBlobDiameter();
                int blobThreshold = trackingPanel.getBlobThreshold();
                int linkingMaxDistance = trackingPanel.getLinkingMaxDistance();
                int gapClosingMaxDistance = trackingPanel.getGapClosingMaxDistance();
                int gapClosingMaxFrameGap = trackingPanel.getGapClosingMaxFrameGap();
                int initialSpotFilterValue = trackingPanel.getInitialSpotFilterValue();

                final ProcessingOptions procOptions = new ProcessingOptions(rolling, threshold, darkBg);
                final TrackingOptions trackOptions = new TrackingOptions(blobDiameter, blobThreshold, linkingMaxDistance, gapClosingMaxDistance);
                trackOptions.setGapClosingMaxFrameGap(gapClosingMaxFrameGap);
                trackOptions.setInitialSpotFilterValue(initialSpotFilterValue);

                engine.setFlashDetect(flashDetectMode);
                engine.setOffsetBeforeFlash(offsetBefore);
                engine.setOffsetAfterFlash(offsetAfter);
                engine.setDetectFlash(detectFlash);
                engine.setDeleteFlashOnly(deleteFlashOnly);

                Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                                final VideoRange videoRange = engine.analyzeVideo();
                                spotsPanel.clearSpotTracks();

                                System.gc();

                                if (videoRange != null) {
                                        EventQueue.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        if (videoRange.hasFlashRange()) {
                                                                viewerPanel.setFlashFrame(String.valueOf(videoRange.getFlashFrame()));
                                                        }
                                                        else {
                                                                viewerPanel.setFlashFrame("Flash detection disabled");
                                                        }

                                                        viewerPanel.setVideoSelectRange(videoRange.getStartSlice() + " - " + videoRange.getEndSlice());
                                                }
                                        });

                                        engine.setProcessingOptions(procOptions);
                                        engine.processVideo();
                                        engine.setTrackingOptions(trackOptions);
                                        Map<String, MyTrack> tracksMap = engine.trackSpots();
                                        
//                                        IJ.log("Is tracksMap null? "+(tracksMap == null));
                                        
                                        if (tracksMap != null) {
                                                IJ.log("tracksMap size = "+tracksMap.size());
                                                spotsPanel.clearSpotTreeSelection();
                                                spotsPanel.setSpotTracks(tracksMap, analysisPanel.getXoffset(), analysisPanel.getYoffset(), analysisPanel.getWoffset(), analysisPanel.getHoffset(), true);
                                                spotsPanel.setImagePlus(engine.getImagePlus());
                                                spotsPanel.setImagePlusColor(engine.getImagePlusColor());
                                                spotsPanel.setAnalysisOptions(analysisPanel.getAnalysisOptions());
                                                tabs.setSelectedIndex(4);
                                        }

                                        System.gc();

                                        EventQueue.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        loadVideoButton.setEnabled(true);
                                                        startButton.setEnabled(true);
                                                        progressBar.setIndeterminate(false);
                                                        progressBar.setString("Done");
                                                }
                                        });
                                }
                                else {
                                        EventQueue.invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        loadVideoButton.setEnabled(true);
                                                        startButton.setEnabled(true);
                                                        progressBar.setIndeterminate(false);
                                                        progressBar.setString("Not Processed");
                                                }
                                        });
                                }
                        }
                });
                t.start();
        }

        @Override
        public void updateProgressBar(final String message) {
                EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                progressBar.setString(message);
                        }
                });
        }

        public void setProcessButtonEnabled(boolean enabled) {
                EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                loadVideoButton.setEnabled(enabled);
                        }
                });
        }

        public void setProgressBarIndeterminate(boolean indeterminate) {
                EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                                progressBar.setIndeterminate(indeterminate);
                        }
                });
        }

        public File getVideoFile() {
                return videoFile;
        }
        
        public AnalysisOptions getAnalysisOptions() {
                return analysisPanel.getAnalysisOptions();
        }
        
        public Map<Savables, String> getSavableDataPanel(Panels panelName) throws NullPointerException {
                if(panelName == null) {
                        throw new NullPointerException("panelName cannot be null");
                }
                
                switch(panelName) {
                        case VIEWER_PANEL: {
                                return viewerPanel.getSavableData();
                        }
                        case FLASH_PANEL: {
                                return flashPanel.getSavableData();
                        }
                        
                        case ANALYSIS_PANEL: {
                                return analysisPanel.getSavableData();
                        }
                        
                        case TRACKING_PANEL: {
                                return trackingPanel.getSavableData();
                        }
                        
                        case PROCESSING_PANEL: {
                                return processingPanel.getSavableData();
                        }
                        
                        case SPOT_METRICS_FRAME: {
                                return this.getSavableData();
                        }
                        
                        default: {
                                return null;
                        }
                }
        }
        
        public void setSavableDataPanel(Panels panelName, Map<Savables, String> savableData) throws NullPointerException {
                if(panelName == null) {
                        throw new NullPointerException("panelName cannot be null");
                }
                
                if(savableData == null) {
                        throw new NullPointerException("savableData cannot be null");
                }
                
                switch(panelName) {
                        case VIEWER_PANEL: {
                                viewerPanel.setSavableData(savableData);
                                break;
                        }
                        case FLASH_PANEL: {
                                flashPanel.setSavableData(savableData);
                                break;
                        }
                        
                        case ANALYSIS_PANEL: {
                                analysisPanel.setSavableData(savableData);
                                break;
                        }
                        
                        case TRACKING_PANEL: {
                                trackingPanel.setSavableData(savableData);
                                break;
                        }
                        
                        case PROCESSING_PANEL: {
                                processingPanel.setSavableData(savableData);
                                break;
                        }
                        
                        case SPOT_METRICS_FRAME: {
                                setSavableData(savableData);
                                break;
                        }
                        
                        default: {
                                break;
                        }
                }
        }

        @Override
        public Map<Savables, String> getSavableData() {
                Map<Savables,String> savableData = new HashMap<Savables,String>();
                if(getVideoFile() != null) {
                        savableData.put(Savables.MAIN_VIDEO_FILE_PATH, getVideoFile().getParent());
                        savableData.put(Savables.MAIN_VIDEO_FILE_NAME, getVideoFile().getName());
                }
                else {
                        savableData.put(Savables.MAIN_VIDEO_FILE_PATH, "");
                        savableData.put(Savables.MAIN_VIDEO_FILE_NAME, "");
                }
                
                return savableData;
        }

        @Override
        public void setSavableData(Map<Savables, String> savableData) {
                if(savableData.containsKey(Savables.MAIN_VIDEO_FILE_PATH)) {
                        String videoFilePath = savableData.get(Savables.MAIN_VIDEO_FILE_PATH);
                        String videoFileName = savableData.get(Savables.MAIN_VIDEO_FILE_NAME);
                        videoFile = new File(videoFilePath+File.separator+videoFileName);
                        videoField.setText(videoFile.getAbsolutePath());
                }
        }
        
        public static final void main(String... args) {
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
