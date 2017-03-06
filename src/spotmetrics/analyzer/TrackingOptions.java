package spotmetrics.analyzer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 6:32:51 PM - Jan 12, 2015
 * 
 */
public class TrackingOptions {

        private int blobDiameter = -1;
        private int blobThreshold = -1;
        private int linkingMaxDistance = -1;
        private int gapClosingMaxDistance = -1;
        private int gapClosingMaxFrameGap = -1;
        private int initialSpotFilterValue = -1;

        public TrackingOptions() {

        }

        public TrackingOptions(int blobDiameter, int blobThreshold, int linkingMaxDistance, int gapClosingMaxDistance) {
                setBlobDiameter(blobDiameter);
                setBlobThreshold(blobThreshold);
                setLinkingMaxDistance(linkingMaxDistance);
                setGapClosingMaxDistance(gapClosingMaxDistance);
        }

        public final int getBlobDiameter() {
                return blobDiameter;
        }

        public final void setBlobDiameter(int blobDiameter) {
                this.blobDiameter = blobDiameter;
        }

        public final int getBlobThreshold() {
                return blobThreshold;
        }

        public final void setBlobThreshold(int blobThreshold) {
                this.blobThreshold = blobThreshold;
        }

        public int getLinkingMaxDistance() {
                return linkingMaxDistance;
        }

        public void setLinkingMaxDistance(int linkingMaxDistance) {
                this.linkingMaxDistance = linkingMaxDistance;
        }

        public int getGapClosingMaxDistance() {
                return gapClosingMaxDistance;
        }

        public void setGapClosingMaxDistance(int gapClosingMaxDistance) {
                this.gapClosingMaxDistance = gapClosingMaxDistance;
        }

        public int getGapClosingMaxFrameGap() {
                return gapClosingMaxFrameGap;
        }

        public void setGapClosingMaxFrameGap(int gapClosingMaxFrameGap) {
                this.gapClosingMaxFrameGap = gapClosingMaxFrameGap;
        }

        public int getInitialSpotFilterValue() {
                return initialSpotFilterValue;
        }

        public void setInitialSpotFilterValue(int initialSpotFilterValue) {
                this.initialSpotFilterValue = initialSpotFilterValue;
        }
}
