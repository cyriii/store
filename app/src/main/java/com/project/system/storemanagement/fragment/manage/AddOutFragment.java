package com.project.system.storemanagement.fragment.manage;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.bean.GoodsBean;
import com.project.system.storemanagement.event.AddOutEvent;
import com.project.system.storemanagement.http.ApiServiceResult;
import com.project.system.storemanagement.http.Client;
import com.project.system.storemanagement.http.DialogTransformer;
import com.project.system.storemanagement.http.RxsRxSchedulers;
import com.project.system.storemanagement.utils.TimeUtils;
import com.project.system.storemanagement.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加出库
 */
public class AddOutFragment extends BaseBackFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_supply_num)
    EditText etSupplyNum;
    @BindView(R.id.et_supply_univalence)
    EditText etSupplyUnivalence;
    @BindView(R.id.tv_supplier_name)
    TextView tvSupplierName;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_supply_date)
    TextView tvSupplyDate;

    private String customerId;
    private String goodId;
    private String demandDate;

    private TimePickerView pvTime;

    public static AddOutFragment newInstance() {
        AddOutFragment fragment = new AddOutFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_out_store;
    }

    @Override
    protected View setToolBarView() {
        return toolbar;
    }

    @Override
    protected int statusBarColor() {
        return R.color.transparent;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.add_out));
    }


    @OnClick({R.id.tv_supplier_name, R.id.tv_goods_name, R.id.tv_supply_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_supplier_name:
                startForResult(CustomerListFragment.newInstance(), 110);
                break;
            case R.id.tv_goods_name:
                startForResult(GoodsListFragment.newInstance(), 119);
                break;
            case R.id.tv_supply_date:
                selectData();
                break;
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            GoodsBean goodsBean = (GoodsBean) data.getSerializable("data");
            if (requestCode == 110) {
                customerId = goodsBean.getId();
                tvSupplierName.setText(goodsBean.getName());
            } else if (requestCode == 119) {
                goodId = goodsBean.getId();
                tvGoodsName.setText(goodsBean.getName());
            }
        }
    }

    /**
     * 选择日期
     */
    private void selectData() {
        Calendar selectedDate = Calendar.getInstance();
        if (!TextUtils.isEmpty(demandDate))
            selectedDate.setTime(TimeUtils.string2Date(demandDate, "yyyy-MM-dd"));

        //时间选择器
        if (pvTime == null) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(1990, 0, 1);
            Calendar endDate = Calendar.getInstance();
            pvTime = new TimePickerBuilder(_mActivity, (date, v) -> {
                demandDate = TimeUtils.date2String(date, "yyyy-MM-dd");
                tvSupplyDate.setText(demandDate);
                pvTime.dismiss();
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setLabel("", "", "", "时", "分", "秒")//默认设置为年月日时分秒
                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                    .setRangDate(startDate, endDate)//起始终止年月日设定
                    .setContentTextSize(18)//滚轮文字大小
                    .build();
        }
        pvTime.setDate(selectedDate);
        pvTime.show();
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(customerId)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_client_name));
            return;
        }
        if (TextUtils.isEmpty(goodId)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_goods_name));
            return;
        }
        String supplyNum = etSupplyNum.getText().toString().trim();
        if (TextUtils.isEmpty(supplyNum)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_out_num));
            return;
        }
        String supplyUnivalence = etSupplyUnivalence.getText().toString().trim();
        if (TextUtils.isEmpty(supplyUnivalence)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_out_univalence));
            return;
        }
        if (TextUtils.isEmpty(demandDate)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_out_date));
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("demandNum", supplyNum);
        map.put("demandUnivalence", supplyUnivalence);
        map.put("customerId", customerId);
        map.put("goodId", goodId);
        map.put("demandDate", demandDate);

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().storeOut(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        EventBus.getDefault().post(new AddOutEvent());
                        ToastUtils.showShortToast(R.string.add_success);
                        _mActivity.onBackPressed();
                    }
                });
    }


}
