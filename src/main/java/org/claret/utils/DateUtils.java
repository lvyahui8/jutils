package org.claret.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2017/5/3 13:31
 */
public class DateUtils extends CommonUtils{
    public static List<String> getDayList(String startDay,String endDay) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return getDayList(simpleDateFormat.parse(startDay),simpleDateFormat.parse(endDay));
    }

    private static List<String> getDayList(Date startDate, Date endDate) {
        List<String> dayList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int oneDay = 24 * 60 * 60 * 1000;
        while(startDate.getTime() <= endDate.getTime()){
            dayList.add(simpleDateFormat.format(startDate));
            startDate.setTime(startDate.getTime() + oneDay);
        }
        return dayList;
    }

}
