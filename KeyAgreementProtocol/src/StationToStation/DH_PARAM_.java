/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author LU$er
 */
public class DH_PARAM_ 
{
    private static final String Algorithm = "DH" ;
    private BigInteger G, P ;
    private int L ;
    private byte [ ] PublicKeyEncd ;
    private PrivateKey PrivateKey ;
    private DHParameterSpec dhSpec ;
    
    
    public DH_PARAM_ ( ) throws NoSuchAlgorithmException
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
    
}
