package DH ;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DHParameterSpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class DHKeysCreator 
{
    private BigInteger G, P ;
    private int L ;
    private static final String Algorithm = "DH" ;
    private byte [ ] PublicKeyEncd ;
    private PrivateKey PrivateKey ;
    private DHParameterSpec dhSpec ;
    private KeyAgreement ka ;
    
    public DHKeysCreator ( ) throws NoSuchAlgorithmException
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance ( Algorithm ) ;
	kpg.initialize ( 2048 ) ;
	KeyPair kp = kpg.generateKeyPair ( ) ;
        //
        this.dhSpec = ( (DHPublicKey) kp.getPublic ( ) ) .getParams ( ) ;
	this.G = dhSpec.getG ( ) ;
	this.P = dhSpec.getP ( ) ;
	this.L = dhSpec.getL ( ) ;
	this.PublicKeyEncd = kp.getPublic().getEncoded ( ) ;
        this.PrivateKey = kp.getPrivate ( ) ;
    }
    
    public byte [ ] GetPublicKeyEncoded ( )
    {
        return this.PublicKeyEncd ;
    }
    
    public BigInteger GetP ( )   
    {
        return this.P ;
    }
    
    public BigInteger GetG ( )   
    {
        return this.G ;
    }
    
    public int GetL ( )   
    {
        return this.L ;
    }
    
    public DHParameterSpec DHSPEC ( )
    {
        return this.dhSpec ;
    }
    
    public PrivateKey GetPrivatecKey ( )
    {
        return this.PrivateKey ;
    }
    
    public void FirstPhase ( PrivateKey pk ) throws NoSuchAlgorithmException, InvalidKeyException
    {
        this.ka = KeyAgreement.getInstance ( Algorithm ) ;
	this.ka.init ( pk ) ;
    }
    
    public SecretKey SecondPhase ( byte [ ] OtherClientPubKeyEncdc ) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException
    {
        KeyFactory kf = KeyFactory.getInstance ( Algorithm ) ;
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec ( OtherClientPubKeyEncdc ) ;
        PublicKey puk = kf.generatePublic ( x509Spec ) ;
	this.ka.doPhase ( puk, true ) ;
        byte secret[] = this.ka.generateSecret ( ) ;
	// Step 6:  Alice generates a DES key
	SecretKeyFactory skf = SecretKeyFactory.getInstance ( "DES" ) ;
	DESKeySpec desSpec = new DESKeySpec ( secret ) ;
	return skf.generateSecret ( desSpec ) ;
    }
}
