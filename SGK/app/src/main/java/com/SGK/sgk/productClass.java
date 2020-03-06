package com.SGK.sgk;

public class productClass {

    int pid;
    String pname;
    String pprice;
    String pimage;
    int quant;

    public productClass(int pid,String pname, String pprice, String pimage,int quant) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.pimage = pimage;
        this.quant = quant;
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

    public String getPprice() {
        return pprice;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
}

