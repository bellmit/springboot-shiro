package com.sq.transportmanage.gateway.api.common;

/**
 * @author admin
 */

public enum UseCarDriverEnum {
    SSX("舒适型", 34), HHX("豪华型", 41), SWFZC("商务福祉车", 40), ADZC("奥迪专车", 116), HQH7("红旗 H7", 118),
    FTKST("丰田考斯特", 222), BS21("巴士21座", 148), HHBS21("豪华巴士21(宇通)", 234), BS50("巴士50座", 77), SW6("商务6座", 35);
    private String name;
    private int id;

    UseCarDriverEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static String getName(int id) {
        for (UseCarDriverEnum c : UseCarDriverEnum.values()) {
            if (c.getId() == id) {
                return c.name;
            }
        }
        return null;
    }

    public static int getIdByName(String name) {
        for (UseCarDriverEnum c : UseCarDriverEnum.values()) {
            if (name.equals(c.getName())) {
                return c.id;
            }
        }
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
