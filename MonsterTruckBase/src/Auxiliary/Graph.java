package Auxiliary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Graph<T> {

    public static class GraphItem<T> {

	private Set<GraphItem<T>> adjacentVertex = new HashSet<GraphItem<T>>();
	private final T vertex;

	private GraphItem(T object) {
	    this.vertex = (T) object;
	}

	private void addAdjacentVertex(GraphItem<T> item) {
	    if (!adjacentVertex.contains(item)) {
		adjacentVertex.add(item);
		item.addAdjacentVertex(this);
	    }
	}

	private void addDirectedAdjacentVertex(GraphItem<T> item) {
	    if (!adjacentVertex.contains(item)) {
		adjacentVertex.add(item);
	    }
	}

	public T getObect() {
	    return vertex;
	}

    }

    private HashMap<T, GraphItem<T>> itemList = new HashMap<T, GraphItem<T>>();

    public void addVertex(T vertex) {
	if (!itemList.containsKey(vertex)) {
	    itemList.put(vertex, new GraphItem<T>(vertex));
	}
    }

    public boolean contains(T vertex) {
	return itemList.containsKey(vertex);
    }

    public void clear() {
	for (GraphItem<T> item : itemList.values()) {
	    item.adjacentVertex.clear();
	}
	itemList.clear();
    }

    public void addConnection(T vertex1, T vertex2) {
	if (!itemList.containsKey(vertex1))
	    itemList.put(vertex1, new GraphItem<T>(vertex1));
	if (!itemList.containsKey(vertex2))
	    itemList.put(vertex2, new GraphItem<T>(vertex2));
	itemList.get(vertex1).addAdjacentVertex(itemList.get(vertex2));
    }

    public void addDirectedConnection(T vertex1, T vertex2) {
	if (!itemList.containsKey(vertex1))
	    itemList.put(vertex1, new GraphItem<T>(vertex1));
	if (!itemList.containsKey(vertex2))
	    itemList.put(vertex2, new GraphItem<T>(vertex2));
	itemList.get(vertex1).addDirectedAdjacentVertex(itemList.get(vertex2));
    }

    public Iterable<T> getVertexIterable() {
	return itemList.keySet();
    }
    
    public Set<T> getVertexSet(){
	return itemList.keySet();
    }

    public List<T> getAdjacentVertexList(T vertex) {
	if (!itemList.containsKey(vertex))
	    return null;
	List<T> retList = new LinkedList<T>();
	for (GraphItem<T> item : itemList.get(vertex).adjacentVertex) {
	    retList.add(item.vertex);
	}
	return retList;
    }
    

    public Iterable<T> getAdjacentVertexIterable(final T vertex) {
	if (!itemList.containsKey(vertex))
	    return null;
	return new Iterable<T>() {

	    Iterator<GraphItem<T>> iterator = itemList
		    .get(vertex).adjacentVertex.iterator();

	    @Override
	    public Iterator<T> iterator() {
		return new Iterator<T>() {

		    @Override
		    public boolean hasNext() {
			return iterator.hasNext();
		    }

		    @Override
		    public T next() {
			return iterator.next().vertex;
		    }
		};
	    }
	};
    }

}
