package de.valeapps.teamrandomizer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;




public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> implements SwipeListener {
    private List<String> mData;
    private LayoutInflater mInflater;

    ItemAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.row_layout, parent, false);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Log.d("ItemAdapter.this", "Position-" + position);
        holder.textView.setText(mData.get(position));

    }

    @Override
    public int getItemCount() {


        return mData.size();
    }

    @Override
    public void onSwipe(int pos) {
        mData.remove(pos);
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ItemHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
        }
    }
}
