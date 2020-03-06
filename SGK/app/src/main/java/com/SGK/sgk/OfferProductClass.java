package com.SGK.sgk;

public class OfferProductClass {
    String pname,pimage;
    int pid,pprice,offerprice;

    public OfferProductClass(String pname, String pimage, int pprice, int offerprice,int pid) {
        this.pname = pname;
        this.pimage = pimage;
        this.pprice = pprice;
        this.offerprice = offerprice;
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }

    public int getOfferprice() {
        return offerprice;
    }

    public void setOfferprice(int offerprice) {
        this.offerprice = offerprice;
    }
}
