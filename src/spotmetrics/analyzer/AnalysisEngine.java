package spotmetrics.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import fiji.plugin.trackmate.Logger;
import fiji.plugin.trackmate.Model;
import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.TrackModel;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.DogDetectorFactory;
import fiji.plugin.trackmate.tracking.LAPUtils;
import fiji.plugin.trackmate.tracking.TrackerKeys;
import fiji.plugin.trackmate.tracking.oldlap.SimpleLAPTrackerFactory;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.gui.Roi;
import ij.measure.Calibration;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import spotmetrics.data.MySpot;
import spotmetrics.data.MyTrack;
import spotmetrics.plugins.AVI_Reader;
import spotmetrics.plugins.RGBMeasurements;
import spotmetrics.plugins.RGB_Measure_Plus;
import spotmetrics.ui.ProgressUpdatableFrame;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 9:37:43 AM - Sep 19, 2014
 * 
 */
public final class AnalysisEngine {

        private FlashDetect flashDetect = null;
        private int offsetBeforeFlash = 0;
        private int offsetAfterFlash = 0;
        private boolean detectFlash = false;
        private boolean deleteFlashOnly = false;
        private ProcessingOptions processingOptions = null;
        private TrackingOptions trackingOptions = null;
        private ProgressUpdatableFrame parentFrame = null;
        private ImagePlus imagePlus = null;
        private ImagePlus imagePlusColor = null;
        
        public AnalysisEngine() {

        }

        public final void setFlashDetect(FlashDetect flashDetect) throws NullPointerException {
                if (flashDetect == null) {
                        throw new NullPointerException("FlashDetect parameter cannot be set to null");
                }

                this.flashDetect = flashDetect;
        }

        public final FlashDetect getFlashDetect() {
                return flashDetect;
        }

        public final int getOffsetBeforeFlash() {
                return offsetBeforeFlash;
        }

        public final void setOffsetBeforeFlash(int offsetBeforeFlash) {
                this.offsetBeforeFlash = offsetBeforeFlash;
        }

        public final int getOffsetAfterFlash() {
                return offsetAfterFlash;
        }

        public final void setOffsetAfterFlash(int offsetAfterFlash) {
                this.offsetAfterFlash = offsetAfterFlash;
        }

        public final boolean isDetectFlash() {
                return detectFlash;
        }

        public final void setDetectFlash(boolean detectFlash) {
                this.detectFlash = detectFlash;
        }
        
        public final boolean isDeleteFlashOnly() {
                return deleteFlashOnly;
        }
        
        public final void setDeleteFlashOnly(boolean deleteFlashOnly) {
                this.deleteFlashOnly = deleteFlashOnly;
        }
        
        public final void setImagePlus(ImagePlus imagePlus) {
                this.imagePlus = imagePlus;
        }
        
        public final ImagePlus getImagePlus() {
                return imagePlus;
        }
        
        public final ImagePlus getImagePlusColor() {
                return imagePlusColor;
        }

        public final void setParentFrame(ProgressUpdatableFrame parentFrame) throws NullPointerException {
                if (parentFrame == null) {
                        throw new NullPointerException("Cannot set a null parent frame");
                }

                this.parentFrame = parentFrame;
        }

        public final ProcessingOptions getProcessingOptions() {
                return processingOptions;
        }

        public final void setProcessingOptions(ProcessingOptions processingOptions) throws NullPointerException {
                if (processingOptions == null) {
                        throw new NullPointerException("Cannot set a null processing options");
                }

                this.processingOptions = processingOptions;
        }

        public final TrackingOptions getTrackingOptions() {
                return trackingOptions;
        }

        public final void setTrackingOptions(TrackingOptions trackingOptions) throws NullPointerException {
                if (trackingOptions == null) {
                        throw new NullPointerException("Cannot set a null tracking options");
                }

                this.trackingOptions = trackingOptions;
        }

        public final VideoRange analyzeVideo() {
                VideoRange videoRange = calculateVideoRange(imagePlus);
                return videoRange;
        }
        
        public final void processVideo() {
                if (processingOptions != null && imagePlus != null) {
                        parentFrame.updateProgressBar("Converting to 8bit ...");

                        IJ.run(imagePlus, "8-bit", "");

                        parentFrame.updateProgressBar("Subtracting background ...");

                        int rolling = processingOptions.getSubtractBackgroundRollingValue();
                        boolean blackBackground = processingOptions.isBlackBackground();

                        IJ.run(imagePlus, "Subtract Background...", "rolling=" + rolling + " " + (blackBackground == false ? "light" : "") + " stack");

                        parentFrame.updateProgressBar("Auto adjusting threshold ...");

                        IJ.setAutoThreshold(imagePlus, "Default");
                        Prefs.blackBackground = blackBackground;

                        parentFrame.updateProgressBar("Converting to mask ...");
                        IJ.run(imagePlus, "Convert to Mask", "method=Default background=" + (blackBackground == false ? "Light" : "") + " calculate stack");
                }
        }

        public final HashMap<String, MyTrack> trackSpots() {
                HashMap<String, MyTrack> tracksMap = null;

                calibrateImageDimensions();

                parentFrame.updateProgressBar("Setting up TrackMate ...");

                Model model = new Model();
                model.setLogger(Logger.IJ_LOGGER);

                int blobDiameter = trackingOptions.getBlobDiameter();
                int blobThreshold = trackingOptions.getBlobThreshold();
                int linkingMaxDistance = trackingOptions.getLinkingMaxDistance();
                int gapClosingMaxDistance = trackingOptions.getGapClosingMaxDistance();
                int gapClosingMaxFrameGap = trackingOptions.getGapClosingMaxFrameGap();
                int initialSpotFilterValue = trackingOptions.getInitialSpotFilterValue();

                Settings settings = new Settings();
                settings.setFrom(imagePlus);
                settings.detectorFactory = new DogDetectorFactory<UnsignedShortType>();
                settings.detectorSettings.put(DetectorKeys.KEY_DO_SUBPIXEL_LOCALIZATION, Boolean.TRUE);
                settings.detectorSettings.put(DetectorKeys.KEY_DO_MEDIAN_FILTERING, Boolean.FALSE);
                settings.detectorSettings.put(DetectorKeys.KEY_RADIUS, Double.valueOf((blobDiameter / 2)));
                settings.detectorSettings.put(DetectorKeys.KEY_THRESHOLD, Double.valueOf(blobThreshold));
                settings.detectorSettings.put(DetectorKeys.KEY_TARGET_CHANNEL, Integer.valueOf(1));
                settings.trackerFactory = new SimpleLAPTrackerFactory();
                settings.trackerSettings = LAPUtils.getDefaultLAPSettingsMap();
                settings.trackerSettings.put(TrackerKeys.KEY_LINKING_MAX_DISTANCE, Double.valueOf(linkingMaxDistance));
                settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_DISTANCE, Double.valueOf(gapClosingMaxDistance));
                settings.trackerSettings.put(TrackerKeys.KEY_GAP_CLOSING_MAX_FRAME_GAP, Integer.valueOf(gapClosingMaxFrameGap));
                settings.initialSpotFilterValue = Double.valueOf(initialSpotFilterValue);

                TrackMate trackMate = new TrackMate(model, settings);

                boolean ok = trackMate.checkInput();
                if (ok) {
                        boolean success = false;
                        parentFrame.updateProgressBar("Detecting spots ...");
                        success = trackMate.execDetection();
                        if (!success) {
                                IJ.error("TrackMate Error - Detection", trackMate.getErrorMessage());
                        }

                        if (success) {
                                parentFrame.updateProgressBar("Initial spot filtering ...");
                                success = trackMate.execInitialSpotFiltering();
                                if (!success) {
                                        IJ.error("TrackMate Error - Initial Filtering", trackMate.getErrorMessage());
                                }
                        }

                        if (success) {
                                parentFrame.updateProgressBar("Spot filtering ...");
                                success = trackMate.execSpotFiltering(false);
                                if (!success) {
                                        IJ.error("TrackMate Error - Spot Filtering", trackMate.getErrorMessage());
                                }
                        }

                        if (success) {
                                parentFrame.updateProgressBar("Tracking spots ...");
                                success = trackMate.execTracking();
                                if (!success) {
                                        IJ.error("TrackMate Error - Tracking", trackMate.getErrorMessage());
                                }
                        }

                        if (success) {
                                parentFrame.updateProgressBar("Track filtering ...");
                                success = trackMate.execTrackFiltering(false);
                                if (!success) {
                                        IJ.error("TrackMate Error - Track Filtering", trackMate.getErrorMessage());
                                }
                        }

                        tracksMap = new HashMap<String, MyTrack>();
                        TrackModel trackModel = model.getTrackModel();
                        Set<Integer> trackIds = trackModel.trackIDs(true);

                        for (Integer trackId : trackIds) {
                                Set<Spot> spots = trackModel.trackSpots(trackId);

                                MyTrack tr = new MyTrack();
                                tr.setLabel("Track_" + trackId);
                                tr.setTrackId(trackId.intValue());
                                tr.setNumberOfSpots(spots.size());
                                tr.setTrackDuration(spots.size());

                                for (Spot spot : spots) {
                                        MySpot sp = new MySpot();
                                        sp.setId(spot.ID());
                                        sp.setTrackId(trackId.intValue());
                                        sp.setLabel(spot.getName());
                                        sp.setX(spot.getFeature(Spot.POSITION_X).intValue());
                                        sp.setY(spot.getFeature(Spot.POSITION_Y).intValue());
                                        sp.setFrame(spot.getFeature(Spot.FRAME).intValue());
                                        sp.setVisibility(spot.getFeature(SpotCollection.VISIBLITY).intValue());
                                        sp.setQuality(spot.getFeature(Spot.QUALITY).floatValue());
                                        sp.setRadius(spot.getFeature(Spot.RADIUS).floatValue());
                                        tr.setSpot(sp);
                                }

                                tr.orderSpotArray();
                                tracksMap.put(tr.getLabel(), tr);
                        }

                        trackIds = null;
                        trackModel = null;
                }
                else {
                        IJ.error("TrackMate Error - Settings", trackMate.getErrorMessage());
                }

                parentFrame.updateProgressBar("Syncing tracks to video range ...");
                filterTrackList(tracksMap, model);

                model = null;
                settings = null;
                trackMate = null;

                return tracksMap;
        }

        private final void filterTrackList(HashMap<String, MyTrack> tracksMap, Model model) {
                //Only keep the tracks who have spots between startFrame and endFrame
                //Remove all tracks that don't match
                //Remove all spots below and above the range

                parentFrame.updateProgressBar("Synchronizng tracks to video range ... ");

                List<String> deleteTrackIDs = new ArrayList<String>();

                Iterator<MyTrack> iter = tracksMap.values().iterator();
                while (iter.hasNext()) {
                        MyTrack track = iter.next();

                        if (track.getTrackDuration() != imagePlus.getStackSize()) {
                                deleteTrackIDs.add(track.getLabel());
                        }
                }

                for (String trackID : deleteTrackIDs) {
                        tracksMap.remove(trackID);
                }

                deleteTrackIDs.clear();
                deleteTrackIDs = null;
        }
        
        public final ImagePlus loadVideo(File videoFile) {
                if (videoFile == null) {
                        throw new NullPointerException("Cannot analyzer a null video");
                }
                
                AVI_Reader aviReader = new AVI_Reader();
                parentFrame.updateProgressBar("Loading video file ...");
                aviReader.openVideo(videoFile, true);
                ImagePlus imagePlus = aviReader.getImagePlus();
                aviReader = null;
                return imagePlus;
        }
        
        public final void cropVideo() {
                if(imagePlus != null) {
                        Roi roi = imagePlus.getRoi();
                        if(roi != null) {
                                IJ.run(imagePlus, "Crop", "");
                        }
                }
        }

        private final VideoRange calculateVideoRange(ImagePlus imagePlus) {
                VideoRange videoRange = null;
                
                if (detectFlash) {
                        parentFrame.updateProgressBar("Detecting flash ...");

                        int flashSlice = detectFlash(imagePlus);
                        if (flashSlice > -1) {
                                parentFrame.updateProgressBar("Removing flash ...");
                                ImageStack imageStack = imagePlus.getStack();
                                
                                if(deleteFlashOnly) {
                                        imageStack.deleteSlice(flashSlice);
                                        
                                        videoRange = new VideoRange(imageStack.getSize());
                                        videoRange.setFlashFrame(flashSlice);
                                        videoRange.setStartSlice(1);
                                        videoRange.setEndSlice(imageStack.getSize());
                                }
                                else {
                                        int beforeFlashRange = flashSlice - offsetBeforeFlash;
                                        int afterFlashRange = flashSlice + offsetAfterFlash;
                                        
                                        imageStack.deleteSlice(flashSlice);
                                        
                                        if(afterFlashRange < imageStack.getSize()) {
                                                int toDelete = imageStack.getSize() - afterFlashRange;
                                                for(int i=0; i < toDelete; i++) {
                                                        imageStack.deleteSlice(imageStack.getSize());
                                                }
                                        }
                                        
                                        
                                        if(beforeFlashRange > 1) {
                                                int toDelete = beforeFlashRange;
                                                for(int i=0; i < toDelete; i++) {
                                                        imageStack.deleteSlice(1);
                                                }
                                        }
                                        
                                        videoRange = new VideoRange(imageStack.getSize());
                                        videoRange.setFlashFrame(flashSlice);
                                        videoRange.setStartSlice(beforeFlashRange);
                                        videoRange.setEndSlice(afterFlashRange);
                                }
                                
                                imagePlus.resetStack();
                                imagePlus.setStack(imageStack);
                                imagePlus.updateAndRepaintWindow();
                                imagePlus.updateStatusbarValue();
                        }
                        else {
                                IJ.showMessage("Flash was not detected");
                        }
                }
                else {
                        videoRange = new VideoRange(1, imagePlus.getStackSize(), imagePlus.getStackSize());
                }

                this.imagePlus = imagePlus;
                this.imagePlusColor = imagePlus.duplicate();

                imagePlus.show();

                return videoRange;
        }

        /**
         * <p>Returns the slice number (frame number) where the flash was detected.</p>
         * 
         * @param imagePlus - the image to detect flash frame
         * @return the slice number or frame number containing the flash
         */
        private final int detectFlash(ImagePlus imagePlus) {
                ImageStack imageStack = imagePlus.getStack();
                int stackSize = imageStack.getSize();

                RGB_Measure_Plus rgbMeasurePlus = new RGB_Measure_Plus();
                RGBMeasurements rgbMsr = null;

                int flashSlice = -1;

                for (int i = 1; i <= stackSize; i++) {
                        rgbMsr = rgbMeasurePlus.measureRGB(imagePlus.getWidth(), imagePlus.getHeight(), imageStack.getPixels(i));

                        if (isFlash(rgbMsr)) {
                                flashSlice = i;
                        }
                }

                rgbMeasurePlus = null;
                rgbMsr = null;

                return flashSlice;
        }

        private final boolean isFlash(RGBMeasurements rgbMeasurements) {
                switch (flashDetect) {
                        case STRICT: {
                                if (Double.isNaN(rgbMeasurements.getrStdDev()) && Double.isNaN(rgbMeasurements.getgStdDev()) && Double.isNaN(rgbMeasurements.getbStdDev())) {
                                        return true;
                                }
                                else {
                                        return false;
                                }
                        }

                        case SEMISTRICT: {
                                if (Double.isNaN(rgbMeasurements.getrStdDev()) && Double.isNaN(rgbMeasurements.getgStdDev()) || Double.isNaN(rgbMeasurements.getbStdDev())) {
                                        return true;
                                }
                                else {
                                        return false;
                                }
                        }

                        case RELAXED: {
                                if (Double.isNaN(rgbMeasurements.getrStdDev()) || Double.isNaN(rgbMeasurements.getgStdDev()) || Double.isNaN(rgbMeasurements.getbStdDev())) {
                                        return true;
                                }
                                else {
                                        return false;
                                }
                        }

                        default: {
                                return false;
                        }
                }
        }

        public final void close() {
                closeImagePlus();
                closeImagePlusColor();
        }
        
        private final void closeImagePlus() {
                if (imagePlus != null) {
                        imagePlus.hide();
                        imagePlus.unlock();
                        imagePlus.flush();
                        imagePlus.close();
                        imagePlus = null;
                }
        }
        
        private final void closeImagePlusColor() {
                if (imagePlusColor != null) {
                        imagePlusColor.hide();
                        imagePlusColor.unlock();
                        imagePlusColor.flush();
                        imagePlusColor.close();
                        imagePlusColor = null;
                }
        }

        /**
         * @author tinevez
         * https://github.com/fiji/TrackMate/commits/master/src/main/java/fiji/plugin/trackmate/gui/GuiUtils.java
         */
        public final void calibrateImageDimensions() {
                final int[] dims = imagePlus.getDimensions();
                if (dims[4] == 1 && dims[3] > 1) {
                        imagePlus.setDimensions(dims[2], dims[4], dims[3]);
                        Calibration calibration = imagePlus.getCalibration();
                        if (calibration.frameInterval == 0) {
                                calibration.frameInterval = 1;
                        }
                }
        }
}
