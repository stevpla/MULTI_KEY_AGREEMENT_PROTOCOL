package Encapsulation ;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class UniqueSecretKey 
{
    private SecretKey secretKey ;
    private byte [ ] secretBytesKey ;
    
    public UniqueSecretKey ( ) throws NoSuchAlgorithmException
    {
        SecureRandom randomno = new SecureRandom ( ) ;
        byte [ ] ByteA = new byte [ 30 ] ;
        byte [ ] ByteB = new byte [ 30 ] ;
        randomno.nextBytes ( ByteA ) ;
        randomno.nextBytes ( ByteB ) ;
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator ( ) ;
        gen.init ( ByteA, ByteB, 2000 ) ;
        this.secretBytesKey = ( ( KeyParameter ) gen.generateDerivedParameters ( 256 ) ).getKey ( ) ;  
    }
    
    public SecretKey GetSecretKey ( )
    {
        secretKey = new SecretKeySpec ( this.secretBytesKey, 0, this.secretBytesKey.length, "AES" ) ;
        return this.secretKey ;
    }
    
    public byte [ ] GetBytedSecretKey ( )
    {
        return this.secretBytesKey ;
    }
}
