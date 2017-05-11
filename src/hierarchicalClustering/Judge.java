package hierarchicalClustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import euclideanMetric.EuclideanMetric;

public class Judge{
	ArrayList<ArrayList<ArrayList<Double>>> points = new ArrayList<ArrayList<ArrayList<Double>>>();
	ArrayList<Double> max_Distance = new ArrayList<Double>();
	ArrayList<Double> min_Distance = new ArrayList<Double>();
	
	String dataPath = "C:\\Graduate Design\\Cluster\\model.txt";
	public Judge(){
		
	}
	public void loadData() throws IOException{
		FileReader fr = new FileReader(dataPath);
		BufferedReader br = new BufferedReader(fr);
		while(true){
			String tmpLine = br.readLine();
			if(tmpLine == null)break;
			String[] tmpList = tmpLine.split(",");
			max_Distance.add(Double.parseDouble(tmpList[2]));
			min_Distance.add(Double.parseDouble(tmpList[3]));
			
			int size = Integer.parseInt(tmpList[1]);
			ArrayList<ArrayList<Double>> tmpSet = new ArrayList<ArrayList<Double>>();
			for(int i = 0 ;i<size;i++){
				String[] tLine = br.readLine().split(",");
				ArrayList<Double> tmpPoint = new ArrayList<Double>();
				for(int j = 1; j<tLine.length;j++){
					tmpPoint.add(Double.parseDouble(tLine[j]));
				}
				tmpSet.add(tmpPoint);
			}
			points.add(tmpSet);
		}
	}
	
	public void getPointClass(List<Double> judgedPoint){
		int pointClass = -1;
		double minDistance = 9999;
		double per_inMax = 0;
		double per_inMin = 0;
		for(int i = 0;i<points.size();i++){
			ArrayList<ArrayList<Double>> tmp = points.get(i);
			double dis = 0;
			for(int j = 0;j<tmp.size();j++){
				dis+= EuclideanMetric.sim_distance(judgedPoint, tmp.get(j));
				
			}
			dis/=tmp.size();
			//System.out.println(i+","+dis);
			if(dis<minDistance){
				pointClass = i;
				minDistance = dis;
			}
		}
		
		per_inMax = minDistance/max_Distance.get(pointClass);
		per_inMin = minDistance/min_Distance.get(pointClass);
		//System.out.println(judgedPoint);
		if(per_inMax>1.2){
			System.out.println("找不到分类！");
		}
		else{
			System.out.println("属于第"+pointClass+"类！");
		}
		//System.out.println(pointClass+","+minDistance+","+per_inMax+","+per_inMin);
		
		
	}
}