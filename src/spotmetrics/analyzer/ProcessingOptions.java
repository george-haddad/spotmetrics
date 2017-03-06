package spotmetrics.analyzer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 2:27:18 PM - Sep 19, 2014
 * 
 */
public class ProcessingOptions {

        private int subtractBackgroundRollingValue = 0;
        private String autoThresholdValue = "";
        private boolean blackBackground = false;

        public ProcessingOptions() {

        }

        public ProcessingOptions(int subtractBackgroundRollingValue, String autoThresholdValue, boolean blackBackground) {
                setSubtractBackgroundRollingValue(subtractBackgroundRollingValue);
                setAutoThresholdValue(autoThresholdValue);
                setBlackBackground(blackBackground);
        }

        public final int getSubtractBackgroundRollingValue() {
                return subtractBackgroundRollingValue;
        }

        public final void setSubtractBackgroundRollingValue(int subtractBackgroundRollingValue) {
                this.subtractBackgroundRollingValue = subtractBackgroundRollingValue;
        }

        public final String getAutoThresholdValue() {
                return autoThresholdValue;
        }

        public final void setAutoThresholdValue(String autoThresholdValue) {
                this.autoThresholdValue = autoThresholdValue;
        }

        public final boolean isBlackBackground() {
                return blackBackground;
        }

        public final void setBlackBackground(boolean blackBackground) {
                this.blackBackground = blackBackground;
        }
}
