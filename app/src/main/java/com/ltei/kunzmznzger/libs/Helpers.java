package com.ltei.kunzmznzger.libs;

import com.ltei.kunzmznzger.libs.models.Model;

import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.Normalizer;

/**
 * @author Robin
 * @date 14/06/2017
 */
public class Helpers
{
    private static DateTimeFormatter dateTimeFormatter;
    private static DateTimeFormatter timeFormatter;
    private static DateTimeFormatter dateFormatter;

    private static String modelsPackage = "com.ltei.kunzmzmzger";

    public static String camelCase(String s) {
        String res = "";

        String[] split = s.split("_");

        res += split[0];
        for (int i = 1; i < split.length; i++) {
            res += split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
        }

        return res;
    }

    public static String snakeCase(@NotNull String s) {
        if (s.isEmpty()) {
            return "";
        }

        String res = s.substring(0, 1).toLowerCase();

        for (int i = 1; i < s.length(); i++) {
            String substr = s.substring(i, i + 1);
            String substrLower = substr.toLowerCase();

            if (!substr.equals(substrLower)) {
                res += "_";
            }

            res += substrLower;
        }

        return res;
    }

    public static String plural(String str) {
        if (str.endsWith("y")) {
            str = str.substring(0, str.length() - 1) + "ies";
        }
        else {
            str += "s";
        }

        return str;
    }

    public static String getManagerOfModel(String modelName) // path/of/the/class
    {
        String modelsPackage = modelName.substring(0, modelName.lastIndexOf('.'));
        String modelClassName = modelName.substring(modelName.lastIndexOf('.') + 1);

        String managerClassname = modelClassName + "DAO";

        String managerPath = modelsPackage + ".dao." + managerClassname;

        return managerPath;
    }

    public static String getAddingMethodName(String str) {
        str = "add_" + str;
        str = camelCase(str);

        if (str.endsWith("ies")) {
            String beg = str.split("ies")[0];
            return beg + "y";
        }

        return str.substring(0, str.length() - 1);
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public static DateTimeFormatter getDateFormatter() {
        if (Helpers.dateFormatter == null) {
            Helpers.dateFormatter = new DateTimeFormatterBuilder()
                    .appendYear(4, 4)
                    .appendLiteral('-')
                    .appendMonthOfYear(2)
                    .appendLiteral('-')
                    .appendDayOfMonth(2)
                    .toFormatter();
        }

        return Helpers.dateFormatter;
    }

    public static DateTimeFormatter getTimeFormatter() {
        if (Helpers.timeFormatter == null) {
            Helpers.timeFormatter = new DateTimeFormatterBuilder()
                    .appendHourOfDay(2)
                    .appendLiteral(':')
                    .appendMinuteOfHour(2)
                    .appendLiteral(':')
                    .appendSecondOfMinute(2)
                    .toFormatter();
        }

        return Helpers.timeFormatter;
    }

    public static DateTimeFormatter getDatetimeFormatter() {
        if (Helpers.dateTimeFormatter == null) {
            Helpers.dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendYear(4, 4)
                    .appendLiteral('-')
                    .appendMonthOfYear(2)
                    .appendLiteral('-')
                    .appendDayOfMonth(2)
                    .appendLiteral(' ')
                    .appendHourOfDay(2)
                    .appendLiteral(":")
                    .appendMinuteOfHour(2)
                    .appendLiteral(":")
                    .appendSecondOfMinute(2)
                    .toFormatter();
        }

        return Helpers.dateTimeFormatter;
    }

    public static String arrayJoin(Object[] array, String joinWith) {
        String res = "";
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                res += joinWith;
            }
            res += array[i].toString();
        }

        return res;
    }

    public static boolean isModel(Class<?> clazz) {
        return Model.class.isAssignableFrom(clazz);
    }
}
