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
import com.inderproduction.theinderapp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PentListAdapter extends RecyclerView.Adapter<PentListAdapter.PentViewHolder> {

    private List<Object> pentProducts;
    private OnPentItemClickListener mListener;
    private int screenWidth;
    private Context context;
    private boolean pent;

    public PentListAdapter(Context context, List<Object> data, OnPentItemClickListener listener, int screenWidth){
        this.pentProducts = data;
        mListener = listener;
        this.screenWidth = screenWidth;
        this.context = context;
    }

    public void updateData(List<Object> newData){
        this.pentProducts = new ArrayList<>();
        this.pentProducts.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pent_list_item,parent,false);
        PentViewHolder holder = new PentViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PentViewHolder holder, final int position) {
        Object o = pentProducts.get(position);

        if (o instanceof Pent) {
            Pent j = (Pent) o;

            Picasso.get().load(Uri.parse(j.getProductImage())).fit().into(holder.pentImage);

            holder.pentName.setText(j.getItemName());
            holder.pentRsTag.setText("CAN ");
            holder.pentPrice.setText(String.valueOf(j.getItemPrice()));

            int dis=j.getDiscount();
            if(dis>0){
            holder.pentDiscount.setText(String.valueOf(j.getDiscount())+"% off");
            holder.pentFinalPrice.setText(String.valueOf(j.getItemPrice() - ((double)j.getDiscount()/100 * j.getItemPrice())));
            holder.pentPrice.setPaintFlags(holder.pentPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else {
                holder.pentFinalPrice.setText(String.valueOf(j.getItemPrice()));
                holder.pentPrice.setText("");
                holder.pentDiscount.setText("");
            }
            pent=true;
//            holder.pentATC.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onItemAddToCart(position,"pent");
//                }
//            });
        } }

    @Override
    public int getItemCount() {
        return pentProducts.size();
    }

    class PentViewHolder extends RecyclerView.ViewHolder{

        private ImageView pentImage;
        private TextView pentName,pentPrice,pentFinalPrice,pentDiscount,pentRsTag;
//        private Button pentATC;

        public PentViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT));
            pentImage = itemView.findViewById(R.id.pent_image);
            pentName = itemView.findViewById(R.id.pent_name);
            pentPrice = itemView.findViewById(R.id.pent_price);
            pentFinalPrice=itemView.findViewById(R.id.pent_final_price);
            pentDiscount=itemView.findViewById(R.id.pent_discount);
            pentRsTag=itemView.findViewById(R.id.pent_rs_tag);

//            pentATC = itemView.findViewById(R.id.pent_wish_buttton);

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

    public interface OnPentItemClickListener {
        void onItemAddToCart(int position,String category);
        void onItemClick(int position);
    }
}

