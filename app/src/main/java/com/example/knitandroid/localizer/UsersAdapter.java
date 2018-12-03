package com.example.knitandroid.localizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Darqwski on 2018-12-02.
 */

public class UsersAdapter extends ArrayAdapter<UserLog> {

    ArrayList<UserLog> items;
    int resource;
    public UsersAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public UsersAdapter(Context context, int resource, ArrayList<UserLog> items) {
        super(context, resource, items);
        this.items= items;
        this.resource=resource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, null);

        }
        final UserLog value = items.get(position);
        ((TextView)v.findViewById(R.id.PostitionTextView)).setText(String.valueOf(value.getDateString()));
        /*
        * TODO caaaaaaaaały adapter jakoś ładnie rozłożone elementy z UserLog
        * */
        return v;
    }




}