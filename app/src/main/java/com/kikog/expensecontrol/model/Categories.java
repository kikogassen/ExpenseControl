package com.kikog.expensecontrol.model;

public class Categories {
    public static String[] categories = {
        "RU", "Mercado", "Farmácia", "Lancheria", "ATU", "Festas", "Outros"
    };

    public static int indexOf(String entry){
        for (int i=0;i<categories.length;i++){
            if (categories[i].equals(entry)){
                return i;
            }
        }
        return -1;
    }
}
