package com.inderproduction.theinderapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonParser;
import com.inderproduction.theinderapp.Modals.Filter;
import com.inderproduction.theinderapp.Modals.FinalOrder;
import com.inderproduction.theinderapp.Modals.OrderItem;
import com.inderproduction.theinderapp.Modals.Pent;
import com.inderproduction.theinderapp.Modals.Shirt;
import com.inderproduction.theinderapp.Modals.Shoes;
import com.inderproduction.theinderapp.Utilities.CustomBackgroundService;
import com.inderproduction.theinderapp.ui.main.OnFilterAppliedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OneApplication extends Application {

//    public static Map<String, Object> cart;
    public static Map<String,OrderItem> cart;
    public static FinalOrder finalOrder;

    public static JSONObject APPLICATION_COLORS;

    public static Filter APPLICATION_FILTER;

    public static Map<Integer,OnFilterAppliedListener> filterAppliedListeners;


    private Intent backgroundService;

    @Override
    public void onCreate() {
        super.onCreate();
        cart = new HashMap<>();
        finalOrder = new FinalOrder();
        filterAppliedListeners = new HashMap<>();

        APPLICATION_FILTER = new Filter();

        try {
            loadColor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        backgroundService = new Intent(getApplicationContext(), CustomBackgroundService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("messages","Inder Messages", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This handles notification for Message Service");
            NotificationChannel channel2 = new NotificationChannel("background","Service", NotificationManager.IMPORTANCE_NONE);
            channel2.setDescription("This handles background Service!");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channel2);
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                    startService(backgroundService);
                } else {
                    stopService(backgroundService);
                }
            }
        });
    }


    public static boolean addItemToCart(OrderItem item){
        if(!isInCart(item)){
            cart.put(item.getOrderItemID(),item);
            return true;
        }
        return false;
    }

    public static boolean removeItemFromCart(OrderItem item){
        if(isInCart(item)){
             cart.remove(item.getOrderItemID());
        }
        return false;
    }

    public static void incrementItemCount(OrderItem item){
        if(isInCart(item)){
            OrderItem oi = cart.get(item.getOrderItemID());
            oi.setItemCount(oi.getItemCount()+1);
            cart.put(item.getOrderItemID(),oi);
        }
    }

    public static void decrementItemCount(OrderItem item){
        if(isInCart(item)){
            OrderItem oi = cart.get(item.getOrderItemID());
            if(oi.getItemCount()>1){
                oi.setItemCount(oi.getItemCount()-1);
                cart.put(item.getOrderItemID(),oi);
            } else {
                cart.remove(item.getOrderItemID());
            }
        }
    }

    private static boolean isInCart(OrderItem item){
       for(OrderItem oi:cart.values()){
           if(oi.getItemColor().equalsIgnoreCase(item.getItemColor())
                   && oi.getItemSize().equalsIgnoreCase(item.getItemSize())
                   && oi.getItemID().equalsIgnoreCase(item.getItemID())){
               return true;
           }
       }
       return false;
    }

    public static double calculatePrice(){
        double totalPrice = 0 ;
        List<OrderItem> itemsList = new ArrayList<>();
        for(OrderItem o: cart.values()){
            double newPrice = o.getItemPrice() - ((double)o.getItemDiscount()/100 * o.getItemPrice());
            totalPrice = totalPrice + (newPrice*o.getItemCount());
            o.setItemFinalPrice(totalPrice);
            itemsList.add(o);
        }

        finalOrder.setCartItems(itemsList);

        return totalPrice;
    }

//    public static void addItemToCart(Object newItem,String category){
//           if(category.equals("footwear")){
//            Shoes s = (Shoes) newItem;
//            String id = s.getItemID();
//            if((s= (Shoes) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//                s.setCartCount(++prevCount);
//            } else {
//                s = (Shoes)newItem;
//            }
//            cart.put(s.getItemID(),s);
//        } else  if(category.equals("shirt")){
//            Shirt s = (Shirt) newItem;
//            String id = s.getItemID();
//            if((s = (Shirt) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//                s.setCartCount(++prevCount);
//            } else {
//                s = (Shirt)newItem;
//            }
//            cart.put(s.getItemID(),s);
//        }  else if(category.equals("pent")){
//            Pent s = (Pent) newItem;
//            String id = s.getItemID();
//            if((s= (Pent) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//                s.setCartCount(++prevCount);
//            } else {
//                s = (Pent)newItem;
//            }
//            cart.put(s.getItemID(),s);
//        }
//    }
//
//    public static void removeItemFromCart(Object newItem,String category){
//        if(category.equals("footwear")){
//            Shoes s = (Shoes) newItem;
//            String id = s.getItemID();
//            if((s= (Shoes) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//
//                if(prevCount >1){
//                    s.setCartCount(--prevCount);
//                } else {
//                    cart.remove(s.getItemID());
//                }
//            }
//        } else  if(category.equals("shirt")){
//            Shirt s = (Shirt) newItem;
//            String id = s.getItemID();
//            if((s= (Shirt) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//
//                if(prevCount >1){
//                    s.setCartCount(--prevCount);
//                } else {
//                    cart.remove(s.getItemID());
//                }
//            }
//        }  else if(category.equals("pent")){
//            Pent s = (Pent) newItem;
//            String id = s.getItemID();
//            if((s= (Pent) cart.get(id)) != null){
//                int prevCount = s.getCartCount();
//
//                if(prevCount >1){
//                    s.setCartCount(--prevCount);
//                } else {
//                    cart.remove(s.getItemID());
//                }
//            }
//        }
//    }
//
//    public static double calculatePrice(){
//        double totalPrice = 0 ;
//        List<OrderItem> itemsList = new ArrayList<>();
//        for(Object o: cart.values()){
//
//            OrderItem item = new OrderItem();
//
//            if(o instanceof Pent){
//                Pent j = (Pent)o;
//                totalPrice = totalPrice + (j.getItemPrice()*j.getCartCount());
//                item = new OrderItem(j.getItemID(),j.getCartCount());
//            }
//            else if(o instanceof Shirt){
//                Shirt s = (Shirt)o;
//                totalPrice = totalPrice + (s.getItemPrice()*s.getCartCount());
//                item = new OrderItem(s.getItemID(),s.getCartCount());
//            } else if(o instanceof Shoes){
//                Shoes s = (Shoes) o;
//                totalPrice = totalPrice + (s.getItemPrice()*s.getCartCount());
//                item = new OrderItem(s.getItemID(),s.getCartCount());
//            }
//            itemsList.add(item);
//        }
//
//        finalOrder.setCartItems(itemsList);
//
//        return totalPrice;
//    }


    private void loadColor() throws IOException, JSONException {
        InputStream stream = getResources().getAssets().open("colors.json");
        Scanner scan = new Scanner(stream);
        String jsonData = "";
        while(scan.hasNextLine()){
            String nl = scan.nextLine();
            jsonData = jsonData + nl;
        }
        APPLICATION_COLORS = new JSONObject(jsonData);
    }

}

