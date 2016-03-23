/* KMeans.java
 *
 * Author: Lohit Velagapudi
 *
 */

import java.util.*;
import java.awt.Color;

public class KMeans
{
   ArrayList<Integer> copy_x=new ArrayList<Integer>();
   ArrayList<Integer> copy_y=new ArrayList<Integer>();
   ArrayList<Float> cent_x=new ArrayList<Float>();
   ArrayList<Float> cent_y=new ArrayList<Float>();

   ArrayList<HashSet<Integer>> cluster_data=new ArrayList<HashSet<Integer>>();
   int copy_k;

  KMeans(ArrayList<Integer> x, ArrayList<Integer> y, int algo, int k)
  {     
      copy_x=x;
      copy_y=y;
      copy_k=k;

      if (algo==1) RoundRobin();
      else if(algo==2) sequentialNbyK();
      else if(algo==3) RandomNbyK();
      else
      {
        System.out.println("Invalid Selection");
        return;
      }
      
      CentroidCalc();

      int changes=1;
      while(changes!=0)
      {
         changes=0;
         Iterator it_x=copy_x.iterator();
         Iterator it_y=copy_y.iterator();
         int i=0;
         while(it_x.hasNext() && it_y.hasNext() && copy_x.size()==copy_y.size())
         {
            float xv=(float)(int)it_x.next();
            float yv=(float)(int)it_y.next();
            Iterator it_cx=cent_x.iterator();
            Iterator it_cy=cent_y.iterator();
            float small_dist=0;
            int cent_no=0;
            int to_cent=0;
            while(cent_x.size()==cent_y.size() && it_cx.hasNext() && it_cy.hasNext())
            {
              float cx=(float)it_cx.next();
              float cy=(float)it_cy.next();
           
              float dist=(cx-xv)*(cx-xv)+(cy-yv)*(cy-yv);
              if(small_dist==0) small_dist=dist;
              if(dist<=small_dist) 
              {
                 small_dist=dist;
                 to_cent=cent_no;
              }
              cent_no++;
            }
            if(!cluster_data.get(to_cent).contains(i))
            {
               changes++;
               Iterator it_s=cluster_data.iterator();
               while(it_s.hasNext())
               {
                  HashSet<Integer> set_r=(HashSet<Integer>) it_s.next();
                  set_r.remove(i);
               }
               cluster_data.get(to_cent).add(i);
            }
 
            i++;
         }
       
         cent_x.clear();
         cent_y.clear();
    
         CentroidCalc();
      }
      dispCluster();
      errorCalc();
   }

   void errorCalc()
   {
      Iterator it_cd= cluster_data.iterator();
      Iterator it_cx=cent_x.iterator();
      Iterator it_cy=cent_y.iterator();
      int i=1;
      while(it_cd.hasNext() && it_cx.hasNext() && it_cy.hasNext())
      {
         HashSet<Integer> temp=(HashSet<Integer>) it_cd.next();
         float cx=(float) it_cx.next();
         float cy=(float) it_cy.next();
         Iterator it_s=temp.iterator();
         float sum_of_errors=0;
         while(it_s.hasNext())
         {
           int v=(int)it_s.next();
           sum_of_errors=sum_of_errors+((cx-(float)copy_x.get(v))*(cx-(float)copy_x.get(v))+(cy-(float)copy_y.get(v))*(cy-(float)copy_y.get(v)));
         }
         if (temp.size()==0) System.out.println("Cluster "+i+" is empty");
         else System.out.println("Error of cluster "+i+" is "+sum_of_errors);
         i++;
      }
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
     new Visualize("K-Means").showPoints(P);    
   }

   void CentroidCalc()
   {
      Iterator it_al=cluster_data.iterator();
      while(it_al.hasNext())
      {
         HashSet<Integer> temp_set=(HashSet<Integer>)it_al.next();
         Iterator it_set=temp_set.iterator();
         int count=0;
         int sum_x=0;
         int sum_y=0;
         while(it_set.hasNext())
         {
            int set_no=(int)it_set.next();
            sum_x=sum_x+copy_x.get(set_no);
            sum_y=sum_y+copy_y.get(set_no);
            count++;
          }
          cent_x.add((float)sum_x/count);
          cent_y.add((float)sum_y/count);
      }

   }


   void RoundRobin()
   { 
      addBlankSets();

      //assigning values in the round robin method
      Iterator it_cd=cluster_data.iterator();
      int j=0;
      while(it_cd.hasNext() && j<copy_x.size())
      {
        HashSet<Integer> temp=(HashSet<Integer>)it_cd.next();
        temp.add(j);
        if(!it_cd.hasNext()) it_cd=cluster_data.iterator();
        j++;
      }
   }

   void sequentialNbyK()
   {
      addBlankSets();

      int reqd_val=(int)copy_x.size()/copy_k;

      Iterator it_cd=cluster_data.iterator();
      int i=0;
      while(it_cd.hasNext())
      {
         HashSet<Integer> temp=(HashSet<Integer>) it_cd.next();
         int count=0;
         while(count<reqd_val)
         {
           temp.add(i);
           i++;
           count++;
         }
         
         if(i==(reqd_val*copy_k) && i<copy_x.size())
         {
           while(i<copy_x.size())
           {
             temp.add(i);
             i++;
           }
         }               
      }
   }

   void RandomNbyK()
   {
      addBlankSets();
      int reqd_val=(int)copy_x.size()/copy_k;

      Random rn=new Random();
      int n=rn.nextInt(copy_x.size()/10);

      Iterator it_cd=cluster_data.iterator();
      HashSet<Integer> clustered=new HashSet<Integer>();
      int i=0;
      while(it_cd.hasNext())
      {
         HashSet<Integer> temp=(HashSet<Integer>) it_cd.next();
         int count=0;
         while(count<n && i<copy_x.size())
         {
           temp.add(i);
           clustered.add(i);
           i++;
           count++;
         }
         if(i==(reqd_val*copy_k) && i<copy_x.size())
         {
           for(int j=0; j<copy_x.size(); j++)
           {
             if(!clustered.contains(j))
             {
               temp.add(j);
               clustered.add(j);
               i++;
             }
           }
         }
         if(!it_cd.hasNext() && i<copy_x.size()) it_cd=cluster_data.iterator();
      }
   }

   void addBlankSets()
   {
      //adding blank sets to the arraylist to iterate and add values to them later
      for (int i=0; i<copy_k; i++)
      {
         HashSet<Integer> temp=new HashSet<Integer>();
         cluster_data.add(temp);
      }
   }

}  
