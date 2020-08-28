package com.inderproduction.theinderapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inderproduction.theinderapp.Modals.Pent;
import com.inderproduction.theinderapp.Modals.Shirt;
import com.inderproduction.theinderapp.Modals.Shoes;
import com.inderproduction.theinderapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShoesListAdapter extends RecyclerView.Adapter<ShoesListAdapter.ShoesViewHolder> {

    private List<Object> shoesProducts;
    private OnShoesItemClickListener mListener;
    private int screenWidth;
    private Context context;
    private boolean shoe;

    public ShoesListAdapter(Context context, List<Object> data, OnShoesItemClickListener listener, int screenWidth){
        this.shoesProducts = data;
        mListener = listener;
        this.screenWidth = screenWidth;
        this.context = context;
    }

    public void updateData(List<Object> newData){
        this.shoesProducts = new ArrayList<>();
        this.shoesProducts.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShoesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoes_list_item,parent,false);
        ShoesViewHolder holder = new ShoesViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoesViewHolder holder, final int position) {
        Object o = shoesProducts.get(position);

        if (o instanceof Shoes) {
            Shoes s = (Shoes) o;

            Picasso.get().load(Uri.parse(s.getProductImage())).fit().into(holder.shoeImage);

            holder.shoeName.setText(s.getItemName());
            holder.shoeRsTag.setText("CAN ");
            holder.shoePrice.setText(String.valueOf(s.getItemPrice()));

            int dis=s.getDiscount();
            if(dis>0) {
                holder.shoeDiscount.setText(String.valueOf(s.getDiscount()) + "% off");
                holder.shoeFinalPrice.setText(String.valueOf(s.getItemPrice() - ((double) s.getDiscount() / 100 * s.getItemPrice())));
                holder.shoePrice.setPaintFlags(holder.shoePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.shoeFinalPrice.setText(String.valueOf(s.getItemPrice()));
                holder.shoePrice.setText("");
                holder.shoeDiscount.setText("");
            }
            shoe=true;
//            holder.shoeATC.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onItemAddToCart(position,"footwear");
//                }
//            });
        } }

    @Override
    public int getItemCount() {
        return shoesProducts.size();
    }

    class ShoesViewHolder extends RecyclerView.ViewHolder{

        private ImageView shoeImage;
        private TextView shoeName,shoePrice,shoeFinalPrice,shoeDiscount,shoeRsTag;
//        private Button shoeATC;

        public ShoesViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT));
            shoeImage = itemView.findViewById(R.id.shoe_image);
            shoeName = itemView.findViewById(R.id.shoe_name);
            shoePrice = itemView.findViewById(R.id.shoe_price);
            shoeFinalPrice=itemView.findViewById(R.id.shoe_final_price);
            shoeDiscount=itemView.findViewById(R.id.shoe_discount);
            shoeRsTag=itemView.findViewById(R.id.shoe_rs_tag);
//            shoeATC = itemView.findViewById(R.id.shoe_atc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnShoesItemClickListener {
        void onItemAddToCart(int position, String category);
        void onItemClick(int position);
    }
}

