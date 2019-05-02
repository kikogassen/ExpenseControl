package com.kikog.expensecontrol.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.kikog.expensecontrol.R;
import com.kikog.expensecontrol.model.Categories;
import com.kikog.expensecontrol.model.Expense;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    ArrayList<String> listMonths = new ArrayList<>();
    ArrayList<Float> totalPricesByCategory = new ArrayList<>();
    Spinner sMonth;
    AnyChartView anyChartView;
    Pie pie;

    public DashboardFragment(){}

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            reloadChart();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);

        instantiateViews(view);
        functionsView();

        return view;
    }

    private void functionsView(){
        createCategories();
        createSpinnerAdapter();
        setCurrentMonth();
        createChart();
    }

    private void instantiateListOfPrices(){
        totalPricesByCategory.clear();
        for (int i=0;i<Categories.categories.length;i++){
            totalPricesByCategory.add(Float.valueOf(0));
        }
    }

    private void setCurrentMonth(){
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        sMonth.setSelection(Integer.parseInt(dateFormat.format(date))-1);
    }

    private void reloadChart(){
        List<Expense> listExpenses = Expense.getAllByMonth(String.format("%02d", sMonth.getSelectedItemPosition()+1));
        instantiateListOfPrices();
        for (Expense expense: listExpenses){
            totalPricesByCategory.set(expense.getCategory(), totalPricesByCategory.get(expense.getCategory())+expense.getPrice());
        }

        List<DataEntry> data = new ArrayList<>();
        for (int i=0;i<Categories.categories.length;i++){
            data.add(new ValueDataEntry(Categories.categories[i], totalPricesByCategory.get(i).intValue()));
        }

        pie.title("Gastos no mês de "+sMonth.getSelectedItem().toString().toLowerCase());

        pie.data(data);

    }

    private void createChart(){
        pie = AnyChart.pie();

        List<Expense> listExpenses = Expense.getAllByMonth(String.format("%02d", sMonth.getSelectedItemPosition()+1));
        instantiateListOfPrices();
        for (Expense expense: listExpenses){
            totalPricesByCategory.set(expense.getCategory(), totalPricesByCategory.get(expense.getCategory())+expense.getPrice());
        }

        List<DataEntry> data = new ArrayList<>();
        for (int i=0;i<Categories.categories.length;i++){
            data.add(new ValueDataEntry(Categories.categories[i], totalPricesByCategory.get(i).intValue()));
        }

        pie.data(data);


        pie.title("Gastos no mês de "+sMonth.getSelectedItem().toString().toLowerCase());

        anyChartView.setChart(pie);
    }

    private void instantiateViews(View view){
        sMonth = view.findViewById(R.id.s_month);
        anyChartView = view.findViewById(R.id.any_chart_view);
    }

    private void createCategories(){
        listMonths.add("Janeiro");
        listMonths.add("Fevereiro");
        listMonths.add("Março");
        listMonths.add("Abril");
        listMonths.add("Maio");
        listMonths.add("Junho");
        listMonths.add("Julho");
        listMonths.add("Agosto");
        listMonths.add("Setembro");
        listMonths.add("Outubro");
        listMonths.add("Novembro");
        listMonths.add("Dezembro");
    }

    private void createSpinnerAdapter(){
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listMonths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMonth.setAdapter(adapter);
        sMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reloadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
