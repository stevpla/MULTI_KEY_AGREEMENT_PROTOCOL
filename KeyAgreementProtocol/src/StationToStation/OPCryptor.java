/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author LU$er
 */
public class OPCryptor 
{
    private SecretKey sk ;
    
    public OPCryptor ( SecretKey sk )
    {
        this.sk = sk ;
    }
    
    public byte [ ] DecryptEncryptedSignature ( byte [ ] EncrpdSgntr ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance ( "DES" ) ; 
        cipher.init ( Cipher.DECRYPT_MODE, this.sk ) ;
        return cipher.doFinal ( EncrpdSgntr ) ;
    }
    
    public byte [ ] EncryptSignature ( byte [ ] EncrpdSgntr ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance ( "DES" ) ; 
        cipher.init ( Cipher.ENCRYPT_MODE, this.sk ) ;
        return cipher.doFinal ( EncrpdSgntr ) ;
    }
}
