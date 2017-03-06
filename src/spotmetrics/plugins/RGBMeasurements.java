package spotmetrics.plugins;

import ij.IJ;


/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 10:32:23 AM - Sep 5, 2014
 * 
 */
public final class RGBMeasurements {
        
        /*
         * RGB Mean
         */
        private double rMean = 0.0;
        private double gMean = 0.0;
        private double bMean = 0.0;
        
        /*
         * RGB Standard Deviation
         */
        private double rStdDev = 0.0;
        private double gStdDev = 0.0;
        private double bStdDev = 0.0;
        
        /*
         * RGB Area Fraction
         */
        private double rArea = 0.0;
        private double gArea = 0.0;
        private double bArea = 0.0;
        
        public RGBMeasurements() {
                
        }

        public final double getrMean() {
                return rMean;
        }

        public final void setrMean(double rMean) {
                this.rMean = rMean;
        }

        public final double getgMean() {
                return gMean;
        }

        public final void setgMean(double gMean) {
                this.gMean = gMean;
        }

        public final double getbMean() {
                return bMean;
        }

        public final void setbMean(double bMean) {
                this.bMean = bMean;
        }

        public final double getrStdDev() {
                return rStdDev;
        }

        public final void setrStdDev(double rStdDev) {
                this.rStdDev = rStdDev;
        }

        public final double getgStdDev() {
                return gStdDev;
        }

        public final void setgStdDev(double gStdDev) {
                this.gStdDev = gStdDev;
        }

        public final double getbStdDev() {
                return bStdDev;
        }

        public final void setbStdDev(double bStdDev) {
                this.bStdDev = bStdDev;
        }

        public final double getrArea() {
                return rArea;
        }

        public final void setrArea(double rArea) {
                this.rArea = rArea;
        }

        public final double getgArea() {
                return gArea;
        }

        public final void setgArea(double gArea) {
                this.gArea = gArea;
        }

        public final double getbArea() {
                return bArea;
        }

        public final void setbArea(double bArea) {
                this.bArea = bArea;
        }
        
        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("mean=[");
                sb.append(IJ.d2s(rMean, 2));
                sb.append(',');
                sb.append(IJ.d2s(gMean, 2));
                sb.append(',');
                sb.append(IJ.d2s(bMean, 2));
                sb.append(']');
                sb.append(';');
                sb.append("st. dev=[");
                sb.append(IJ.d2s(rStdDev, 2));
                sb.append(',');
                sb.append(IJ.d2s(gStdDev, 2));
                sb.append(',');
                sb.append(IJ.d2s(bStdDev, 2));
                sb.append(']');
                sb.append(';');
                sb.append("area fraction=[");
                sb.append(IJ.d2s(rArea, 4));
                sb.append(',');
                sb.append(IJ.d2s(gArea, 4));
                sb.append(',');
                sb.append(IJ.d2s(bArea, 4));
                sb.append(']');
                sb.append(';');
                return sb.toString();
        }
}
