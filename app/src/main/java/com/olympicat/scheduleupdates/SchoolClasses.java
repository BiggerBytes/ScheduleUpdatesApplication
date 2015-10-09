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
    public static HashMap<Integer, Integer> classes = new HashMap<Integer, Integer>() {{
//        put(3, "י1");
//        put(5, "י2");
//        put(6, "י3");
//        put(7, "י4");
//        put(8, "י5");
//        put(9, "י6");
//        put(10, "י7");
//        put(11, "י8");
//        put(38, "י9");
//        put(39, "י10");
//        put(13, "יא1");
//        put(14, "יא2");
//        put(15, "יא3");
//        put(16, "יא4");
//        put(17, "יא5");
//        put(18, "יא6");
//        put(19, "יא7");
//        put(20, "יא8");
//        put(22, "יב1");
//        put(23, "יב2");
//        put(24, "יב3");
//        put(25, "יב4");
//        put(26, "יב5");
//        put(27, "יב6");
//        put(28, "יב7");
//        put(29, "יב8");
//        put(30, "יב9");
//        put(41, "יב10");
//        put(36, "יב11");

        put(0, 3);
        put(1, 5);
        put(2, 6);
        put(3, 7);
        put(4, 8);
        put(5, 9);
        put(6, 10);
        put(7, 11);
        put(8, 38);
        put(9, 39);
        put(10, 13);
        put(11, 14);
        put(12, 15);
        put(13, 16);
        put(14, 17);
        put(15, 18);
        put(16, 19);
        put(17, 20);
        put(18, 22);
        put(19, 23);
        put(20, 24);
        put(21, 25);
        put(22, 26);
        put(23, 27);
        put(24, 28);
        put(25, 29);
        put(26, 30);
        put(27, 41);
        put(28, 36);
    }};

    public static int getScoolClassId(int index) {
        return classes.get(index);
    }
}

