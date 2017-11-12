package Main_ ;

import DH.DHellman;
import Encapsulation.Encapsulation;
import StationToStation.StS;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class Listener implements Runnable
{
    
    public void run ( )
    {
        final boolean  LISTENING = true ;
        final int PORT = 8888 ;
        ExecutorService Executor = Executors.newCachedThreadPool ( ) ;
        ServerSocket Server_Socket ;    
        try 
        {
            Server_Socket = new ServerSocket ( PORT ) ;
            while ( LISTENING )
            {
                System.out.println ( "Waiting for a client for KAP..." ) ;
                Socket Client_Socket = Server_Socket.accept ( ) ;  
                ObjectInputStream OIS = new ObjectInputStream ( Client_Socket.getInputStream ( ) ) ;
                ObjectOutputStream OOS = new ObjectOutputStream ( Client_Socket.getOutputStream ( )) ;
                //READ FIRST STRING TO SEE WHICH PROTOCOL IS
                Object protocolVersion =  OIS.readObject ( ) ;
                
                if ( protocolVersion instanceof KindOfProtocol )
                {
                    KindOfProtocol kop = ( KindOfProtocol ) protocolVersion ;
                    if ( kop.GetKindOfPROTOCOL ( ).equals ( "ENCAPSULATION" ) )
                    {
                        Runnable Run = new Encapsulation ( Client_Socket, OOS, OIS ) ;      
                        Executor.execute ( Run ) ;
                    }
                    if ( kop.GetKindOfPROTOCOL ( ).equals ( "DH" ) )
                    {
                        Runnable Run = new DHellman ( Client_Socket, OOS, OIS ) ;      
                        Executor.execute ( Run ) ;
                    }
                    if ( kop.GetKindOfPROTOCOL ( ).equals ( "STS" ) )
                    {
                        Runnable Run = new StS ( Client_Socket, OOS, OIS ) ;      
                        Executor.execute ( Run ) ;
                    }
                }
                else
                {
                    Client_Socket.close ( ) ;
                    OIS.close ( )  ;
                    OOS.close ( ) ;
                }
            }
        } 
        catch ( IOException ex ) 
        {
            Logger.getLogger ( Listener.class.getName ( ) ).log ( Level.SEVERE, null, ex ) ;
        } 
        catch ( ClassNotFoundException ex ) 
        {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
