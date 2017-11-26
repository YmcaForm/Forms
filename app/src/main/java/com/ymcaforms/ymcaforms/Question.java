package com.ymcaforms.ymcaforms;
public class Question {

    //private variables
    int _id;
    String _question;
    String _type;
    int count;
    String options;


    // Empty constructor
    public Question(){

    }
    // constructor
    public Question(int id, String question, String type, int count, String options){
        this._id = id;
        this._question = question;
        this._type = type;
        this.count=count;
        this.options=options;
    }

    // constructor
    public Question(String question, String type){
        this._question = question;
        this._type = type;
    }

    public Question(String question, String type, int count, String options){
        this._question = question;
        this._type = type;
        this.count=count;
        this.options=options;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getQuestion(){
        return this._question;
    }

    // setting name
    public void setQuestion(String question){
        this._question = question;
    }

    // getting phone number
    public String get_type(){
        return this._type;
    }

    // setting phone number
    public void set_type(String type){
        this._type = type;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
