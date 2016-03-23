/* Point.java
 *
 * Author: Lohit Velagapudi
 *
 */




import java.awt.Color;

public class Point{
  int x;
  int y;
  Color color;

  Point(int a, int b, Color c)
  {
     x=a;
     y=b;
     color=c;
  }
 
  int getX()
  {
     return x;
  }

  int getY()
  {
    return y;
  }
  
  Color getColor()
  {
     return color;
  }
}
