package helpers;
import java.util.ArrayList;
import java.util.List;

public class MyMap<K,V> {

	private List<Node<K,V>> map ;
	
	public MyMap() {
		// TODO Auto-generated constructor stub
		map = new ArrayList<Node<K,V>>();
	}
	public void put(K key,V value) {
		map.add(new Node<K, V>(key,value));
	}
	public List<Node<K,V>> getMap() {
		return map;
	}
	public K getKey(V value) {
		for(Node<K,V> node : map) {
			if(node.getValue()==value) {
				return  node.getKey();
			}
		}
		return null;
		
		
	}
	public int size() {
		return map.size();
	}
	
	
	
	
}
 