/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;
import java.util.Arrays;
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
	private int distanceThreshold;
	private int numOfClusters;
	private double previousDistance;
	private double[] bigCluster;
	private ArrayList<Double> cluster;
	//private VisitedPoint[] visit;
	private ArrayList<Double> tempList;
	
	/**
	 * Default constructor sets the threshold.
	 */
	public FractalInitialization () {
		
		Random random = new Random();
		this.distanceThreshold = random.nextInt(6) + 1;
		if (this.distanceThreshold == 0) 
			this.distanceThreshold = random.nextInt(6) + 1;
		this.previousDistance = 100000000000000.00;
		bigCluster = combineArrays(generateClusters(1, 25), generateClusters(100, 125));
		
		//visit = new VisitedPoint[bigCluster.length];
		tempList = new ArrayList<Double>();
		cluster = new ArrayList<Double>();
		
		cluster.clear();
		
		if(bigCluster.length != 0) {
			for (int i = 0; i < bigCluster.length; i++) {
				double temp = bigCluster[i];
				tempList.add(temp);
			}
		}		

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
		double [] initialCluster = new double[4];
		for (int i = 0; i < initialCluster.length; i++) {
			initialCluster[i] = start + (range * random.nextDouble());
			//log("first cluster " + initialCluster[i]);
		}
		return initialCluster;
	}
	
	/**
	 * Method to combine the test clusters.
	 * @param first
	 * @param second
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
	
	private double nearestNeighbor(double currentPoint, double[] neighbor) {
		
		HashMap<Double, Double> unsorted = new HashMap<Double, Double>();
		TreeMap<Double, Double> sorted = null; 
		
		for (int i = 0; i < neighbor.length; i++) {
			if (neighbor[i] != 0.0 && neighbor[i] != currentPoint) {
				double shortestDistance = Math.abs(currentPoint - neighbor[i]);
				unsorted.put(shortestDistance, neighbor[i]);
			}
			//else
			//	return 0.0;
		}
		if (!unsorted.isEmpty()) {
			sorted = new TreeMap<Double, Double>(unsorted);
			return sorted.firstEntry().getValue();
		}
		
		return 0.0;
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
	public void isBelongToCluster(double point) {

		Iterator<Double> iterator = tempList.iterator();
	
		while (iterator.hasNext()) {
			
			for (int i=0; i < tempList.size(); i++) {
				
				double resultNeighbor = 0;
				double aPointInCluster = tempList.get(i);
				cluster.add(aPointInCluster);
				double[] neighbors = pointsWithinThreshold(aPointInCluster);
				//resultNeighbor = nearestNeighbor(aPointInCluster, neighbors);
				if ( nearestNeighbor(aPointInCluster, neighbors) != 0.0) {
					cluster.add(resultNeighbor);
				}

			}
			this.setDistanceThreshold(this.distanceThreshold * 2);
			iterator.next();
		}

		for (int i=0; i < cluster.size(); i++) {
			if (cluster.get(i) != 0.0)
				System.out.println("whats in the cluster -> " + cluster.get(i)); 
		} 
	}
	
	/**
	 * Get the threshold distance value.
	 * @return
	 */
	public int getDistanceThreshold() {
		return distanceThreshold;
	}
	/**
	 * Set the threshold value
	 * @param distanceThreshold
	 */
	public void setDistanceThreshold(int distanceThreshold) {
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
	
	/**
	 * Get previous distance to of the point.
	 * @return
	 */
	public double getPreviousDistance() {
		return previousDistance;
	}
	public void setPreviousDistance(double previousDistance) {
		this.previousDistance = previousDistance;
	}
	
	/**
	 * just simple printing method.
	 */
	private static void log(String aMessage) {
		System.out.println(aMessage);
	}
	
	public static void main (String[] args) {
		FractalInitialization fractInt = new FractalInitialization();
		fractInt.isBelongToCluster(0.0);
		fractInt.distanceGrid();
		/*
		Iterator<Double> iterator = fractInt.distanceMap.keySet().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		} */

	}	

}
