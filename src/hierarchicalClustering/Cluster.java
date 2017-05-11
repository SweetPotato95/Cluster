package hierarchicalClustering;  
  
import java.util.ArrayList;
import java.util.List;  
  
/** 
 * @author shenchao 
 *  
 *      封装每一个聚类 
 * 
 */  
public class Cluster {  
      
    /** 
     * 左节点 
     */  
    private Cluster left;  
    /** 
     * 右节点 
     */  
    private Cluster right;  
    /** 
     * data 
     */  
    private List<Double> vector;  
    /** 
     * id 
     */  
    private int id;  
    /** 
     * 距离 
     */  
    private double distance;  
    /** 
     * 标签名 
     */  
    private String name;  
      
    public Cluster(){}  
      
    public Cluster(Cluster left, Cluster right, List<Double> vector, int id,  
            double distance) {  
        this.left = left;  
        this.right = right;  
        this.vector = vector;  
        this.id = id;  
        this.distance = distance;  
    }  
  
    public Cluster getLeft() {  
        return left;  
    }  
  
    public void setLeft(Cluster left) {  
        this.left = left;  
    }  
  
    public Cluster getRight() {  
        return right;  
    }  
  
    public void setRight(Cluster right) {  
        this.right = right;  
    }  
  
    public List<Double> getVector() {  
        return vector;  
    }  
  
    public void setVector(List<Double> vector) {  
        this.vector = vector;  
    }  
  
    public int getId() {  
        return id;  
    }  
  
    public void setId(int id) {  
        this.id = id;  
    }  
  
    public double getDistance() {  
        return distance;  
    }  
  
    public void setDistance(double distance) {  
        this.distance = distance;  
    }  
      
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
    
    public void getChild(List<Cluster> res){
    	if(this.name!=null){
    		res.add(this);
    	}
    	if(this.left!=null){
    		this.left.getChild(res);
    	}
    	if(this.right!=null){
    		this.right.getChild(res);
        		
    	}
    	
    }
}  