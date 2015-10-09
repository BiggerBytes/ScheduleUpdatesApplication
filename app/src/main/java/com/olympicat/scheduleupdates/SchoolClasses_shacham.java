package com.olympicat.scheduleupdates;

/**
 * Created by shachamginat on 9/28/15.
 */
public enum SchoolClasses_shacham {
    y1(3), y2(5), y3(6), y4(7), y5(8), y6(9), y7(10), y8(11), y9(38), y10(39),
    ya1(13), ya2(14), ya3(15), ya4(16), ya5(17), ya6(18), ya7(19), ya8(20),
    yb1(22), yb2(23), yb3(24), yb4(25), yb5(26), yb6(27), yb7(28), yb8(29), yb9(30), yb10(41), yb11(36);

    Integer id;

    SchoolClasses_shacham(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
