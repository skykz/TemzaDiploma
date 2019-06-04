package com.example.asus.mobiletracker.userSide.googleApi.adapters;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.userSide.googleApi.models.Products;


import org.w3c.dom.Text;

import java.util.List;

public class ListOfProductsAdapter extends RecyclerView.Adapter<ListOfProductsAdapter.MyViewHolder>{

    private final static String TAG = "ListOfProducts";

    private Context context;
    private List<Products> postList;
    private OnNoteListener MonNoteListener;


    public ListOfProductsAdapter(List<Products> postList,Context context,OnNoteListener onNoteListener) {

        this.postList = postList;
        this.context = context;
        this.MonNoteListener = onNoteListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView id;
        public TextView title;
        private ImageView image1;
        private TextView country;
        private TextView price;
        private TextView status;
        private TextView used_amount;

        OnNoteListener MonNoteListener;

        public MyViewHolder(@NonNull View itemView,OnNoteListener onNoteListener) {
            super(itemView);

            id = itemView.findViewById(R.id.id_item);
            title = itemView.findViewById(R.id.title);
            country = itemView.findViewById(R.id.status);
            status = itemView.findViewById(R.id.text_status_item);
            image1 = itemView.findViewById(R.id.image_order);
            price = itemView.findViewById(R.id.price);

            this.MonNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position  = getAdapterPosition();
                MonNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        return new MyViewHolder(itemView,MonNoteListener);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Products posts = postList.get(position);

//        Log.d(TAG,"position " + position);

        holder.id.setText(String.valueOf(posts.getId()));
        holder.title.setText(posts.getName_of_product());
        holder.price.setText("Цена: " + String.valueOf(posts.getPrice()) + " тг");
        holder.country.setText("Производитель: " + posts.getCountry());

        if (posts.getStatus().equals("PENDING")) {
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.status_pending));

            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time, 0, 0, 0);

            holder.status.setText("ожидание");
        }

        else if (posts.getStatus().equals("DRAFT")) {
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.status_done));
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_order, 0, 0, 0);
            holder.status.setText("доставлено");
        }

        else if (posts.getStatus().equals("PROCESS")) {
            holder.status.setBackground(context.getResources().getDrawable(R.drawable.status_proccess));
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_refresh, 0, 0, 0);
            holder.status.setText("в пути");
        }
//        holder.used_amount.setText(String.valueOf(posts.getUsed_by_people()));
//

        Glide.with(context)
                .load("https://temza.kz/storage/"+posts.getImage())
                .into(holder.image1);
    }
    @Override
    public int getItemCount() {
        return postList.size();
    }


    public interface OnNoteListener{
        void onNoteClick(int position);
    }

}