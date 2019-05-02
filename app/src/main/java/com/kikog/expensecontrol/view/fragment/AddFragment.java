package com.kikog.expensecontrol.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.kikog.expensecontrol.R;
import com.kikog.expensecontrol.model.Categories;
import com.kikog.expensecontrol.model.DBHelper;
import com.kikog.expensecontrol.model.Expense;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddFragment extends Fragment {

    ArrayList<String> listCategories = new ArrayList<>();
    Spinner sCategories;
    EditText etPrice, etDescription;
    Button bSave;

    public AddFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        instantiateViews(view);
        functionViews();

        return view;
    }

    private void functionViews(){
        createCategories();
        createSpinnerAdapter();
        createMaskPrice();
        bSaveFunction();
    }

    private void bSaveFunction(){
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPrice.getText().toString().equals("R$ 0,00") && !sCategories.getSelectedItem().toString().equals("Categoria")){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    Expense expense = new Expense(Float.parseFloat(removeMask(etPrice)), sCategories.getSelectedItemPosition()-1, etDescription.getText().toString(), dateFormat.format(date));
                    expense.createExpense();
                    resetFields();
                } else {
                    Toast.makeText(getActivity(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetFields(){
        etPrice.setText("R$ 0,00");
        sCategories.setSelection(0);
        etDescription.setText("");
        etPrice.requestFocus();
        removeFocus(etPrice);
    }

    private String removeMask(EditText editText){
        return etPrice.getText().toString().substring(3).replace(",", ".");
    }

    private void createMaskPrice(){
        etPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP){
                    String text = etPrice.getText().toString();
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        etPrice.setText(putMask(putNecessaryZeros(removeMask(text).substring(0, removeMask(text).length()-1))));
                    } else if (keyCode == KeyEvent.KEYCODE_ENTER){
                        removeFocus(etPrice);
                    } else {
                        String keyTyped = String.valueOf((keyCode - KeyEvent.KEYCODE_0));
                        etPrice.setText(putMask(removeMask(text)+keyTyped));
                    }
                }
                etPrice.setSelection(etPrice.getText().toString().length());
                return true;
            }

            private String putNecessaryZeros(String text){
                while (text.length()<3){
                    text = "0" + text;
                }
                return text;
            }

            private String removeUnnecessaryZeros(String text){
                while (text.length()>3){
                    if (text.startsWith("0")){
                        text = text.substring(1);
                    } else {
                        break;
                    }
                }
                return text;
            }

            private String putMask(String text){
                text = removeUnnecessaryZeros(text);
                return "R$ "+text.substring(0, text.length()-2)+","+text.substring(text.length()-2);
            }

            private String removeMask(String text){
                return text.replace("R$ ", "").replace(",", "");
            }
        });
    }

    private void removeFocus(EditText editText){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
    }

    private void instantiateViews(View view){
        etPrice = view.findViewById(R.id.et_price);
        sCategories = view.findViewById(R.id.s_category);
        etDescription = view.findViewById(R.id.et_description);
        bSave = view.findViewById(R.id.b_save);
    }

    private void createCategories(){
        listCategories.add("Categoria");
        listCategories.addAll(Arrays.asList(Categories.categories));
    }

    private void createSpinnerAdapter() {
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategories.setAdapter(adapter);
    }
}
