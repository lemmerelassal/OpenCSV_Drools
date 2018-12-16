/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lemmer.csvexample.data.model;

import java.math.BigDecimal;

/**
 *
 * @author Lemmer El Assal
 */
public class InvoiceItem {
    public String item_name;
    public Integer item_quantity;
    public BigDecimal tariff;
    public BigDecimal total;
    
    public void setTariff(BigDecimal value) {
        this.tariff = value;
        this.total = BigDecimal.valueOf(this.item_quantity).multiply(this.tariff);
    }
    
    public BigDecimal getTotal() {
        return this.total;
    }
    
    public String[] toCsvStrings(){
        String[] res = new String[4];
        res[0] = this.item_name;
        res[1] = new String(" ") + this.item_quantity.toString();
        res[2] = new String(" ") + this.tariff.toString();
        res[3] = new String(" ") + this.total.toString();
        return res;
    }
    
    public InvoiceItem(String p_item_name, Integer p_item_quantity){
        this.item_name = p_item_name;
        this.item_quantity = p_item_quantity;
        
        
//        if(this.item_name.toLowerCase().equals("blue pen"))
//            if(this.item_quantity > 10)
//                this.tariff = BigDecimal.valueOf(2.5);
//            else
//                this.tariff = BigDecimal.valueOf(3);
//        else if(this.item_name.toLowerCase().equals("red pen"))
//            if(this.item_quantity > 10)
//                this.tariff = BigDecimal.valueOf(3);
//            else
//                this.tariff = BigDecimal.valueOf(3.5);
    }
    
    
}
