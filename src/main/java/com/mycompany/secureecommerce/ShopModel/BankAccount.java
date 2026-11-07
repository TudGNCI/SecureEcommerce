/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce.ShopModel;

/**
 *
 * @author Alexb
 */
public class BankAccount {
    private String accountNumber;
    private String ownerUserId;
    private String paymentTokenHash; 
    private double balance;


    public BankAccount(String accountNumber, String ownerUserId, String paymentTokenHash, double balance){
        this.accountNumber = accountNumber;
        this.ownerUserId = ownerUserId;
        this.paymentTokenHash = paymentTokenHash;
        this.balance = balance;
    }
    
    
    public String getAccountNumber() {
       return accountNumber;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public double getBalance() {
        return balance;
    }
        
        public String getPaymentTokenHash() {
            return paymentTokenHash;
        }
        
        
        
        // debits users bank balance if the have enough money and netered positive value
        public boolean debit(double amount) {
            if(amount <= balance && amount > 0){
                balance -= amount;
                return true;
                 
          }   
            
                return false;
             }   
        
        
         
        

}