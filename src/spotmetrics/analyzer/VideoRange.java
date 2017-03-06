package spotmetrics.analyzer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 1:14:55 PM - Sep 5, 2014
 * 
 */
public class VideoRange {

        private int flashFrame = -1;
        private int startSlice = 0;
        private int endSlice = 0;
        private int size = 0;

        public VideoRange(int size) {
                setSize(size);
        }

        public VideoRange(int startSlice, int endSlice, int size) {
                setSize(size);
                setStartSlice(startSlice);
                setEndSlice(endSlice);
        }

        public VideoRange(int flashFrame, int startSlice, int endSlice, int size) {
                setSize(size);
                setFlashFrame(flashFrame);
                setStartSlice(startSlice);
                setEndSlice(endSlice);
        }

        public final int getStartSlice() {
                return startSlice;
        }

        public final void setStartSlice(int startSlice) {
                this.startSlice = startSlice;
        }

        public final int getEndSlice() {
                return endSlice;
        }

        public final void setEndSlice(int endSlice) {
                this.endSlice = endSlice;
        }

        public final int getFlashFrame() {
                return flashFrame;
        }

        public final void setFlashFrame(int flashFrame) {
                this.flashFrame = flashFrame;
        }

        public final void setSize(int size) {
                this.size = size;
        }

        public final int getSize() {
                return size;
        }

        public final boolean hasFlashRange() {
                return flashFrame != -1;
        }
}
