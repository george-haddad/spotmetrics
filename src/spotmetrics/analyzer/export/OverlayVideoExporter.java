package spotmetrics.analyzer.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ij.ImagePlus;
import ij.gui.Overlay;
import ij.gui.Roi;
import spotmetrics.analyzer.AnalysisOptions;
import spotmetrics.analyzer.TrackAnalyzer;
import spotmetrics.data.MyTrack;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Nov 27, 2016
 */
public final class OverlayVideoExporter {

        private Map<String, MyTrack> tracksMap = null;
        private ImagePlus imagePlus = null;
        private ImagePlus imagePlusColor = null;
        private AnalysisOptions analysisOpt = null;
        
        public OverlayVideoExporter() {
                
        }
        
        public OverlayVideoExporter(HashMap<String, MyTrack> tracksMap, ImagePlus imagePlus, ImagePlus imagePlusColor, AnalysisOptions analysisOpt) {
                setTracks(tracksMap);
                setImagePlus(imagePlus);
                setImagePlusColor(imagePlusColor);
                setAnalysisOptions(analysisOpt);
        }
        
        public final void export() throws NotInitializedException {
                if (!isInit()) {
                        throw new NotInitializedException("The Overlay Exporter has not been properly initialized");
                }
                
                ArrayList<Roi> roiList = new ArrayList<Roi>(tracksMap.size());
                Iterator<MyTrack> iter = tracksMap.values().iterator();
                while (iter.hasNext()) {
                        MyTrack track = iter.next();
                        Roi[] rois = TrackAnalyzer.analyzeParticlesForOverlays(track, imagePlus, analysisOpt);
                        for(int i=0; i < rois.length; i++) {
                                roiList.add(rois[i]);
                        }
                }
                
                Overlay over = new Overlay();
                for(int i=0; i < roiList.size(); i++) {
                        over.add(roiList.get(i));
                }
                
                over.drawBackgrounds(false);
                over.drawNames(true);
                over.drawLabels(true);
                
                ImagePlus overlayVideo = imagePlusColor.duplicate();
                overlayVideo.setOverlay(over);
                overlayVideo.show();
        }
        
        public final void setTracks(Map<String, MyTrack> tracksMap) throws NullPointerException {
                if (tracksMap == null) {
                        throw new NullPointerException("Cannot set null tracksMap");
                }

                this.tracksMap = tracksMap;
        }

        public final void setImagePlus(ImagePlus imagePlus) throws NullPointerException {
                if (imagePlus == null) {
                        throw new NullPointerException("Cannot set null imagePlus");
                }

                this.imagePlus = imagePlus;
        }

        public final void setImagePlusColor(ImagePlus imagePlusColor) throws NullPointerException {
                if (imagePlusColor == null) {
                        throw new NullPointerException("Cannot set null imagePlusColor");
                }

                this.imagePlusColor = imagePlusColor;
        }

        public final void setAnalysisOptions(AnalysisOptions analysisOpt) throws NullPointerException {
                if (analysisOpt == null) {
                        throw new NullPointerException("Cannot set null analysisOpt");
                }

                this.analysisOpt = analysisOpt;
        }

        public final boolean isInit() {
                return tracksMap != null && imagePlus != null && analysisOpt != null;
        }
}
