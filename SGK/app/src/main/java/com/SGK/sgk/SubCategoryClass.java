package com.SGK.sgk;

public class SubCategoryClass {

    int subcatid,catid;
    String subcatname,subcatpic;

    public SubCategoryClass(int subcatid, int catid, String subcatname, String subcatpic) {
        this.subcatid = subcatid;
        this.catid = catid;
        this.subcatname = subcatname;
        this.subcatpic = subcatpic;
    }

    public int getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(int subcatid) {
        this.subcatid = subcatid;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getSubcatname() {
        return subcatname;
    }

    public void setSubcatname(String subcatname) {
        this.subcatname = subcatname;
    }

    public String getSubcatpic() {
        return subcatpic;
    }

    public void setSubcatpic(String subcatpic) {
        this.subcatpic = subcatpic;
    }
}
