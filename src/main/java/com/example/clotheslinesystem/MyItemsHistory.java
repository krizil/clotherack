package com.example.clotheslinesystem;

public class MyItemsHistory {


    private String key;
    //String key, id, ItemId;
    private final String Rain, Remarks, Temp, Date, Humid;

    public MyItemsHistory(String Rain, String Remarks, String Temp, String Date, String Humid) {
        this.Rain = Rain;
        this.Remarks = Remarks;
        this.Temp = Temp;
        // this.Battery = Battery;
        this.Date = Date;
        this.Humid = Humid;

    }

    public String getRain() {
        return Rain;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getTemp() {
        return Temp;
    }

    //  public String getBattery() {
//        return Battery;
//    }

    public String getDate() {
        return Date;
    }

    public String getHumid() {
        return Humid;
    }

    public String getKey() { return key;}
}
