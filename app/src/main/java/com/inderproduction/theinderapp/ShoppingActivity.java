package com.inderproduction.theinderapp;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.inderproduction.theinderapp.Modals.Filter;
import com.inderproduction.theinderapp.Utilities.CustomUtils;
import com.inderproduction.theinderapp.Utilities.DisplayUtils;
import com.inderproduction.theinderapp.Utilities.Validations;
import com.inderproduction.theinderapp.ui.main.OnRecyclerScrolled;
import com.inderproduction.theinderapp.ui.main.SectionsPagerAdapter;

public class ShoppingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, OnRecyclerScrolled {
    TextView cartCountTextview;
    private AppBarLayout tabContainer;
    private LinearLayout viewContainer;

    Filter filter;

    private int currentTab = 0;

    private ObjectAnimator downAnimation,upAnimation;
    private ObjectAnimator shrinkRecycler,extendRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Toolbar toolbar = findViewById(R.id.shopping_toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Fashion Hub");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);
        cartCountTextview = findViewById(R.id.cart_count_textview);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTab = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabContainer = findViewById(R.id.tab_container);
        viewContainer = findViewById(R.id.viewContainer);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);


        tabContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float posY = tabContainer.getY();
                int height = tabContainer.getHeight();

                float viewPagerHeight = viewPager.getHeight();



                downAnimation = ObjectAnimator.ofFloat(viewContainer,"translationY",posY,-height);
                downAnimation.setDuration(1000);
                upAnimation = ObjectAnimator.ofFloat(viewContainer,"translationY",-height,posY);
                upAnimation.setDuration(1000);

                tabContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OneApplication.cart.size() > 0) {
                    Intent cartIntent = new Intent(ShoppingActivity.this, CartActivity.class);
                    startActivity(cartIntent);
                } else {
                    DisplayUtils.showToast(ShoppingActivity.this, "No Items in Cart", Toast.LENGTH_SHORT);
                }
            }
        });

        FloatingActionButton fabFilter = findViewById(R.id.fab_filter);
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFilterDialog();
            }
        });
    }

    private void cartCountDisplay(){
        if(OneApplication.cart.size()>0) {
            cartCountTextview.setVisibility(View.VISIBLE);
            cartCountTextview.setText(OneApplication.cart.size() + "");
        } else {
            cartCountTextview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCountDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile_menu && CustomUtils.isInternetConnected(this)) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                Intent toProfile = new Intent(ShoppingActivity.this, UserProfileActivity.class);
                startActivity(toProfile);
            } else {
                Intent toLogin = new Intent(ShoppingActivity.this, LoginActivity.class);
                toLogin.putExtra("sender", "shopping");
                startActivity(toLogin);
            }
        }

        if(item.getItemId()==R.id.item_search &&CustomUtils.isInternetConnected(this)){
            Intent searchIntent = new Intent(ShoppingActivity.this, SearchActivity.class);
            startActivity(searchIntent);

        }

            return super.onOptionsItemSelected(item);
    }


    private void createFilterDialog(){


        View v = LayoutInflater.from(this).inflate(R.layout.filter_dialog_view,null);
        filter = OneApplication.APPLICATION_FILTER;


        final EditText minPriceET = v.findViewById(R.id.filter_minprice);
        final EditText maxPriceET = v.findViewById(R.id.filter_maxprice);
        final CheckBox maleCB = v.findViewById(R.id.filter_male);
        final CheckBox femaleCB = v.findViewById(R.id.filter_female);

        minPriceET.setText(filter.priceFilter.get("min")+"");
        maxPriceET.setText(filter.priceFilter.get("max")+"");

        maleCB.setChecked(filter.genderFilter.get("male"));
        femaleCB.setChecked(filter.genderFilter.get("female"));

        minPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                setMinFilterField(maxPriceET,minPriceET);

            }
        });

        maxPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    setMaxFilterField(maxPriceET,minPriceET);
            }
        });

        maleCB.setOnCheckedChangeListener(this);
        femaleCB.setOnCheckedChangeListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(v)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isFieldValid(minPriceET) && isFieldValid(maxPriceET)){
                            filter.setMinPrice(Integer.parseInt(minPriceET.getText().toString()));
                            filter.setMaxPrice(Integer.parseInt(maxPriceET.getText().toString()));
                        }
                        if(!maleCB.isChecked() && !femaleCB.isChecked()){
                            filter.setFemaleFilter(true);
                            filter.setMaleFilter(true);
                        }
                        OneApplication.APPLICATION_FILTER = filter;
                        OneApplication.filterAppliedListeners.get(currentTab).onFilterApplied();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setMaxFilterField(EditText maxPriceET,EditText minPriceET){
        if(!maxPriceET.getText().toString().equals("")){
            int maxPrice = Integer.parseInt(maxPriceET.getText().toString());
            int minPrice = Integer.parseInt(minPriceET.getText().toString());
            if(maxPrice<minPrice){
                maxPriceET.setText(minPrice+"");
            }
        }
    }

    private void setMinFilterField(EditText maxPriceET,EditText minPriceET){
        if(!minPriceET.getText().toString().equals("")){
            int maxPrice = Integer.parseInt(maxPriceET.getText().toString());
            int minPrice = Integer.parseInt(minPriceET.getText().toString());
            if(minPrice>maxPrice){
                minPriceET.setText(maxPrice+"");
            }
        } else {
            minPriceET.setText("0");
        }
    }

    private boolean isFieldValid(EditText editText){
        String input = editText.getText().toString().trim();
        if(input.equals("")){
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.filter_male){

            filter.setMaleFilter(b);
        } else if(compoundButton.getId() == R.id.filter_female) {

            filter.setFemaleFilter(b);
        }
    }

    @Override
    public void onScrollUp() {
        if(!upAnimation.isRunning()){
            upAnimation.start();
        }
    }

    @Override
    public void onScrollDown() {
        if(!downAnimation.isRunning()){
            downAnimation.start();
        }

    }
}
