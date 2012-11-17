package hk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import exception.CentroidNotExist;
import exception.VectorSizeDismatch;

import util.Vector;

public class Kmeans
{
	public ArrayList<Point> points = new ArrayList<Point>();
	// k for kmeans
	public int k = 0;
	// # of points
	public int npoint = 0;
	// list of cluster
	// public Cluster[] clusters = null;
	public ArrayList<Cluster> clusters = new ArrayList<Cluster>();

	public Kmeans()
	{
		// TODO auto generated
	}

	public Kmeans(String[] filenames) throws InvalidParameterException,
			IOException
	{
		load(filenames);
	}

	public void load(String[] filenames) throws IOException,
			InvalidParameterException
	{
		if (filenames.length < 1)
			throw new InvalidParameterException("at least 1 type are required");
		File file = null;
		for (String filename : filenames)
		{
			file = new File(filename);
			if (!file.exists() || file.isDirectory())
				throw new InvalidParameterException(filename
						+ " is not a file !!");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String nextline;
			while ((nextline = reader.readLine()) != null)
			{
				String[] strings = nextline.split("\\s+");
				double[] v = new double[strings.length];
				for (int i = 0; i < v.length; i++)
					v[i] = Double.parseDouble(strings[i]);
				points.add(new Point(npoint, k, new Vector(v)));
				npoint++;
			}
			reader.close();
			k++;
		}
	}

	public void getFirstKPoints() throws VectorSizeDismatch, CentroidNotExist
	{
		if (points.size() == 0)
			return;
		clusters.clear();
		HashSet<Point> selectedPoints = new HashSet<>();
		HashSet<Integer> selectedTypes=new HashSet<>();
		// randomly pick the 1st point
		int randfirst = new Random().nextInt(npoint);
		clusters.add(new Cluster());
		clusters.get(0).add(points.get(randfirst));
		clusters.get(0).setType(points.get(randfirst).truetype);
		clusters.get(0).updateCentroid();
		selectedPoints.add(points.get(randfirst));
		selectedTypes.add(points.get(randfirst).truetype);
		for (int i = 1; i < k; i++)
		{
			Point addthisPoint = null;
			double maxdis = 0;
			// pick the farest point from the clusters
			for (Point point : points)
			{
				if (selectedTypes.contains(point.truetype)||selectedPoints.contains(point))
					continue;
				double sumDistance = 0;
				for (Cluster cluster : clusters)
				{
					sumDistance += cluster.getDistance(point);
				}
				if (sumDistance > maxdis)
				{
					maxdis = sumDistance;
					addthisPoint = point;
				}
			}
			Cluster newcluster = new Cluster();
			newcluster.add(addthisPoint);
			newcluster.setType(addthisPoint.truetype);
			newcluster.updateCentroid();
			clusters.add(newcluster);
			selectedPoints.add(addthisPoint);
			selectedTypes.add(addthisPoint.truetype);
		}
	}

	public void reassign() throws VectorSizeDismatch, CentroidNotExist
	{
		for (Cluster cluster : clusters)
		{
			cluster.updateCentroid();
		}
		
		for (Point point : points)
		{
			Cluster newclu = null;
			double mindistance = Double.MAX_VALUE;
			for (Cluster cluster : clusters)
			{
				double dis = cluster.getDistance(point);
				if (dis < mindistance)
				{
					newclu = cluster;
					mindistance = dis;
				}
			}
			Cluster oldc = point.getCluster();
			if(oldc!=null)
				oldc.remove(point);
			point.setCluster(newclu);
			newclu.add(point);
		}
	}

	public Point[] getCentroids()
	{
		Point[] centroids=new Point[k];
		for (int i = 0; i < centroids.length; i++)
		{
			centroids[i]=clusters.get(i).getCentroid();
		}
		return centroids;
	}
	
	public void updateCentroids() throws VectorSizeDismatch
	{
		for (Cluster cluster : clusters)
		{
			cluster.updateCentroid();
		}
	}

	public double getPrecision()
	{
		int right=0;
		for (Cluster cluster : clusters)
		{
			for (Point point : cluster)
			{
				if(point.truetype==cluster.getType())
					right++;
				point.assigntype=cluster.getType();
			}
		}
		return (double)right/(double)npoint;
	}
	
	
}
