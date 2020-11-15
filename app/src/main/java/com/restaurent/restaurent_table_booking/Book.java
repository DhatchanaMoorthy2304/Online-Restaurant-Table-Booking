package com.restaurent.restaurent_table_booking;

public class Book {

    private String _Booked_Customer_Name,_Booked_Customer_Email,_Booked_Customer_Phone,_Total_Seats,_Booked_Date,_Booked_Time,_Amount_Paid,_Amount_Status,_Status;
    private String _Current_Date_at_the_Time_of_Booking_Food;
    private String _Current_Time_at_the_Time_of_Booking_Food;
    private String _Charged;
    private String _Refunded;
    private String _Charge_Status;
    private String _Request_Date,_Request_Time,_Return_Date,_Return_Time;

    public Book(){

    }
    public Book(String resname,String resmail){
        if (resname.trim().equals("")) {
            resname = "No Restaurant Name";
        }
        if (resmail.trim().equals("")) {
            resmail = "No Restaurant Mail";
        }
        _Restaurant_Mail_Id=resmail;
        _Restaurant_Name=resname;
    }
    public Book(String resname,String resmail,String bookedcusname, String bookedcusmail, String bookedcusphone, String bookedtotalseats,
                String bookeddate, String bookedtime, String amountpaid,String amountstatus,String cdate,String ctime,String status,
                String charge,String charged,String refunded,String reqdate,String reqtime,String retdate,String rettime) {
        if (bookedcusname.trim().equals("")) {
            bookedcusname = "No Booked Customer Name";
        }
        if (bookedcusmail.trim().equals("")) {
            bookedcusmail = "No Booked Customer email";
        }
        if (bookedcusphone.trim().equals("")) {
            bookedcusphone = "No Booked Customer phone";
        }
        if (bookedtotalseats.trim().equals("")) {
            bookedtotalseats = "No Seats";
        }
        if (bookeddate.trim().equals("")) {
            bookeddate = "No Booked Date";
        }
        if (bookedtime.trim().equals("")) {
            bookedtime = "No Booked Time";
        }
        if (amountpaid.trim().equals("")) {
            amountpaid = "No Amount Paid";
        }
        if (cdate.trim().equals("")) {
            cdate = "No Current Date";
        }
        if (ctime.trim().equals("")) {
            ctime = "No Current Time";
        }
        if (status.trim().equals("")) {
            status = "No Current Time";
        }
        if (resname.trim().equals("")) {
            resname = "No Restaurant Name";
        }
        if (resmail.trim().equals("")) {
            resmail = "No Restaurant Mail";
        }
        if(charge.trim().equals("")){
            charge="No";
        }
        if(charged.trim().equals("")){
            charged="No";
        }
        if(refunded.trim().equals("")){
            refunded="No";
        }
        if(reqdate.trim().equals("")){
            reqdate="No";
        }
        if(reqtime.trim().equals("")){
            reqtime="No";
        }
        if(retdate.trim().equals("")){
            retdate="No";
        }
        if(rettime.trim().equals("")){
            rettime="No";
        }
        _Request_Date=reqdate;
        _Request_Time=reqtime;
        _Return_Date=retdate;
        _Return_Time=rettime;
        _Charged=charged;
        _Refunded=refunded;
        _Charge_Status=charge;
        _Restaurant_Mail_Id=resmail;
        _Restaurant_Name=resname;
        _Amount_Paid=amountpaid;
        _Amount_Status=amountstatus;
        _Booked_Customer_Email=bookedcusmail;
        _Booked_Customer_Name=bookedcusname;
        _Booked_Customer_Phone=bookedcusphone;
        _Booked_Date=bookeddate;
        _Booked_Time=bookedtime;
        _Total_Seats=bookedtotalseats;
        _Current_Date_at_the_Time_of_Booking_Food=cdate;
        _Current_Time_at_the_Time_of_Booking_Food=ctime;
        _Status=status;
    }
    public String get_Request_Date() {
        return _Request_Date;
    }

    public void set_Request_Date(String _Request_Date) {
        this._Request_Date = _Request_Date;
    }

    public String get_Request_Time() {
        return _Request_Time;
    }

    public void set_Request_Time(String _Request_Time) {
        this._Request_Time = _Request_Time;
    }

    public String get_Return_Date() {
        return _Return_Date;
    }

    public void set_Return_Date(String _Return_Date) {
        this._Return_Date = _Return_Date;
    }

    public String get_Return_Time() {
        return _Return_Time;
    }

    public void set_Return_Time(String _Return_Time) {
        this._Return_Time = _Return_Time;
    }
    public String get_Charge_Status() {
        return _Charge_Status;
    }

    public void set_Charge_Status(String _Charge_Status) {
        this._Charge_Status = _Charge_Status;
    }

    public String get_Charged() {
        return _Charged;
    }

    public void set_Charged(String _Charged) {
        this._Charged = _Charged;
    }

    public String get_Refunded() {
        return _Refunded;
    }

    public void set_Refunded(String _Refunded) {
        this._Refunded = _Refunded;
    }


    private String _Restaurant_Name,_Restaurant_Mail_Id;
    public String get_Restaurant_Mail_Id() {
        return _Restaurant_Mail_Id;
    }

    public void set_Restaurant_Mail_Id(String _Restaurant_Mail_Id) {
        this._Restaurant_Mail_Id = _Restaurant_Mail_Id;
    }

    public String get_Restaurant_Name() {
        return _Restaurant_Name;
    }

    public void set_Restaurant_Name(String _Restaurant_Name) {
        this._Restaurant_Name = _Restaurant_Name;
    }

    public String get_Status() {
        return _Status;
    }

    public void set_Status(String _Status) {
        this._Status = _Status;
    }

    public String get_Current_Date_at_the_Time_of_Booking_Food() {
        return _Current_Date_at_the_Time_of_Booking_Food;
    }

    public void set_Current_Date_at_the_Time_of_Booking_Food(String _Current_Date_at_the_Time_of_Booking_Food) {
        this._Current_Date_at_the_Time_of_Booking_Food = _Current_Date_at_the_Time_of_Booking_Food;
    }

    public String get_Current_Time_at_the_Time_of_Booking_Food() {
        return _Current_Time_at_the_Time_of_Booking_Food;
    }

    public void set_Current_Time_at_the_Time_of_Booking_Food(String _Current_Time_at_the_Time_of_Booking_Food) {
        this._Current_Time_at_the_Time_of_Booking_Food = _Current_Time_at_the_Time_of_Booking_Food;
    }

    public String get_Amount_Status() {
        return _Amount_Status;
    }

    public void set_Amount_Status(String _Amount_Status) {
        this._Amount_Status = _Amount_Status;
    }

    public String get_Booked_Customer_Phone() {
        return _Booked_Customer_Phone;
    }

    public void set_Booked_Customer_Phone(String _Booked_Customer_Phone) {
        this._Booked_Customer_Phone = _Booked_Customer_Phone;
    }

    public String get_Booked_Customer_Name() {
        return _Booked_Customer_Name;
    }

    public void set_Booked_Customer_Name(String _Booked_Customer_Name) {
        this._Booked_Customer_Name = _Booked_Customer_Name;
    }

    public String get_Booked_Customer_Email() {
        return _Booked_Customer_Email;
    }

    public void set_Booked_Customer_Email(String _Booked_Customer_Email) {
        this._Booked_Customer_Email = _Booked_Customer_Email;
    }


    public String get_Total_Seats() {
        return _Total_Seats;
    }

    public void set_Total_Seats(String _Total_Seats) {
        this._Total_Seats = _Total_Seats;
    }

    public String get_Booked_Date() {
        return _Booked_Date;
    }

    public void set_Booked_Date(String _Booked_Date) {
        this._Booked_Date = _Booked_Date;
    }

    public String get_Booked_Time() {
        return _Booked_Time;
    }

    public void set_Booked_Time(String _Booked_Time) {
        this._Booked_Time = _Booked_Time;
    }

    public String get_Amount_Paid() {
        return _Amount_Paid;
    }

    public void set_Amount_Paid(String _Amount_Paid) {
        this._Amount_Paid = _Amount_Paid;
    }
}
