//package com.driver;
//
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.google.common.collect.Maps.newHashMap;
//@Repository
//public class OrderRepository {
//    Map<String,Order>ordersDb= new HashMap<>();
//    Map<String,DeliveryPartner> deliveryPartnersDb=new HashMap<>();
//    Map<String,String> orderPartnerDb=new HashMap<>();
//    Map<String, List<String>> partnerOrdersDb=new HashMap<>();
//
//    public void addOrder(Order order) {
//        ordersDb.put(order.getId(),order);
//    }
//
//    public void addPartner(String partnerId) {
//        deliveryPartnersDb.put(partnerId,new DeliveryPartner(partnerId));
//    }
//
//    public void addOrderPartnerPair(String orderId, String partnerId) {
//        if(ordersDb.containsKey(orderId) && deliveryPartnersDb.containsKey(partnerId)){
//            orderPartnerDb.put(orderId,partnerId);
//        }
//        List<String>currentOrders=new ArrayList<>();
//        if(partnerOrdersDb.containsKey(partnerId)){
//            currentOrders=partnerOrdersDb.get(partnerId);
//        }
//        currentOrders.add(orderId);
//        partnerOrdersDb.put(partnerId,currentOrders);
//        DeliveryPartner deliveryPartner=deliveryPartnersDb.get(partnerId);
//        deliveryPartner.setNumberOfOrders(currentOrders.size());
//    }
//
//    public Order getOrderById(String orderId) {
//        return ordersDb.get(orderId);
//    }
//
//    public DeliveryPartner getPartnerById(String partnerId) {
//        return deliveryPartnersDb.get(partnerId);
//    }
//
//    public Integer getOrderCountByPartnerId(String partnerId) {
//        return partnerOrdersDb.get(partnerId).size();
//    }
//
//    public List<String> getOrdersByPartnerId(String partnerId) {
//        return partnerOrdersDb.get(partnerId);
//    }
//
//    public List<String> getAllOrders() {
//        List<String>orders=new ArrayList<>();
//        for(String order:ordersDb.keySet()){
//            orders.add(order);
//        }
//        return orders;
//    }
//
//    public Integer getCountOfUnassignedOrders() {
//        return ordersDb.size()-orderPartnerDb.size();
//    }
//
//    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int newTime, String partnerId) {
//        int count=0;
//        List<String> orders=partnerOrdersDb.get(partnerId);
//        for(String orderId:orders){
//            int deliveryTime=ordersDb.get(orderId).getDeliveryTime();
//            if(deliveryTime>newTime){
//                count++;
//            }
//        }
//        return count;
//
//    }
//
//    public int getLastDeliveryTimeByPartnerId(String partnerId) {
//        int MaxTime=0;
//        List<String>orders=partnerOrdersDb.get(partnerId);
//        for(String orderId:orders){
//            int currentTime=ordersDb.get(orderId).getDeliveryTime();
//            MaxTime=Math.max(MaxTime,currentTime);
//        }
//        return MaxTime;
//    }
//
//    public void deletePartnerById(String partnerId) {
//        deliveryPartnersDb.remove(partnerId);
//        List<String>listOfOrders=partnerOrdersDb.get(partnerId);
//        partnerOrdersDb.remove(partnerId);
//        for(String order:listOfOrders){
//            orderPartnerDb.remove(order);
//        }
//    }
//
//    public void deleteOrderById(String orderId) {
//        ordersDb.remove(orderId);
//        String partnerId=orderPartnerDb.get(orderId);
//        orderPartnerDb.remove(orderId);
//        partnerOrdersDb.get(partnerId).remove(orderId);
//        deliveryPartnersDb.get(partnerId).setNumberOfOrders(partnerOrdersDb.get(partnerId).size());
//    }
//}
package com.driver;

import java.util.*;
import java.util.HashMap;

public class OrderRepository {
    HashMap<String,Order> orderdb = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerDB = new HashMap<>();
    HashMap<String,List<String>> pairDB = new HashMap<>();
    HashMap<String,String> assignDB=new HashMap<>();

    public String addOrder(Order order){
        orderdb.put(order.getId(),order);
        return "Added";
    }
    public String addPartner(String partnerId){
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerDB.put(partner.getId(),partner);
        return "Added";
    }
    public String addOrderPartner(String orderId,String partnerId){
        List<String> list = pairDB.getOrDefault(partnerId,new ArrayList<>());
        list.add(orderId);
        pairDB.put(partnerId,list);
        assignDB.put(orderId,partnerId);
        DeliveryPartner partner = partnerDB.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Added";
    }
    public Order getOrderById(String id){
        for(String s : orderdb.keySet()){
            if(s.equals(id))return orderdb.get(s);
        }
        return null;
    }
    public DeliveryPartner getPartnerById(String partnerId){
        if(partnerDB.containsKey(partnerId))return partnerDB.get(partnerId);
        return null;
    }
    public int getOrderCountByPartnerId(String partnerId){
        int order = pairDB.getOrDefault(partnerId,new ArrayList<>()).size();
        return order;
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        return pairDB.getOrDefault(partnerId,new ArrayList<>());
    }
    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        for(String s : orderdb.keySet()){
            orders.add(s);
        }
        return orders;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        int orderCount=0;
        List<String> list = pairDB.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for(String s:list){
            Order order = orderdb.get(s);
            if(order.getDeliveryTime()>deliveryTime)orderCount++;
        }
        return orderCount;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time="";
        List<String> list =pairDB.get(partnerId);
        int deliveryTime=0;
        for(String s:list){
            Order order =orderdb.get(s);
            deliveryTime=Math.max(deliveryTime,order.getDeliveryTime());
        }
        int hour=deliveryTime/60;
        String sHour="";
        if(hour<10)sHour="0"+String.valueOf(hour);
        else sHour=String.valueOf(hour);

        int min=deliveryTime%60;
        String sMin = "";
        if(min<10)sMin = "0" + String.valueOf(min);
        else sMin = String.valueOf(min);

        time=sHour+":"+sMin;
        return time;
    }

    public String deletePartnerById(String partnerId){
        partnerDB.remove(partnerId);
        List<String>list = pairDB.getOrDefault(partnerId,new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while(itr.hasNext()){
            assignDB.remove(itr.next());
        }
        pairDB.remove(partnerId);
        return "Deleted";
    }

    public String deleteOrderById(String orderId){
        orderdb.remove(orderId);
        String partnerId =assignDB.get(orderId);
        assignDB.remove(orderId);
        List<String> list =pairDB.get(partnerId);
        ListIterator<String> itr =list.listIterator();
        while(itr.hasNext()){
            String s= itr.next();
            if(s.equals(orderId))itr.remove();
        }
        pairDB.put(partnerId,list);
        return "Deleted";

    }

    public int getCountOfUnassignedOrders(){
        return orderdb.size()-assignDB.size();
    }

}