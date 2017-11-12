
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class KeyPairCreator 
{
    private PublicKey PubKey ;
    private PrivateKey PrivKey ;
    
    public KeyPairCreator ( ) throws NoSuchAlgorithmException
    {
        KeyPairGenerator OBJECT_KEY = null ;
        KeyPair KEY_PAIR = null ;
        OBJECT_KEY = KeyPairGenerator.getInstance( "RSA" ) ;
        OBJECT_KEY.initialize ( 2048 ) ; 
        KEY_PAIR = OBJECT_KEY.genKeyPair ( ) ; 
        this.PrivKey = KEY_PAIR.getPrivate ( ) ; 
        this.PubKey = KEY_PAIR.getPublic ( ) ; 
    }
    
    public PublicKey GetPublicKey ( )
    {
        return this.PubKey ;
    }
    
    public PrivateKey GetPrivateKey ( )
    {
        return this.PrivKey ;
    }
}
