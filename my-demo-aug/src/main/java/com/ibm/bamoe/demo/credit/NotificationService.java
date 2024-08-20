package com.ibm.bamoe.demo.credit;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationService {

    public void notify(String customerName){
        System.out.println("Dear " + customerName + ", your application has been dedined");
    }

    public void notify(String customerName, String accountId, int amount){
        System.out.println("Dear " + customerName + ", your account is '" + accountId + "' funded with '" + amount + "'");
    }

}
