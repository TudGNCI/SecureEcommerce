/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.secureecommerce.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;



/**
 *
 * @author Alexb
 */
public class SecurePayments {
    
    private SecretKey aesKey;
    
    
    public  SecurePayments(SecretKey aesKey){
        this.aesKey = aesKey;
    }
    
    
    
    public static String hashPassword(String password){
      // Generate a salt and return the complete encoded bcrypt hash.
        return BCrypt.withDefaults()
            .hashToString(12, password.toCharArray());

    }
                       
    
     public static boolean verifyPassword(String password, String storedHash){
      // Check the password using the salt and settings in storedHash.
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedHash);

            return result.verified;
            
        }
            
    
     
     
      public static boolean verifyToken(String token, String storedTokenHash){
                        return verifyPassword(token, storedTokenHash);
      }
      
     
         public String encrypt(String message){
         
             try{
                    
                 byte[] randomByte = generateNonce();

                    // choose new cipher AES/GCM
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    
                    // encryption settings with tag to prevent tampering 
                    // note Decrypt() needs the same AES key and the nonce used for encrypt()
                    cipher.init(Cipher.ENCRYPT_MODE,aesKey, new GCMParameterSpec(128, randomByte));
    
                    
                    
                    // encrypts message to bytes
                    byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
                    
                      // Package the nonce and encrypted data for sending.
                     String nonceText = Base64.getEncoder().encodeToString(randomByte);
                     String encryptedText = Base64.getEncoder().encodeToString(encrypted);

                             return nonceText + ":" + encryptedText;
                 
             }catch(GeneralSecurityException e){
                  throw new IllegalStateException("Encryption failed.", e);
                 
             }
             
         
         }
            
        public String decrypt(String message){
        
            try{
                    
                 String[] parts = message.split(":", -1);
                 
                 
                 if(parts.length != 2 ){
                     
                       throw new IllegalArgumentException("Invalid encrypted message.");
                 }
                 
                    // decode string 
                    byte[] nonce = Base64.getDecoder().decode(parts[0]);
                    byte[] encryptedMessage = Base64.getDecoder().decode(parts[1]);
                 
                 
                    // create decryption 
                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    cipher.init( Cipher.DECRYPT_MODE,aesKey, new GCMParameterSpec(128, nonce));
                          
                        
                        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
                        
                         return new String(decryptedMessage, StandardCharsets.UTF_8);
                 
             }catch(GeneralSecurityException e){
                  throw new IllegalStateException("Decryption failed.", e);
                 
             }
        }
    
     
      
      // generate random 12 bytes 
     public byte[] generateNonce(){ 
         
        byte[] nonce = new byte[12];

        SecureRandom random = new SecureRandom();
        random.nextBytes(nonce);

            return nonce;
     }
    
}
