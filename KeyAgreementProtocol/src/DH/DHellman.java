package DH ;

import Main_.KindOfProtocol;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
public class DHellman implements Runnable
{
    private Socket sock ;
    private ObjectInputStream OIS = null ;
    private ObjectOutputStream OOS = null ;
    
    public DHellman ( ) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, KeyStoreException, NoSuchProviderException, InvalidKeySpecException 
    {
        //Start this Client the Protocol
        Socket socketToAClient = null ;
        ObjectOutputStream OS = null ;
        ObjectInputStream IS = null ;
        
        try
        {
            socketToAClient = new Socket ( "192.168.1.6", 8888 ) ;
            OS = new ObjectOutputStream ( socketToAClient.getOutputStream ( ) ) ;
            IS = new ObjectInputStream ( socketToAClient.getInputStream ( ) ) ;
            //Send Init Protocol way
            OS.writeObject ( new KindOfProtocol ("DH") ) ;
            //Start Session
	    // Step 1:  Alice generates a key pair
	    DHKeysCreator DHC = new DHKeysCreator ( ) ;
            PrivateKey privkey = DHC.GetPrivatecKey ( ) ;
	    // Step 2:  Alice sends the public key and the
            // Diffie-Hellman key parameters to Bob
            DHSpecsPublicKey opg = new DHSpecsPublicKey ( DHC.GetPublicKeyEncoded ( ), DHC.GetP( ), DHC.GetG( ), DHC.GetL( ) ) ;
            OS.writeObject ( opg ) ;
            //First Phase
            DHC.FirstPhase ( privkey ) ;
            byte [ ] otherPublicKey = ( byte [ ] ) IS.readObject ( ) ;
            SecretKey mysk = DHC.SecondPhase ( otherPublicKey ) ;
            System.out.println ( "Bob : SecretKey is -> " + mysk.toString ( ) ) ;
            IS.close ( ) ;
            OS.close ( ) ;
            socketToAClient.close ( ) ;
        }
        catch ( IOException se )
        {
            //Close
            IS.close ( ) ;
            OS.close ( ) ;
            socketToAClient.close ( ) ;
        }
    }
    
    public DHellman ( Socket sock, ObjectOutputStream OOS, ObjectInputStream OIS )
    {
        this.sock = sock ;
        this.OOS = OOS ;
        this.OIS = OIS ;
    }
    
    public void run ( )  //Incoming message from someone
    {
        try 
        {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance ("DH") ;
            DHSpecsPublicKey dhspeec = ( DHSpecsPublicKey ) this.OIS.readObject ( ) ;
            BigInteger Ga = dhspeec.GetG ( ) ;
            BigInteger Pa = dhspeec.GetP ( ) ;
            int La = dhspeec.GetL ( ) ;
            byte [ ] OtherPublicKey = dhspeec.GetPK ( ) ;
            //Bob uses the parameters supplied by Alice
	    //to generate a key pair and sends the public key
            DHParameterSpec Myspec = new DHParameterSpec ( Pa, Ga, La ) ;
            //
            kpg.initialize ( Myspec ) ;
            KeyPair kp = kpg.generateKeyPair ( ) ;
            byte [ ] mypubkey = kp.getPublic ( ).getEncoded ( ) ;
            this.OOS.writeObject ( mypubkey ) ;
            // Step 5 part 1:  Bob uses his private key to perform the
	    //first phase of the protocol
            KeyAgreement ka = KeyAgreement.getInstance ( "DH" ) ;
	    ka.init ( kp.getPrivate( ) ) ;
            // Step 5 part 2:  Bob uses Alice's public key to perform
	    //	the second phase of the protocol.
	    KeyFactory kf = KeyFactory.getInstance ( "DH") ;
	    X509EncodedKeySpec x509Spec = new X509EncodedKeySpec ( OtherPublicKey ) ;
	    PublicKey pk = kf.generatePublic ( x509Spec ) ;
	    ka.doPhase ( pk, true ) ;
            //======================
            // Step 6:  Bob generates a DES key
            byte secret [] = ka.generateSecret ( ) ;
	    SecretKeyFactory skf = SecretKeyFactory.getInstance ( "DES" ) ;
            DESKeySpec desSpec = new DESKeySpec ( secret ) ;
	    SecretKey key = skf.generateSecret ( desSpec ) ;
            System.out.println ( " SecretKey is -> " + key.toString ( ) ) ;
            this.sock.close ( ) ;
            this.OOS.close ( ) ;
            this.OIS.close ( ) ;
        } 
        catch ( IOException IOE )
        {
            
        }
        catch ( NoSuchAlgorithmException ex ) 
        {
            Logger.getLogger(DHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch ( ClassNotFoundException ex ) 
        {
            Logger.getLogger(DHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch ( InvalidAlgorithmParameterException ex ) 
        {
            //Logger.getLogger(DHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch ( InvalidKeyException ex ) 
        {
            //Logger.getLogger(DHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch ( InvalidKeySpecException ex ) 
        {
            //Logger.getLogger(DHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
