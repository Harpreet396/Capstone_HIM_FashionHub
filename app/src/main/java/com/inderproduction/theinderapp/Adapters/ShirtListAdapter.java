package com.inderproduction.theinderapp.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inderproduction.theinderapp.Modals.Shirt;
import com.inderproduction.theinderapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShirtListAdapter extends RecyclerView.Adapter<ShirtListAdapter.ShirtViewHolder> {

    private List<Object> shirtProducts;
    private OnShirtItemClickListener mListener;
    private int screenWidth;
    private Context context;
    private boolean shirt;

    public ShirtListAdapter(Context context, List<Object> data, OnShirtItemClickListener listener, int screenWidth) {
        this.shirtProducts = data;
        mListener = listener;
        this.screenWidth = screenWidth;
        this.context = context;
    }

    public void updateData(List<Object> newData) {
        this.shirtProducts = new ArrayList<>();
        this.shirtProducts.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShirtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shirt_list_item, parent, false);
        ShirtViewHolder holder = new ShirtViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShirtViewHolder holder, final int position) {
        Object o = shirtProducts.get(position);

        if (o instanceof Shirt) {
            Shirt s = (Shirt) o;

            if (s.getProductImage() != null) {
                Picasso.get().load(Uri.parse(s.getProductImage())).fit().into(holder.shirtImage);
            }
            holder.shirtName.setText(s.getItemName());
            holder.shirtRsTag.setText("CAN ");
            holder.shirtPrice.setText(String.valueOf(s.getItemPrice()));

            int dis = s.getDiscount();
            if (dis > 0) {
                holder.shirtDiscount.setText(String.valueOf(s.getDiscount()) + "% off");
                holder.shirtFinalPrice.setText(String.valueOf(s.getItemPrice() - ((double) s.getDiscount() / 100 * s.getItemPrice())));
                holder.shirtPrice.setPaintFlags(holder.shirtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.shirtFinalPrice.setText(String.valueOf(s.getItemPrice()));
                holder.shirtPrice.setText("");
                holder.shirtDiscount.setText("");
            }
            shirt = true;
//            holder.shirtATC.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onItemAddToCart(position,"shirt");
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return shirtProducts.size();
    }

    class ShirtViewHolder extends RecyclerView.ViewHolder {

        private ImageView shirtImage;
        private TextView shirtName, shirtPrice, shirtFinalPrice, shirtDiscount, shirtRsTag;
//        private Button shirtATC;

        public ShirtViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT));
            shirtImage = itemView.findViewById(R.id.shirt_image);
            shirtName = itemView.findViewById(R.id.shirt_name);
            shirtPrice = itemView.findViewById(R.id.shirt_price);
            shirtFinalPrice = itemView.findViewById(R.id.shirt_final_price);
            shirtDiscount = itemView.findViewById(R.id.shirt_discount);
            shirtRsTag = itemView.findViewById(R.id.shirt_rs_tag);
//            shirtATC = itemView.findViewById(R.id.shirt_atc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnShirtItemClickListener {
        void onItemAddToCart(int position, String category);

        void onItemClick(int position);
    }
}

