package tools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import entity.DataEntry;
import entity.Schema;
import entity.SplitResult;
import entity.TreeNode;
public class TreeTools {
	static int count = 0;
	public static TreeNode BuildTree(Schema schema,List<DataEntry> data, Set<Integer> attributes) throws Exception{
		TreeNode node = new TreeNode();
		node.setSize(data.size());
		node.setTargetValueMap(EntropyTools.getTargetValueMap(data));
		if(EntropyTools.isPure(data)||attributes.size()==0){
			node.setNodeName("LeafNode");
			node.setValue(EntropyTools.getMostTargetValue(data));
			return node;
		}
		SplitResult best = null;
		double GainInfoRatio = 0;
		int BestAttributes = -1;
		for(Integer attributesNum:attributes){
			SplitResult result = EntropyTools.getSplitResult(attributesNum, schema, data);
			//System.out.println(schema.getName(attributesNum)+","+result.getInfoGain());
			if(result.getInfoGainRatio()>GainInfoRatio){
				best = result;
				GainInfoRatio = result.getInfoGainRatio();
				BestAttributes = attributesNum;
			}
		}
		//Can not split anymore, the rest attributes is helpless
		if(GainInfoRatio==0){
			node.setNodeName("LeafNode");
			node.setValue(EntropyTools.getMostTargetValue(data));
			return node;
		}
		//System.out.println("Won:"+schema.getName(BestAttributes)+","+best.getInfoGain());
		node.setIndex(BestAttributes);
		node.setNodeName(schema.getName(BestAttributes));
		//attributes.remove(BestAttributes);
		for(int i=0;i<best.length();i++){
			node.addPath(best.getDescribe(i));
			node.addChildren(BuildTree(schema,new ArrayList<DataEntry>(best.getSplitData(i)),
					new HashSet<Integer>(attributes)));
		}
		//attributes.add(BestAttributes);
		return node;
	}
	public static void printTree(TreeNode root){
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		if(root==null)
			return;
		queue.add(root);
		int level = 1;
		int rest =  0;
		while(!queue.isEmpty()){
			TreeNode node = queue.poll();
			level--;
			if(node.getNodeName().toLowerCase().startsWith("leaf"))
				System.out.print(" "+node.getValue());
			else
				System.out.print(" "+node.getNodeName()+node.showPath());
			for(TreeNode n:node.getChildren()){
				queue.add(n);
				rest++;
			}
			if(level==0){
				level = rest;
				rest = 0;
				System.out.println();
			}
		}
		return;
	}
	public static void printDNF(TreeNode root){
		count = 0;
		DFS(root,new ArrayList<String>());
	}
	private static boolean DFS(TreeNode root,ArrayList<String>Path){
		if(count>16)
			return false;
		if(root.isLeaf()){
			count +=1;
			System.out.print("If: "+Path.get(0));
			for(int i=1;i<Path.size();i++){
				System.out.print("&"+Path.get(i));
			}
			System.out.println();
			System.out.println("Then:"+root.getValue());
		}else{
			for(int i=0;i<root.getChildren().size();i++){
				Path.add(root.getNodeName()+root.getPath(i));
				boolean result = DFS(root.getChildren().get(i),Path);
				Path.remove(Path.size()-1);
				if(!result)
					break;
				
			}
		}
		if(count>16){
			return false;
		}
		return true;
	}
}
