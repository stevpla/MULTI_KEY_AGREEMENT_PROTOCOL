package Encapsulation ;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class PublicPrivateKeys 
{
    private PublicKey PubKey ;
    private PrivateKey PrivKey ;
    private static KeyStore keyStore ;  //not every object, but create JKS once at  memoryFileOutputStream OUTPUT_STREAM = null ;
    private ObjectOutputStream OUTPUT_STREAM_2 = null ;
    private FileOutputStream OUTPUT_STREAM = null ;
    
    
    public PublicPrivateKeys ( ) throws NoSuchAlgorithmException
    {
        KeyPairGenerator OBJECT_KEY = null ;
        KeyPair KEY_PAIR = null ;
        OBJECT_KEY = KeyPairGenerator.getInstance( "RSA" ) ;
        OBJECT_KEY.initialize ( 2048 ) ; 
        KEY_PAIR = OBJECT_KEY.genKeyPair ( ) ; 
        this.PrivKey = KEY_PAIR.getPrivate ( ) ; 
        this.PubKey = KEY_PAIR.getPublic ( ) ; 
        //Write them to file
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
    
    public PublicPrivateKeys ( PublicKey pk )
    {
        final String jksFileName = "JKSSpace.jks" ;
        final String FolderName = "USERS" ;
        final String jksPassword = "password" ;
        try
        {
            keyStore = KeyStore.getInstance( "JKS" ) ;
            keyStore.load ( null, jksPassword.toCharArray ( ) ) ;  //create empty JKS
            //pk to bytes to save it to JKS
            byte [ ] ar = pk.getEncoded ( ) ; 
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(ar);

            //keyStore.setKeyEntry ( "PUBLIC_KEY", privateKeySpec, null ) ;
            //Create JKS to /JKSSpace.jks
            FileOutputStream Fostm = new FileOutputStream ( "JKSSpace.jks" ) ;
            keyStore.store ( Fostm, jksPassword.toCharArray ( ) ) ;
            Fostm.close ( ) ;
        }
        catch ( IOException IOE )
        {
            System.out.println ("Error 1" ) ;
        }
        catch ( NoSuchAlgorithmException NSAE )
        {
            System.out.println ("Error 2" ) ;
        }
        catch ( CertificateException CE )
        {
            System.out.println ("Error 3" ) ;
        }
        catch ( KeyStoreException KSE )
        {
            System.out.println ("4 error -> "+ KSE.getMessage ( ) ) ;
        }
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
