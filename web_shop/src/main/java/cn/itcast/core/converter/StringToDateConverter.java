package cn.itcast.core.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringToDateConverter implements Converter<String,Date> {
    @Override
    public Date convert(String source) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date parse =null;
        try {
            if (source!=null&&!"undefined".equals(source)){
                parse = simpleDateFormat.parse(source);
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(parse);
                calendar.add(Calendar.DATE,+1);
                parse= calendar.getTime();
            }else{
                parse=new Date();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }
}
