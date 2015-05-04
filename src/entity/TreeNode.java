package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import tools.Tool;

public class TreeNode implements Serializable{
	private static final long serialVersionUID = 1L;
	ArrayList<TreeNode> children;
	ArrayList<String> path; 
	HashMap<String,Long> TargetValueMap;
	public HashMap<String, Long> getTargetValueMap() {
		return TargetValueMap;
	}
	public void setTargetValueMap(HashMap<String, Long> targetValueMap) {
		TargetValueMap = targetValueMap;
	}
	String Value;
	int    index;
	String NodeName;
	int size;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public ArrayList<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<TreeNode> children) {
		this.children = children;
	}
	public void addChildren(TreeNode node){
		this.children.add(node);
	}
	public ArrayList<String> getPath() {
		return path;
	}
	public String getPath(int i) {
		return path.get(i);
	}
	public void setPath(ArrayList<String> path) {
		this.path = path;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getNodeName() {
		return NodeName;
	}
	public void setNodeName(String nodeName) {
		NodeName = nodeName;
	}
	public TreeNode(){
		children = new ArrayList<TreeNode>();
		path = new ArrayList<String>();
	}
	public String showPath(){
		String returnMessage = "(";
		for(String p:path){
			returnMessage+=" "+p+" ";
		}
		returnMessage+=")";
		return returnMessage;
	}
	public void addPath(String s){
		path.add(s);
	}
	public boolean isLeaf(){
		return this.NodeName.toLowerCase().startsWith("leaf");
	}
	public TreeNode getMostChild(){
		int max = 0;
		TreeNode result = null;
		for(TreeNode c:children){
			if(c.getSize()>max){
				max = c.getSize();
				result = c;
			}
		}
		return result;
	}
	public TreeNode findChild(String data,boolean isdiscrete){
		if(data.equals("?"))
			return getMostChild();
		if(!isdiscrete){
			String cmp = getPath().get(0).replace("<=", "");
			if(Tool.cmp(data, cmp)){
				return children.get(0);
			}
			return children.get(1);
		}else{
			for(int i=0;i<path.size();i++){
				String cmp = path.get(i).replace("=", "");
				if(Tool.equal(data, cmp)){
					return children.get(i);
				}
			}
		}
		return getMostChild();
	}
	public String mostTargetValue(){
		Iterator<Entry<String, Long>> it = TargetValueMap.entrySet().iterator();
		String returnMessage = null;
		long max = Long.MIN_VALUE;
		while(it.hasNext()){
			Entry<String,Long> e = it.next();
			if(e.getValue()>max){
				max = e.getValue();
				returnMessage = e.getKey();
			}
		}
		return returnMessage;
	}
}
