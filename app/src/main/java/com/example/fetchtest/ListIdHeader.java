package com.example.fetchtest;

public class ListIdHeader implements RecyclerViewItem {
    private int listId;

    public ListIdHeader(int listId) {
        this.listId = listId;
    }

    public int getListId() {
        return listId;
    }
}