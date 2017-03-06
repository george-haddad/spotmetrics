package spotmetrics.analyzer;

import java.awt.Color;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import spotmetrics.data.MySpot;
import spotmetrics.data.MyTrack;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 22, 2016
 *
 */
public final class TrackAnalyzer {

        private TrackAnalyzer() {

        }
        
        public static final Roi[] analyzeParticlesForOverlays(MyTrack track, ImagePlus imagePlus, AnalysisOptions analysisOpt) {
                int options = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES + ParticleAnalyzer.ADD_TO_MANAGER;
                
                RoiManager.getRoiManager().reset();
                
                if(RoiManager.getRoiManager().isShowing()) {
                        RoiManager.getRoiManager().setVisible(false);
                }
                
                ResultsTable rt = analyzeTracks(track, imagePlus, analysisOpt, options);
                
                Roi[] rois = RoiManager.getRoiManager().getRoisAsArray();
                for(int i=0; i < rois.length; i++) {
                        rois[i].setPosition(i+1);
                        if (rt.size() > i) {
                                double area = rt.getValueAsDouble(0, i);
                                rois[i].setName("T"+track.getTrackId()+" : "+area);
                        }
                        else {
                                rois[i].setName("T"+track.getTrackId()+" : 0.0");
                        }
                }
                
                RoiManager.getRoiManager().reset();
                
                return rois;
        }
        
        public static final ResultsTable analyzeParticles(MyTrack track, ImagePlus imagePlus, AnalysisOptions analysisOpt, boolean showResults, boolean showRoiManager) {
                int options = ParticleAnalyzer.EXCLUDE_EDGE_PARTICLES;

                if (showResults) {
                        options = options + ParticleAnalyzer.SHOW_RESULTS;
                }

                if (showRoiManager) {
                        options = options + ParticleAnalyzer.ADD_TO_MANAGER;
                        if(RoiManager.getRoiManager().isShowing()) {
                                RoiManager.getRoiManager().setVisible(false);
                        }
                        
                        RoiManager.getRoiManager().reset();
                }
                
                ResultsTable rt = analyzeTracks(track, imagePlus, analysisOpt, options);
                
                if(showRoiManager) {
                        Roi[] rois = RoiManager.getRoiManager().getRoisAsArray();
                        for(int i=0; i < rois.length; i++) {
                                rois[i].setPosition(i+1);
                                rois[i].setName("Track "+track.getTrackId());
                        }
                        
                        RoiManager.getRoiManager().reset();
                        
                        for(int i=0; i < rois.length; i++) {
                                RoiManager.getRoiManager().addRoi(rois[i]);
                        }
                        
                        if(!RoiManager.getRoiManager().isShowing()) {
                                RoiManager.getRoiManager().setVisible(true);
                        }
                        
                        RoiManager.getRoiManager().runCommand("Associate", "true");
                        RoiManager.getRoiManager().runCommand("Centered", "false");
                        RoiManager.getRoiManager().runCommand("UseNames", "true");
                        RoiManager.getRoiManager().runCommand(imagePlus, "Show All with labels");
                }

                return rt;
        }
        
        private static final ResultsTable analyzeTracks(MyTrack track, ImagePlus imagePlus, AnalysisOptions analysisOpt, int options) {
                int measurements = Measurements.AREA;

                double minSize = analysisOpt.getMinSize();
                double maxSize = 0;
                if (analysisOpt.isInfinity()) {
                        maxSize = Double.POSITIVE_INFINITY;
                }
                else {
                        maxSize = analysisOpt.getMaxSize();
                }

                double minCirc = analysisOpt.getMinCircularity();
                double maxCirc = analysisOpt.getMaxCircularity();
                
                ImageStack imageStack = imagePlus.getStack();
                ImageProcessor sliceProc = null;
                ImagePlus imp = null;
                
                ResultsTable rt = new ResultsTable();
                MySpot[] spots = track.getSpots();
                for (int i = 0; i < spots.length; i++) {
                        MySpot spot = spots[i];
                        Roi spotRoi = new Roi(spot.getX() - spot.getXOffset(), spot.getY() - spot.getYOffset(), spot.getW(), spot.getH());
                        spotRoi.setStrokeColor(Color.MAGENTA);
                        spotRoi.setStrokeWidth(1.0);
                        spotRoi.setPosition(spot.getFrame() + 1);

                        sliceProc = imageStack.getProcessor(spot.getFrame() + 1);
                        imp = new ImagePlus(spot.getLabel() + "_temp_" + i, sliceProc);
                        imp.setRoi(spotRoi);

                        ParticleAnalyzer analyzer = new ParticleAnalyzer(options, measurements, rt, minSize, maxSize, minCirc, maxCirc);
                        analyzer.setHideOutputImage(false);
                        analyzer.analyze(imp);
                        
                        imp.flush();
                        imp.close();
                        imp = null;
                        sliceProc = null;
                        analyzer = null;
                }
                
                return rt;
        }

        public static final void measureColors(MyTrack track, ImagePlus imagePlusColor) {
                ImageStack imageStack = imagePlusColor.getStack();
                int stackSize = imageStack.getSize();

                for (int i = 1; i <= stackSize; i++) {
                        MySpot spot = track.getSpotAt(i - 1);
                        ImageProcessor ip = imagePlusColor.getImageStack().getProcessor(i);
                        int pixel = ip.get(spot.getX(), spot.getY());
                        int[] rgb = getRGB(pixel);
                        spot.setRgb(rgb);
                }
        }
        
        private static final int RED = 16;
        private static final int GREEN = 8;
        private static final int BLUE = 0;

        private static final int[] getRGB(int pixel) {   
                int[] color = new int[3];
                color[0] = pixel >> RED & 0xff;
                color[1] = pixel >> GREEN & 0xff;
                color[2] = pixel >> BLUE & 0xff;
                return color;
        }
}
