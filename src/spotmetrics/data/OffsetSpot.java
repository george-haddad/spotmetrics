package spotmetrics.data;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 22, 2016
 *
 */
public abstract class OffsetSpot {

        protected int xOffset = 0;
        protected int yOffset = 0;
        protected int w = 0;
        protected int h = 0;

        public OffsetSpot() {

        }

        public OffsetSpot(int xOffset, int yOffset, int w, int h) {
                this.xOffset = xOffset;
                this.yOffset = yOffset;
                this.w = w;
                this.h = h;
        }

        public final int getXOffset() {
                return xOffset;
        }

        public final void setXOffset(int xOffset) {
                this.xOffset = xOffset;
        }

        public final void setXOffset(double xOffset) {
                this.xOffset = (int) xOffset;
        }

        public final int getYOffset() {
                return yOffset;
        }

        public final void setYOffset(int yOffset) {
                this.yOffset = yOffset;
        }

        public final void setYOffset(double yOffset) {
                this.yOffset = (int) yOffset;
        }

        public final int getW() {
                return w;
        }

        public final void setW(int w) {
                this.w = w;
        }

        public final void setW(double w) {
                this.w = (int) w;
        }

        public final int getH() {
                return h;
        }

        public final void setH(int h) {
                this.h = h;
        }

        public final void setH(double h) {
                this.h = (int) h;
        }
}
