package main;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dataloader.ConfigReader;
import dataloader.EntryBuilder;
import dataloader.SchemaBuilder;
import entity.DataEntry;
import entity.Schema;
import entity.TreeNode;
public class TrainWithPrune {
	public static void main(String args[]) throws Exception{
		TreeNode node = null;
		SchemaBuilder sbuilder = new SchemaBuilder();
		EntryBuilder ebuilder = new EntryBuilder();
		String FileName = ConfigReader.getTrainFile();
		Schema schema = sbuilder.BuildSchema(FileName);
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < schema.length() - 1; i++)
			set.add(i);
		ArrayList<DataEntry> e = ebuilder.buildEntry(FileName, schema,true);
		ArrayList<DataEntry> train = new ArrayList<DataEntry>();
		ArrayList<DataEntry> verification = new ArrayList<DataEntry>();
		for(int i=0;i<e.size()*ConfigReader.getLearningSize();i++){
			train.add(e.get(i));
		}
		for(int i=train.size();i<e.size();i++){
			verification.add(e.get(i));
		}
		node = Train.BuildModel(train, schema);
		node = doPrune(node,verification,schema);
		System.out.println("After Pruning:"+Validate.doValid(e, node, schema));
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("Prune_"+ConfigReader.getModelFile()));
		os.writeObject(node);
		os.close();
	}
	public static TreeNode doPrune(TreeNode root,ArrayList<DataEntry> e,Schema schema) throws Exception{
		ArrayList<List<TreeNode>> levelOrder = new ArrayList<List<TreeNode>>();
		System.out.println("data size:"+e.size()*9);
		int level = 1;
		int rest =  0;
		if(root==null)
			return root;
		int count = 1;
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);
		List<TreeNode> buffer = new ArrayList<TreeNode>();
		while(!queue.isEmpty()){
			TreeNode node = queue.poll();
			buffer.add(node);
			level--;
			for(TreeNode child:node.getChildren()){
				queue.add(child);
				rest++;
				count++;
			}
			if(level==0){
				level = rest;
				levelOrder.add(buffer);
				rest = 0;
				buffer = new ArrayList<TreeNode>();
			}
		}
		System.out.println("level order store finished.. Total Node:"+count);
		System.out.println("Start pruning.....");
		System.out.println("Loading validate data");
		double CurrentAccuracy = Validate.doValid(e, root, schema);
		System.out.println(CurrentAccuracy*100+"%");
		boolean keep = true;
		int minus = 0;
		for(int i=levelOrder.size()-2;i>=0&&keep;i--){
			//System.out.println("Pruning:"+(1-(double)i/(double)levelOrder.size())*100+"%");
			int prune = 0;
			for(TreeNode node:levelOrder.get(i)){
				double a = Validate.doValid(e, root, schema);
			//	System.out.println("before:"+a);
				String nodeName = node.getNodeName();
				node.setNodeName("LeafNode");
				ArrayList<TreeNode> temp = new ArrayList<TreeNode>(node.getChildren());
				node.setChildren(new ArrayList<TreeNode>());
				node.setValue(node.mostTargetValue());
				double b = Validate.doValid(e, root, schema);
			//	System.out.println("after:"+b);
				if(b<a){
					node.setChildren(temp);
					node.setNodeName(nodeName);
					node.setValue("");
				}else{
					//System.out.println("prune!");
					prune++;
					minus++;
				}
			}
			if(prune==0){
				//nothing prune at this layer
				keep = false;
			}
		}
		System.out.println("Removed:"+minus);
		return root;
	}
}
