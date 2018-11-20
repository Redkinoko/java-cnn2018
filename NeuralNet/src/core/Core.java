/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.Hashtable;
import main.Tools;

/**
 *
 * @author Red
 */
public class Core
{
    private Neural neural;
    private Image[] data;
    private Image target;
    
    public Neural getNeural() { return neural; }
    public Image getTarget()  { return target;  }
    
    public Core()
    {
        data = getAllImages();//Liste des motifs possible
        target = data[0];//Le motif à reconnaître
        neural = new Neural();
    }
    
    private Image[] clone(Image[] images)
    {
        Image[] clone = new Image[images.length];
        for(int i=0 ; i<images.length ; i++)
        {
            clone[i] = images[i].clone();
        }
        return clone;
    }
    
    public void shuffle(Image[] data)
    {
        int rnd1 = 0;
        int rnd2 = 0;
        int rnd3 = 0;
        int length = data.length;
        int max = length -1;
        for(int i=0 ; i<length ; i++)
        {
            rnd1 = Tools.randomNumber(0, max);
            rnd2 = Tools.randomNumber(0, max);
            rnd3 = Tools.randomNumber(0, 3);
            data[rnd1].invert(data[rnd2], rnd3);
        }
    }
    
    public TrainResult train()
    {
        Image[] images = clone(data);
        shuffle(images);
        String comment = "";
        int bonneReponses = 0;
        for(int i=0 ; i<images.length ; i++)
        {
            Image m = images[i];
            int trueResult = target.equalsResult(m);
            int result     = neural.getPrediction(m);
            comment += 
                "[" + i + "] \t" + 
                m.toString() + "\t" +
                "pred/res = " + result + "/" + trueResult + "\t" +
                neural.toString() +
                "\n"
            ;
            if(result == trueResult) 
            { 
                bonneReponses++; 
            }
            else
            {
                neural.learn(m, trueResult);
            }
        }
        float percent = ((0.0f + bonneReponses)/16.0f)*100;
        //System.out.println("Il y a " + bonneReponses + "/16 bonnes réponses (" + percent + ")");
        return new TrainResult(percent, comment);
    }
    
    public String ask(Image m)
    {
        int trueResult = target.equalsResult(m);
        int result     = neural.getPrediction(m);
        String s = "";
        if (result == 1)
        {
            s += "Je reconnais la cible ! \n";
        }
        else
        {
            s += "Ce n'est pas la cible ! \n";
        }
        if(trueResult == result)
        {
            s += "Prédiction VALIDE ! \n";
        }
        else
        {
            s += "Prédiction NON VALIDE ! \n";
        }
        s += "détails :\n" + 
        target.toString() + "/" + m.toString() + "\n" +
        neural.toString();
        
        return s;
    }
    
    public Image[] getAllImages()
    {
        Image[] images = new Image[16];
        images[15]  = new Image(0,0,0,0);
        images[14]  = new Image(0,0,0,1);
        images[13]  = new Image(0,0,1,0);
        images[12]  = new Image(0,0,1,1);
        images[11]  = new Image(0,1,0,0);
        images[10]  = new Image(0,1,0,1);
        images[9]   = new Image(0,1,1,0);
        images[8]   = new Image(0,1,1,1);
        images[7]   = new Image(1,0,0,0);
        images[6]   = new Image(1,0,0,1);
        images[5]   = new Image(1,0,1,0);
        images[4]   = new Image(1,0,1,1);
        images[3]   = new Image(1,1,0,0);
        images[2]   = new Image(1,1,0,1);
        images[1]   = new Image(1,1,1,0);
        images[0]   = new Image(1,1,1,1);
        return images;
    }
}
