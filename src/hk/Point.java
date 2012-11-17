package hk;

import util.Vector;
import exception.*;

public class Point
{
	int index;
	
	public static final int NONEXIST = Integer.MIN_VALUE;
	// just used for evaluation
	public int truetype;
	// belong to this cluster
	private Cluster cluster=null;
	
	public int assigntype=NONEXIST;
	
	public Vector vector;

	Point(int index, int truetype,Vector vector)
	{
		this.index = index;
		this.truetype = truetype;
		this.vector = vector;
	}
	
	public double getDistance(Point item) throws VectorSizeDismatch
	{
		Vector delta=vector.sub(item.vector);
		return delta.square().sum();
	}

	public static Point getMean(Point[] points) throws VectorSizeDismatch
	{
		int n = points.length;
		if (n == 0) return null;
		Vector v=new Vector(points[0].vector.getLength());
		for (int i = 0; i < n; i++)
		{
			v=v.add(points[i].vector);
		}
		v.div(n);
		return new Point(NONEXIST,NONEXIST,v);
	}

	/**
	 * @return cluster
	 */
	public Cluster getCluster()
	{
		return cluster;
	}

	/**
	 * @param cluster 要设置的 cluster
	 */
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
}
