package com.example.uidemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uidemo.R;


import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends BaseAdapter {
    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;
    private List<String> commList;
    private Context context;//上下文环境
    private int layout;
//    private ArrayList<Moment> mList;
//    private Context mContext;
//    private CustomTagHandler mTagHandler;
    public CommentAdapter(List<String> commList, Context context, int layout) {
        this.commList = commList;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return commList.size();
    }

    @Override
    public Object getItem(int i) {
        return commList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {

                view = View.inflate(context, R.layout.trends_comment_list_item, null);
                ViewHolder holder = new ViewHolder();
                holder.mCommentList = (LinearLayout) view.findViewById(R.id.trend_comment_list);
                holder.mBtnInput = view.findViewById(R.id.btn_input_comment);
//                holder.mContent = (TextView) convertView.findViewById(R.id.content);
                view.setTag(holder);

        }
        //防止ListView的OnItemClick与item里面子view的点击发生冲突
        ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        if (i == 0) {

        } else {
            int index = i - 1;
            ViewHolder holder = (ViewHolder) view.getTag();
//            holder.mContent.setText(mList.get(index).mContent);
//            CommentFun.parseCommentList(context, commList.get(index),
//                    holder.mCommentList, holder.mBtnInput, mTagHandler);
        }
        return view;
    }
    static class ViewHolder{
        LinearLayout mCommentList;
        View mBtnInput;
        TextView mContent;
    }
}
