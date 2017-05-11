package hierarchicalClustering;  
  
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;

import euclideanMetric.EuclideanMetric;  
  
public class HierarchicalClustering {  
  
    private List<Cluster> clusters = null;  
    String dataPath = "C:\\Graduate Design\\Cluster\\data.txt";
    String modelPath = "C:\\Graduate Design\\Cluster\\model.txt";
    static String testPath = "C:\\Graduate Design\\Cluster\\testPoints.txt";
    public HierarchicalClustering() throws IOException {  
        initData();  
    }  
  
    /** 
     * 初始化数据集 
     *  
     * @throws IOException 
     */  
    private void initData() throws IOException {  
        clusters = new ArrayList<Cluster>();  
        FileReader fr = new FileReader(dataPath);
		BufferedReader bufferedReader = new BufferedReader(fr);  
        String line = null;  
        int i = 0;  
        while ((line = bufferedReader.readLine()) != null) {  
            String[] s = line.split(" ");  
            Cluster cluster = new Cluster();  
            List<Double> list = new ArrayList<Double>();
            int index = 1;
            for (String string : s) {  
                try{  
                    list.add(Double.parseDouble(string));  
                }catch(Exception e) {  
                      
                }  
            }
            cluster.setName("point"+index);
            cluster.setId(i++);  
            cluster.setVector(list);  
            index++;
            clusters.add(cluster);  
        }
    }  
  
    public Cluster hcluster() {  
        // 用distances来缓存任意两聚类之间的距离,其中map集合的键为两个聚类的id  
        Map<Integer[], Double> distances = new HashMap<Integer[], Double>();  
  
        int currentId = -1;  
  
        while (clusters.size() > 3) {  
            // 最短距离的两聚类id  
            int lowestpair1 = 0;  
            int lowestpair2 = 1;  
            // 最短距离  
            double closest = EuclideanMetric.sim_distance(clusters.get(0)  
                    .getVector(), clusters.get(1).getVector());  
            
            for (int i = 0; i < clusters.size(); i++) {  
                for (int j = i + 1; j < clusters.size(); j++) {  
                    Integer[] key = { clusters.get(i).getId(),  
                            clusters.get(j).getId() };  
                    if (!distances.containsKey(key)) {  
                        distances.put(key, EuclideanMetric.sim_distance(  
                                clusters.get(i).getVector(), clusters.get(j)  
                                        .getVector()));  
                    }
                    double d = distances.get(key);  
                    if (d < closest) {  
                        closest = d;  
                        lowestpair1 = i;  
                        lowestpair2 = j;  
                    }  
                }  
            }  
  
            // 计算两个最短距离聚类的平均值  
            List<Double> midvec = mergevec(clusters.get(lowestpair1),  
                    clusters.get(lowestpair2));  
              
            Cluster cluster = new Cluster(clusters.get(lowestpair1),clusters.get(lowestpair2),midvec,currentId,closest);  
  
            currentId -= 1;  
              
            //注意删除顺序，先删除大的id号，否则会出现越界  
            if (lowestpair1 < lowestpair2) {  
                clusters.remove(clusters.get(lowestpair2));  
                clusters.remove(clusters.get(lowestpair1));  
            }else {  
                clusters.remove(clusters.get(lowestpair1));  
                clusters.remove(clusters.get(lowestpair2));  
            }  
            clusters.add(cluster);  
        }  
        return clusters.get(0);  
    }  
  
    private List<Double> mergevec(Cluster cluster1, Cluster cluster2) {  
        List<Double> midvec = new ArrayList<Double>();  
        for (int i = 0; i < cluster1.getVector().size(); i++) {  
            midvec.add((cluster1.getVector().get(i) + cluster2.getVector().get(i)) / 2.0);  
        }  
        return midvec;    
    }  
      
    /** 
     * 打印输出 
     */  
    public void printCluster(Cluster cluster,int n) {  
        for (int i = 0; i < n; i++) {  
            System.out.print("  ");  
        }  
        //负数标记代表这是一个分支  
        if (cluster.getId() < 0) {  
            System.out.println("-");  
        }else {  
            //代表是一个叶子节点   
            System.out.println(cluster.getName());  
        }  
          
        if (cluster.getLeft()!= null) {  
            printCluster(cluster.getLeft(),++n);  
        }  
        if (cluster.getRight()!=null) {  
            printCluster(cluster.getRight(), ++n);  
        }  
    }
    
    public ArrayList<ArrayList<Cluster>> saveSets() throws IOException{
    	ArrayList<ArrayList<Cluster>> res = new ArrayList<ArrayList<Cluster>>();
    	FileWriter fw = new FileWriter(modelPath);
    	BufferedWriter bw = new BufferedWriter(fw);
    	
    	for(int i = 0 ;i<clusters.size();i++){
    		ArrayList<Cluster> tmp = new ArrayList<Cluster>();
    		clusters.get(i).getChild(tmp);
    		System.out.println(tmp.size());
    		int tmpSize = tmp.size();
    		double maxDis = 0;
    		double minDis = 9999;
    		for(int j = 0;j<tmpSize;j++){
    			double dis = 0;
    			for(int k = 0;k<tmpSize;k++){
    				dis+=EuclideanMetric.sim_distance(tmp.get(k).getVector(), tmp.get(j).getVector());
    			}
    			dis/= tmpSize;
    			//System.out.println(j+","+dis);
    			if(dis>maxDis){
    				maxDis = dis;
    			}	
    			if(dis<minDis){
    				minDis = dis;
    			}
    		}
    		fw.write(i+","+tmpSize+","+maxDis+","+minDis+"\r\n");
    		for(int j = 0;j<tmpSize;j++){
    			fw.write(String.valueOf(i));
    			List<Double> tmpd = tmp.get(j).getVector();
    			for(int k=0;k<tmpd.size();k++){
    				fw.write(","+tmpd.get(k).toString());
    			}
    			fw.write("\r\n");
    		}
    		res.add(tmp);
    		//System.out.println(tmpRes.size()+tmpRes.toString());
    	}
    	bw.flush();
    	bw.close();
    	return res;
    	
    }
    public void ml() throws IOException{
    	hcluster();
        saveSets();
        
    }
    public static void main(String[] args) throws IOException {  
        HierarchicalClustering hierarchicalClustering = new HierarchicalClustering();  
        //hierarchicalClustering.printCluster(hierarchicalClustering.hcluster(), 0);
        hierarchicalClustering.ml();
        
        //Test Process
        Judge judge = new Judge();
        judge.loadData();
        ArrayList<Double> testPoints= new ArrayList<Double>();
        FileReader fr = new FileReader(testPath);
        BufferedReader br = new BufferedReader(fr);
        String tmpLine = "";
        while((tmpLine = br.readLine())!=null){
        	String[] tmpList = tmpLine.split(" ");
        	List<Double> askPoint = new ArrayList<Double>();
            for(int i = 0;i<tmpList.length;i++){
            	askPoint.add(Double.parseDouble(tmpList[i]));
            }
            judge.getPointClass(askPoint);
        }
        
    }  
  
}
