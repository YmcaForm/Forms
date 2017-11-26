package com.ymcaforms.ymcaforms;

/**
 * Created by Himanshu on 11/19/2017.
 */
public class Forms {

    String name,description;

    public Forms()
    {

    }

    public Forms(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
