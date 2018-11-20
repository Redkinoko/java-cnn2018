package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

class IMG {
    public int[][] v;
    public int w;
    public int h;
    public IMG(int w, int h)
    {
        v = new int[w][h];
        this.w = w;
        this.h = h;
    }
    public IMG(IMG data)
    {
        v = new int[data.w][data.h];
        w = data.w;
        h = data.h;
        for(int i=0 ; i<w; i++)
        {
            for(int j=0 ; j<h ; j++)
            {
                v[i][j] = data.v[i][j];
            }
        }
    }
}

/**
 *
 * @author Red
 */
public class Main {
    
    public static double[][] blur        = new double[][] {{0.0625, 0.125, 0.0625},{0.125,0.25,0.125},{0.0625,0.125,0.0625}};
    public static double[][] bot_sobel   = new double[][] {{-1,-2,-1},{0,0,0},{1,2,1}};
    public static double[][] emboss      = new double[][] {{-2,-1,0},{-1,1,1},{0,1,2}};
    public static double[][] identity    = new double[][] {{0,0,0},{0,1,0},{0,0,0}};
    public static double[][] left_sobel  = new double[][] {{1,0,-1},{2,0,-2},{1,0,-1}};
    public static double[][] outline     = new double[][] {{-1, -1, -1},{-1, 8, -1},{ -1, -1, -1}};
    public static double[][] right_sobel = new double[][] {{-1,0,1},{-2,0,2},{-1,0,1}};
    public static double[][] sharpen     = new double[][] {{0,-1,0},{-1,5,-1},{0,-1,0}};
    public static double[][] top_sobel   = new double[][] {{1,2,1},{0,0,0},{-1,-2,-1}};
    
    public static void main(String[] args) throws IOException
    {
        BufferedImage cat = getImage(getAbsPath() + "/src/data/img/cat.png");
        List<IMG> chan = getChannels(cat);
        List<IMG> test = copy(chan);
        test = max(conv(test));
        int[] v = toVector(test);
        
        getFrame(test);
    }
    
    public static List<IMG> copy(List<IMG> data)
    {
        List<IMG> cop = new ArrayList();
        for(IMG item:data)
        {
            cop.add(new IMG(item));
        }
        return cop;
    }
    
    public static IMG getIMG(int[] vect)
    {
        IMG img = new IMG(vect.length, 1);
        for(int i=0 ; i<vect.length ; i++)
        {
            img.v[i][0] = vect[i];
        }
        return img;
    }
    
    public static int[] toVector(List<IMG> imgs)
    {
        int n = imgs.size()*imgs.get(0).h*imgs.get(0).w;
        int s = imgs.get(0).h*imgs.get(0).w;
        int[] vec = new int[n];
        
        for(int i=0 ; i<imgs.size() ; i++)
        {
            for(int x=0 ;x<imgs.get(i).w ; x++)
            {
                for(int y=0 ;y<imgs.get(i).h ; y++)
                {
                    vec[i*s+(x*imgs.get(i).w+y)] = imgs.get(i).v[x][y];
                }
            }
        }
        return vec;
    }
    
    public static List<IMG> conv(List<IMG> imgs)
    {
        List<IMG> list = new ArrayList();
        for(IMG i:imgs)
        {
            list.add(relu(filter2(i, bot_sobel)));
            list.add(relu(filter2(i, left_sobel)));
            list.add(relu(filter2(i, right_sobel)));
            list.add(relu(filter2(i, top_sobel)));
            /*
            list.add(relu(filter2(i, blur)));
            list.add(relu(filter2(i, bot_sobel)));
            list.add(relu(filter2(i, emboss)));
            list.add(relu(filter2(i, identity)));
            list.add(relu(filter2(i, left_sobel)));
            list.add(relu(filter2(i, outline)));
            list.add(relu(filter2(i, right_sobel)));
            list.add(relu(filter2(i, sharpen)));
            list.add(relu(filter2(i, top_sobel)));
            */
        }
        return list;
    }
    
    public static List<IMG> max(List<IMG> imgs)
    {
        List<IMG> list = new ArrayList();
        for(IMG i:imgs)
        {
            list.add(maxPooling(i));
        }
        return list;
    }
    
    public static IMG maxPooling(IMG img)
    {
        IMG out = new IMG(img.w/2, img.h/2);
        for(int i=0 ; i<out.w ; i++)
        {
            for(int j=0 ; j<out.h ; j++)
            {
                out.v[i][j] = 0;
            }
        }
        for(int i=0 ; i<img.w ; i++)
        {
            for(int j=0 ; j<img.h ; j++)
            {
                int x = i/2;
                int y = j/2;
                out.v[x][y] = Math.max(out.v[x][y], img.v[i][j]);
            }
        }
        return out;
    }
    
    public static IMG relu(IMG img)
    {
        for(int i=0 ; i<img.w ; i++)
        {
            for(int j=0 ; j<img.h ; j++)
            {
                img.v[i][j] = Math.max(0, img.v[i][j]);
            }
        }
        return img;
    }
    
    public static IMG filter2(IMG img, double[][] filter)
    {
        IMG tmp = new IMG(img.w, img.h);
        for(int i=1 ; i<(tmp.w-1) ; i++)
        {
            for(int j=1 ; j<(tmp.h-1) ; j++)
            {
                tmp.v[i][j] = 0;
                double it = 0.f;
                for(int x=0 ; x<filter.length ; x++)
                {
                    for(int y=0 ; y<filter.length ; y++)
                    {
                        it += filter[x][y]*(img.v[i-1+x][j-1+y] + 0.0f);
                    }
                }
                tmp.v[i][j] = (int)it;
            }
        }
        return tmp;
    }
    
    public static List<IMG> getChannels(BufferedImage img)
    {
        List<IMG> layers = new ArrayList();
        for(int i=0 ; i<3 ; i++)
        {
            layers.add(new IMG(img.getWidth(),img.getHeight()));
        }
        for(int i=0 ; i<img.getWidth() ; i++)
        {
            for(int j=0 ; j<img.getHeight() ; j++)
            {
                layers.get(0).v[i][j] = getRed(img,i,j);
                layers.get(1).v[i][j] = getGreen(img,i,j);
                layers.get(2).v[i][j] = getBlue(img,i,j);
            }
        }
        return layers;
    }
    
    public static void getFrame(IMG img)
    {
        List<IMG> i = new ArrayList();
        i.add(img);
        getFrame(i, 1);
    }
    
    public static void getFrame(List<IMG> imgs)
    {
        getFrame(imgs, imgs.size());
    }
    
    public static void getFrame(List<IMG> imgs, int n)
    {
        JFrame frame = new JFrame();
        frame.addWindowListener(
            new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                  System.exit(0);
                }
            }
        );
        frame.add(new JPanel()
        {
            @Override
            public void paint(Graphics g)
            {
                for(int w=0 ; w<n ; w++)
                {
                    IMG img = imgs.get(w);
                    int debI = img.w*w;
                    for(int i=0 ; i<img.w ; i++)
                    {
                        for(int j=0 ; j<img.h ; j++)
                        {
                            g.setColor(new Color(img.v[i][j]));
                            g.drawRect(debI + i, j, 1, 1);
                        }
                    }
                }
            }
        });
        frame.setSize(800,600);
        frame.setVisible(true);
    }
    
    public static BufferedImage getImage(String path) throws IOException
    {
        return ImageIO.read(new File(path));
    }
    public static int getRed(BufferedImage b, int x, int y)   { return b.getRGB(x,y) >> 16 & 0xFF; }
    public static int getGreen(BufferedImage b, int x, int y) { return b.getRGB(x,y) >> 8 & 0xFF; }
    public static int getBlue(BufferedImage b, int x, int y)  { return b.getRGB(x,y) & 0xFF; }
    
    public static boolean exists(String s)
    {
        return new File(s).exists();
    }
    
    public static String getAbsPath()
    {
        return new File("").getAbsolutePath();
    }
    
    public static void printf(String s)
    {
        System.out.println(s);
    }
}
