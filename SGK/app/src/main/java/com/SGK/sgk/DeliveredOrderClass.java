package com.SGK.sgk;

public class DeliveredOrderClass {
    String pname,pimage;
    int pid,quant,price,total,dispatch,delivered;

    public DeliveredOrderClass(String pname, String pimage, int pid, int quant, int price, int total, int dispatch, int delivered) {
        this.pname = pname;
        this.pimage = pimage;
        this.pid = pid;
        this.quant = quant;
        this.price = price;
        this.total = total;
        this.dispatch = dispatch;
        this.delivered = delivered;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDispatch() {
        return dispatch;
    }

    public void setDispatch(int dispatch) {
        this.dispatch = dispatch;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }
}
