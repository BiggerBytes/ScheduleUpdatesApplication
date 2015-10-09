package com.olympicat.scheduleupdates;

import java.util.HashMap;

/**
 * Created by OrenUrbach on 10/9/15.
 * static class that can get a class id and return a readable name for that class
 * for example: get(24) --> "יב3"
 *
 * classes:
 * y1(3), y2(5), y3(6), y4(7), y5(8), y6(9), y7(10), y8(11), y9(38), y10(39),
 ya1(13), ya2(14), ya3(15), ya4(16), ya5(17), ya6(18), ya7(19), ya8(20),
 yb1(22), yb2(23), yb3(24), yb4(25), yb5(26), yb6(27), yb7(28), yb8(29), yb9(30), yb10(41), yb11(36);
 */
public class SchoolClasses {

    // init classes
    public static HashMap<Integer, String> classes = new HashMap<Integer, String>() {{
        put(3, "y1");
        put(5, "y2");
        put(6, "y3");
        put(7, "y4");
        put(8, "y5");
        put(9, "y6");
        put(10, "y7");
        put(11, "y8");
        put(38, "y9");
        put(39, "y10");
        put(13, "ya1");
        put(14, "ya2");
        put(15, "ya3");
        put(16, "ya4");
        put(17, "ya5");
        put(18, "ya6");
        put(19, "ya7");
        put(20, "ya8");
        put(22, "yb1");
        put(23, "yb2");
        put(24, "yb3");
        put(25, "yb4");
        put(26, "yb5");
        put(27, "yb6");
        put(28, "yb7");
        put(29, "yb8");
        put(30, "yb9");
        put(41, "yb10");
        put(36, "yb11");
    }};

    public static String getScoolClassName(int id) {
        return classes.get(id);
    }
}

