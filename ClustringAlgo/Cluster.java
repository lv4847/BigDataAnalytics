/* Cluster.java
 *
 * Author: Lohit Velagapudi
 *
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Cluster
{
    public static ArrayList<Integer> x=new ArrayList<Integer>();
    public static ArrayList<Integer> y=new ArrayList<Integer>();

    public static void main(String[] args)
    {
       String Algorithm=args[0];
       String TextFile=args[1];
       getText(TextFile);
       chooseAlgo(Algorithm, args[2], args[3]);
    }
    
    private static void getText(String TextFile)
    {
       try{ 
          BufferedReader br=new BufferedReader(new FileReader(TextFile));
          String line=br.readLine();
                    while(line != null)
          {   
            String[] parts=line.split("\\t");
            x.add(Integer.parseInt(parts[0]));
            y.add(Integer.parseInt(parts[1]));
            line=br.readLine();
          }
       }catch(Exception e){System.out.println("File Error: "+e);}
    }	

    private static void chooseAlgo(String Algo, String Arg3, String Arg4)
    {
       if(Algo.equalsIgnoreCase("K"))
       {
          new KMeans(x,y,Integer.parseInt(Arg3), Integer.parseInt(Arg4));
       }
       else if(Algo.equalsIgnoreCase("D"))
       {
          new DBSCAN(x,y,Integer.parseInt(Arg3), Integer.parseInt(Arg4));
       }
    }  
}
