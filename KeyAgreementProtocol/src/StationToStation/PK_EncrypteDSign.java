/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StationToStation;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;

/**
 *
 * @author LU$er
 */
public class PK_EncrypteDSign implements Serializable
{
    private byte [ ] DHpublicKey, encryptedSignature ;
    private byte [] RSApublicKey ;
    
    public PK_EncrypteDSign ( byte [ ] DHpublicKey, byte [] RSApublicKey, byte [] encryptedSignature )
    {
        this.RSApublicKey = RSApublicKey ;
        this.encryptedSignature = encryptedSignature ;
        this.DHpublicKey = DHpublicKey ;
    }
    
    public PK_EncrypteDSign ( byte [ ] encryptedSignature, byte [] pk )
    {
        this.encryptedSignature = encryptedSignature ;
        this.RSApublicKey = pk ;
    }
    
    public byte [ ] GetEncyptedSignature ( )
    {
        return this.encryptedSignature ;
    }
    
    public byte [] GetRSApublicKey ( )
    {
        return this.RSApublicKey ;
    }
    
    public byte [ ] GetDHpublicKey ( )
    {
        return this.DHpublicKey ;
    }
    
    
}
