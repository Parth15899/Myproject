package com.SGK.sgk;

public class CategoryClass {

    int catid;
    String catname,catpic;

    public CategoryClass(int catid, String catname, String catpic) {
        this.catid = catid;
        this.catname = catname;
        this.catpic = catpic;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatpic() {
        return catpic;
    }

    public void setCatpic(String catpic) {
        this.catpic = catpic;
    }
}
