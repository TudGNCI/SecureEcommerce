/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce.ShopModel;

/**
 *
 * @author Alexb
 */
public class Transaction {
    private String transactionId;
    private String userId;
    private String productId;
    private double amount;
    private String status;
    
    public Transaction(String userId, String productId, double amount){
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
        this.status = "PENDING";
        this.transactionId = java.util.UUID.randomUUID().toString();
    }
    
    public String getTransactionId() {
         return transactionId;
    }

    public String getUserId() {
          return userId;
    }

    public String getProductId() {
             return productId;
    }

    public double getAmount() {
         return amount;
    }

        public String getStatus() {
              return status;
        }
        
        
        // update Transaction status
    public void updateStatus(String newStatus) {
         this.status = newStatus;
    }
    
    
}
