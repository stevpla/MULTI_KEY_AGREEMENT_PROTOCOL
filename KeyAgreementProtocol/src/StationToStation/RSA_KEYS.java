/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.swing.JOptionPane;

/**
 *
 * @author LU$er
 */
public class RSA_KEYS 
{
    private PublicKey PubKey ;
    private PrivateKey PrivKey ;
    private ObjectOutputStream OUTPUT_STREAM_2 = null ;
    private FileOutputStream OUTPUT_STREAM = null ;
    
    public RSA_KEYS ( ) throws NoSuchAlgorithmException
    {
        KeyPairGenerator OBJECT_KEY = null ;
        KeyPair KEY_PAIR = null ;
        OBJECT_KEY = KeyPairGenerator.getInstance( "RSA" ) ;
        OBJECT_KEY.initialize ( 2048 ) ; 
        KEY_PAIR = OBJECT_KEY.genKeyPair ( ) ; 
        this.PrivKey = KEY_PAIR.getPrivate ( ) ; 
        this.PubKey = KEY_PAIR.getPublic ( ) ; 
        File file = new File ( "PublicPrivateKeyPair" ) ;
        file.mkdir ( ) ;
        try
        {
             OUTPUT_STREAM =  new FileOutputStream ( "PublicPrivateKeyPair\\PUBLIC_KEY.key" ) ;
             OUTPUT_STREAM_2 = new ObjectOutputStream ( OUTPUT_STREAM ) ;  // 1st Level of Stream, create Object OutputStream with FileOutputStream as parameter
             OUTPUT_STREAM_2.writeObject (  this.PubKey ) ;   //Wirte public key in file.data
             OUTPUT_STREAM =  new FileOutputStream ( "PublicPrivateKeyPair\\PRIVATE_KEY.key" ) ;
             OUTPUT_STREAM_2 = new ObjectOutputStream ( OUTPUT_STREAM ) ;  // 1st Level of Stream, create Object OutputStream with FileOutputStream as parameter
             OUTPUT_STREAM_2.writeObject (  this.PrivKey ) ;   //Wirte private key in file.data
        }
        catch ( FileNotFoundException F )
        {
               JOptionPane.showMessageDialog ( null , "Error -> " + F.getLocalizedMessage ( ), "Exception - FileNotFoundException", JOptionPane.ERROR_MESSAGE, null ) ;
        }
        catch ( IOException I )
        {
               JOptionPane.showMessageDialog ( null , "Error -> " + I.getLocalizedMessage ( ), "Exception - IOException", JOptionPane.ERROR_MESSAGE, null ) ;
        }
    }
    
    public PublicKey GetRSAPublicKey ( )
    {
        return this.PubKey ;
    }
    
    public PrivateKey GetRSAPrivateKey ( )
    {
        return this.PrivKey ;
    }
}
