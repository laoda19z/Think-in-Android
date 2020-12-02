package com.yalantis.euclid.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii Shliama on 1/27/15.
 */
public class EuclidListAdapter extends ArrayAdapter<Map<String, Object>> {
    //头像
    public static final String KEY_AVATAR = "avatar";
    //
    public static final String KEY_NAME = "name";
    //简短描述
    public static final String KEY_DESCRIPTION_SHORT = "description_short";
    //详细描述
    public static final String KEY_DESCRIPTION_FULL = "description_full";

    //布局填充器
    private final LayoutInflater mInflater;
    //数据准备
    private List<Map<String, Object>> mData;

    public EuclidListAdapter(Context context, int layoutResourceId, List<Map<String, Object>> data) {
        super(context, layoutResourceId, data);
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();

            //是View的那个覆盖 上滑的时候使用
            viewHolder.mViewOverlay = convertView.findViewById(R.id.view_avatar_overlay);
            //图片控件imageview
            viewHolder.mListItemAvatar = (ImageView) convertView.findViewById(R.id.image_view_avatar);
            viewHolder.mListItemName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.mListItemDescription = (TextView) convertView.findViewById(R.id.text_view_description);
            //填充整合
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //得到本地资源来加载图片
        Picasso.with(getContext())//context
                .load((Integer) mData.get(position).get(KEY_AVATAR))//得到的是图片资源id
                .resize(EuclidActivity.sScreenWidth, EuclidActivity.sProfileImageHeight)//设置大小
                .centerCrop()//重新设定大小
                .placeholder(R.color.blue)//当图片还没有加载完成的时候填充蓝色
                .into(viewHolder.mListItemAvatar);//设置到图片控件中

        viewHolder.mListItemName.setText(mData.get(position).get(KEY_NAME).toString().toUpperCase());
        viewHolder.mListItemDescription.setText((String) mData.get(position).get(KEY_DESCRIPTION_SHORT));
        viewHolder.mViewOverlay.setBackground(EuclidActivity.sOverlayShape);

        return convertView;
    }

    static class ViewHolder {
        View mViewOverlay;
        ImageView mListItemAvatar;
        TextView mListItemName;
        TextView mListItemDescription;
    }
}
