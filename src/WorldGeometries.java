import java.util.Arrays;

public class WorldGeometries extends Project1_Codebase {

        int edgex;
        int edgey;

        float max;
        int scale;
        double scalefactor;

        public void setEdgex(int width) {
            edgex = width;
        }

        public void setEdgey(int height) {
            edgey = height;
        }

        public void sortarray(){
            max = 0;
            for(int i =0; i < walk.getFinalarray().length ;i++)
            {
                for(int j =0; j < walk.getFinalarray().length;j++)
                {
                    if (walk.getFinalarray()[i][j] > max)
                    {
                        max = walk.getFinalarray()[i][j];
                    }
                }
            }

        }

        public void satelliteRendering(int i, int j)
        {
            float value = walk.finalarray[i][j];

               if(max * .75f <= (value) )
               {
                   path.SetColor("0xE6E6FF");
               }
                else if(max * .50f <= (value))
                {
                    path.SetColor("0x644B32");
                }
                else if(max * .10f <= (value))
                {
                    path.SetColor("0x1E961E");

                }

                else if(max * .05f <= (value))
                {
                    path.SetColor("0xFFFFC3");
                }
                else if(max * .05f > (value))
                {
                    path.SetColor("0x1BE4FF");
                }
        }

        public int scale()
        {
            scale = (WIDTH/walk.nsize);

            return scale;
        }

    public double getscaleheight()
    {
        scalefactor = (((double)WIDTH/(double)walk.nsize)/10);
        return scalefactor;
    }

        public boolean edgecase(int x, int y)
        {
            if ((edgey <= y) | (x >= edgex) | (1 >= y) | (1 >= x) | (x== edgex) |(y== edgey))
            {
                return true;
            }
            else {
                return false;
            }
        }

        public boolean checkcorners(int x, int y)
        {
            if ((( 1== y) && (1 == x)) || ((edgey == y) && (x == edgex)) || ((1 == y) && (edgex == x)) || ((1 == x) && (edgey == y)) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void checkside(int x, int y) {
            if ((1 >= y) & (edgex >= x) & (1 <= x) & (edgex >= y) ) {
                //topside
                walk.setY(walk.getY() + 1);

            }
            if ((1 <= y) & (edgex >= x) & (1 <= x) & (edgey <= y) ) {

                //botside
                walk.setY(walk.getY()-1);

            }
            if ((1 <= y) & (edgex >= x) & (1 >= x) & (edgey >= y) ) {

                walk.setX(walk.getX() + 1);

                //leftside
            }
            if ((1 <= y) & (edgex <= x) & (1 <= x) & (edgey >= y) ) {
                walk.setX(walk.getX() - 1);

                //rightside
            }

        }



        public void tpside(int x, int y) {
            if ((1 >= y) & (edgex >= x) & (1 <= x) & (edgex >= y) ) {
                //topside
                walk.setY(walk.getY() + (edgey-1));

            }
            if ((1 <= y) & (edgex >= x) & (1 <= x) & (edgey <= y) ) {

                //botside
                walk.setY(walk.getY()-(edgey-1));

            }
            if ((1 <= y) & (edgex >= x) & (1 >= x) & (edgey >= y) ) {

                walk.setX(walk.getX() +(edgey-1));

                //leftside
            }
            if ((1 <= y) & (edgex <= x) & (1 <= x) & (edgey >= y) ) {
                walk.setX(walk.getX() - (edgey-1));

                //rightside
            }

        }
}
