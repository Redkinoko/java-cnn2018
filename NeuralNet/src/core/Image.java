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
public class Image {
    private int[] value;
    public int[] getValue() { return value; }
    public int getValueAt(int i) { return value[i];/*(i>=0 && i<(value.length-1))?value[i]:0;*/ }
    public void setValueAt(int i, int j) { value[i] = j; }
    
    private static int length = 4;
    public int getLength() { return length; }
    
    public Image(int a, int b, int c, int d)
    {
        value = new int[length];
        value[0] = a;
        value[1] = b;
        value[2] = c;
        value[3] = d;
    }
    
    public Image clone()
    {
        return new Image(value[0], value[1], value[2], value[3]);
    }
    
    public void invert(Image m, int i)
    {
        int t = value[i];
        value[i] = m.value[i];
        m.value[i] = t;
    }
    
    public boolean equals(Image m)
    {
        for(int i=0 ; i<m.length ; i++)
        {
            if (!(m.value[i] == value[i])) { return false; }
        }
        return true;
    }
    
    public int equalsResult(Image m)
    {
        return (equals(m))?1:0;
    }
    
    @Override
    public String toString() { 
        String s = "" + value[0];
        for(int i=1 ; i<value.length ; i++)
        {
            s += "," + value[i];
        }
        return("[" + s + "]"); 
    }
}
