/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce;


import com.mycompany.secureecommerce.Service.SecurePayments;
import com.mycompany.secureecommerce.ShopModel.Transaction;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class PaymentClient {

    private String serverAddress;
    private int port;
    private SecurePayments securePayments;


         public PaymentClient(String serverAddress, int port,SecurePayments securePayments){
             
              this.serverAddress = serverAddress;
              this.port = port;
              this.securePayments = securePayments;
             }


                // Build request, encrypt, send, receive and decrypt response.
                public String sendPayment(Transaction transaction, String accountNumber, String token){
                        
                    
                        // build payment request
                    String request = transaction.getTransactionId() + ";" + transaction.getUserId() + ";" + transaction.getProductId() + ";" + 
                                     accountNumber + ";" + token + ";" + transaction.getAmount();

                        // Encrypt before sending.
                    String encryptedRequest = securePayments.encrypt(request);
                    
                            try(Socket link = new Socket(serverAddress, port)){
                                
                                PrintWriter out = new PrintWriter(new OutputStreamWriter(link.getOutputStream(), StandardCharsets.UTF_8), true);

                                out.println(encryptedRequest);
                                System.out.println("[CLIENT] Sent encrypted request: " + encryptedRequest);
                                
                                
                                    BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream(), StandardCharsets.UTF_8));
                                    
                                    
                                    // read and store  response
                                    String encryptedResponse = in.readLine();
                                    
                                    
                                    if(encryptedResponse == null){
                                        throw new IOException("Server closed without a response.");
                                    }

                                    // Decrypt the respone
                                    String response = securePayments.decrypt(encryptedResponse);
                                        
                                    // Check the reply matches this payment.
                                    if(!validateResponse(response, transaction)){
                                         throw new IllegalStateException("Invalid payment response.");
                                    }
                                    
                                    System.out.println("[CLIENT] Response decrypted and matched to transaction.");
                                    return response;
                                
                            }catch(IOException e){
                                       throw new IllegalStateException("Cannot connect", e);
                                
                            } 
                }


                // Check response transaction ID, amount and status.
                private boolean validateResponse(String response, Transaction transaction){
                        
                        String[] fields = response.split(";", -1);

                            if(fields.length != 4){
                              return false;
                            }
                            
                            String expectedAmount = Double.toString(transaction.getAmount());
                            
                            
                                if(fields[0].equals(transaction.getTransactionId())&& fields[1].equals(expectedAmount)&& (fields[2].equals("APPROVED") || fields[2].equals("DECLINED"))){
                                         return true;
                                }
                                    return false;
                                }
}
