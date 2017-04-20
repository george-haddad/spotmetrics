package spotmetrics.data.save;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Apr 18, 2017
 *
 */
public enum Savables {
        MAIN_VIDEO_FILE_PATH(Panels.SPOT_METRICS_FRAME, "frame.videoFilePath"),
        MAIN_VIDEO_FILE_NAME(Panels.SPOT_METRICS_FRAME, "frame.videoFileName"),
        VIEWER_FLASH_FRAME(Panels.VIEWER_PANEL, "panel.viewer.flashFrame"),
        VIEWER_VIDEO_SELECTION(Panels.VIEWER_PANEL, "panel.viewer.videoSelection"),
        PROCESSING_SUBTRACT_BACKGROUND(Panels.PROCESSING_PANEL, "panel.processing.subtractBackground"),
        PROCESSING_DARK_BACKGROUND(Panels.PROCESSING_PANEL, "panel.processing.darkBackground"),
        PROCESSING_THRESHOLD_METHOD(Panels.PROCESSING_PANEL, "panel.processing.thresholdMethod"),
        FLASH_DETECT_MODE(Panels.FLASH_PANEL, "panel.flash.flashDetectMode"),
        FLASH_OFFSET_BEFORE(Panels.FLASH_PANEL, "panel.flash.offsetBefore"),
        FLASH_OFFSET_AFTER(Panels.FLASH_PANEL, "panel.flash.offsetAfter"),
        FLASH_DETECT(Panels.FLASH_PANEL, "panel.flash.detectFlash"),
        FLASH_DELETE_ONLY(Panels.FLASH_PANEL, "panel.flash.deleteFlashOnly"),
        TRACK_BLOB_DIAMETER(Panels.TRACKING_PANEL, "panel.tracking.blobDiameter"),
        TRACK_BLOB_THRESHOLD(Panels.TRACKING_PANEL, "panel.tracking.blobThreshold"),
        TRACK_LINKING_MAX_DISTANCE(Panels.TRACKING_PANEL, "panel.tracking.linkingMaxDistance"),
        TRACK_GAP_CLOSING_MAX_DISTANCE(Panels.TRACKING_PANEL, "panel.tracking.gapClosingMaxDistance"),
        TRACK_GAP_CLOSING_MAX_FRAME_GAP(Panels.TRACKING_PANEL, "panel.tracking.gapClosingMaxFrameGap"),
        TRACK_INITIAL_SPOT_FILTER_VALUE(Panels.TRACKING_PANEL, "panel.tracking.initialSpotFilterValue"),
        ANALYSIS_X_OFFSET(Panels.ANALYSIS_PANEL, "panel.analysis.xOffset"),
        ANALYSIS_Y_OFFSET(Panels.ANALYSIS_PANEL, "panel.analysis.yOffset"),
        ANALYSIS_W_OFFSET(Panels.ANALYSIS_PANEL, "panel.analysis.wOffset"),
        ANALYSIS_H_OFFSET(Panels.ANALYSIS_PANEL, "panel.analysis.hOffset"),
        ANALYSIS_MIN_SIZE(Panels.ANALYSIS_PANEL, "panel.analysis.minSize"),
        ANALYSIS_MAX_SIZE(Panels.ANALYSIS_PANEL, "panel.analysis.maxSize"),
        ANALYSIS_MIN_CIRCULARITY(Panels.ANALYSIS_PANEL, "panel.analysis.minCirc"),
        ANALYSIS_MAX_CIRCULARITY(Panels.ANALYSIS_PANEL, "panel.analysis.maxCirc"),
        ANALYSIS_INFINITY(Panels.ANALYSIS_PANEL, "panel.analysis.infinity");
        
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
