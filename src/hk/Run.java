package hk;

import java.io.IOException;
import java.security.InvalidParameterException;

import exception.CentroidNotExist;
import exception.VectorSizeDismatch;

public class Run
{
	/**
	 * @param args
	 * @throws IOException
	 * @throws InvalidParameterException
	 * @throws CentroidNotExist
	 * @throws VectorSizeDismatch
	 * @throws Exception
	 */
	public static void main(String[] args) throws InvalidParameterException,
			IOException, VectorSizeDismatch, CentroidNotExist
	{
		if(args.length==0)
		{	
			System.err.println("at least a file is needed");
			return ;
		}
		Kmeans kmeans = new Kmeans(args);
		kmeans.getFirstKPoints();
		Point[] oldcentroids = kmeans.getCentroids();
		Point[] newcentroids = null;
		do
		{
			oldcentroids=newcentroids;
			kmeans.reassign();
			kmeans.updateCentroids();
			newcentroids = kmeans.getCentroids();
			System.out.println(kmeans.getPrecision());
		} while (!equals(oldcentroids, newcentroids));
		return ;
	}

	public static boolean equals(Point[] oldcentroids, Point[] newcentroids)
	{
		if (oldcentroids == null && newcentroids != null)
			return false;
		if (oldcentroids != null && newcentroids == null)
			return false;
		if (oldcentroids.length != newcentroids.length)
			return false;
		for (int i = 0; i < newcentroids.length; i++)
		{
			if (!oldcentroids[i].vector.equals(newcentroids[i].vector))
				return false;
		}
		return true;
	}
}
