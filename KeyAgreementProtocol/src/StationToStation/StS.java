/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import Main_.KindOfProtocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author LU$er
 */
public class StS implements Runnable  //BASIC STS
{
    private Socket sock ;
    private ObjectInputStream OIS = null ;
    private ObjectOutputStream OOS = null ;
    
    public StS ( ) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
    {
        //Start this Client the Protocol
        Socket socketToAClient = null ;
        try
        {
            socketToAClient = new Socket ( "192.168.1.6", 8888 ) ;
            ObjectOutputStream OS = new ObjectOutputStream ( socketToAClient.getOutputStream ( ) ) ;
            ObjectInputStream IS = new ObjectInputStream ( socketToAClient.getInputStream ( ) ) ;
            //Send Init Protocol way
            OS.writeObject ( new KindOfProtocol ("STS") ) ;
            //Start Session
            //-------------------
            //CREATE RSA KEY PAIR
            RSA_KEYS rsaKeyPair = new RSA_KEYS ( ) ;
            PublicKey authenticatePublicKey = rsaKeyPair.GetRSAPublicKey ( ) ;
            PrivateKey authenticatePrivateKey = rsaKeyPair.GetRSAPrivateKey ( ) ;
            //------------------
            //CREATE now DHellman Key Pair. So private value x, and public value y ( value = key )
            DH_PARAM_ dhParam = new DH_PARAM_ ( ) ;
            byte [ ] DHPublicKeyEncoded = dhParam.GetPublicKeyEncoded ( ) ;
            PrivateKey DHPrivateKey = dhParam.GetPrivatecKey ( ) ;
            //-----------------
            //Now send to Other Client the P,Q, and the DH Public Value
            OS.writeObject ( new InitParameters ( dhParam.GetP ( ), dhParam.GetG ( ), dhParam.GetL ( ), DHPublicKeyEncoded ) ) ;
            PK_EncrypteDSign pces = ( PK_EncrypteDSign ) IS.readObject ( ) ;
            //-------------
            byte [ ] EncryptedSignfromClient = pces.GetEncyptedSignature ( ) ;
            PublicKey RSAPKOTHERCLIENT = KeyFactory.getInstance ("RSA").generatePublic ( new X509EncodedKeySpec ( pces.GetRSApublicKey ( ) ) ) ;
            byte [ ] BYTES_DHPKOTHERCLIENT = pces.GetDHpublicKey ( ) ;
            //1. Produce Secret Key from Public Value from Other Client
            KeyFactory kf = KeyFactory.getInstance ( "DH" ) ;
            X509EncodedKeySpec x509Spec = new X509EncodedKeySpec ( pces.GetDHpublicKey ( ) ) ;
            PublicKey DHPKOTHERCLIENT = kf.generatePublic ( x509Spec ) ;
            //2. Produce Secret Key
            KeyAgreement ka = KeyAgreement.getInstance ( "DH" ) ;
            ka.init ( DHPrivateKey ) ;
            ka.doPhase ( DHPKOTHERCLIENT, true ) ;
            byte secret [ ] = ka.generateSecret ( ) ;
	    //3: generate a DES key
	    SecretKeyFactory skf = SecretKeyFactory.getInstance ( "DES" ) ;
	    DESKeySpec desSpec = new DESKeySpec ( secret ) ;
	    SecretKey Sk = skf.generateSecret ( desSpec ) ;
            //----------------
            //4. Now decrypt the encrypted Signature
            //PublicKey RSAPKOtherClient = KeyFactory.getInstance ("RSA") .generatePublic ( new X509EncodedKeySpec ( pces.GetRSApublicKey ( ) ) ) ;
            OPCryptor op = new OPCryptor ( Sk ) ;
            byte [ ] DecryptedSignature = op.DecryptEncryptedSignature ( EncryptedSignfromClient ) ;
            //5. Now verify signature
            Signature sign = Signature.getInstance ( "SHA1withRSA") ;
            sign.initVerify ( RSAPKOTHERCLIENT ) ;
            sign.update ( BYTES_DHPKOTHERCLIENT ) ;
            sign.update ( DHPublicKeyEncoded ) ;
            
            if ( sign.verify ( DecryptedSignature ) )
            {
                //6. Now sign the PuclicKey of me and PK of other Client (DH PUBLIC KEYS)
                Signature sign2 = Signature.getInstance ( "SHA1withRSA" ) ;
                sign2.initSign ( authenticatePrivateKey ) ;
                sign2.update ( DHPublicKeyEncoded ) ;
                sign2.update ( BYTES_DHPKOTHERCLIENT ) ;
                byte [ ] encryptedSign = op.EncryptSignature ( sign2.sign ( ) ) ;
                OS.writeObject ( new PK_EncrypteDSign ( encryptedSign, authenticatePublicKey.getEncoded ( ) ) ) ;
                System.out.println ( "Everything OK ! sk -> " + Sk.toString ( ) ) ;
            }
            else
            {
                System.out.println ( "Signing ERRORSSS-=-=--=-=-=-=-=--=") ;
                socketToAClient.close ( ) ;
                OS.close ( ) ;
                IS.close ( ) ;
            }
        }
        catch ( IOException IOE )
        {
            System.out.println ( " 1.IOException + " + IOE.getLocalizedMessage ( ) );
        }
        catch ( ClassNotFoundException CNFE )
        {
            System.out.println ( " 2.ClassNotFoundException + " + CNFE.getLocalizedMessage ( ) );
        }
        catch ( InvalidKeySpecException o )
        {
            System.out.println ( " 3.InvalidKeySpecException + " + o.getLocalizedMessage ( ) );
        }
        catch ( SignatureException se )
        {
            System.out.println ( " 4.SignatureException + " + se.getLocalizedMessage ( ) );
        }
    }
    
    public StS ( Socket sock, ObjectOutputStream OOS, ObjectInputStream OIS )
    {
        this.sock = sock ;
        this.OOS = OOS ;
        this.OIS = OIS ;
    }
    
    public void run ( )
    {
        try
        {
            //CREATE RSA KEY PAIR
            RSA_KEYS rsaKeyPair = new RSA_KEYS ( ) ;
            PublicKey authenticatePublicKey = rsaKeyPair.GetRSAPublicKey ( ) ;
            PrivateKey authenticatePrivateKey = rsaKeyPair.GetRSAPrivateKey ( ) ;
            //------------------
            InitParameters ip = ( InitParameters ) this.OIS.readObject ( ) ;
            //CREATE now DHellman Key Pair. So private value x, and public value y ( value = key )
            KeyPairGenerator kpg = KeyPairGenerator.getInstance ("DH") ;
            DHParameterSpec Myspec = new DHParameterSpec ( ip.GetP ( ), ip.GetQ ( ), ip.GetL ( ) ) ;
            kpg.initialize ( Myspec ) ;
            KeyPair kp = kpg.generateKeyPair ( ) ;
            byte [ ] myDHpubkey = kp.getPublic ( ).getEncoded ( ) ;
            //-----------------
            //Sign the 2 DH Public Values
            Signature sign2 = Signature.getInstance ( "SHA1withRSA" ) ;
            sign2.initSign ( authenticatePrivateKey ) ;
            sign2.update ( myDHpubkey ) ;
            sign2.update ( ip.GetPublicValue ( ) ) ;
            //-----------------
            //Produce SK
            KeyAgreement ka = KeyAgreement.getInstance ( "DH" ) ;
            ka.init ( kp.getPrivate ( ) ) ;
            ka.doPhase ( KeyFactory.getInstance ("DH") .generatePublic ( new X509EncodedKeySpec ( ip.GetPublicValue ( ) ) ) , true ) ;
            byte secret [ ] = ka.generateSecret ( ) ;
	    //3: generate a DES key
	    SecretKeyFactory skf = SecretKeyFactory.getInstance ( "DES" ) ;
	    DESKeySpec desSpec = new DESKeySpec ( secret ) ;
	    SecretKey Sk = skf.generateSecret ( desSpec ) ;
            System.out.println ( "SK is -> " + Sk.toString()) ;
            OPCryptor op = new OPCryptor ( Sk ) ;
            byte [] yolo = sign2.sign ( ) ;
            byte [ ] encryptedSign = op.EncryptSignature ( yolo ) ;
            this.OOS.writeObject ( new PK_EncrypteDSign ( myDHpubkey,authenticatePublicKey.getEncoded ( ), encryptedSign) ) ;
            PK_EncrypteDSign cm = ( PK_EncrypteDSign ) this.OIS.readObject ( ) ;
            //--------------------
            byte [ ] bytes_RSAPKOTHERCLIENT = cm.GetRSApublicKey ( ) ;
            PublicKey RSAPKOTHERCLIENT = KeyFactory.getInstance ("RSA") .generatePublic ( new X509EncodedKeySpec ( bytes_RSAPKOTHERCLIENT ) ) ;
            byte [ ] EncryptedSignedfromCLient = cm.GetEncyptedSignature ( ) ;
            //--------------------
            //Decrypt the message with SK
            byte [ ] signatureD = op.DecryptEncryptedSignature ( EncryptedSignedfromCLient ) ;
            //Verify the signature
            Signature sign3 = Signature.getInstance ( "SHA1withRSA" ) ;
            sign3.initVerify ( RSAPKOTHERCLIENT ) ;
            sign3.update ( ip.GetPublicValue ( ) ) ;
            sign3.update ( myDHpubkey ) ;
            
            if ( sign3.verify ( signatureD ) )
            {
                System.out.println ( " All ok" ) ;
            }
            else
            {
                this.OOS.close ( ) ;
                this.OIS.close ( ) ;
                this.sock.close ( ) ;
            }
        }
        catch ( IOException IOE )
        {
            
        }
        catch ( ClassNotFoundException CNSFE )
        {
            
        } 
        catch ( NoSuchAlgorithmException ex ) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch ( InvalidAlgorithmParameterException ex ) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalStateException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SignatureException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvalidKeySpecException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (NoSuchPaddingException ex)
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalBlockSizeException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (BadPaddingException ex) 
        {
            Logger.getLogger(StS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
