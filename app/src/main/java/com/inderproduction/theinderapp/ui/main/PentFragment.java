package com.inderproduction.theinderapp.ui.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.inderproduction.theinderapp.Modals.Pent;
import com.inderproduction.theinderapp.OneApplication;
import com.inderproduction.theinderapp.ProductDetail;
import com.inderproduction.theinderapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PentFragment extends Fragment implements PentListAdapter.OnPentItemClickListener,OnFilterAppliedListener {

    private ProgressBar progressBar;
    private RecyclerView productList;

    private List<Object> completeData;

    private int screenWidth;
    private Context activityContext;
    private OnRecyclerScrolled mRecyclerScrolled;
    private int mainListHeight;

    public static PentFragment newInstance(String itemType) {
        PentFragment fragment = new PentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("itemType", itemType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activityContext = context;
        this.mRecyclerScrolled = (OnRecyclerScrolled)activityContext;
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
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String type = getArguments().getString("itemType");

        OneApplication.filterAppliedListeners.put(0,this);

        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference colRef = firestore.collection("database");
        colRef.whereEqualTo("itemCategory", "pent").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    completeData = new ArrayList<>();
                    for (DocumentSnapshot snap : task.getResult().getDocuments()) {
                        Pent p = snap.toObject(Pent.class);
                        completeData.add(p);
                    }
                    updateListData(completeData);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



        productList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainListHeight = productList.getHeight();
                productList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



        productList.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
//                Log.e("height is ",""+mainListHeight);
                if(velocityY < 0 ){
                    productList.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,mainListHeight));
                    mRecyclerScrolled.onScrollUp();
                } else {
                    productList.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,mainListHeight + 200));

                    mRecyclerScrolled.onScrollDown();

                }
                return false;
            }
        });
    }

    private void updateListData(List<Object> newData) {
        if (newData.size() > 0) {
            if (productList.getAdapter() != null) {
                ((PentListAdapter) productList.getAdapter()).updateData(newData);
            } else {
                productList.setLayoutManager(new GridLayoutManager(activityContext, 2));
                productList.setAdapter(new PentListAdapter(activityContext, newData, this, screenWidth));
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemAddToCart(int position, String category) {
//        OneApplication.addItemToCart(completeData.get(position), category);
        Log.e("pent", position + "");
        Log.e("pentt", category + "");

        //JUST FOR UPDATING SINGLE ITEM # NOT USEFUL IN CURRENT SITUATION
//        if(productList.getAdapter() != null){
//            ((ClothListAdapter)productList.getAdapter()).updateSingleItem(completeData.get(position),position);
//        }

    }

    @Override
    public void onItemClick(int position) {
        Pent data = (Pent) completeData.get(position);

        Intent toDetails = new Intent(activityContext, ProductDetail.class);
        toDetails.putExtra("itemID", data.getItemID());
        toDetails.putExtra("itemCategory", data.getItemCategory());
        startActivity(toDetails);
    }

    @Override
    public void onFilterApplied() {

        List<Object> newData = new ArrayList<>();
        for(Object o:completeData){
            Pent p = (Pent)o;

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

    private boolean isPriceCompatible(Pent p) {
        Map<String,Integer> priceLimits = OneApplication.APPLICATION_FILTER.priceFilter;
        if(p.getItemPrice()>priceLimits.get("min") && p.getItemPrice()<priceLimits.get("max")){
            return true;
        }
        return false;
    }



//    private class LoadDataAsync extends AsyncTask<String, Integer, List<Object>> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setMax(100);
//        }
//
//        @Override
//        protected List<Object> doInBackground(String... strings) {
//
//            String data = "";
//            Scanner scan;
//            List<Object> pentList = new ArrayList<>();
//            if(CustomUtils.isInternetAvailable()){
//                try {
//
//                    InputStream stream =  activityContext.getResources().getAssets().open(strings[0].concat(".json"));
//                    scan = new Scanner(stream);
//                    while (scan.hasNextLine()) {
//                        String nl = scan.nextLine();
//                        data = data + nl;
//                    }
//                    scan.close();
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    JSONArray pentListArray = new JSONArray(data);
//                    for (int i = 0; i < pentListArray.length(); i++) {
//                        JSONObject jsonObject = (JSONObject) pentListArray.get(i);
//                        if (jsonObject.getString("itemCategory").equals("pent")) {
//                            Pent j = new Pent();
//                            j.setItemID(jsonObject.getString("itemID"));
//                            j.setItemCategory(jsonObject.getString("itemCategory"));
//                            j.setItemName(jsonObject.getString("itemName"));
//                            j.setItemBrand(jsonObject.getString("itemBrand"));
//                            j.setItemPrice(jsonObject.getInt("itemPrice"));
//                            j.setDiscount(jsonObject.getInt("discount"));
//                            j.setSizeChartType(jsonObject.getInt("sizeChartType"));
//                            j.setSizesAvailable(CustomUtils.createStringArrayFromJSON(jsonObject.getJSONArray("sizesAvailable")));
//                            j.setAvailableColors(CustomUtils.createStringArrayFromJSON(jsonObject.getJSONArray("availableColors")));
//                            j.setAvailableImages(CustomUtils.createIntArrayFromJSON(jsonObject.getJSONArray("availableImages")));
//                            j.setProductDetail(jsonObject.getString("productDetail"));
//                            j.setProductMaterial(jsonObject.getString("productMaterial"));
//                            j.setProductImage(jsonObject.getString("productImage"));
//                            j.setGender(jsonObject.getString("gender"));
//
//                            pentList.add(j);
//                        }
//                    }
//                } catch (JSONException e) {
//
//                    Log.e("INDER EXP",e.getMessage());
//                }
//            } else {
//                return null;
//            }
//
//            return pentList;
//        }
//
//        @Override
//        protected void onPostExecute(List<Object> pentList) {
//            super.onPostExecute(pentList);
//            if(pentList == null){
//
//                DisplayUtils.showToast(activityContext,"Error Loading Items",Toast.LENGTH_LONG);
//            } else {
//               updateListData(pentList);
//                completeData = pentList;
//            }
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//    }
}