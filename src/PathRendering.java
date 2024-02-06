import java.awt.*;
import java.util.Arrays;

public class PathRendering extends Project1_Codebase
{
    String color;
    int blackandwhite;
    int val;
    int[] array;

    public void SetColor(String i)
    {
        color = i;
    }
    public  String GetBlack() {
          return "0x000000";
    }
    public String GetColor()
    {
        return color;
    }
    public void SetGradient()
    {
        String startvalue = ("000000");
        String endvalue = ("FFFFFF");

        int RStart = Integer.valueOf(startvalue.substring(0,2),16);
        int REnd = Integer.valueOf(endvalue.substring(0,2),16);
        int GStart = Integer.valueOf(startvalue.substring(2,4),16);
        int GEnd = Integer.valueOf(endvalue.substring(2,4),16);
        int BStart = Integer.valueOf(startvalue.substring(4,6),16);
        int BEnd = Integer.valueOf(endvalue.substring(4,6),16);
        int N = (int)(world.max);
        int[]interpolation = new int[N];

        float Rstep_dif = (float)(REnd-RStart)/(N-1);
        float Rstep_value = RStart;
        float Gstep_dif = (float)(GEnd-GStart)/(N-1);
        float Gstep_value = GStart;
        float Bstep_dif = (float)(BEnd-BStart)/(N-1);
        float Bstep_value = BStart;


        for (int i = 0; i < N;i++)
        {

                int R = ((int) Rstep_value);
                int G = ((int) Gstep_value);
                int B = ((int) Bstep_value);

                Color value = new Color(R, G, B);
                int rgb = value.getRGB();
                interpolation[i] =rgb;
                Rstep_value += Rstep_dif;
                Gstep_value += Gstep_dif;
                Bstep_value += Bstep_dif;


        }

        array = interpolation;
    }

    public int GetGradient(int i)
    {

        if(i == (int)world.max)
        {
            blackandwhite = 0xFFFFFFFF;
        }
        else
        {
            blackandwhite = array[i];
        }
        return blackandwhite;
    }

}

