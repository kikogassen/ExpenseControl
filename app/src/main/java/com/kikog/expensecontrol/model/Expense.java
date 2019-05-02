package com.kikog.expensecontrol.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

@DatabaseTable(tableName = "expense")
public class Expense {

    @DatabaseField(columnName = "id", uniqueIndex = true, generatedId = true)
    private int id;

    @DatabaseField(columnName = "price")
    private float price;

    @DatabaseField(columnName = "category")
    private int category;

    @DatabaseField(columnName = "description")
    private String description;

    @DatabaseField(columnName = "date")
    private String date;

    public Expense(){}

    public Expense(float price, int category, String description, String date) {
        this.price = price;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static List<Expense> getAll(){
        try {
            Dao<Expense, Long> dao = DaoManager.createDao(DBHelper.getHelper(null).getConnectionSource(), Expense.class);
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Expense> getAllByMonth(String month){
        try {
            Dao<Expense, Long> dao = DaoManager.createDao(DBHelper.getHelper(null).getConnectionSource(), Expense.class);
            return dao.queryBuilder().where().like("date", "%/"+month+"/%").query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearDatabase(){
        try {
            Dao<Expense, Long> dao = DaoManager.createDao(DBHelper.getHelper(null).getConnectionSource(), Expense.class);
            dao.queryRaw("DELETE FROM expense");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createExpense(){
        try {
            Dao<Expense, Integer> dao = DaoManager.createDao(DBHelper.getHelper(null).getConnectionSource(), Expense.class);
            dao.create(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
