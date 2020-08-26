package com.inderproduction.theinderapp.Modals;

import java.util.HashMap;
import java.util.Map;

public class Filter {

    public Map<String,Integer> priceFilter;
    public Map<String,Boolean> genderFilter;

    public Filter(){
        priceFilter = new HashMap<>();
        genderFilter = new HashMap<>();
        priceFilter.put("min",0);
        priceFilter.put("max",10000);
        genderFilter.put("male",true);
        genderFilter.put("female",true);
    }

    public Map<String, Integer> getPriceFilter() {
        return priceFilter;
    }

    public void setMaxPrice(int maxPrice){
        priceFilter.put("max",maxPrice);
    }

    public void setMaleFilter(boolean isApp){
        genderFilter.put("male",isApp);
    }

    public void setFemaleFilter(boolean isApp){
        genderFilter.put("female",isApp);
    }

    public void setMinPrice(int minPrice){
        priceFilter.put("min",minPrice);
    }

    public Map<String,Boolean> getGenderFilter() {
        return genderFilter;
    }
}
