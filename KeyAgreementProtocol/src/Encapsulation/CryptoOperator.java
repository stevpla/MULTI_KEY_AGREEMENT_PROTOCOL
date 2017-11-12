package Encapsulation ;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class CryptoOperator 
{
    private PublicKey pk ;
    private byte [ ] sk ;
    private SecretKey secretKey ;
    private PrivateKey privateKey ;
    
    public CryptoOperator ( PublicKey pk, byte [ ] sk ) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        this.pk = pk ;
        this.sk = sk ;
    }
    
    public CryptoOperator ( SecretKey secretkey, byte [ ] sk ) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        this.secretKey = secretkey ;
        this.sk = sk ;
    }
    
    public CryptoOperator ( PrivateKey privateKey, byte [ ] sk ) throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        this.privateKey = privateKey ;
        this.sk = sk ;
    }
    
    public byte [ ] EncryptSymmetricKeywithPublicKey ( ) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance ("RSA") ;
        cipher.init ( Cipher.ENCRYPT_MODE, this.pk ) ;
        return cipher.doFinal ( this.sk ) ;
    }
    
    public byte [ ] DecryptSymmetricKeywithPrivateKey ( ) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance ("RSA") ;
        cipher.init ( Cipher.DECRYPT_MODE, this.privateKey ) ;
        return cipher.doFinal ( this.sk ) ;
    }
    
    public byte [ ] DecryptObjectwithSecretKey ( ) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
        Cipher cipher = Cipher.getInstance ("AES") ;
        cipher.init ( Cipher.DECRYPT_MODE, this.secretKey ) ;
        return cipher.doFinal ( this.sk ) ;
    }
    
    public byte [ ] EncryptObjectwithSecretKey ( ) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance ("AES") ;
        cipher.init ( Cipher.ENCRYPT_MODE, this.secretKey ) ;
        return cipher.doFinal ( this.sk ) ;
    }
}
