import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.neumont.util.Queue;


public class LinkedList<T> implements Queue<T>
{	
	private Node<T> head;
	private Node<T> tail;
	private int size;
	
	public LinkedList()
	{
		head = null;
		tail = null;
		size = 0;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return new LinkedListIterator<T>(head);
	}

	@Override
	public T poll() 
	{
		T retrievedData;
		if(isEmpty())
		{
			retrievedData = null;
		}
		else if(head == tail)
		{
			retrievedData = head.data;
			head = tail = null;
			size--;
		}
		else
		{
			retrievedData = head.data;
			head = head.nextNode;
			size--;
		}
		return retrievedData;
	}

	@Override
	public boolean offer(T data) 
	{
		if(data == null)
		{
			throw new NullPointerException("The node cannot contain null data.");
		}
		else if(isEmpty())
		{
			Node newNode = new Node(data);
			head = tail = newNode;
		}
		else
		{
			Node newNode = new Node(data);
			tail.nextNode = newNode;
			tail = tail.nextNode;
		}
		size++;
		return true;
	}

	@Override
	public T peek() 
	{
		if(isEmpty())
		{
			throw new IllegalStateException("The list is empty, so no data can be returned.");
		}
		T firstData = head.data;
		return firstData;
	}
	
	private boolean isEmpty()
	{
		return (head == null && tail == null);
	}
	
	public int length()
	{
		return size;
	}
	
	public String toString()
	{
		String listString = "";
		if(head != null && tail != null)
		{
			Node list = head;
			while(list != null)
			{
				listString += list.data + "-> ";
				list = list.nextNode;
			}
		}
		listString += "null";
		return listString;
	}
	
	
	
	private static class Node<T>
	{
		public T data;
		public Node nextNode;
		public Node(T nodeData)
		{
			this.data = nodeData;
			nextNode = null;
		}
	}
	
	
	
	private class LinkedListIterator<T> implements Iterator<T>
	{
		private Node <T> currentNode;
		
		public LinkedListIterator(Node<T> head)
		{
			currentNode = head;
		}
		
		@Override
		public boolean hasNext() 
		{
			return (currentNode != null);
		}

		@Override
		public T next() 
		{
			T retrievedData;
			if(!hasNext())
			{
				throw new NoSuchElementException("The list is empty, so no data can be returned.");
			}
			retrievedData = currentNode.data;
			currentNode = currentNode.nextNode;
			return retrievedData;
		}

		@Override
		public void remove() 
		{
			throw new UnsupportedOperationException();
		}
	}
}
