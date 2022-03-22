package ru.cpc.smartflatview.uiView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.cpc.smartflatview.R;

public class TopExpandableListAdapter extends BaseExpandableListAdapter
{
    private final Context mContext;
    private final List<ExpandedMenuModel> mTopListData; // header titles

    public TopExpandableListAdapter(Context context, List<ExpandedMenuModel> listData) {
        this.mContext = context;
        this.mTopListData = listData;
    }

    @Override
    public int getGroupCount() {
        if(mTopListData == null)
            return 0;

        int i = mTopListData.size();
        Log.d("GROUPCOUNT", String.valueOf(i));
        return this.mTopListData.size();
    }


    @Override
    public int getChildrenCount(int groupPosition)
    {

        if(mTopListData == null)
            return 0;
        return this.mTopListData.get(groupPosition).m_cNestedMenu.size();

//        return 1;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this.mTopListData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        Log.d("CHILD", groupPosition + ":" + childPosition);
        Log.d("CHILD", this.mTopListData.get(groupPosition).m_cNestedMenu.get(childPosition).toString());

        return this.mTopListData.get(groupPosition).m_cNestedMenu.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS =
    {
        EMPTY_STATE_SET, // 0
        GROUP_EXPANDED_STATE_SET // 1
    };

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        Log.d("Menu", "TopExpandable::getGroupView("+groupPosition+") "+convertView);
        return getView((ExpandedMenuModel) getGroup(groupPosition));
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        Log.d("Menu", "TopExpandable::getChildView("+groupPosition+","+childPosition+") "+convertView);
        return getView((ExpandedMenuModel) getChild(groupPosition,childPosition));
    }

    @NonNull
    private View getView(ExpandedMenuModel viewItem) {
        View convertView;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewItem.m_cNestedMenu.size() == 0)
            convertView = inflater.inflate(R.layout.listheader, null);//TODO разобраться, что делать с желтым  null. Если не null - падает.
        else
            convertView = inflater.inflate(R.layout.list_submenu, null);

        TextView nameMiddle = convertView.findViewById(R.id.group_name);
        nameMiddle.setText(viewItem.getName());

        ImageView childIcon = convertView.findViewById(R.id.iconimage);
        if (childIcon != null && viewItem.getIcon() != -1)
            childIcon.setImageResource(viewItem.getIcon());
        viewItem.m_pView = convertView;
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
