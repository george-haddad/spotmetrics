package spotmetrics.data.save;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Apr 18, 2017
 *
 */
public enum Savables {
        MAIN_VIDEO_FILE(Panels.SPOT_METRICS_FRAME, "videoFile"),
        MAIN_TRACKS_MAP(Panels.SPOTS_PANEL, "tracksMap"),
        MAIN_VIDEO_DATA(Panels.SPOTS_PANEL, "imagePlus"),
        MAIN_VIDEO_COLOR_DATA(Panels.SPOTS_PANEL, "imagePlusColor"),
        VIEWER_FLASH_FRAME(Panels.VIEWER_PANEL, "flashFrame"),
        VIEWER_VIDEO_SELECTION(Panels.VIEWER_PANEL, "videoSelection"),
        PROCESSING_SUBTRACT_BACKGROUND(Panels.PROCESSING_PANEL, "subtractBackground"),
        PROCESSING_DARK_BACKGROUND(Panels.PROCESSING_PANEL, "darkBackground"),
        PROCESSING_THRESHOLD_METHOD(Panels.PROCESSING_PANEL, "thresholdMethod"),
        FLASH_DETECT_MODE(Panels.FLASH_PANEL, "flashDetectMode"),
        FLASH_OFFSET_BEFORE(Panels.FLASH_PANEL, "offsetBefore"),
        FLASH_OFFSET_AFTER(Panels.FLASH_PANEL, "offsetAfter"),
        FLASH_DETECT(Panels.FLASH_PANEL, "detectFlash"),
        FLASH_DELETE_ONLY(Panels.FLASH_PANEL, "deleteFlashOnly"),
        TRACK_BLOB_DIAMETER(Panels.TRACKING_PANEL, "blobDiameter"),
        TRACK_BLOB_THRESHOLD(Panels.TRACKING_PANEL, "blobThreshold"),
        TRACK_LINKING_MAX_DISTANCE(Panels.TRACKING_PANEL, "linkingMaxDistance"),
        TRACK_GAP_CLOSING_MAX_DISTANCE(Panels.TRACKING_PANEL, "gapClosingMaxDistance"),
        TRACK_GAP_CLOSING_MAX_FRAME_GAP(Panels.TRACKING_PANEL, "gapClosingMaxFrameGap"),
        TRACK_INITIAL_SPOT_FILTER_VALUE(Panels.TRACKING_PANEL, "initialSpotFilterValue"),
        ANALYSIS_X_OFFSET(Panels.ANALYSIS_PANEL, "xOffset"),
        ANALYSIS_Y_OFFSET(Panels.ANALYSIS_PANEL, "yOffset"),
        ANALYSIS_W_OFFSET(Panels.ANALYSIS_PANEL, "wOffset"),
        ANALYSIS_H_OFFSET(Panels.ANALYSIS_PANEL, "hOffset"),
        ANALYSIS_MIN_SIZE(Panels.ANALYSIS_PANEL, "minSize"),
        ANALYSIS_MAX_SIZE(Panels.ANALYSIS_PANEL, "maxSize"),
        ANALYSIS_MIN_CIRCULARITY(Panels.ANALYSIS_PANEL, "minCirc"),
        ANALYSIS_MAX_CIRCULARITY(Panels.ANALYSIS_PANEL, "maxCirc"),
        ANALYSIS_INFINITY(Panels.ANALYSIS_PANEL, "infinity");
        
        private final Panels panelName;
        private final String key;
        
        Savables(Panels panelName, String key) {
                this.panelName = panelName;
                this.key = key;
        }
        
        public final Panels getPanelName() {
                return panelName;
        }
        
        public final String getKey() {
                return key;
        }
        
        @Override
        public String toString() {
                return key;
        }
}
