package spotmetrics.analyzer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 21, 2016
 * 
 */
public class AnalysisOptions {

        private int xOffset;
        private int yOffset;
        private int wOffset;
        private int hOffset;
        private double minSize;
        private double maxSize;
        private double minCircularity;
        private double maxCircularity;
        private boolean isInfinity;

        public AnalysisOptions() {

        }

        public final int getXOffset() {
                return xOffset;
        }

        public final void setXOffset(int xOffset) {
                this.xOffset = xOffset;
        }

        public final int getYOffset() {
                return yOffset;
        }

        public final void setYOffset(int yOffset) {
                this.yOffset = yOffset;
        }

        public final int getWOffset() {
                return wOffset;
        }

        public final void setWOffset(int wOffset) {
                this.wOffset = wOffset;
        }

        public final int getHOffset() {
                return hOffset;
        }

        public final void setHOffset(int hOffset) {
                this.hOffset = hOffset;
        }

        public final double getMinSize() {
                return minSize;
        }

        public final void setMinSize(double minSize) {
                this.minSize = minSize;
        }

        public final double getMaxSize() {
                return maxSize;
        }

        public final void setMaxSize(double maxSize) {
                this.maxSize = maxSize;
        }

        public final double getMinCircularity() {
                return minCircularity;
        }

        public final void setMinCircularity(double minCircularity) {
                this.minCircularity = minCircularity;
        }

        public final double getMaxCircularity() {
                return maxCircularity;
        }

        public final void setMaxCircularity(double maxCircularity) {
                this.maxCircularity = maxCircularity;
        }

        public final boolean isInfinity() {
                return isInfinity;
        }

        public final void setInfinity(boolean isInfinity) {
                this.isInfinity = isInfinity;
        }
}
