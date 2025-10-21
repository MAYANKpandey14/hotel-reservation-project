package tester;

import model.Customer;

public class Driver {
    public static void main(String[] args){
        Customer customer=new Customer("John","Doe","johndoe@gmail.com");
        System.out.println(customer);
    }
}
