package spotmetrics.data.save;

import java.io.File;
import java.util.HashMap;
import ij.ImagePlus;
import spotmetrics.analyzer.FlashDetect;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Apr 18, 2017
 *
 */
public enum Savables {
        MAIN_VIDEO_FILE("SpotMetricsFrame", "videoFile", File.class.getCanonicalName()),
        MAIN_TRACKS_MAP("SpotsPanel", "tracksMap", HashMap.class.getCanonicalName()),
        MAIN_VIDEO_DATA("SpotsPanel", "imagePlus", ImagePlus.class.getCanonicalName()),
        MAIN_VIDEO_COLOR_DATA("SpotsPanel", "imagePlusColor", ImagePlus.class.getCanonicalName()),
        VIEWER_FLASH_FRAME("ViewerPanel", "flashFrame", String.class.getCanonicalName()),
        VIEWER_VIDEO_SELECTION("ViewerPanel", "videoSelection", String.class.getCanonicalName()),
        PROCESSING_SUBTRACT_BACKGROUND("ProcessingPanel", "subtractBackground", Integer.class.getCanonicalName()),
        PROCESSING_DARK_BACKGROUND("ProcessingPanel", "darkBackground", Boolean.class.getCanonicalName()),
        PROCESSING_THRESHOLD_METHOD("ProcessingPanel", "thresholdMethod", String.class.getCanonicalName()),
        FLASH_DETECT_MODE("FlashPanel", "flashDetectMode", FlashDetect.class.getCanonicalName()),
        FLASH_OFFSET_BEFORE("FlashPanel", "offsetBefore", Integer.class.getCanonicalName()),
        FLASH_OFFSET_AFTER("FlashPanel", "offsetAfter", Integer.class.getCanonicalName()),
        FLASH_DETECT("FlashPanel", "detectFlash", Boolean.class.getCanonicalName()),
        FLASH_DELETE_ONLY("FlashPanel", "deleteFlashOnly", Boolean.class.getCanonicalName()),
        TRACK_BLOB_DIAMETER("TrackingPanel", "blobDiameter", Integer.class.getCanonicalName()),
        TRACK_BLOB_THRESHOLD("TrackingPanel", "blobThreshold", Integer.class.getCanonicalName()),
        TRACK_LINKING_MAX_DISTANCE("TrackingPanel", "linkingMaxDistance", Integer.class.getCanonicalName()),
        TRACK_GAP_CLOSING_MAX_DISTANCE("TrackingPanel", "gapClosingMaxDistance", Integer.class.getCanonicalName()),
        TRACK_GAP_CLOSING_MAX_FRAME_GAP("TrackingPanel", "gapClosingMaxFrameGap", Integer.class.getCanonicalName()),
        TRACK_INITIAL_SPOT_FILTER_VALUE("TrackingPanel", "initialSpotFilterValue", Integer.class.getCanonicalName()),
        ANALYSIS_X_OFFSET("AnalysisPanel", "xOffset", Integer.class.getCanonicalName()),
        ANALYSIS_Y_OFFSET("AnalysisPanel", "yOffset", Integer.class.getCanonicalName()),
        ANALYSIS_W_OFFSET("AnalysisPanel", "wOffset", Integer.class.getCanonicalName()),
        ANALYSIS_H_OFFSET("AnalysisPanel", "hOffset", Integer.class.getCanonicalName()),
        ANALYSIS_MIN_SIZE("AnalysisPanel", "minSize", Double.class.getCanonicalName()),
        ANALYSIS_MAX_SIZE("AnalysisPanel", "maxSize", Double.class.getCanonicalName()),
        ANALYSIS_MIN_CIRCULARITY("AnalysisPanel", "minCirc", Double.class.getCanonicalName()),
        ANALYSIS_MAX_CIRCULARITY("AnalysisPanel", "maxCirc", Double.class.getCanonicalName()),
        ANALYSIS_INFINITY("AnalysisPanel", "infinity", Boolean.class.getCanonicalName());
        
        private final String panelName;
        private final String keyName;
        private final String castType;
        
        Savables(String panelName, String keyName, String castType) {
                this.panelName = panelName;
                this.keyName = keyName;
                this.castType = castType;
        }
        
        public final String getCastType() {
                return castType;
        }
        
        public final String getPanelName() {
                return panelName;
        }
        
        public final String getKeyName() {
                return keyName;
        }
}
