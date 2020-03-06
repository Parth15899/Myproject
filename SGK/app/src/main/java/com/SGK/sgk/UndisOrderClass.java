package com.SGK.sgk;

public class UndisOrderClass {
    String pname,pimage;
    int orderid,userid,pid,quant,price,total;

    public UndisOrderClass(String pname, String pimage, int orderid, int userid, int pid, int quant, int price, int total) {
        this.pname = pname;
        this.pimage = pimage;
        this.orderid = orderid;
        this.userid = userid;
        this.pid = pid;
        this.quant = quant;
        this.price = price;
        this.total = total;

    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

   }
