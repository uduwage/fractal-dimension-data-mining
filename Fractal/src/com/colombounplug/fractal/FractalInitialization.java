/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * @author anuradha.uduwage
 *
 */
public class FractalInitialization {
	
	//distance threshold
	private int distanceThreshold;
	private int numOfClusters;
	private HashMap<Double, Double> distanceMap = new HashMap<Double, Double>();
	private double previousDistance;
	private double[] visited;
	private boolean wasVisited;
	/**
	 * Default constructor sets the threshold.
	 */
	public FractalInitialization () {
		
		Random random = new Random();
		this.distanceThreshold = random.nextInt(6) + 1;
		if (this.distanceThreshold == 0) 
			this.distanceThreshold = random.nextInt(6) + 1;
		this.previousDistance = 100000000000000.00;
		//System.out.println("threshold " + this.distanceThreshold);
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
		double [] initialCluster = new double[50];
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
	 * Combine any type/number of arrays to a one arrays.
	 * @param <T>
	 * @param first
	 * @param rest
	 * @return
	 */
	public static <T> T[] concatArrays (T[] first, T[]... rest) {
		
		int fullLength = first.length;
		for (T[] array : rest)
			fullLength += array.length;
		T[] finalArray = Arrays.copyOf(first, fullLength);
		int offSet = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, finalArray, offSet, array.length);
			offSet += array.length;
		}
		return  finalArray;
	}
	
	/**
	 * Initial stage we generate random number to replicate the points.
	 * @return returns stack with random integers.
	 */
	public Stack<Double> stackOfPoints(double[] cluster) {
		
		Stack<Double> stack = new Stack<Double>();
		for (int i=0; i < cluster.length; i++)
		{
			if (cluster[i] != 0 && cluster.length != 0) {
				stack.push(cluster[i]);
				//log("whats in stack " + stack.pop());
			}
		}
		return stack;
	}
	
	/**
	 * Find the nearest neighbor based on the distance threshold.
	 * @param currentPoint current point in the memory.
	 * @param threshold dynamic distance threshold.
	 * @return return the neighbor.
	 */
	private double nearestNeighbor(double currentPoint, double neighbor, double threshold) {
		double foundNeighbor = 0;
		if (currentPoint != 0 && neighbor != 0 && threshold != 0 && currentPoint != neighbor) {
			double distance = 0;
			distance = Math.abs(neighbor - currentPoint);
			if (distance != 0 && distance <= threshold) {
				foundNeighbor = neighbor;
				if (distance < getPreviousDistance()) {
					setPreviousDistance(distance);
					return foundNeighbor; 
				}
			}
		}
		return 0;
	}
	
	/**
	 * Method will check if a point belongs to a cluster based on the dynamic 
	 * threshold.
	 */
	public void isBelongToCluster() {
		
		double[] bigCluster = combineArrays(generateClusters(1, 25), generateClusters(100, 125));
		
		ArrayList<Double> tempList = new ArrayList<Double>();
		ArrayList<Double> cluster = new ArrayList<Double>();
		ArrayList<Double> finalCluster =  new ArrayList<Double>();
		
		cluster.clear();
		
		if(bigCluster.length != 0) {
			for (int i = 0; i < bigCluster.length; i++) {
				double temp = bigCluster[i];
				tempList.add(temp);
			}
		}
	
		Iterator<Double> iterator = tempList.iterator();
		//double resultNeighbor  = 0;


		while (iterator.hasNext()) {
			for (int i=0; i < tempList.size(); i++) {
				System.out.println("threshold " + this.getDistanceThreshold());
				//double resultNeighbor = 0;
				double aPointInCluster = tempList.get(i);
				
				cluster.add(aPointInCluster);
				double foundNeighbor = 0;
				for (int k = 0; k < bigCluster.length; k++) {
					//resultNeighbor = nearestNeighbor(aPointInCluster, bigCluster[k], this.getDistanceThreshold());
					//foundNeighbor = 0;
					if (aPointInCluster != 0 && bigCluster[k] != 0 && this.getDistanceThreshold() != 0 && aPointInCluster != bigCluster[k]) {
						double distance = 0;
						distance = Math.abs(aPointInCluster - bigCluster[k]);
						if (distance != 0 && distance <= this.getDistanceThreshold() && distance <= this.getPreviousDistance()) {
							foundNeighbor = bigCluster[k];
							cluster.add(foundNeighbor);
							this.setPreviousDistance(distance);
							distanceMap.put(aPointInCluster, foundNeighbor);
						}
					}
				}

				if (foundNeighbor == 0) {
					int newThreshold = (this.distanceThreshold * 3);
					this.setDistanceThreshold(newThreshold);
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
		//fractInt.isBelongToCluster();
		//double[] bigCluster = fractInt.combineArrays(fractInt.generateClusters(1, 25), fractInt.generateClusters(500, 625));
		//fractInt.testPoints(bigCluster);
		fractInt.isBelongToCluster();
		/*
		Iterator<Double> iterator = fractInt.distanceMap.keySet().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		} */

	}	

}
