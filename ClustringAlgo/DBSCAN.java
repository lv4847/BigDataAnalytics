/* DBSCAN.java
 *
 * Author: Lohit Velagapudi
 *
 */


import java.util.*;
import java.awt.Color;

class DBSCAN
{
   ArrayList<Integer> copy_x=new ArrayList<Integer>();
   ArrayList<Integer> copy_y=new ArrayList<Integer>();
   ArrayList<Integer> Noise= new ArrayList<Integer>();
   HashSet<Integer> Visited=new HashSet<Integer>();
   ArrayList<HashSet<Integer>> cluster_data=new ArrayList<HashSet<Integer>>();

   DBSCAN(ArrayList<Integer> x, ArrayList<Integer> y, int minpts, int rad )
   {
      copy_x=x;
      copy_y=y;

      Iterator it_x=copy_x.iterator();
      Iterator it_y=copy_y.iterator();
      int pos=0;
      while(it_x.hasNext() && it_y.hasNext())
      {
         int x_now=(int)it_x.next();
         int y_now=(int)it_y.next();
         if(!Visited.contains(pos))
         {
            ArrayList<Integer> Neighbors= getNeighbors(x_now, y_now, rad);
            Visited.add(pos);
            if(Neighbors.size()<minpts)
            {
               Noise.add(pos);
               Visited.add(pos);
            }
            else
            {
              addNeighbors(pos, Neighbors, minpts, rad);
            }
         }
         pos++;
      }
      System.out.println(cluster_data.size()+" density clusters found");
      dispNoise();
      dispCluster();       
   }

   void addNeighbors(int pos, ArrayList<Integer> Neighbors, int minpts, int rad)
   {
     HashSet<Integer> Cluster=new HashSet<Integer>();
     Cluster.add(pos);
  
     ArrayList<Integer> acc_moreNeighbors=new ArrayList<Integer>();
     boolean to_enter=true;
      while(acc_moreNeighbors.size()>0 || to_enter)
     {
       acc_moreNeighbors.clear();
       to_enter=false;
       Iterator it_n=Neighbors.iterator();
       while(it_n.hasNext())
       {
        int t=(int)it_n.next();
        if(!Visited.contains(t))
        {
           Cluster.add(t);
           Visited.add(t);
 
           ArrayList<Integer> moreNeighbors=getNeighbors(copy_x.get(t), copy_y.get(t), rad);
           if (moreNeighbors.size()>minpts)
           {
              Iterator it_mn=moreNeighbors.iterator();
              while(it_mn.hasNext())
              {
                 acc_moreNeighbors.add((int)it_mn.next());
              }
           }
         
        }
      }
      Iterator it_amn=acc_moreNeighbors.iterator();
      while(it_amn.hasNext())
      {
        Neighbors.add((int)it_amn.next());
      }
     }
     cluster_data.add(Cluster);
   }

   ArrayList<Integer> getNeighbors(int x, int y, int rad)
   {
     ArrayList<Integer> return_al=new ArrayList<Integer>();
     Iterator it_x=copy_x.iterator();
     Iterator it_y=copy_y.iterator();
     int pos=0;
     while(it_x.hasNext() && it_y.hasNext())
     {
        int x_now=(int)it_x.next();
        int y_now=(int)it_y.next();
        if(!Visited.contains(pos))
        {
           float  dist=(float)Math.sqrt((x-x_now)*(x-x_now)+(y-y_now)*(y-y_now));
     
           if(dist<=(float)rad && dist!=0)
           {
              return_al.add(pos);
           } 
        }
        pos++;
     }
     return return_al;
   }
   
   void dispCluster()
   {
       ArrayList<Point> P=new ArrayList<Point>();
     
       Iterator iter_al=cluster_data.iterator();

       while(iter_al.hasNext())
       {
         Random ran=new Random();
         int r=ran.nextInt(255);
         int b=ran.nextInt(255);
         int g=ran.nextInt(255);
         HashSet<Integer> temp_set=(HashSet<Integer>)iter_al.next();
         Iterator temp_it=temp_set.iterator();
         Color col=new Color(r, b, g);
         while(temp_it.hasNext())
         {
           int n=(int)temp_it.next();
           P.add(new Point(copy_x.get(n), copy_y.get(n), col.darker()));
         }
     }     
     new Visualize("DBSCAN").showPoints(P);    
   }

  void dispNoise()
  {
    Iterator it_n=Noise.iterator();
    while(it_n.hasNext())
    {
      int n=(int)it_n.next();
      System.out.println("Point ["+copy_x.get(n)+","+copy_y.get(n)+"] not assigned to a density cluster");
    }
  }
}
