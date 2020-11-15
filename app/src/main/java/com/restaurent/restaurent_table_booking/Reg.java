package com.restaurent.restaurent_table_booking;

public class Reg {
    public static final String CusUserEmailkey="UserEmail";
    public static final String CusUserPasswordkey="UserPassword";
    public static final String RestUserEmailkey="bUserEmail";
    public static final String RestUserPasswordkey="bUserPassword";

    private String _Food_Id, _Food_Name, _Food_Price, _Food_Quantity, _Food_Image_Url, _Food_Category;//_Food_Id
    private String _Customer_Name,_Customer_Address,_Customer_UPI,_Customer_Mail_Id,_Customer_Phone;
    private String _Restaurant_Name,_Restaurant_Owner_Name,_Restaurant_Available_Seats,_Restaurant_Total_Seats,_Restaurant_Category,_Restaurant_Address,_Restaurant_UPI,
            _Restaurant_Mail_Id,_Restaurant_Phone;
    public Reg() {
    }
    public Reg(String cusname,String cusmail){
        if(cusname.trim().equals("")){
            cusname="no customer Name";
        }
        if(cusmail.trim().equals("")){
            cusmail="no customer email";
        }
        _Customer_Name=cusname;
        _Customer_Mail_Id=cusmail;
    }
    public Reg(String foodid , String foodname,String foodcat, String foodprice, String foodqty, String foodimageurl){
        if(foodid.trim().equals("")){
            foodid="No Food Id";
        }
        if(foodname.trim().equals("")){
            foodname="No Food Name";
        }
        if(foodprice.trim().equals("")){
            foodprice="No Food Price";
        }
        if(foodqty.trim().equals("")){
            foodqty="No Food Quantity";
        }
        if(foodimageurl.trim().equals("")){
            foodimageurl="No Food Image Url";
        }
        if(foodcat.trim().equals("")){
            foodcat="No Food Category";
        }
        _Food_Id=foodid;
        _Food_Name=foodname;
        _Food_Price=foodprice;
        _Food_Quantity=foodqty;
        _Food_Image_Url=foodimageurl;
        _Food_Category=foodcat;
    }
    public Reg(String cusname , String cusadd, String cusupi, String cusmail, String cusphone){
        if(cusname.trim().equals("")){
            cusname="no customer Name";
        }
        if(cusadd.trim().equals("")){
            cusadd="no customer address";
        }
        if(cusupi.trim().equals("")){
            cusupi="no customer upi id";
        }
        if(cusmail.trim().equals("")){
            cusmail="no customer email";
        }
        if(cusphone.trim().equals("")){
            cusphone="no customer phone";
        }
        _Customer_Name=cusname;
        _Customer_Address=cusadd;
        _Customer_UPI=cusupi;
        _Customer_Mail_Id=cusmail;
        _Customer_Phone=cusphone;
    }
    public Reg(String resname,String resownname,String resseats,String restotalseats,String rescategory, String resadd, String resupi, String resmail, String resphone){


        if(resname.trim().equals("")){
                resname="no Restaurant Name";
        }
        if(resownname.trim().equals("")){
            resownname="no Restaurant Owner Name";
        }
        if(resseats.trim().equals("")){
            resseats="no Seats ";
        }
        if(restotalseats.trim().equals("")){
            restotalseats="no Seats ";
        }
        if(rescategory.trim().equals("")){
            rescategory="no Restaurant Category";
        }
        if(resadd.trim().equals("")){
            resadd="no Restaurant address";
        }
        if(resupi.trim().equals("")){
            resupi="no Restaurant upi id";
        }
        if(resmail.trim().equals("")){
            resmail="no Restaurant email";
        }
        if(resphone.trim().equals("")){
            resphone="no Restaurant phone";
        }
        _Restaurant_Name=resname;
        _Restaurant_Owner_Name=resownname;
        _Restaurant_Available_Seats=resseats;
        _Restaurant_Total_Seats=restotalseats;
        _Restaurant_Category=rescategory;
        _Restaurant_Address=resadd;
        _Restaurant_UPI=resupi;
        _Restaurant_Mail_Id=resmail;
        _Restaurant_Phone=resphone;
    }

    public String get_Restaurant_Available_Seats() {
        return _Restaurant_Available_Seats;
    }

    public void set_Restaurant_Available_Seats(String _Restaurant_Available_Seats) {
        this._Restaurant_Available_Seats = _Restaurant_Available_Seats;
    }

    public String get_Restaurant_Total_Seats() {
        return _Restaurant_Total_Seats;
    }

    public void set_Restaurant_Total_Seats(String _Restaurant_Total_Seats) {
        this._Restaurant_Total_Seats = _Restaurant_Total_Seats;
    }

    public String get_Food_Category() {
        return _Food_Category;
    }

    public void set_Food_Category(String _Food_Category) {
        this._Food_Category = _Food_Category;
    }
    public String get_Food_Id() {
        return _Food_Id;
    }

    public void set_Food_Id(String _Food_Id) {
        this._Food_Id = _Food_Id;
    }

    public String get_Food_Name() {
        return _Food_Name;
    }

    public void set_Food_Name(String _Food_Name) {
        this._Food_Name = _Food_Name;
    }

    public String get_Food_Price() {
        return _Food_Price;
    }

    public void set_Food_Price(String _Food_Price) {
        this._Food_Price = _Food_Price;
    }

    public String get_Food_Quantity() {
        return _Food_Quantity;
    }

    public void set_Food_Quantity(String _Food_Quantity) {
        this._Food_Quantity = _Food_Quantity;
    }

    public String get_Food_Image_Url() {
        return _Food_Image_Url;
    }

    public void set_Food_Image_Url(String _Food_Image_Url) {
        this._Food_Image_Url = _Food_Image_Url;
    }

    public String get_Restaurant_Name() {
        return _Restaurant_Name;
    }

    public void set_Restaurant_Name(String _Restaurant_Name) {
        this._Restaurant_Name = _Restaurant_Name;
    }

    public String get_Restaurant_Owner_Name() {
        return _Restaurant_Owner_Name;
    }

    public void set_Restaurant_Owner_Name(String _Restaurant_Owner_Name) {
        this._Restaurant_Owner_Name = _Restaurant_Owner_Name;
    }

    public String get_Restaurant_Category() {
        return _Restaurant_Category;
    }

    public void set_Restaurant_Category(String _Restaurant_Category) {
        this._Restaurant_Category = _Restaurant_Category;
    }

    public String get_Restaurant_Address() {
        return _Restaurant_Address;
    }

    public void set_Restaurant_Address(String _Restaurant_Address) {
        this._Restaurant_Address = _Restaurant_Address;
    }

    public String get_Restaurant_UPI() {
        return _Restaurant_UPI;
    }

    public void set_Restaurant_UPI(String _Restaurant_UPI) {
        this._Restaurant_UPI = _Restaurant_UPI;
    }

    public String get_Restaurant_Mail_Id() {
        return _Restaurant_Mail_Id;
    }

    public void set_Restaurant_Mail_Id(String _Restaurant_Mail_Id) {
        this._Restaurant_Mail_Id = _Restaurant_Mail_Id;
    }

    public String get_Restaurant_Phone() {
        return _Restaurant_Phone;
    }

    public void set_Restaurant_Phone(String _Restaurant_Phone) {
        this._Restaurant_Phone = _Restaurant_Phone;
    }

    public String get_Customer_Name() {
        return _Customer_Name;
    }
    public void set_Customer_Name(String _Customer_Name) {
        this._Customer_Name = _Customer_Name;
    }
    public String get_Customer_Address() {
        return _Customer_Address;
    }
    public void set_Customer_Address(String _Customer_Address) {
        this._Customer_Address = _Customer_Address;
    }
    public String get_Customer_UPI() {
        return _Customer_UPI;
    }
    public void set_Customer_UPI(String _Customer_UPI) {
        this._Customer_UPI = _Customer_UPI;
    }
    public String get_Customer_Mail_Id() {
        return _Customer_Mail_Id;
    }
    public void set_Customer_Mail_Id(String _Customer_Mail_Id) {
        this._Customer_Mail_Id = _Customer_Mail_Id;
    }

    public String get_Customer_Phone() {
        return _Customer_Phone;
    }
    public void set_Customer_Phone(String _Customer_Phone) {
        this._Customer_Phone = _Customer_Phone;
    }
}

