package com.inderproduction.theinderapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.inderproduction.theinderapp.Adapters.PentListAdapter;
import com.inderproduction.theinderapp.Adapters.ShirtListAdapter;
import com.inderproduction.theinderapp.Modals.Pent;
import com.inderproduction.theinderapp.Modals.Shirt;
import com.inderproduction.theinderapp.OneApplication;
import com.inderproduction.theinderapp.ProductDetail;
import com.inderproduction.theinderapp.R;
import com.inderproduction.theinderapp.Utilities.CustomUtils;
import com.inderproduction.theinderapp.Utilities.DisplayUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ShirtFragment extends Fragment  implements ShirtListAdapter.OnShirtItemClickListener, OnFilterAppliedListener {

    private ProgressBar progressBar;
    private RecyclerView productList;

    private List<Object> completeData;

    private int screenWidth;
    private Context activityContext;

    public static ShirtFragment newInstance(String itemType) {
        ShirtFragment fragment = new ShirtFragment();
        Bundle bundle = new Bundle();
        bundle.putString("itemType", itemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activityContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = activityContext.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
                View root = inflater.inflate(R.layout.fragment_shopping, container, false);
                progressBar = root.findViewById(R.id.shopping_progress);
                productList = root.findViewById(R.id.product_list);
                return root; }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String type = getArguments().getString("itemType");
        OneApplication.filterAppliedListeners.put(1,this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference colRef = firestore.collection("database");
        colRef.whereEqualTo("itemCategory", "shirt").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    completeData = new ArrayList<>();
                    for (DocumentSnapshot snap : task.getResult().getDocuments()) {
                        Shirt p = snap.toObject(Shirt.class);
                        completeData.add(p);
                    }
                    Log.e("COMPLETE DATA SIZEZ", completeData.size() + "");
                    updateListData(completeData);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


        private void updateListData(List<Object> newData){
        if(newData.size()>0){
            if(productList.getAdapter() != null){
                ((ShirtListAdapter)productList.getAdapter()).updateData(newData);
            } else {
                productList.setLayoutManager(new GridLayoutManager(activityContext,2));
                productList.setAdapter(new ShirtListAdapter(activityContext,newData,this,screenWidth));
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemAddToCart(int position, String category) {
//          OneApplication.addItemToCart(completeData.get(position),category);
        Log.e("shirt",position+"");
        Log.e("shirtt",category+"");

        //JUST FOR UPDATING SINGLE ITEM # NOT USEFUL IN CURRENT SITUATION
//        if(productList.getAdapter() != null){
//            ((ClothListAdapter)productList.getAdapter()).updateSingleItem(completeData.get(position),position);
//        }

    }
    @Override
    public void onItemClick(int position) {
        Shirt data = (Shirt) completeData.get(position);

        Intent toDetails = new Intent(activityContext, ProductDetail.class);
        toDetails.putExtra("itemID", data.getItemID());
        toDetails.putExtra("itemCategory", data.getItemCategory());
        startActivity(toDetails);
    }

    @Override
    public void onFilterApplied() {

        List<Object> newData = new ArrayList<>();
        for(Object o:completeData){
            Shirt p = (Shirt)o;

            boolean genderMaleS = OneApplication.APPLICATION_FILTER.genderFilter.get("male");
            boolean genderFemaleS = OneApplication.APPLICATION_FILTER.genderFilter.get("female");

            if(isPriceCompatible(p)){
                if(genderMaleS && p.getGender().equalsIgnoreCase("male")){
                    newData.add(p);
                } else if(genderFemaleS && p.getGender().equalsIgnoreCase("female")) {
                    newData.add(p);
                }
            }
        }
        if(newData.size()>0){
            updateListData(newData);
        } else {
            updateListData(completeData);
        }

    }

    private boolean isPriceCompatible(Shirt p) {
        Map<String,Integer> priceLimits = OneApplication.APPLICATION_FILTER.priceFilter;
        if(p.getItemPrice()>priceLimits.get("min") && p.getItemPrice()<priceLimits.get("max")){
            return true;
        }
        return false;
    }
}