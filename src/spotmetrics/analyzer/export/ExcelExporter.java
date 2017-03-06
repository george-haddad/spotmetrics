package spotmetrics.analyzer.export;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ij.ImagePlus;
import ij.measure.ResultsTable;
import spotmetrics.analyzer.AnalysisOptions;
import spotmetrics.analyzer.TrackAnalyzer;
import spotmetrics.data.MySpot;
import spotmetrics.data.MyTrack;

/**
 * 
 * @author George El Haddad (george.dma@gmail.com)
 * <p>
 * Created on: Mar 22, 2016
 *
 */
public final class ExcelExporter implements Closeable {

        private HashMap<String, MyTrack> tracksMap = null;
        private ImagePlus imagePlus = null;
        private ImagePlus imagePlusColor = null;
        private AnalysisOptions analysisOpt = null;

        private XSSFWorkbook wb = null;
        private File excelFile = null;

        public ExcelExporter() {

        }

        public ExcelExporter(HashMap<String, MyTrack> tracksMap, ImagePlus imagePlus, ImagePlus imagePlusColor, AnalysisOptions analysisOpt) {
                setTracks(tracksMap);
                setImagePlus(imagePlus);
                setImagePlusColor(imagePlusColor);
                setAnalysisOptions(analysisOpt);
        }

        public final void export(File excelFile) throws NotInitializedException, IOException {
                if (!isInit()) {
                        throw new NotInitializedException("The ExcelExporter has not been properly initialized");
                }

                if (excelFile == null) {
                        throw new NullPointerException("Cannot set a null excelFile");
                }

                this.excelFile = excelFile;

                wb = new XSSFWorkbook();
                Sheet sheetSpots = wb.createSheet("Spot Area");
                writeAreaMeasurements(sheetSpots);

                Sheet sheetRgbs = wb.createSheet("Spot RGB");
                writeRgbMeasurements(sheetRgbs);
        }

        private final void writeAreaMeasurements(Sheet sheet) {
                int columnIndex = 0;
                writeFrameColumn(sheet, columnIndex);
                columnIndex++;

                Iterator<MyTrack> iter = tracksMap.values().iterator();
                while (iter.hasNext()) {
                        MyTrack track = iter.next();
                        ResultsTable rt = TrackAnalyzer.analyzeParticles(track, imagePlus, analysisOpt, false, false);
                        writeTrackColumn(track, rt, sheet, columnIndex);
                        rt = null;
                        columnIndex++;
                }
        }

        private final void writeFrameColumn(Sheet sheet, int columnIndex) {
                //First row is header

                int frames = imagePlus.getStackSize();
                sheet.createRow(0).createCell(columnIndex).setCellValue("Frame");

                for (int i = 0; i < frames; i++) {
                        sheet.createRow(i + 1).createCell(columnIndex).setCellValue(i + 1);
                }
        }

        private final void writeTrackColumn(MyTrack track, ResultsTable rt, Sheet sheet, int columnIndex) {
                sheet.getRow(0).createCell(columnIndex).setCellValue("#" + track.getTrackId());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

                sheet.getRow(0).getCell(columnIndex).setCellStyle(cellStyle);
                MySpot[] spots = track.getSpots();
                for (int i = 0; i < spots.length; i++) {
                        //Print 0.0 if the spot array is less than the total number of frames
                        if (rt.size() > i) {
                                sheet.getRow(i + 1).createCell(columnIndex).setCellValue(rt.getValueAsDouble(0, i));
                        }
                        else {
                                sheet.getRow(i + 1).createCell(columnIndex).setCellValue(0.0d);
                        }
                }
        }
        
        private int rowIndex = 0;
        
        private final void writeRgbMeasurements(Sheet sheet) {
                rowIndex = 0;
                writeRgbRowHeaders(sheet);
                rowIndex++;
                
                Collection<MyTrack> coll = tracksMap.values();
                for (MyTrack myTrack : coll) {
                        TrackAnalyzer.measureColors(myTrack, imagePlusColor);
                        writeRgbTrackRow(myTrack, sheet);
                }
        }

        private final void writeRgbRowHeaders(Sheet sheet) {
                Row rowHeader = sheet.createRow(rowIndex);
                rowHeader.createCell(0).setCellValue("Spot #");
                rowHeader.createCell(1).setCellValue("Frame");
                rowHeader.createCell(2).setCellValue("R");
                rowHeader.createCell(3).setCellValue("G");
                rowHeader.createCell(4).setCellValue("B");
        }

        private final void writeRgbTrackRow(MyTrack track, Sheet sheet) {
                MySpot[] spots = track.getSpots();

                for (int i = 0; i < spots.length; i++) {
                        MySpot mySpot = spots[i];
                        Row row = sheet.createRow(rowIndex);
                        row.createCell(0).setCellValue(mySpot.getTrackId());
                        row.createCell(1).setCellValue(mySpot.getFrame());
                        int[] rgb = mySpot.getRgb();
                        if (rgb != null) {
                                row.createCell(2).setCellValue(rgb[0]);
                                row.createCell(3).setCellValue(rgb[1]);
                                row.createCell(4).setCellValue(rgb[2]);
                        }
                        else {
                                row.createCell(2).setCellValue(Double.NaN);
                                row.createCell(3).setCellValue(Double.NaN);
                                row.createCell(4).setCellValue(Double.NaN);
                        }

                        rowIndex++;
                }
        }

        @Override
        public final void close() throws IOException {
                if (wb != null) {
                        try {
                                FileOutputStream fileOut = new FileOutputStream(excelFile);
                                wb.write(fileOut);
                                fileOut.close();
                        }
                        finally {
                                wb = null;
                        }
                }
        }

        public final void setTracks(HashMap<String, MyTrack> tracksMap) throws NullPointerException {
                if (tracksMap == null) {
                        throw new NullPointerException("Cannot set null tracksMap");
                }

                this.tracksMap = tracksMap;
        }

        public final void setImagePlus(ImagePlus imagePlus) throws NullPointerException {
                if (imagePlus == null) {
                        throw new NullPointerException("Cannot set null imagePlus");
                }

                this.imagePlus = imagePlus;
        }

        public final void setImagePlusColor(ImagePlus imagePlusColor) throws NullPointerException {
                if (imagePlusColor == null) {
                        throw new NullPointerException("Cannot set null imagePlusColor");
                }

                this.imagePlusColor = imagePlusColor;
        }

        public final void setAnalysisOptions(AnalysisOptions analysisOpt) throws NullPointerException {
                if (analysisOpt == null) {
                        throw new NullPointerException("Cannot set null analysisOpt");
                }

                this.analysisOpt = analysisOpt;
        }

        public final boolean isInit() {
                return tracksMap != null && imagePlus != null && analysisOpt != null;
        }
}
