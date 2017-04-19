package spotmetrics.ui.file;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 20, 2015
 *
 */
public final class FileSave {

        private FileSave() {

        }
        
        public static final File saveFile(String title, File startDirectory, final String fileDesc, String defaultFileName) {
                return saveFile(title, startDirectory, fileDesc, defaultFileName, false);
        }
        
        public static final File saveFile(String title, File startDirectory, final String fileDesc, String defaultFileName, boolean dirsOnly) {

                JFileChooser chooser = new JFileChooser();
                
                if(dirsOnly) {
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                }
                else {
                        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                }
                
                chooser.setDialogTitle(title);

                if (startDirectory != null) {
                        chooser.setCurrentDirectory(startDirectory);
                }
                else {
                        chooser.setCurrentDirectory(new File(System.getProperties().getProperty("user.home")));
                }
                
                File defaultFile = null;
                if(defaultFileName != null) {
                        defaultFile = new File(chooser.getCurrentDirectory().getAbsolutePath() + File.separator + defaultFileName);
                }
                else {
                        defaultFile = new File(chooser.getCurrentDirectory().getAbsolutePath());
                }
                
                chooser.setSelectedFile(defaultFile);
                chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                                return f.isDirectory() || f.isFile();
                        }

                        @Override
                        public String getDescription() {
                                return fileDesc;
                        }
                });

                int r = chooser.showSaveDialog(null);
                
                File path = null;
                if (r == JFileChooser.APPROVE_OPTION) {
                        path = new File(chooser.getSelectedFile().getAbsolutePath());
                }
                
                return path;
        }
}
