/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	/**
	 * Default constructor sets the threshold.
	 */
	public FractalInitialization () {
		
		Random random = new Random();
		this.distanceThreshold = random.nextInt(5);
		System.out.println("threshold " + this.distanceThreshold);
	}
	
	/**
	 * Method to generate initial clusters.
	 * @param start value of the starting point.
	 * @param end value of the ending point.
	 * @return an array with double values.
	 */
	public double[] generateClusters(int start, int end) {
		
		Random random =  new Random();
		double range =  (double)end - (double)start +1;
		double [] initialCluster = new double[50];
		for (int i = 0; i < initialCluster.length; i++) {
			initialCluster[i] = range * random.nextDouble();
			log("first cluster " + initialCluster[i]);
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
		
		for (int i = 0; i < second.length; i++) {
			newDoubles[i] = second[i];
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
	public Stack<Integer> testPoints() {
		Random randNumGenerator = new Random();
		Stack<Integer> stack = new Stack<Integer>();

		int[] x = new int[100];
		for (int i=0; i<x.length; i++)
		{
			x[i] = (randNumGenerator.nextInt(1000)+1);
			stack.push(x[i]);
			//System.out.println(x[i]);
		}
		return stack;
	}
	
	/**
	 * Find the nearest neighbor based on the distance threshold.
	 * @param currentPoint current point in the memory.
	 * @param threshold dynamic distance threshold.
	 * @return return the neighbor.
	 */
	private int nearestNeighbor(int currentPoint, int neighbor, int threshold) {
		int foundNeighbor = 0;
		if (currentPoint != 0 && neighbor != 0 && threshold != 0) {
			int distance = 0;
			distance = Math.abs(neighbor - currentPoint);
			if (distance != 0 && distance <= threshold) {
				foundNeighbor = neighbor;
				return foundNeighbor;
			}
		}
		return 0;
	}
	
	/**
	 * Method will check if a point belongs to a cluster based on the dynamic 
	 * threshold.
	 */
	public void isBelongToCluster() {
		
		double[] bigCluster = combineArrays(generateClusters(1, 25), generateClusters(500, 625));
		
		LinkedList<Integer> tempList = new LinkedList<Integer>();
		ArrayList<Integer> cluster = new ArrayList<Integer>();
	
		if(!testPoints().isEmpty()) {
			for(int i =0; i < testPoints().size(); i++) {
				tempList.add(testPoints().pop());
			}
		}
		int tempHolder = tempList.getFirst();
		System.out.println("first item of the linkedlist " + tempHolder);
		Iterator<Integer> iterator = tempList.iterator();
		
		while (iterator.hasNext()) {
			for (int i=0; i < tempList.size(); i++) {
				int tempNeighbor = testPoints().pop();
				int resultNeighbor = nearestNeighbor(tempList.get(i), tempNeighbor, this.getDistanceThreshold());
				if (resultNeighbor != 0) {
					System.out.println("Found Neighbor " + resultNeighbor);
					if (!cluster.contains(i))
						cluster.add(tempList.get(i));
					cluster.add(resultNeighbor);
				}
				else {
					i--;
				}
				//Avg distance between pairs in the cluster... can't really remember.
				// I dont think threshold should get update here
				//int newThreshold = (this.distanceThreshold * 3);
				//this.setDistanceThreshold(newThreshold);
			}
			int newThreshold = (this.distanceThreshold * 3);
			this.setDistanceThreshold(newThreshold);			
			iterator.next();
		}
		int size = cluster.size();
		System.out.println("cluster size " + size);
		for (int i=0; i < cluster.size(); i++) {
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
	 * just simple printing method.
	 */
	private static void log(String aMessage) {
		System.out.println(aMessage);
	}
	
	public static void main (String[] args) {
		FractalInitialization fractInt = new FractalInitialization();
		//fractInt.isBelongToCluster();
		double[] bigCluster = new double[50];
		double[] cluster1 = fractInt.generateClusters(1, 24);
		double[] cluster2 = fractInt.generateClusters(100, 225);
		bigCluster = fractInt.combineArrays(cluster1, cluster2);
		for (int i = 0; i < bigCluster.length; i++) {
			fractInt.log("Whats in array" + bigCluster[i]);
		}
		System.out.println(bigCluster.length);

	}	

}
