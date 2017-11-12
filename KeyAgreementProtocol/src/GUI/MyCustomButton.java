package GUI ;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author LU$er
 */

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


	

public class MyCustomButton extends JButton 
{
    private Color pressedColor = Color.BLACK;
    private Color rolloverColor = Color.LIGHT_GRAY;
    private Color normalColor = Color.DARK_GRAY;

    public MyCustomButton (String text) 
    {
        super(text);
        setBorderPainted(false);
        setFocusPainted(false);

        setContentAreaFilled(false);
        setOpaque(true);

        setBackground(normalColor);
        setForeground(Color.WHITE);
        setFont(new Font("Tahoma", Font.BOLD, 12));
        setText(text);

        addChangeListener(new ChangeListener() 
        {
            @Override
            public void stateChanged(ChangeEvent evt) 
            {
                if (getModel().isPressed()) 
                {
                    setBackground(pressedColor);
                } 
                else if (getModel().isRollover()) 
                {
                    setBackground(rolloverColor);
                } 
                else 
                {
                    setBackground(normalColor);
                }
            }
        });
    }
}

