/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.secureecommerce;


import com.mycompany.secureecommerce.Service.BankService;
import com.mycompany.secureecommerce.Service.SecurePayments;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

/**
 *
 * @author Alexb
 */
    public class PaymentServer {
    
    private int port;
    private BankService bankService;
    private static ServerSocket serverSocket;

    
    PaymentServer(int port, BankService bankService){
        this.port = port;
        this.bankService = bankService;
    }
    
    
         
    
    
     public static void main(String[] args) {
            try{
                
        // Both programs must load the same AES key.
        SecretKey key = SecurePayments.loadSharedKey();

        SecurePayments securePayments = new SecurePayments(key);
        BankService bankService = new BankService(securePayments);

        PaymentServer server = new PaymentServer(5000, bankService);

        System.out.println("Starting payment server on port 5000...");
        server.startServer();

    }catch(Exception e){
        System.err.println("Payment server failed: " + e.getMessage());
    }
       
       
    }
     
     
     // starts connection
     public void startServer() {
         
        System.out.println("Opening port...");

        
        try{
            serverSocket = new ServerSocket(port);
            
            while(true){
                
             Socket link = serverSocket.accept();
                 handleConnection(link);
                 
            }
           
                  

        }catch(IOException e){
            System.out.println("Server error: " + e.getMessage());
        }}
            
            
     // request and respopnse 
     private void handleConnection(Socket socket) {
        
         
         try{
          Socket link = socket;
         
         BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream(),StandardCharsets.UTF_8));
         PrintWriter out = new PrintWriter(new OutputStreamWriter(link.getOutputStream(),StandardCharsets.UTF_8), true);
         
        String request = in.readLine();

        String response = bankService.ProcessPayment(request);

        out.println(response);
        link.close();
        
         }catch(IOException e){
             System.out.println("Connection error: " + e.getMessage());
         }
       
    }
     
}

