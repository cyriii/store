package com.project.system.storemanagement.fragment.manage;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.project.system.storemanagement.R;
import com.project.system.storemanagement.base.BaseBackFragment;
import com.project.system.storemanagement.base.BaseBean;
import com.project.system.storemanagement.bean.GoodsBean;
import com.project.system.storemanagement.event.AddInEvent;
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
 * 添加入库
 */
public class AddInFragment extends BaseBackFragment {
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

    private String supplierId;
    private String goodId;
    private String supplyDate;


    private TimePickerView pvTime;
    private OptionsPickerView pvSexOptions;

    public static AddInFragment newInstance() {
        AddInFragment fragment = new AddInFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_add_store;
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
        title.setText(getString(R.string.add_in));
    }


    @OnClick({R.id.tv_supplier_name, R.id.tv_goods_name, R.id.tv_supply_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_supplier_name:
                startForResult(SupplierListFragment.newInstance(), 110);
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
                supplierId = goodsBean.getId();
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
        if (!TextUtils.isEmpty(supplyDate))
            selectedDate.setTime(TimeUtils.string2Date(supplyDate, "yyyy-MM-dd"));

        //时间选择器
        if (pvTime == null) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(1990, 0, 1);
            Calendar endDate = Calendar.getInstance();
            pvTime = new TimePickerBuilder(_mActivity, (date, v) -> {
                supplyDate = TimeUtils.date2String(date, "yyyy-MM-dd");
                tvSupplyDate.setText(supplyDate);
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
        if (TextUtils.isEmpty(supplierId)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_supplier_name));
            return;
        }
        if (TextUtils.isEmpty(goodId)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_goods_name));
            return;
        }
        String supplyNum = etSupplyNum.getText().toString().trim();
        if (TextUtils.isEmpty(supplyNum)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_supply_num));
            return;
        }
        String supplyUnivalence = etSupplyUnivalence.getText().toString().trim();
        if (TextUtils.isEmpty(supplyUnivalence)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_supply_univalence));
            return;
        }
        if (TextUtils.isEmpty(supplyDate)) {
            ToastUtils.showShortToast(getResources().getString(R.string.select_supply_date));
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("supplyNum", supplyNum);
        map.put("supplyUnivalence", supplyUnivalence);
        map.put("supplierId", supplierId);
        map.put("goodId", goodId);
        map.put("supplyDate", supplyDate);

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().storeIn(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {

                    @Override
                    public void onFinish(BaseBean bean) {
                        EventBus.getDefault().post(new AddInEvent());
                        ToastUtils.showShortToast(R.string.add_success);
                        _mActivity.onBackPressed();
                    }
                });
    }


}
