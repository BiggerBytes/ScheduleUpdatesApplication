package com.olympicat.scheduleupdates;

import java.util.ArrayList;

/**
 * Created by OrenUrbach on 10/9/15.
 * static class that can get a class id and return a readable name for that class
 * for example: get(24) --> "יב3"
 * <p/>
 * classes:
 * y1(3), y2(5), y3(6), y4(7), y5(8), y6(9), y7(10), y8(11), y9(38), y10(39),
 * ya1(13), ya2(14), ya3(15), ya4(16), ya5(17), ya6(18), ya7(19), ya8(20),
 * yb1(22), yb2(23), yb3(24), yb4(25), yb5(26), yb6(27), yb7(28), yb8(29), yb9(30), yb10(41), yb11(36);
 */
public class SchoolClasses {

    public static ArrayList<Integer> classes = new ArrayList<Integer>() {{
        add(3);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
        add(10);
        add(11);
        add(38);
        add(39);
        add(13);
        add(14);
        add(15);
        add(16);
        add(17);
        add(18);
        add(19);
        add(20);
        add(22);
        add(23);
        add(24);
        add(25);
        add(26);
        add(27);
        add(28);
        add(29);
        add(30);
        add(41);
        add(36);
    }};

    public static int getScoolClassId(int index) {
        return classes.get(index);
    }

    public static int getIndexByClassId(int id) {
        return classes.indexOf(id);
    }
}

