import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class WalkerBehavior extends Project1_Codebase {
    static PathRendering path = new PathRendering();
    static WorldGeometries world = new WorldGeometries();
    float[][] finalarray;
    int nsize;
    int x = nsize/2;
    int y = nsize/2;
    int count;
    int colortotal;
    int pickynumber;

    int RandomNum;

    float[][] kernal = {
            {0.0625f,0.125f,0.0625f},
            {0.125f,0.25f,0.125f},
            {0.0625f,0.125f,0.0625f},
    };



    public void setarray(int x, int y )
    {
        if (((nsize <= y) | (x >= nsize) | (1 >= y) | (1 >= x))) {

        }
        else
        {
            finalarray[x][y] += 1.0f;
            finalarray[x][y + 1] += 1.0f;
            finalarray[x][y - 1] += 1.0f;
            finalarray[x + 1][y] += 1.0f;
            finalarray[x - 1][y] += 1.0f;
            finalarray[x + 1][y + 1] += 1.0f;
            finalarray[x + 1][y - 1] += 1.0f;
            finalarray[x - 1][y + 1] += 1.0f;
            finalarray[x - 1][y - 1] += 1.0f;
        }
        colortotal++;

    }



    public void StandardWalker() {

        int RandomNum = (int) ((Math.random() * (9 - 1)) + 1);
        switch (RandomNum) {
            case 1:
                x = x + 1;
                break;

            case 2:
                x = x + 1;
                y = y + 1;
                break;
            case 3:
                y = y + 1;
                break;
            case 4:
                x = x - 1;
                y = y + 1;
                break;
            case 5:
                x = x - 1;
                break;
            case 6:
                x = x - 1;
                y = y - 1;
                break;
            case 7:
                y = y - 1;

                break;
            case 8:
                x = x + 1;
                y = y - 1;
                break;
        }
    }

    public void PickyWalker() {

        RandomNum = (int) ((Math.random() * (9 - 1)) + 1);
        pickynumber = (int) ((Math.random() * (11 - 1)) + 1);
    }

    public void BoxSelection()
    {
            switch (RandomNum) {

                case 1:
                    x = x + 1;
                    break;

                case 2:
                    x = x + 1;
                    y = y + 1;
                    break;
                case 3:
                    y = y + 1;
                    break;
                case 4:
                    x = x - 1;
                    y = y+ 1;
                    break;
                case 5:
                    x = x - 1;
                    break;
                case 6:
                    x = x - 1;
                    y = y - 1;
                    break;
                case 7:
                    y = y - 1;

                    break;
                case 8:
                    x = x + 1;
                    y = y - 1;
                    break;
            }
    }




    public int getX() {
        return x;
    }
    public int getY() {
        return y;

    }

    public void setDimensions(int size)
    {
        nsize = size;
        finalarray = new float[nsize+1][nsize+1];

    }
    public void setX(int value) {
        x = value;
    }
    public void setY(int value) {
        y = value;
    }

    public int getPickyNumber()
    {
        return pickynumber;
    }


    public void setCount(int num)
    {
        count = num;
    }

    public int getCount()
    {
        return count;
    }

    public float[][] getFinalarray() {
        return finalarray;
    }
}

