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

public class PutInAdapter extends BaseMultiItemQuickAdapter<GoodsBean, BaseViewHolder> {
    private String searchStr;

    public PutInAdapter() {
        super(null);
        setDefaultViewTypeLayout(R.layout.item_putin);
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

        holder.setText(R.id.tv_unit, item.getUnit());
        holder.setText(R.id.tv_remark, item.getSupplyNum());
        holder.setText(R.id.tv_goods_money, item.getSupplyUnivalence());

        String str1 = item.getSupplierName();
        if (!TextUtils.isEmpty(searchStr) && str1.contains(searchStr)) {
            SpannableString msp = new SpannableString(str1);
            int startPosition = str1.indexOf(searchStr);
            msp.setSpan(new ForegroundColorSpan(Color.parseColor("#DF654C"))
                    , startPosition, startPosition + searchStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.setText(R.id.tv_supplier_name, msp);
        } else {
            holder.setText(R.id.tv_supplier_name, item.getSupplierName());
        }

    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }
}