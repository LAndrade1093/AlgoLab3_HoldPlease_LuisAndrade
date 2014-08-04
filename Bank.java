import java.util.Iterator;

import edu.neumont.util.Client;
import edu.neumont.util.QueueableService;


public class Bank implements QueueableService 
{
	private int numberOfTellers;
	private int clientsInLine;
	private ArrayList<Client> busyBankTellers = new ArrayList<Client>();
	private LinkedList<Client> clientLine = new LinkedList<Client>();
	
	public Bank(int numberOfTellers)
	{
		this.numberOfTellers = numberOfTellers;
		for(int i = 0; i < numberOfTellers; i++)
		{
			busyBankTellers.add(new Client(0));
		}
		clientsInLine = 0;
	}
	
	@Override
	public void advanceMinute() 
	{
		for(int i = 0; i < busyBankTellers.size(); i++)
		{
			if(busyBankTellers.get(i).getExpectedServiceTime() > 0)
			{
				int clientServiceTime = busyBankTellers.get(i).servedMinute();
				if(clientServiceTime <= 0)
				{
					Client nextClient = clientLine.poll();
					if(nextClient != null)
					{
						busyBankTellers.set(i, nextClient);
					}
				}
			}
		}
	}

	@Override
	public double getAverageClientWaitTime() 
	{
		double numberOfClients = (double)clientLine.length() + (double)busyBankTellers.size();
		double totalWaitTime = 0.0;
		Iterator<Client> clientIterator = clientLine.iterator();
		while(clientIterator.hasNext())
		{
			Client c = clientIterator.next();
			totalWaitTime += getClientWaitTime(c);
		}
		double average = (numberOfClients > 0) ? totalWaitTime / (double)numberOfClients : 0.0;
		return average;
	}

	@Override
	public double getClientWaitTime(Client client) 
	{
		double waitTime = 0.0;
		boolean found = false;
		int[] clientTimes = createServiceTimeArray();
		Iterator<Client> clientIterator = clientLine.iterator();
		while(clientIterator.hasNext() && !found)
		{
			int minIndex = 0;
			for(int i = 0; i < clientTimes.length; i++)
			{
				if(clientTimes[i] < clientTimes[minIndex])
				{
					minIndex = i;
				}
			}
			Client nextClient = clientIterator.next();
			if(nextClient.equals(client))
			{
				found = true;
				waitTime = clientTimes[minIndex];
			}
			else
			{
				clientTimes[minIndex] += nextClient.getExpectedServiceTime();
			}
		}
		
		if(!found)
		{
			waitTime = 0.0;
		}
		return waitTime;
	}
	
	//This was in the Lab Instructions, so I'm adding it just in case
	public void addCustomer(Client client)
	{
		addClient(client);
	}

	@Override
	public boolean addClient(Client client) 
	{
		boolean added = false;
		for(int i = 0; i < numberOfTellers && !added; i++)
		{
			if(busyBankTellers.get(i).getExpectedServiceTime() == 0)
			{
				busyBankTellers.set(i, client);
				added = true;
			}
		}
		if(!added)
		{
			clientLine.offer(client);
		}
		return true;
	}
	
	public String toString()
	{
		String s = "";
		for(int i = 0; i < busyBankTellers.size(); i++)
		{
			s += "Bank Teller " + (i+1) + ": (" + busyBankTellers.get(i).getExpectedServiceTime() + ")\n";
		}
		
		Iterator<Client> i = clientLine.iterator();
		while(i.hasNext())
		{
			s += i.next().getExpectedServiceTime() + "->";
		}
		s += "null \n";
		return s;
	}
	
	private int[] createServiceTimeArray()
	{
		int[] serviceTimes = new int[busyBankTellers.size()];
		for(int i = 0; i < busyBankTellers.size(); i++)
		{
			serviceTimes[i] = busyBankTellers.get(i).getExpectedServiceTime();
		}
		return serviceTimes;
	}
}
