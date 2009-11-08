/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @author anuradha.uduwage
 *
 */
public class FractalInitialization {
	
	//distance threshold
	private double distanceThreshold;
	private int numOfClusters;
	private double[] bigCluster;
	public ArrayList<Double> cluster;
	public ArrayList<Double> tempList;
	private HashMap<Double, Double> unsorted;
	private boolean[] visited;
	private VisitedPoint[] isVisited;
	private ArrayList<Double> visitedPoints;
	public HashMap<Double, Boolean> visitedMap;
	private double initialDistanceThreshold;
	
	/**
	 * Default constructor sets the threshold.
	 */
	public FractalInitialization () {
		
		Random random = new Random();
		this.distanceThreshold = random.nextInt(10) + 1;
		if (this.distanceThreshold == 0) 
			this.distanceThreshold = random.nextInt(10) + 1;

		initialDistanceThreshold = this.getDistanceThreshold();
		bigCluster = combineArrays(generateClusters(1, 25), generateClusters(100, 125));
		
		tempList = new ArrayList<Double>();
		cluster = new ArrayList<Double>();
		
		cluster.clear();
		
		if(bigCluster.length != 0) {
			for (int i = 0; i < bigCluster.length; i++) {
				double temp = bigCluster[i];
				tempList.add(temp);
			}
		}		
		visited = new boolean[bigCluster.length];
		unsorted = new HashMap<Double, Double>();
		visitedPoints = new ArrayList<Double>();
		visitedMap = new HashMap<Double, Boolean>();
	}
	
	/**
	 * Method to generate initial clusters.
	 * @param start value of the starting point.
	 * @param end value of the ending point.
	 * @return an array with double values.
	 */
	public double[] generateClusters(int start, int end) {
		
		Random random =  new Random();
		double range =  (double)end - (double)start;
		double [] initialCluster = new double[25];
		for (int i = 0; i < initialCluster.length; i++) {
			initialCluster[i] = start + (range * random.nextDouble());
			//log("first cluster " + initialCluster[i]);
		}
		return initialCluster;
	}
	
	/**
	 * Method to combine the test clusters.
	 * @param first array with points
	 * @param second array with points
	 * @return
	 */
	public double[] combineArrays(double[] first, double[] second) {
		
		int totalLenth = first.length + second.length;
		double[] newDoubles = new double[totalLenth];
		for (int i = 0; i < first.length; i++) {
			newDoubles[i] = first[i];
		}
		
		for (int j = first.length; j < newDoubles.length; j++) {
			newDoubles[j] = second[j - first.length];
		}
		return newDoubles;
	}
			
	/**
	 * Find the nearest neighbor based on the distance threshold.
	 * TODO:
	 * @param currentPoint current point in the memory.
	 * @param threshold dynamic distance threshold.
	 * @return return the neighbor.
	 */
	
	private double nearestNeighbor(double currentPoint) {
		
		HashMap<Double, Double> unsorted = new HashMap<Double, Double>();
		TreeMap<Double, Double> sorted = null; 
		double foundNeighbor = 0.0;
		
		for (int i = 0; i < bigCluster.length; i++) {
			if (bigCluster[i] != 0.0 && bigCluster[i] != currentPoint) {
				double shortestDistance = Math.abs(currentPoint - bigCluster[i]);
				if (shortestDistance <= this.getDistanceThreshold())
					unsorted.put(shortestDistance, bigCluster[i]);
			}
		}
		if (!unsorted.isEmpty()) {
			sorted = new TreeMap<Double, Double>(unsorted);
			//this.setDistanceThreshold(avgDistanceInCluster());
			foundNeighbor = sorted.firstEntry().getValue();
			return foundNeighbor;
		} else {
			return 0.0;
		}
	}
	
	public double[][] distanceGrid() {
		double[] gridSize = combineArrays(generateClusters(1, 3), generateClusters(12, 15));
		double [][] pointsDistanceGrid = new double[gridSize.length][gridSize.length];
		for (int i = 0; i < pointsDistanceGrid.length; i++) {
			for (int j = 0; j < pointsDistanceGrid[i].length; j++) {
				pointsDistanceGrid[i][j] = Math.abs(gridSize[i] - gridSize[j] );
				System.out.print(" " + pointsDistanceGrid[i][j]);
			}
			System.out.println("");
		}

		return pointsDistanceGrid;
	}
	
	/**
	 * Given a point method returns an array with point that are within the limit of threshold.
	 * @param point
	 * @return
	 */
	public double[] pointsWithinThreshold(double point) {
		double[] neighbors = new double[bigCluster.length];
		for (int i = 0; i < bigCluster.length; i++) {
			if (bigCluster[i] != point) {
				double distance = 0;
				distance = Math.abs(point - bigCluster[i]);
				if (distance <= getDistanceThreshold()) {
					neighbors[i] = bigCluster[i];
				}
			}
		}
		return neighbors;
	}
	/**
	 * Method will check if a point belongs to a cluster based on the dynamic 
	 * threshold.
	 */
	public void callNearestDFSFashion(double point) {

		double aPointInCluster = point;
		if(!cluster.contains(aPointInCluster))
			cluster.add(aPointInCluster);
		visitedMap.put(aPointInCluster, true);
		double newNeighbor = nearestNeighbor(aPointInCluster);
		if (newNeighbor != 0.0) {
			cluster.add(newNeighbor);
			this.setDistanceThreshold(avgDistanceInCluster());
			if (!visitedMap.containsKey(newNeighbor)) {
				callNearestDFSFashion(newNeighbor);
			}
		}
	}

	public void dfsNearestNeighbor(double point, ArrayList<Double> list, boolean[] pointIsVisited ) {
		if (point != 0.0) {
			list.add(point);
			cluster.add(point);
			pointIsVisited[(int) point] = true;
			double newNeighbor =  nearestNeighbor(point);
			if (newNeighbor !=  0.0) {
				cluster.add(newNeighbor);
				for (int i = 0; i < tempList.size(); i++) {
					if (!pointIsVisited[i]) {
						dfsNearestNeighbor(newNeighbor, list, pointIsVisited);
					}
				}
			}
		}
	}
	
	/**
	 * Calculate avg distance between points in cluster
	 * @return
	 */
	public double avgDistanceInCluster() {
		double avgDistance = 0.0;
		Stack<Double> holder = new Stack<Double>();
		for (int i = 0; i < cluster.size() - 1; i++) {
			System.out.println(cluster.get(i));
			for (int j = i+1; j < cluster.size(); j++) {
				avgDistance = (cluster.get(i) + cluster.get(j))/2; 
				holder.push(avgDistance);
			}
		}
		Iterator<Double> iter = holder.iterator();
		double avgClusterDist = 0;
		while (iter.hasNext()) {
			avgClusterDist =+ holder.pop();
			System.out.println(avgClusterDist);
		}
		return avgClusterDist/cluster.size();
	}
	
	/**
	 * Get the threshold distance value.
	 * @return
	 */
	public double getDistanceThreshold() {
		return distanceThreshold;
	}
	/**
	 * Set the threshold value
	 * @param distanceThreshold
	 */
	public void setDistanceThreshold(double distanceThreshold) {
		this.distanceThreshold = distanceThreshold;
	}
	
	/**
	 * Get number of clusters.
	 * @return
	 */
	public int getNumOfClusters() {
		return numOfClusters;
	}
	
	/**
	 * Set the number of clusters
	 * @param numClusters
	 */
	public void setNumOfClusters(int numClusters) {
		this.numOfClusters = numClusters;
	}
	
	
	public static void main (String[] args) {
		ArrayList<Double> list = new ArrayList<Double>();
		
		FractalInitialization fractInt = new FractalInitialization();
		boolean[] bool = new boolean[fractInt.tempList.size()];
		for(int i = 0; i < fractInt.tempList.size(); i++) {
			fractInt.callNearestDFSFashion(fractInt.tempList.get(i));
		}
		for (int i = 0; i < fractInt.cluster.size(); i++) {
			if (fractInt.cluster.get(i) != 0.0)
				System.out.println("whats in the cluster -> " + fractInt.cluster.get(i));
		}		
		//fractInt.distanceGrid();
		
	}	

}
