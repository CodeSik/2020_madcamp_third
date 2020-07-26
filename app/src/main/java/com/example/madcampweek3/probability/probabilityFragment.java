package com.example.madcampweek3.probability;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.madcampweek3.Account.AccountActivity;
import com.example.madcampweek3.Profile.FoldingCell;
import com.example.madcampweek3.Profile.Item;
import com.example.madcampweek3.R;

import java.util.ArrayList;

public class probabilityFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_probability, container, false);

        // get our list view
        ListView theListView = view.findViewById(R.id.calculate_ListView);

        // prepare elements to display
        final ArrayList<Item> items = Item.getTestingList();
        items.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // set elements to adapter
        theListView.setAdapter(adapter);

        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state
                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });


        // add custom btn handler
        return view;
    }

}
