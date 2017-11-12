package GUI ;

import DH.DHellman;
import Encapsulation.Encapsulation;
import StationToStation.StS;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */
public class UserGui 
{
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    private final JFrame frame = new JFrame(); 
    private final JPanel panel = new JPanel();
    private final JLabel title = new JLabel("<html><h1>Key Agreement<h1><html>",SwingConstants.CENTER);
                
    public  void createAndShowGUI ( )
    {                   
        frame.setSize(320,240);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("KAP");
        frame.setResizable(false);
        
        panel.setLayout(new GridLayout(4,0));
        panel.setBackground(Color.WHITE);
        
        final MyCustomButton Diffie_Hellman = new MyCustomButton("Diffie-Hellman");
        final MyCustomButton Encapsulation = new MyCustomButton("Encapsulation");
        final MyCustomButton StS = new MyCustomButton("StS");
                  
        Encapsulation.addActionListener((ActionEvent e) -> 
        {
            try 
            {    
                 this.DeleteCACERTIFICATE ( ) ;
                //Start Encapsulation KAP
                new Encapsulation ( ) ;
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (NoSuchAlgorithmException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (NoSuchPaddingException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (InvalidKeyException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IllegalBlockSizeException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (BadPaddingException ex) 
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch ( KeyStoreException kse )
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, kse);
            }
            catch ( NoSuchProviderException nspe )
            {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, nspe);
            }
            frame.dispose();
        });
        Diffie_Hellman.addActionListener((ActionEvent e) -> 
        {
            try {
                 this.DeleteCACERTIFICATE ( ) ;
                new DHellman ( ) ;
            } catch (IOException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyStoreException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeySpecException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.dispose();
        });
        
        StS.addActionListener((ActionEvent e) -> 
        {
            try {
                this.DeleteCACERTIFICATE ( ) ;
                new StS ( ) ;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            }
             catch (InvalidKeyException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(UserGui.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.dispose();
        });
        
        panel.add(title);
        panel.add(Encapsulation);
        panel.add(Diffie_Hellman);
        panel.add(StS);
        
        frame.add(panel,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void DeleteCACERTIFICATE ( )
    {
        File file = new File ( "CARoot.crt" ) ;
        if( file.delete ( ) )
        {
            System.out.println(file.getName() + " is deleted!");
    	}
        else
        {
            System.out.println("Delete operation is failed.");
    	}
    }
}

    

