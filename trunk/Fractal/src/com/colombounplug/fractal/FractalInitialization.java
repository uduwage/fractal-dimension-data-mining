/**
 * 
 */
package com.colombounplug.fractal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

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
	private double lastValue;
	private double[] newPoints;
	public ArrayList<Double> listOfNewPoints;
	HashMap<Integer, Double> mapOfFractDimension;
	HashMap<Integer, Double> originalFractDimension;
	File outPut;
	PrintStream ps;
	
	/**
	 * Default constructor sets the threshold and handles necessary object creation.
	 * @throws FileNotFoundException 
	 */
	public FractalInitialization () throws FileNotFoundException {
		
		Random random = new Random(); 
		this.distanceThreshold = random.nextInt(10) + 1;
		if (this.distanceThreshold == 0) 
			this.distanceThreshold = random.nextInt(10) + 1; 
		//this.setDistanceThreshold(15.4564);
		
		initialDistanceThreshold = this.getDistanceThreshold();
		bigCluster = combineArrays(generateClusters(1, 25), generateClusters(100, 125));
		newPoints = combineArrays(generateClusters(25, 75), generateClusters(125, 150));
		
		tempList = new ArrayList<Double>();
		cluster = new ArrayList<Double>();
		listOfNewPoints = new ArrayList<Double>();
		
		
		cluster.clear();
		
		if(bigCluster.length != 0) {
			for (int i = 0; i < bigCluster.length; i++) {
				double temp = bigCluster[i];
				tempList.add(temp);
			}
		}
		
		if(newPoints.length != 0) {
			for(int i = 0; i < newPoints.length; i++) {
				listOfNewPoints.add(newPoints[i]);
			}
		}
		
		listOfNewPoints.add(203.5);
		listOfNewPoints.add(0.00000005);
		
		Collections.sort(tempList);
		lastValue = tempList.get(tempList.size()-1);
		
		visited = new boolean[bigCluster.length];
		unsorted = new HashMap<Double, Double>();
		visitedPoints = new ArrayList<Double>();
		visitedMap = new HashMap<Double, Boolean>();
		this.setNumOfClusters(0);
		mapOfCluster = new HashMap<Double, Integer>();
		tempCluster = new ArrayList<Double>();
		mapOfFractDimension = new HashMap<Integer, Double>();
		originalFractDimension = new HashMap<Integer, Double>();
		outPut = new File("FractalOutput.csv");
		ps = new PrintStream(new FileOutputStream(outPut));
		
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
	 * @param point double value point to check if its within the threshold.
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
	 * Method will do a recursive depth first search to check check if a 
	 * point belongs to a cluster based on the dynamic threshold.<br>
	 * Method also rest the threshold after each iteration.
	 * 
	 * @param point A double value point from a sample data set.
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
		//System.out.println(avgClusterDist/cluster.size());
		return avgClusterDist/cluster.size();
	}

	/**
	 * Method calculate Fractal Dimension using box-counting.
	 */
	public void boxCounting(HashMap<Double, Integer> cluster) {
		List<Double> keyList = new ArrayList<Double>();
		Double range = 5.0;
		double scale = this.lastValue / range;
		int boxCount = 0;
		
		//Get all the elements for each cluster ID and prepare a list 
		//to use for box counting.
		for(int i = 1; i <= this.numOfClusters; i++) {
			if(!keyList.isEmpty())
				keyList.removeAll(keyList);
			System.out.println("Printing Cluster " + i);
			ps.println("Printing Cluster " + i);
			boxCount = 0;
			for(Double key : cluster.keySet()) {
				if(cluster.get(key).equals(i)) {
					keyList.add(key);
					Collections.sort(keyList);
					System.out.println(key);
					ps.println(key);
				}
			
			}
			
			//foreach range for points in each cluster check how many boxes are 
			//needed to represent the cluster.
			for(int rangeCount = 1; rangeCount < scale; rangeCount++) {
				for (Double double1 : keyList) {
					if((double1 < (range * rangeCount)) && (double1 > (rangeCount - 1) * range)) {
						boxCount += 1;
						break;
					}
				}
			}
			
			System.out.println("Cluster " + i + " require " + boxCount + " boxes");
			ps.println("Cluster " + i + " require " + boxCount + " boxes");
			double fractalDimension = Math.log(boxCount) / Math.log(scale);
			System.out.println("Fractal Dimension of cluster " + i + " is " +fractalDimension);
			ps.println("Fractal Dimension of cluster " + i + " is " +fractalDimension);
			//might have to store this in a different map.
			this.mapOfFractDimension.put(i, fractalDimension);

		}
		//System.out.println("Count is " + count + "&& tempCount is " + boxCount);
		//System.out.println("Scale is " + scale);
	}
	
	//idea: create a method that can accpet cluster id, and the map return fd on that cluster.
	public void newFractalDimension() {
		this.originalFractDimension = (HashMap)this.mapOfFractDimension.clone();
		for(int clusterId = 1; clusterId <= this.numOfClusters; clusterId++) {
			for(int i = 0; i < this.listOfNewPoints.size(); i++) {
				double point = this.listOfNewPoints.get(i);
				this.mapOfCluster.put(point, clusterId);
				//calculating new fractal dimension
				boxCounting(mapOfCluster);
				System.out.println("Old FD " + this.originalFractDimension.get(clusterId));
				ps.println("Old FD " + this.originalFractDimension.get(clusterId));
				System.out.println("New FD " + this.mapOfFractDimension.get(clusterId));
				ps.println("New FD " + this.mapOfFractDimension.get(clusterId));
				double fdDifference = Math.abs(this.mapOfFractDimension.get(clusterId) - this.originalFractDimension.get(clusterId));
				System.out.println("FD Difference " + fdDifference);
				ps.println("FD Difference " + fdDifference);
				if(fdDifference > this.initialDistanceThreshold) {
					this.mapOfCluster.remove(point);
				}
				//Calcuate Fractal Dimension
				//Compare it with the Original Fractal Dimension of the cluster.
				//record the deference
				//pick the point that that has the minimum Fractal Impact.
				//check if the minimal impact is greater than threshold 
				//if its greater than threshold
				//eliminate the point as noize
				//other wise leave the point in the cluster.
			}
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
	
	
	public static void main (String[] args) throws FileNotFoundException {
		
		FractalInitialization fractInt = new FractalInitialization();
		boolean[] bool = new boolean[fractInt.tempList.size()];
		for(int i = 0; i < fractInt.tempList.size(); i++) {
			fractInt.dfsNearest(fractInt.tempList.get(i));
		}
		for (int i = 0; i < fractInt.cluster.size(); i++) {
			if (fractInt.cluster.get(i) != 0.0){}
				//System.out.println("whats in the cluster -> " + fractInt.cluster.get(i));
		}	
		
		System.out.println("Cluster id and points");
		fractInt.ps.println("Cluster id and points");
		Set set = fractInt.mapOfCluster.entrySet();
		Iterator iter = set.iterator();	
		while (iter.hasNext()) {
			System.out.println(iter.next());
			fractInt.ps.println(iter.next());
			
		}
		
		System.out.println("Number of clusters " + fractInt.getNumOfClusters());
		fractInt.ps.println("Number of clusters " + fractInt.getNumOfClusters());
		fractInt.boxCounting(fractInt.mapOfCluster);
		//fractInt.originalFractDimension = fractInt.mapOfFractDimension;
		
		set = fractInt.originalFractDimension.entrySet();
		iter = set.iterator();
		System.out.println("Fractal Dimension calculation");
		fractInt.ps.println("Fractal Dimension calculation");
		while (iter.hasNext()) {
			System.out.println(iter.next());
			fractInt.ps.println(iter.next());
		}	
		
		fractInt.newFractalDimension();
		
		System.out.println(fractInt.lastValue);
		fractInt.ps.close();
		
	}	

}
