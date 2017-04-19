package spotmetrics.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 4:53:24 PM - Sep 17, 2014
 * 
 */
public final class MyTrack implements Serializable {

        private static final long serialVersionUID = 2155179873649029886L;
        
        private String label = null;
        private int trackId = -1;
        private int numberOfSpots = -1;
        private int trackDuration = -1;
        private MySpot[] spots = null;
        private int index = 0;

        public MyTrack() {

        }

        public final String getLabel() {
                return label;
        }

        public final void setLabel(String label) {
                this.label = label;
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

        public final int getNumberOfSpots() {
                return numberOfSpots;
        }

        public final void setNumberOfSpots(int numberOfSpots) {
                this.numberOfSpots = numberOfSpots;
                spots = new MySpot[numberOfSpots];
        }

        public final void setNumberOfSpots(String numberOfSpots) {
                setNumberOfSpots(Integer.parseInt(numberOfSpots));
        }

        public final int getTrackDuration() {
                return trackDuration;
        }

        public final void setTrackDuration(int trackDuration) {
                this.trackDuration = trackDuration;
        }

        public final void setTrackDuration(String trackDuration) {
                setTrackDuration((int) Double.parseDouble(trackDuration));
        }

        public final MySpot[] getSpots() {
                return spots;
        }

        public final MySpot getSpotAt(int index) throws ArrayIndexOutOfBoundsException {
                return spots[index];
        }

        public final void setSpot(MySpot spot) {
                spots[index] = spot;
                index++;
        }
        
        public final void orderSpotArray() {
                Arrays.sort(spots, new Comparator<MySpot>() {
                        @Override
                        public int compare(MySpot o1, MySpot o2) {
                                if (o1.getFrame() == o2.getFrame()) {
                                        return 0;
                                }
                                else if (o1.getFrame() > o2.getFrame()) {
                                        return 1;
                                }
                                else {
                                        return -1;
                                }
                        }
                });
        }

        @Override
        public String toString() {
                return label;
        }
}
