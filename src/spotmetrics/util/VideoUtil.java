package spotmetrics.util;

import java.io.File;
import ij.ImagePlus;
import spotmetrics.plugins.AVI_Reader;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: 10:04:32 AM - Apr 20, 2017
 * 
 */
public final class VideoUtil {

        private VideoUtil() {
                
        }
        
        public static final ImagePlus loadVideo(File videoFile) throws NullPointerException {
                if (videoFile == null) {
                        throw new NullPointerException("Cannot load a null video");
                }
                
                AVI_Reader aviReader = new AVI_Reader();
                aviReader.openVideo(videoFile, true);
                ImagePlus imagePlus = aviReader.getImagePlus();
                aviReader = null;
                return imagePlus;
        }
}
