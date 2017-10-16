package com.ddz.mearchant.crash;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CrashLogUtil {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
            Locale.getDefault());

    /**
     * 将日志写入文件。
     * 
     * @param tag
     * @param message
     * @param tr
     */
    public static synchronized void writeLog(File logFile, String tag, String message, Throwable tr) {
        if (!logFile.exists()) {
        	logFile.mkdirs();
        }
        String time = timeFormat.format(Calendar.getInstance().getTime());
        synchronized (logFile) {
            FileWriter fileWriter = null;
            BufferedWriter bufdWriter = null;
            PrintWriter printWriter = null;
            try {
            	String fileName = "crash-" + time + ".txt"; 
            	File file=new File(logFile.getAbsolutePath()+ File.separator+fileName);
            	if(!file.exists()){
            		file.createNewFile();
            	}
                fileWriter = new FileWriter(file, true);
                bufdWriter = new BufferedWriter(fileWriter);
                printWriter = new PrintWriter(fileWriter);
                bufdWriter.append(time).append(" ").append("E").append('/').append(tag).append(" ")
                        .append(message).append('\n');
                bufdWriter.flush();
                tr.printStackTrace(printWriter);
                printWriter.flush();
                fileWriter.flush();
            } catch (IOException e) {
                closeQuietly(fileWriter);
                closeQuietly(bufdWriter);
                closeQuietly(printWriter);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }
}

