package com.example.neu.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neu.shopapp.R;

import java.util.List;

public class ExpandAdapter extends BaseExpandableListAdapter { 
 
    private Context mContext; 
    private LayoutInflater mInflater = null; 
    private List<String>   mGroupStrings = null; 
    private List<List<ExpandListViewItem>>   mData = null; 
 
    public ExpandAdapter(Context ctx, List<List<ExpandListViewItem>> list,List<String> mGroupStrings) { 
        mContext = ctx; 
        mInflater = (LayoutInflater) mContext 
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        this.mGroupStrings = mGroupStrings; 
        mData = list; 
    } 
 
    public void setData(List<List<ExpandListViewItem>> list) { 
        mData = list; 
    } 
 
    @Override 
    public int getGroupCount() { 
        // TODO Auto-generated method stub 
        return mData.size(); 
    } 
 
    @Override 
    public int getChildrenCount(int groupPosition) { 
        // TODO Auto-generated method stub 
        return mData.get(groupPosition).size(); 
    } 
 
    @Override 
    public List<ExpandListViewItem> getGroup(int groupPosition) { 
        // TODO Auto-generated method stub 
        return mData.get(groupPosition); 
    } 
 
    @Override 
    public ExpandListViewItem getChild(int groupPosition, int childPosition) { 
        // TODO Auto-generated method stub 
        return mData.get(groupPosition).get(childPosition); 
    } 
 
    @Override 
    public long getGroupId(int groupPosition) { 
        // TODO Auto-generated method stub 
        return groupPosition; 
    } 
 
    @Override 
    public long getChildId(int groupPosition, int childPosition) { 
        // TODO Auto-generated method stub 
        return childPosition; 
    } 
 
    @Override 
    public boolean hasStableIds() { 
        // TODO Auto-generated method stub 
        return false; 
    } 
 
    @Override 
    public View getGroupView(int groupPosition, boolean isExpanded, 
            View convertView, ViewGroup parent) { 
        // TODO Auto-generated method stub 
        if (convertView == null) { 
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        } 
        GroupViewHolder holder = new GroupViewHolder(); 
        holder.mGroupName = (TextView) convertView 
                .findViewById(R.id.group_name); 
        holder.mGroupName.setText(mGroupStrings.get(groupPosition)); 
       
        return convertView; 
    } 
 
    @Override 
    public View getChildView(int groupPosition, int childPosition, 
            boolean isLastChild, View convertView, ViewGroup parent) { 
        // TODO Auto-generated method stub 
    	ChildViewHolder holder; 
        if (convertView == null) { 
        	holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.child_item_layout, null); 
            holder.mImg1 = (ImageView) convertView.findViewById(R.id.imageView1); 
            holder.mImg2 = (ImageView) convertView.findViewById(R.id.imageView2); 
            holder.mChildName = (TextView) convertView.findViewById(R.id.item_name); 
            convertView.setTag(holder);
        } 
        else{
        	holder=(ChildViewHolder)convertView.getTag();
        }
        holder.mImg1.setBackgroundResource(getChild(groupPosition, 
                childPosition).getImgId1());       
        holder.mImg2.setBackgroundResource(getChild(groupPosition, 
                childPosition).getImgId2());         
        holder.mChildName.setText(getChild(groupPosition, childPosition).getName()); 
        
        
        return convertView; 
    } 
 
    @Override 
    public boolean isChildSelectable(int groupPosition, int childPosition) { 
        // TODO Auto-generated method stub 
        /*很重要：实现ChildView点击事件，必须返回true*/ 
      return true; 
    } 
 
    private class GroupViewHolder { 
        TextView mGroupName; 
        
    } 
 
    private class ChildViewHolder { 
        ImageView mImg1; 
        TextView mChildName; 
        ImageView mImg2; 
    } 
 
} 
