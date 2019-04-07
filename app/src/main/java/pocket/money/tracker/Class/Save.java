package pocket.money.tracker.Class;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class Save {

    static String Name,Email;

    static String time="",date="";

    public static String getName() {
        return Name;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getDate() {

        Calendar calendar = Calendar.getInstance();

        int thisYear = calendar.get(Calendar.YEAR);

        String thisMonth = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)].substring(0,3);

        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        date=thisDay+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+thisYear;
        return date;
    }

    public static void setDate(String date) {
        Save.date = date;
    }

    public static String getTime() {
        Calendar rightNow = Calendar.getInstance();
        int Hour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)

        int minute=new Time(System.currentTimeMillis()).getMinutes();
        String formate="";
        if(Hour==12)
        {
            formate="PM";
        }
        else if(Hour>12)
        {
            formate="PM";
            Hour-=12;
        }
        else if(Hour==0)
        {
            formate="AM";
            Hour+=12;
        }
        else
        {
            formate="AM";
        }

        time=Hour+":"+minute+" "+formate;

        return time;
    }
}
