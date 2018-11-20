/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import view.View;
import core.Core;
import core.Image;

/**
 *
 * @author Red
 */
public class Main {

    public static void main(String[] args) 
    {
        Core model = new Core();
        View v = new View(model);
        v.setVisible(true);
    }
    
    public static void show(Image[] images)
    {
        System.out.println("motifs =\n{");
        for(int i=0 ; i<images.length ; i++)
        {
            System.out.println("\t" + images[i].toString());
        }
        System.out.println("}");
    }
}
