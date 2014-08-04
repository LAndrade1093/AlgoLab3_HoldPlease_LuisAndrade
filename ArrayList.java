import java.util.Arrays;
import java.util.Iterator;
import edu.neumont.util.List;

public class ArrayList<T> implements List<T>
{
	private T[] array;
	private int numOfElements;
	
	public ArrayList()
	{
		array = (T[])new Object[0];
		numOfElements = 0;
	}
	
	public ArrayList(int initialCapacity)
	{
		array = (T[])new Object[initialCapacity];
		numOfElements = 0;
	}

	@Override
	public Iterator<T> iterator() 
	{
		return Arrays.asList(array).iterator();
	}

	@Override
	public boolean add(T data) 
	{
		if(numOfElements >= array.length)
		{
			increaseArraySize();
		}
		array[numOfElements] = data;
		numOfElements++;
		return true;
	}

	@Override
	public T get(int index) 
	{
		if(index >= array.length || index < 0)
		{
			throw new IndexOutOfBoundsException("Index " + index + " is outside the bounds of the array");
		}
		T retrievedData = array[index];
		return retrievedData;
	}

	@Override
	public boolean remove(T data) 
	{
		boolean found = false;
		int index = 0;
		for(int i = 0; i < array.length && !found; i++)
		{
			if(array[i] == data)
			{
				array[i] = null;
				index = i;
				found = true;
			}
		}
		
		if(found)
		{
			decreaseArraySize(index);
			numOfElements--;
		}
		return found;
	}
	
	public T set(int index, T element)
	{
		T replaced = array[index];
		array[index] = element;
		return replaced;
	}

	@Override
	public int size() 
	{
		return numOfElements;
	}
	
	public String toString()
	{
		String arrayString = "";
		if(numOfElements > 0)
		{
			for(int i = 0; i < array.length; i++)
			{
				arrayString += array[i] + ", ";
			}
		}
		return arrayString;
	}
	
	private void increaseArraySize()
	{
		T[] newArray = (T[])new Object[array.length + 1];
		for(int i = 0; i < array.length; i++)
		{
			newArray[i] = array[i];
		}
		array = newArray;
	}
	
	private void decreaseArraySize(int removedIndex)
	{
		T[] newArray = (T[])new Object[array.length - 1];
		for(int i = 0; i < newArray.length; i++)
		{
			if(i < removedIndex)
			{
				newArray[i] = array[i];
			}
			else
			{
				newArray[i] = array[i+1];
			}
		}
		array = newArray;
	}

}















