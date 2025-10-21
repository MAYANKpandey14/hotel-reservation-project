package model;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;


    public Customer(String firstName,String lastName, String email){
        String Email_regex="^(.+)@(.+)[.](.+)$";
        Pattern pattern =Pattern.compile(Email_regex);
        if(email==null||email.trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty.\nPlease provide valid Email address, for ex- johndoe@gmail.com");
        }
        if(!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Invalid Email Format: "+email+"\nExpected Format:name@domain.extension, ex-johndoe@gmail.com");
        }
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String toString(){
        return "Name: "+firstName+" "+lastName+"\nEmail: "+email;
    }
}
