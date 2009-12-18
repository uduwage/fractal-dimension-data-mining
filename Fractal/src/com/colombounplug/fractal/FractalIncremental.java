/**
 * 
 */
package com.colombounplug.fractal;

import java.util.ArrayList;

/**
 * @author anuradha.uduwage
 *
 */
public class FractalIncremental {

	/*
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
	
	
	//Distance grid.
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
	*/	
}
