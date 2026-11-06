/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce.Service;
import com.mycompany.secureecommerce.ShopModel.BankAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexb
 */
public class BankService {
    
    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
    
    
    // hash map which stores response and transaction ID 
    private Map<String, String> processedTransaction = new HashMap<>();
    
    
    
    private SecurePayments securePayments;
    

        public BankService(SecurePayments securePayments) {
         this.securePayments = securePayments;

            accounts.add(new BankAccount("ACC001", "USER001",
                SecurePayments.hashPassword("DemoToken1"), 100.00));

            accounts.add(new BankAccount("ACC002", "USER002",
                SecurePayments.hashPassword("DemoToken2"), 220.00));

            accounts.add(new BankAccount("ACC003", "USER003",
                SecurePayments.hashPassword("DemoToken3"), 300.00));

            accounts.add(new BankAccount("ACC004", "USER004",
               SecurePayments.hashPassword("DemoToken4"), 422.00));

            accounts.add(new BankAccount("ACC005", "USER005",
               SecurePayments.hashPassword("DemoToken5"), 275.00));
                }
    
    
    
    public BankAccount FindAccountByNumber(String accountNumber) {
          for (BankAccount b : accounts) {
             if (accountNumber.equals(b.getAccountNumber())) {
                return b;
                }
          }
             return null;
         }
    
    
    public synchronized String ProcessPayment(String envelope){
        
        String request = securePayments.decrypt(envelope);
        System.out.println("[BANK] Request decrypted and integrity verified.");
        
        // store the request details split in fields 
        String[] fields = request.split(";", -1 );
        
        if(fields.length != 6){
            
            String response =  "Invalid payment request";
                return securePayments.encrypt(response);
        }
            
        
        // store payment details once decrypted 
            String transactionId = fields[0].trim();
            String userId = fields[1].trim();
            String productId = fields[2].trim();
            String accountNumber = fields[3].trim();
            String paymentToken = fields[4];
            double amount = Double.parseDouble(fields[5].trim());
        
        
        BankAccount b = FindAccountByNumber(accountNumber);
        
        
        
        // if the user is found and payment token they entered and account payment token match 
        if(b != null && b.getOwnerUserId().equals(userId) ){
            if(securePayments.verifyToken(paymentToken, b.getPaymentTokenHash())){
                
                // checks for duplicate transactionId
            if(processedTransaction.containsKey(transactionId)){
                 System.out.println("[BANK] Duplicate transaction: returning saved result.");
                 return securePayments.encrypt(processedTransaction.get(transactionId));
                }
                
            if( b.debit(amount)){
                
                  // store trasnaction outcome
            System.out.println("[BANK] Payment approved. Amount debited: " + amount);
            System.out.println("[BANK] Remaining balance: " + b.getBalance());
            String response = transactionId + ";" + amount + ";APPROVED;Payment successful"; 
            
            processedTransaction.put(transactionId, response);
            
            // returns encrypted string 
            return securePayments.encrypt(response);
                       
            
        } else {
                
                System.out.println("[BANK] Payment declined: insufficient funds.");
                String response =  transactionId + ";" + amount + ";DECLINED;Insufficient funds";
                processedTransaction.put(transactionId, response);
                return securePayments.encrypt(response);
                
            }
          }
        }
        
        System.out.println("[BANK] Payment declined: invalid credentials.");
        String response =  transactionId + ";" + amount + ";DECLINED;Invalid payment credentials";
        return securePayments.encrypt(response);
        
    }
    
}
