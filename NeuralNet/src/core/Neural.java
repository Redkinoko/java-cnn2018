/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Red
 */
public class Neural {
    private int weight0;
    private static int nbSynapses = 4;
    private int[] weights;
    
    public Neural()
    {
        weight0 = 2;
        weights = new int[nbSynapses];
        for(int i=0 ; i<weights.length ; i++)
        {
            weights[i] = 0;
        }
    }
    
    public Neural(int w0, int w1, int w2, int w3, int w4)
    {
        weight0 = w0;
        weights = new int[nbSynapses];
        weights[0] = w1;
        weights[1] = w2;
        weights[2] = w3;
        weights[3] = w4;
    }
    
    private int sum(Image m)
    {
        int total  = 0;
        int mLength = m.getLength();
        for(int i=0 ; i<mLength ; i++)
        {
            total += m.getValueAt(i)*weights[i];
        }
        return (total-weight0);
    }
    
    public int getPrediction(Image m)
    {
        return (sum(m) >= 0)?1:0;
    }
    
    public void learn(Image m, int trueResult)
    {
        int pred = getPrediction(m);
        int delta = (trueResult-pred);
        int mLength = m.getLength();
        for(int i=0 ; i<mLength ; i++)
        {
            weights[i] = weights[i] + delta*m.getValueAt(i);
        }
        weight0 = weight0 - delta;
    }
    
    @Override
    public String toString()
    {
        String s = "w0->w" + weights.length  + " {"+ weight0;
        for(int i=0 ; i<weights.length ; i++)
        {
            s += ", " + weights[i];
        }
        s += "}";
        return s;
    }
}
