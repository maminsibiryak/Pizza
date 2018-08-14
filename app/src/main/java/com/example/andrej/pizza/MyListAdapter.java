package com.example.andrej.pizza;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expListTitle;
    private HashMap<String, ArrayList<Product>> expListDetail;

    public MyListAdapter(MainActivity context, List<String> expListTitle, HashMap<String, ArrayList<Product>> expListDetail) {
        this.context = context;
        this.expListTitle = expListTitle;
        this.expListDetail = expListDetail;
    }


    public Object getChildImg(int listPosition, int expListPosition) {
        return expListDetail.get(
                expListTitle.get(listPosition)
        ).get(expListPosition).getImg_url();
    }
    public Object getChildSize(int listPosition, int expListPosition) {
        return expListDetail.get(
                expListTitle.get(listPosition)
        ).get(expListPosition).getSize();
    }

    public Object getChildName(int listPosition, int expListPosition) {
        return expListDetail.get(
                expListTitle.get(listPosition)
        ).get(expListPosition).getProductName();
    }

    @Override
    public Object getChild(int listPosition, int expListPosition) {
        return expListDetail.get(
                expListTitle.get(listPosition)
        ).get(expListPosition).getDesc();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // получаем дочерний элемент
        String productName = (String) getChildName(listPosition, expandedListPosition);
        String productDesc = (String) getChild(listPosition, expandedListPosition);
        HashMap<String,String> productSize = (HashMap<String, String>) getChildSize(listPosition, expandedListPosition);
        String img = (String) getChildImg(listPosition,expandedListPosition);

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
        TextView productNameView = convertView.findViewById(R.id.expandedListItemProductName);
        productNameView.setText(productName);

        TextView productDescView = convertView.findViewById(R.id.expandedListItemProductDesc);
        productDescView.setText(productDesc);

        TextView productSizeView = convertView.findViewById(R.id.expandedListItemProductSize);

        ArrayList<String> temp = new ArrayList<>();
        for (Map.Entry<String, String> t: productSize.entrySet()
             ) {
            temp.add(t.getKey()+" "+t.getValue());
        }
       productSizeView.setText(String.valueOf(temp));

        ImageView imageView = convertView.findViewById(R.id.productImage);
        Picasso.with(context).load(img).into(imageView);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return expListDetail.get(
                expListTitle.get(listPosition)
        ).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return expListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return expListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // получаем родительский элемент
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}