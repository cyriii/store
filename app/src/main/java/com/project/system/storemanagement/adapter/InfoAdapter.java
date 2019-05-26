package com.project.system.storemanagement.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.bean.GoodsBean;

public class InfoAdapter extends BaseMultiItemQuickAdapter<GoodsBean, BaseViewHolder> {
    private String searchStr;

    public InfoAdapter() {
        super(null);
        setDefaultViewTypeLayout(R.layout.item_goods_info);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final GoodsBean item) {
        String str = item.getGoodName();
        if (!TextUtils.isEmpty(searchStr) && str.contains(searchStr)) {
            SpannableString msp = new SpannableString(str);
            int startPosition = str.indexOf(searchStr);
            msp.setSpan(new ForegroundColorSpan(Color.parseColor("#DF654C"))
                    , startPosition, startPosition + searchStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.setText(R.id.tv_name, msp);
        } else {
            holder.setText(R.id.tv_name, item.getGoodName());
        }

        holder.setText(R.id.tv_unit, item.getGoodUnit());
        holder.setText(R.id.tv_remark, item.getStoreNum());
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }
}