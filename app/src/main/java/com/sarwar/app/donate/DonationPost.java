package com.sarwar.app.donate;

public class DonationPost {
    public String name, url, details, phone,address,status,uid, key,cuurent_time;

    public String getName(){return name;}
    public String getUrl(){return url;}

    public String getDetails() { return details; }

    public String getPhone() { return phone; }

    public String getAddress() { return address; }

    public String getStatus() {
        return status;
    }

    public String getKey() { return key; }

    public String getCuurent_time(){return cuurent_time;
    }
    public DonationPost(String name,  String url, String details, String phone, String address, String status, String uid, String key, String cuurent_time) {
        this.name=name;
        this.details=details;
        this.phone = phone;
        this.url=url;
        this.address=address;
        this.status=status;
        this.uid=uid;
        this.key=key;
        this.cuurent_time=cuurent_time;
    }

    public DonationPost(){}
}


