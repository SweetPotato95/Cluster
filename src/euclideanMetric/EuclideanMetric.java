package euclideanMetric;

import java.util.List;

public class EuclideanMetric {  
  
    /** 
     * 两个向量可以为任意维度，但必须保持维度相同，表示n维度中的两点 
     *  
     * @param list 
     * @param list2 
     * @return 两点间距离 
     */  
    public static double sim_distance(List<Double> list, List<Double> list2) {  
        double distance = 0;  
          
        if (list.size() == list2.size()) {  
            for (int i = 0; i < list.size(); i++) {  
                double temp = Math.pow((list.get(i) - list2.get(i)), 2);  
                distance += temp;  
            }  
            distance = Math.sqrt(distance);  
        }  
        return distance;  
    }  
  
}  