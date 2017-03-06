package spotmetrics.analyzer;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 9:54:07 AM - Sep 19, 2014
 * 
 */
public enum FlashDetect {

        STRICT("Strict", "Flash is detected if all RGB color channels are NaN"),
        SEMISTRICT("Semi-Strict", "Flash is detected if at least the R & G color channels are NaN"),
        RELAXED("Relaxed", "Flash is detected if any one of the RGB color channel are NaN");

        private String name = null;
        private String desc = null;

        FlashDetect(String name, String desc) {
                this.name = name;
                this.desc = desc;
        }

        public final String getName() {
                return name;
        }

        public final String getDesc() {
                return desc;
        }

        @Override
        public String toString() {
                return name;
        }
}
