package Encapsulation ;

import Main_.KindOfProtocol;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Encapsulation implements Runnable
{
    private Socket sock ;
    private ObjectInputStream OIS = null ;
    private ObjectOutputStream OOS = null ;
    
    public Encapsulation ( ) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, NoSuchProviderException 
    {   
        //Start this Client the Protocol
        Socket socketToAClient = null ;
        try
        {
        socketToAClient = new Socket ( "192.168.1.6", 8888 ) ;
        ObjectOutputStream OOS = new ObjectOutputStream ( socketToAClient.getOutputStream ( ) ) ;
        ObjectInputStream OIS = new ObjectInputStream ( socketToAClient.getInputStream ( ) ) ;
        //Send Init Protocol way
        OOS.writeObject ( new KindOfProtocol ("ENCAPSULATION") ) ;
        //Start Session
        //first load jks to take the certificate
        final String folderName = "JavaKeyStore" ;
        final String jksFileName = folderName + "\\"+ "Client.jks" ;
        final String jksPassword = "password" ;
        FileInputStream Fis = new FileInputStream ( jksFileName ) ;
        KeyStore ks = KeyStore.getInstance ( "JKS" ) ;
        ks.load ( Fis, jksPassword.toCharArray ( ) ) ;
        Fis.close ( ) ;
        X509Certificate ClientCERT = (X509Certificate) ks.getCertificate ("ClientCert") ;
        //
        OOS.writeObject ( new SerializableMessage ( "Hello",ClientCERT ) ) ; 
        SerializableMessage OtherClientCert = (SerializableMessage) OIS.readObject ( ) ;
        if ( OtherClientCert.GetMessage ( ).equals ( "Hello") )
        {   
            //Verify Client Cert. 2 steps
            //1st Step is to check if CA signed this Certificate. So open TrustStore and load CA cert
            FileInputStream fis = new FileInputStream ( "myTrustStore" ) ; //maybe .jks
            KeyStore TrustStore = KeyStore.getInstance ( "JKS" ) ;
            TrustStore.load ( fis, "changeit".toCharArray ( ) ) ;
            fis.close ( ) ;
            X509Certificate CACert = (X509Certificate) TrustStore.getCertificate ("CAcert") ;
            //So check if has a valid Date
            OtherClientCert.GetClientCertificate ( ).checkValidity ( ) ; //exception will be caught
            //Check if is signed by CA
            OtherClientCert.GetClientCertificate ( ).verify ( CACert.getPublicKey ( ), "BC" ) ;
            //----------------------------------------------------------------------------------
            //Extract Public Key of Client Cert
            PublicKey otherClientPublicKey = OtherClientCert.GetClientCertificate ( ).getPublicKey ( ) ;
            //Produce SecretKey  from AES-256 bit
            UniqueSecretKey secretKey = new UniqueSecretKey ( ) ;
            byte [ ] SKref = secretKey.GetBytedSecretKey ( ) ;
            SecretKey secretkey = secretKey.GetSecretKey ( ) ;
            //Now encrypt the secretKey with Public Key of Alice
            byte [ ] encryptedSymmetricKey = new CryptoOperator ( otherClientPublicKey, SKref ).EncryptSymmetricKeywithPublicKey ( ) ;
            OOS.writeObject ( new SerializableMessage ( encryptedSymmetricKey ) ) ;
            //Now read an encrypted message with the Symmetric key as an ACK
            SerializableMessage encryptedMessage_SymmetricKey = ( SerializableMessage ) OIS.readObject ( ) ;
            //Decrypt it
            byte [ ] encryptedMessage = encryptedMessage_SymmetricKey.GetEncyptedKey ( ) ;
            byte [ ] ClearMessage = new CryptoOperator ( secretkey, encryptedMessage ).DecryptObjectwithSecretKey ( ) ; 
            OIS.close ( ) ;
            OOS.close ( ) ;
            socketToAClient.close ( ) ;
            JOptionPane.showMessageDialog ( null, "ACK from Alice is -> " + new String ( ClearMessage ).toString ( ) ) ;
        }
        else
        {
            //Close
            OIS.close ( ) ;
            OOS.close ( ) ;
            socketToAClient.close ( ) ;
        }
        }
        catch ( CertificateException ce )
        {
            //Close
            OIS.close ( ) ;
            OOS.close ( ) ;
            socketToAClient.close ( ) ;
        }
        catch ( SignatureException se )
        {
            //Close
            OIS.close ( ) ;
            OOS.close ( ) ;
            socketToAClient.close ( ) ;
        }
    }
    
    public Encapsulation ( Socket sock, ObjectOutputStream OOS, ObjectInputStream OIS )
    {
        this.sock = sock ;
        this.OOS = OOS ;
        this.OIS = OIS ;
    }
    
    public void run ( )  //Incoming message from someone
    {
        final String folderName = "JavaKeyStore" ;
        final String jksFileName = folderName + "\\" + "Client.jks" ;
        final String jksPassword = "password" ;
        
        try
        {
            FileInputStream Fis = new FileInputStream ( jksFileName ) ;
            KeyStore ks = KeyStore.getInstance ( "JKS") ;
            ks.load ( Fis, jksPassword.toCharArray ( ) ) ;
            Fis.close ( ) ;
            X509Certificate ClientCERT = (X509Certificate) ks.getCertificate ("ClientCert") ;
            PrivateKey PRIV_KEY = (PrivateKey) ks.getKey ( "PrivateKeyClient", jksPassword.toCharArray ( ) ) ;
            //========================================================================
            SerializableMessage message = (SerializableMessage) this.OIS.readObject ( ) ;
            String stringMessage = message.GetMessage ( ) ;
            X509Certificate OtherClientCert = message.GetClientCertificate ( ) ;
            if ( stringMessage.equals ("Hello") ) 
            {
                FileInputStream fis = new FileInputStream ( "myTrustStore" ) ; //maybe .jks
                KeyStore TrustStore = KeyStore.getInstance ( "JKS" ) ;
                TrustStore.load ( fis, "changeit".toCharArray ( ) ) ;
                X509Certificate CACert = (X509Certificate) TrustStore.getCertificate ("CAcert") ;
                fis.close ( ) ;
                //So check if has a valid Date
                OtherClientCert.checkValidity ( ) ; //exception will be caught
                //Check if is signed by CA
                OtherClientCert.verify ( CACert.getPublicKey ( ), "BC" ) ;
                this.OOS.writeObject ( new SerializableMessage ("Hello", ClientCERT ) ) ;
                SerializableMessage encrypted_symmetrickey = (SerializableMessage) this.OIS.readObject ( ) ;
                //Decrypt it
                byte[] encryptedSymmetricKey = encrypted_symmetrickey.GetEncyptedKey ( ) ;
                byte[] SymmetricKey = new CryptoOperator ( PRIV_KEY, encryptedSymmetricKey ).DecryptSymmetricKeywithPrivateKey ( ) ;
                //
                SecretKey sk = new SecretKeySpec ( SymmetricKey, 0, SymmetricKey.length, "AES") ;
                //Encrypt a message with Symmetric Key
                String example = "All Fine" ;
                byte[] bytes = example.getBytes ( ) ;
                byte[] encryptedMessage = new CryptoOperator ( sk, bytes ).EncryptObjectwithSecretKey ( ) ;
                SerializableMessage sb = new SerializableMessage ( encryptedMessage ) ;
                this.OOS.writeObject( sb ) ;
                this.OOS.flush ( ) ;
                this.OIS.close ( ) ;
                this.OOS.close ( ) ;
                this.sock.close ( ) ;
            } 
            else 
            {
                this.OIS.close ( ) ;
                this.OOS.close ( ) ;
                this.sock.close ( ) ;
            }
            this.OIS.close ( ) ;
            this.OOS.close ( ) ;
            this.sock.close ( ) ;
        }
        catch (IOException ex) 
        {
            System.out.println("" + ex.getMessage());
        } 
        catch (ClassNotFoundException CNFE) 
        {

        } 
        catch (NoSuchAlgorithmException NSAE) 
        {

        }
        catch (InvalidKeyException IKE) 
        {

        } 
        catch (NoSuchPaddingException NSPE) 
        {

        } 
        catch (IllegalBlockSizeException IBSE) 
        {

        } 
        catch (BadPaddingException BPE) 
        {

        }
        catch ( CertificateException ce )
        {
            
        }
        catch ( SignatureException se )
        {
           
        }
        catch ( KeyStoreException se )
        {
           
        }
        catch ( NoSuchProviderException se )
        {
      
        } 
        catch ( UnrecoverableKeyException ex ) 
        {
            Logger.getLogger(Encapsulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

