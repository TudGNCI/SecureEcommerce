/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce.Service;
import java.util.ArrayList;
import com.mycompany.secureecommerce.ShopModel.User;

/**
 *
 * @author Alexb
 */
public class UserManager {
    
    private ArrayList<User> userList = new ArrayList<User>();
    private User currentUser;
    SecurePayments SecurePayments;
    
    public UserManager(){

        userList.add(new User("USER001", "demo1",
            SecurePayments.hashPassword("DemoPass1")));

        userList.add(new User("USER002", "demo2",
            SecurePayments.hashPassword("DemoPass2")));

        userList.add(new User("USER003", "demo3",
            SecurePayments.hashPassword("DemoPass3")));

        userList.add(new User("USER004", "demo4",
            SecurePayments.hashPassword("DemoPass4")));

        userList.add(new User("USER005", "demo5",
            SecurePayments.hashPassword("DemoPass5")));
    }
    
    
    
    public boolean registerUser(String name, String password) {
        if (SearchUserByName(name) != null) {
            return false;
           }
            
        
        // give random string to userId and hash password
            String userId = java.util.UUID.randomUUID().toString();
            
            String passwordHash = SecurePayments.hashPassword(password);

                User u = new User(userId, name, passwordHash);
                    userList.add(u);

                return true;
        }
    
    
        public boolean login(String name, String password){
            
           User user = SearchUserByName(name);
                     
            // if the user is found and password is correct log in 
            if(user != null && SecurePayments.verifyPassword(password, user.getPasswordHash())){
                currentUser = user;
                return true;
                
            }   return false;  
   
        }
        
        
        
        public void logout(){
            
            currentUser = null;
        }
        
        public User getCurrentUser(){
            
            return currentUser;
        }

    

        public User SearchUserByName(String name) {
          for (User u : userList) {
             if (name.equals(u.getName())) {
                return u;
                }
          }
             return null;
         }
        
        public User SearchUserByID(String id) {
          for (User u : userList) {
             if (id.equals(u.getUserId())) {
                return u;
                }
          }
             return null;
         }
        
        
        
        

}