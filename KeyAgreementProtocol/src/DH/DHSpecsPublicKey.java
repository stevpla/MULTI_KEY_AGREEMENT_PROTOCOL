package DH ;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PublicKey;
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
public class DHSpecsPublicKey implements Serializable
{
    private byte [ ] pk ;
    private BigInteger P,G ;
    private int L ;
    
    public DHSpecsPublicKey ( byte [ ] pk, BigInteger P, BigInteger G, int L )
    {
        this.pk = pk ;
        this.G = G ;
        this.P = P ;
        this.L = L ;
    }
    
    public byte [ ] GetPK ( )   
    {
        return this.pk  ;
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
}
//https://docstore.mik.ua/orelly/java-ent/security/ch13_07.htm