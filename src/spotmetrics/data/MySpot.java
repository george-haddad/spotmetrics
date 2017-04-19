package spotmetrics.data;

import java.io.Serializable;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 5:06:52 PM - Sep 17, 2014
 * 
 */
public final class MySpot extends OffsetSpot implements Comparable<MySpot>, Serializable {

        private static final long serialVersionUID = 4154212715782798790L;
        
        private String label = null;
        private int id = -1;
        private int trackId = -1;
        private float quality = 0.0f;
        private int x = -1;
        private int y = -1;
        private int frame = -1;
        private int visibility = -1;
        private float radius = -1;
        private int[] rgb = null;

        public MySpot() {

        }

        public final String getLabel() {
                return label;
        }

        public final void setLabel(String label) {
                this.label = label;
        }

        public final int getId() {
                return id;
        }

        public final void setId(int id) {
                this.id = id;
        }

        public final void setId(String id) {
                setId(Integer.parseInt(id));
        }

        public final int getTrackId() {
                return trackId;
        }

        public final void setTrackId(int trackId) {
                this.trackId = trackId;
        }

        public final void setTrackId(String trackId) {
                setTrackId(Integer.parseInt(trackId));
        }

        public final float getQuality() {
                return quality;
        }

        public final void setQuality(float quality) {
                this.quality = quality;
        }

        public final void setQuality(String quality) {
                setQuality(Float.parseFloat(quality));
        }

        public final int getX() {
                return x;
        }

        public final void setX(int x) {
                this.x = x;
        }

        public final void setX(double x) {
                this.x = (int) x;
        }

        public final void setX(String x) {
                setX((int) Double.parseDouble(x));
        }

        public final int getY() {
                return y;
        }

        public final void setY(int y) {
                this.y = y;
        }

        public final void setY(double y) {
                this.y = (int) y;
        }

        public final void setY(String y) {
                setY((int) Double.parseDouble(y));
        }

        public final int getFrame() {
                return frame;
        }

        public final void setFrame(int frame) {
                this.frame = frame;
        }

        public final void setFrame(String frame) {
                setFrame(Integer.parseInt(frame));
        }

        public final int getVisibility() {
                return visibility;
        }

        public final void setVisibility(int visibility) {
                this.visibility = visibility;
        }

        public final void setVisibility(String visibility) {
                setVisibility(Integer.parseInt(visibility));
        }

        public final float getRadius() {
                return radius;
        }

        public final void setRadius(float radius) {
                this.radius = radius;
        }

        public final void setRgb(int[] rgb) {
                this.rgb = rgb;
        }

        public final int[] getRgb() {
                return rgb;
        }
        
        public final String getRgbString() {
                StringBuilder sb = new StringBuilder();
                sb.append(rgb[0]);
                sb.append(',');
                sb.append(rgb[1]);
                sb.append(',');
                sb.append(rgb[2]);
                return sb.toString();
        }

        @Override
        public int compareTo(MySpot o) {
                if (id == o.getId()) {
                        return 0;
                }
                else if (id < o.getId()) {
                        return -1;
                }
                else {
                        return 1;
                }
        }

        @Override
        public String toString() {
                return label + " [" + x + "," + y + "] @ frame " + frame;
        }
}
