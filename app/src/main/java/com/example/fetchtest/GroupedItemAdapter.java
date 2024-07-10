package com.example.fetchtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupedItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecyclerViewItem> itemList;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public GroupedItemAdapter(List<RecyclerViewItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof ListIdHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ListIdHeader header = (ListIdHeader) itemList.get(position);
            ((HeaderViewHolder) holder).headerTextView.setText("List ID: " + header.getListId());
        } else if (holder instanceof ItemViewHolder) {
            Item item = (Item) itemList.get(position);
            ((ItemViewHolder) holder).nameTextView.setText("Name: " + item.getName());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerTextView;

        public HeaderViewHolder(View view) {
            super(view);
            headerTextView = view.findViewById(R.id.header_text_view);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public ItemViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.name_text_view);
        }
    }
}
