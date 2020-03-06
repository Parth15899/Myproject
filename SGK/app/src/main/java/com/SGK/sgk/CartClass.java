package com.SGK.sgk;

public class CartClass {

    int userid,pid,quant,price,totalprice;
    String pname,pimage;

    public CartClass(int userid, int pid, int quant, int price, int totalprice,String pname,String pimage) {
        this.userid = userid;
        this.pid = pid;
        this.quant = quant;
        this.price = price;
        this.totalprice = totalprice;
        this.pname = pname;
        this.pimage = pimage;

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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }
}
