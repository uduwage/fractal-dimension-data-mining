/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * This class handles initialization process of clusters.
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
	private ArrayList<Double> visitedPoints;
	public HashMap<Double, Boolean> visitedMap;
	private double initialDistanceThreshold;
	HashMap<Double, Integer> mapOfCluster;
	ArrayList<Double> tempCluster;
	
	/**
	 * Default constructor sets the threshold and handles necessary object creation.
	 */
	public FractalInitialization () {
		
		Random random = new Random(); /*
		this.distanceThreshold = random.nextInt(10) + 1;
		if (this.distanceThreshold == 0) 
			this.distanceThreshold = random.nextInt(10) + 1; */
		this.setDistanceThreshold(15.4564);
		
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
		this.setNumOfClusters(0);
		mapOfCluster = new HashMap<Double, Integer>();
		tempCluster = new ArrayList<Double>();
		
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
			if (bigCluster[i] != 0.0 && bigCluster[i] != currentPoint && !visitedMap.containsKey(bigCluster[i])) {
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
	public void dfsNearest(double point) {

		double aPointInCluster = point;
		if(!cluster.contains(aPointInCluster)) {
			cluster.add(aPointInCluster);
			this.setNumOfClusters(this.getNumOfClusters() + 1);
			mapOfCluster.put(aPointInCluster, this.getNumOfClusters());
			tempCluster.clear();
			tempCluster.add(aPointInCluster);
			
		}
		visitedMap.put(aPointInCluster, true);
		double newNeighbor = nearestNeighbor(aPointInCluster);
		if(newNeighbor != 0.0) {
			cluster.add(newNeighbor);
			mapOfCluster.put(newNeighbor, this.getNumOfClusters());
			this.setDistanceThreshold(avgDistanceInCluster() * this.initialDistanceThreshold);
			tempCluster.add(newNeighbor);
			if (!visitedMap.containsKey(newNeighbor)) {
				dfsNearest(newNeighbor);
			}
		}
		if(this.getDistanceThreshold() != this.initialDistanceThreshold) {
			this.setDistanceThreshold(this.initialDistanceThreshold);
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
	 * Calculate avg distance between pair of points in cluster
	 * @return average distance.
	 */
	public double avgDistanceInCluster() {
		double avgDistance = 0.0;
		Stack<Double> holder = new Stack<Double>();
		for (int i = 0; i < cluster.size() - 1; i++) {
			//System.out.println(cluster.get(i));
			for (int j = i+1; j < cluster.size(); j++) {
				avgDistance = (cluster.get(i) + cluster.get(j))/2; 
				holder.push(avgDistance);
			}
		}
		Iterator<Double> iter = holder.iterator();
		double avgClusterDist = 0;
		while (iter.hasNext()) {
			avgClusterDist =+ holder.pop();
			//System.out.println(avgClusterDist);
		}
		System.out.println(avgClusterDist/cluster.size());
		return avgClusterDist/cluster.size();
	}
	/*
	  public static List<Object> getKeysFromValue(Map<?, ?> hm, Object value){
		    List <Object>list = new ArrayList<Object>();
		    for(Object o:hm.keySet()){
		        if(hm.get(o).equals(value)) {
		            list.add(o);
		        }
		    }
		    return list;
		  } */
		  	
	/**
	 * Calculate Fractal Dimension using box-counting.
	 */
	public void boxCounting() {
		List<Double> keyList = new ArrayList<Double>();
		Double range = 5.0;
		int tempCount = 0;
		int count = 0;
		
		for(int i = 1; i <= this.numOfClusters; i++) {
			
			System.out.println("Printing Cluster " + i);
			count = 0;
			for(Double key : mapOfCluster.keySet()) {
				if(mapOfCluster.get(key).equals(i)) {
					keyList.add(key);
					Collections.sort(keyList);
					System.out.println(key);
					/*
					if(key < range) {
						count = count + 1;
					}*/					
					
					//put this in a method and call once before adding a new point to the cluster
					//then call it again to calculate the FD
					//use threshold had benchmark of the FD change.
					if(key < range * 2 && key > range) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 3 && key > range * 2) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 4 && key > range * 3) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 5 && key > range * 4) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 6 && key > range * 5) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 7 && key > range * 6) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 8 && key > range * 7) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 9 && key > range * 8) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 10 && key > range * 9) {
						count += 1;
					}
					else if(key < range * 11 && key > range * 10) {
						count += 1;	
					}
					else if(key < range * 12 && key > range * 11) {
						count += 1;	
					}
					else if(key < range * 13 && key > range * 12) {
						count += 1;	
					}
					else if(key < range * 14 && key > range * 13) {
						count += 1;
					}
					else if(key < range * 15 && key > range * 14) {
						count += 1;	
					}
					else if(key < range * 16 && key > range * 15) {
						count += 1;
					}
					else if(key < range * 17 && key > range * 16) {
						count += 1;	
					}
					else if(key < range * 18 && key > range * 17)
						count += 1;	
					else if(key < range * 19 && key > range * 18)
						count += 1;
					else if(key < range * 20 && key > range * 19)
						count += 1;
					else if(key < range * 21 && key > range * 20)
						count += 1;
					else if(key < range * 22 && key > range * 21)
						count += 1;
					else if(key < range * 23 && key > range * 22)
						count += 1;	
					else if(key < range * 24 && key > range * 23) {
						//count = 0;
						count += 1;
					}
					else if(key < range * 25 && key > range * 24) {
						//count = 0;
						count += 1;	
					}
					else if(key < range)
						count += 1;
				}
			
			}
				
			System.out.println("Cluster " + i + " require " + count + " boxes");
			double fractalDimension = Math.log(count) / Math.log(range);
			System.out.println("Fractal Dimension of cluster " + i + " is " +fractalDimension);
		}
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
		
		FractalInitialization fractInt = new FractalInitialization();
		boolean[] bool = new boolean[fractInt.tempList.size()];
		for(int i = 0; i < fractInt.tempList.size(); i++) {
			fractInt.dfsNearest(fractInt.tempList.get(i));
		}
		for (int i = 0; i < fractInt.cluster.size(); i++) {
			if (fractInt.cluster.get(i) != 0.0)
				System.out.println("whats in the cluster -> " + fractInt.cluster.get(i));
		}	
		
		Set set = fractInt.mapOfCluster.entrySet();
		Iterator iter = set.iterator();
		
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		
		System.out.println("Number of clusters " + fractInt.getNumOfClusters());
		
		fractInt.boxCounting();
	}	

}
