package org.jacoco.example.report;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class DeleteFilesSimple {

//    public static void main(String[] args) {
//        String filePath = "D:\\data\\applogs\\xxl-job";
//        int l = moveFileToReady(filePath);
//        if(l>0){
//            System.out.println(dmy_hms.format(
//                    new Date())+","+filePath+"----------"+l);
//        }else{
//            System.out.println(dmy_hms.format(
//                    new Date())+","+filePath);
//        }
//    }
    public Integer moveFileToReady(String fromDir){
        File srcDir = new File(fromDir);
        if (!srcDir.exists()) {
            return 0;
        }
        File[] files = srcDir.listFiles();
        if (files == null || files.length <= 0) {
            return 0;
        }
        int l = 0;
        Date today = new Date();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                try {
                    File ff = files[i];
                    long time=ff.lastModified();
                    Calendar cal=Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    Date lastModified = cal.getTime();
                    //(int)(today.getTime() - lastModified.getTime())/86400000;
                    long days = getDistDates(today, lastModified);
                    if(days>=60){
                        files[i].delete();
                        l++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return l;
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static long getDistDates(Date startDate,Date endDate)
    {
        long totalDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart))/(1000*60*60*24);
        return totalDate;
    }
}