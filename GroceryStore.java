import java.util.Iterator;

import edu.neumont.util.Client;
import edu.neumont.util.QueueableService;


public class GroceryStore implements QueueableService 
{
	private int numberOfLines;
	private int numberOfClients;
	private ArrayList<LinkedList<Client>> checkoutLines = new ArrayList<LinkedList<Client>>();
	
	public GroceryStore(int numOfCheckoutLines)
	{
		numberOfLines = numOfCheckoutLines;
		for(int i = 0; i < numberOfLines; i++)
		{
			checkoutLines.add(new LinkedList<Client>());
		}
		numberOfClients = 0;
	}
	
	@Override
	public void advanceMinute() 
	{
		for(int i = 0; i < checkoutLines.size(); i++)
		{
			if(checkoutLines.get(i).length() > 0)
			{
				int clientServiceTime = checkoutLines.get(i).peek().servedMinute();
				if(clientServiceTime <= 0)
				{
					numberOfClients--;
					Client removedClient = checkoutLines.get(i).poll();
				}
			}
		}
	}
	
	@Override
	public double getAverageClientWaitTime() 
	{
		double overallWaitTime = 0.0;
		for(int i = 0; i < checkoutLines.size(); i++)
		{
			double lineWaitTime = 0.0f;
			Iterator<Client> clientIterator = checkoutLines.get(i).iterator();
			while(clientIterator.hasNext())
			{
				lineWaitTime += getClientWaitTime(clientIterator.next());
			}
			overallWaitTime += lineWaitTime;
		}
		overallWaitTime = (numberOfClients > 0) ? overallWaitTime/(double)numberOfClients : 0.0;
		
		return overallWaitTime;
	}

	@Override
	public double getClientWaitTime(Client client) 
	{
		double clientWaitTime = 0.0;
		boolean found = false;
		for(int i = 0; i < checkoutLines.size() && !found; i++)
		{
			double currentTotalTime = 0.0f;
			Iterator<Client> clientIterator = checkoutLines.get(i).iterator();
			while(!found && clientIterator.hasNext())
			{
				Client currentClient = clientIterator.next();
				if(currentClient.equals(client))
				{
					found = true;
					clientWaitTime = currentTotalTime;
				}
				else
				{
					currentTotalTime += currentClient.getExpectedServiceTime();
				}
			}
		}
		if(!found)
		{
			clientWaitTime = 0.0;
		}
		return clientWaitTime;
	}
	
	//This was in the Lab Instructions, so I'm adding it just in case
	public void addCustomer(Client client)
	{
		addClient(client);
	}

	@Override
	public boolean addClient(Client client) 
	{
		int shorterLineIndex = 0;
		for(int i = 0; i < checkoutLines.size(); i++)
		{
			if(checkoutLines.get(i).length() < checkoutLines.get(shorterLineIndex).length())
			{
				shorterLineIndex = i;
			}
		}
		numberOfClients++;
		checkoutLines.get(shorterLineIndex).offer(client);
		return true;
	}
	
	public String toString()
	{
		String store = "";
		for(int i = 0; i < checkoutLines.size(); i++)
		{
			store += "Checkout Line " + (i + 1) + ": ";
			Iterator<Client> clientIterator = checkoutLines.get(i).iterator();
			while(clientIterator.hasNext())
			{
				store += clientIterator.next().getExpectedServiceTime() + "->";
			}
			store += "null\n";
		}
		return store;
	}
}
