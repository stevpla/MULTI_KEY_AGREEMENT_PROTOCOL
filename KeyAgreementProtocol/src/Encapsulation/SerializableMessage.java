package Encapsulation ;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class SerializableMessage implements Serializable
{
    private String Message ;
    private PublicKey publicKey ;
    private byte [ ] EncryptedKey ;
    private X509Certificate ClientCert ;
    
    public SerializableMessage ( String Message )
    {
        this.Message = Message ;
    }
    
    public SerializableMessage ( String Message, X509Certificate ClientCert )
    {
        this.Message = Message ;
        this.ClientCert = ClientCert ;
    }
    
    public SerializableMessage ( String Message, PublicKey publicKey )
    {
        this.Message = Message ;
        this.publicKey = publicKey ;
    }
    
    public SerializableMessage ( byte [ ] EncryptedKey )
    {
        this.EncryptedKey = EncryptedKey ;
    }
    
    public String GetMessage ( )
    {
        return this.Message ;
    }
    
    public PublicKey GetPublicKey ( )
    {
        return this.publicKey ;
    }
    
    public byte [ ] GetEncyptedKey ( )
    {
        return this.EncryptedKey ;
    }
    
    public X509Certificate GetClientCertificate ( )
    {
        return this.ClientCert ;
    }
}
