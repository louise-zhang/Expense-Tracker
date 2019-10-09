package com.example.expensetracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


public class PieChartFragment extends Fragment {

    int hue0 = Color.argb(255, 255, 179, 179);
    int hue60 = Color.argb(255, 255, 255, 179);
    int hue105 = Color.argb(255, 198, 255, 179);
    int hue180 = Color.argb(255, 179, 255, 255);

    private PieChartView pieChart;
    private PieChartData pieChartData;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private int[] data = {21, 20, 9, 2, 8, 33, 14, 12};

    private float totalMoney;

    private TextView centerTitle;
    private TextView centerMoney;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        findView(view);
        return view;

    }

    private void findView(View view) {
        pieChart = view.findViewById(R.id.chart);
        centerTitle = view.findViewById(R.id.center_title);
        centerMoney = view.findViewById(R.id.center_money);

        setPie();
    }

    private void setPie() {
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听
        setPieChartData();
        initPieChart();
    }

    /**
     * 获取数据
     */
    private void setPieChartData() {

        for (int i = 0; i < data.length; ++i) {
            SliceValue sliceValue = new SliceValue((float) data[i], RandomColor.getRandomColor());//这里的颜色是我写了一个工具类 是随机选择颜色的
            values.add(sliceValue);
        }

        centerMoney.setText("" + totalMoney);


    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        List pieData = new ArrayList<>();
//        pieData.add(new SliceValue(15, hue0).setLabel("Q1: $10"));
//        pieData.add(new SliceValue(25, hue60).setLabel("Q2: $4"));
//        pieData.add(new SliceValue(10, hue105).setLabel("Q3: $18"));
//        pieData.add(new SliceValue(60, hue180).setLabel("Q4: $28"));
//        pieData.add(new SliceValue(0, hue180).setLabel(""));
//
//
//        PieChartData pieChartData = new PieChartData(pieData);
//        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
//        pieChartData.setHasCenterCircle(true).setCenterText1("").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#4d4d4d"));
//        pieChartView.setPieChartData(pieChartData);
//
//    }


    /**
     * 初始化
     */
    private void initPieChart() {
        pieChartData = new PieChartData();
        pieChartData.setHasLabels(true);//显示标签
        pieChartData.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        pieChartData.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieChartData.setHasCenterCircle(true);//是否是环形显示
        pieChartData.setValues(values);//填充数据
        pieChartData.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieChartData.setCenterCircleScale(0.5f);//设置环形的大小级别
        pieChartData.setCenterText1("Expense");//环形中间的文字1
        pieChartData.setCenterText1Color(Color.BLACK);//文字颜色
        pieChartData.setCenterText1FontSize(14);//文字大小

        pieChartData.setCenterText2("" + totalMoney);
        pieChartData.setCenterText2Color(Color.BLACK);
        pieChartData.setCenterText2FontSize(18);
        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//		Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//		data.setCenterText1Typeface(tf);

        pieChart.setPieChartData(pieChartData);
        pieChart.setValueSelectionEnabled(true);//选择饼图某一块变大
        pieChart.setAlpha(0.9f);//设置透明度
        pieChart.setCircleFillRatio(1f);//设置饼图大小

    }

    /**
     * 监听事件
     */
    private PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            // TODO Auto-generated method stub
            Toast.makeText(getActivity(), "Selected: " + value.getValue(), Toast.LENGTH_SHORT).show();
        }
    };


}
