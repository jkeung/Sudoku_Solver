package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * This Sudoku class implements an application that solves
 * an NxN square. The class requires an input location and an
 * output location for the relevant csv files.
 * 
 *  The CSV input should be in the following format:
 * 	0,3,5,2,9,0,8,6,4
 *	0,8,2,4,1,0,7,0,3
 *	7,6,4,3,8,0,0,9,0
 *	2,1,8,7,3,9,0,4,0
 *	0,0,0,8,0,4,2,3,0
 *	0,4,3,0,5,2,9,7,0
 *	4,0,6,5,7,1,0,0,9
 *	3,5,9,0,2,8,4,1,7
 *	8,0,0,9,0,0,5,2,6
 *
 *  The zeros represent the missing values
 *
 * @author Jason Keung
 *
 */

public class Sudoku 
{
	private final int width = 9;										// Grid Width (ex. 9 for a 9x9 grid)
	private int[][] grid;												// Grid that will hold the Sudoku values
	private List<Integer> possibleValues;								// Assigns possible values to each Sudoku cell 
	private static String sudokuinput = "sudokuinput.csv";				// Location of the Sudoku input csv file
	private static String sudokuoutput = "sudokuoutput.csv"; 			// Location of the Sudoku output csv file

	public static void main(String[] args) throws Exception {
		Sudoku sudoku = new Sudoku();	
		sudoku.getData(sudokuinput);
		sudoku.solve();
		sudoku.printData(sudokuoutput);
	}

/*--------------------------------- Begin Data Input ------------------------- */
	
	
	/**
	 * Reads in the data from the input csv file
	 * @param filepath
	 */

	private void getData(String filepath) {
		grid = new int[width][width];
		Scanner scanIn = null;					//Retrieves Input Data
		String InputLine = "";
		int row = 0;
		possibleValues = new ArrayList<Integer>();
		
		try {
			scanIn = new Scanner(new BufferedReader(new FileReader(filepath)));
			while (scanIn.hasNextLine()) {
				InputLine = scanIn.nextLine();
				String[] InArray = InputLine.split(",");
				for (int col = 0; col < InArray.length; col++) {
					grid[row][col] = Integer.parseInt(InArray[col]);
				}
				row++;
			}
			scanIn.close();

		} catch (Exception e) {
			System.out.println("Bad Input: " + e);
			scanIn.close();
			return;
		}
		
		for (int i=1; i<=width; i++) {       // Specifies the possible values for a given cell (ex. in a 3x3 puzzle 1 to 9 are possible values)
			possibleValues.add(i);
		}

	}

/*--------------------------------- End Data Input ------------------------- */
	
	
/*--------------------------------- Data output --------------------------- */
	
	/**
	 * Prints the Sudoku grid to console
	 */

	private void printData(String filepath) 
	{
		FileWriter output = null;
		BufferedWriter bw = null;

		try {
			output = new FileWriter(filepath);
			bw = new BufferedWriter(output); 
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < width; j++) {
					if (j==width-1) 
					{
						output.write(grid[i][j] + "\r");
						System.out.println(grid[i][j]);
					}
					else 
					{
						output.write(grid[i][j] + ",");
						System.out.print(grid[i][j] + ",");
					}
				}

			}
			bw.close();
			output.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
			return;
		}
	}

/*------------------------End Data Onput -----------------------------------------------------*/
	
	
/* -----------------------Logical Check for Possible Values ----------------------------------*/	

	/**
	 * Find what possible values are allowed in each cell based on rows, columns, and box
	 * @param row
	 * @param col
	 * @return Set<Integer>
	 */

	
	private Set<Integer> getPossibleValue(int row, int col) 
	{
		Set<Integer> rowSet = new TreeSet<Integer>(getRow(row));
		Set<Integer> colSet = new TreeSet<Integer>(getCol(col));
		Set<Integer> boxSet = new TreeSet<Integer>(getBox(row, col));
		Set<Integer> intersection;
		rowSet.retainAll(colSet);
		rowSet.retainAll(boxSet);
		intersection = rowSet;
		return intersection;
	}

	/**
	 * Eliminate certain numbers if they already exist in the same row
	 * @param row
	 * @return ArrayList<Integer>
	 */

	private ArrayList<Integer> getRow(int row) 
	{

		ArrayList<Integer> possibleRowValues = new ArrayList<Integer>(possibleValues);  //Assigns the row all the possible values

		for (int j = 0; j<grid[row].length; ++j)
		{
			if (grid[row][j] != 0)
			{
				possibleRowValues.remove(Integer.valueOf(grid[row][j]));				//If there is a number that already exists in the same row, remove from possible values
			}
		}
		return possibleRowValues;	
	}


	/**
	 * Eliminate certain numbers if they already exist in the same column
	 * @param col
	 * @return ArrayList<Integer>
	 */

	private ArrayList<Integer> getCol(int col) 
	{

		ArrayList<Integer> possibleColValues = new ArrayList<Integer>(possibleValues);	//Assigns the column all the possible values

		for (int i = 0; i<grid[col].length; ++i)
		{

			if (grid[i][col] != 0)
			{
				possibleColValues.remove(Integer.valueOf(grid[i][col]));				//If there is a number that already exists in the same column, remove from possible values
			}

		}
		return possibleColValues;	
	}

	/**
	 * Eliminate certain numbers if they already exist in the same NxN box
	 * @param row
	 * @param col
	 * @return ArrayList<Integer>
	 */

	private ArrayList<Integer> getBox(int row, int col) 
	{

		ArrayList<Integer> possibleBoxValues = new ArrayList<Integer>(possibleValues);
		int sqrtSize = (int) Math.sqrt(width);
		int boxrowoffset = (row/sqrtSize)*sqrtSize;
		int boxcoloffset = (col/sqrtSize)*sqrtSize;

		for (int i = 0; i<sqrtSize; ++i)
		{
			for (int j = 0; j<sqrtSize; ++j)
			{
				possibleBoxValues.remove(Integer.valueOf(grid[i+boxrowoffset][j+boxcoloffset]));
				//System.out.print(grid[i+boxrowoffset][j+boxcoloffset]);
			}
			//System.out.println("");
		}
		//System.out.println("-----");
		return possibleBoxValues;
	}
	
	
	
/* ----------------------------End Logical Checks --------------------------------*/	

	

/* ---------------------------------- Solver -------------------------------------*/	


	
	/**
	 * public method to start solving our Sudoku
	 */

	private void solve() 
	{
		if (!solve(0, 0)) 
		{	// Start with the first cell in our grid, if everything returns false, then puzzle is unsolvable.
			System.out.println("Not a solvable Sudoku, check your input grid.");
		}
	}


	/**
	 * Recursive function that will iterate through all the grids
	 * and try every possible value (1-9).  Also this function
	 * provides a backtrack, so we can revert back to our grid
	 * when we are guessing.
	 * @param row
	 * @param col
	 * @return boolean
	 */
	
	private boolean solve(int row, int col) 
	{
		// We are going top to down, and left to right
		if (row == width)   //this will bring us back to the top row once we reach the bottom
		{
			row = 0;
			col++;
		}
		if (col == width)   //if we reach the last row cell in the last column, the puzzle is solved!
		{
			return true;
		}

		if (grid[row][col] == 0) 
		{
			for (int i: getPossibleValue(row, col))   // this will iterate this loop only for the possible values that are determined based on what exists in the same row, column, and box
			{
				{
					grid[row][col] = i;				//if value is eligible assign it to the cell in the grid
					if (solve(row + 1, col)) 		//recursive function that will move on to the next cell and solve again
					{
						return true;
					}
				}
			}
		} 
		else 			
		{
			return solve(row + 1, col);           // if cell is not zero move onto the next cell
		}
		grid[row][col] = 0;						  //if value does not work, reset the cell back to 0
		return false;
	}

	
/* ------------------------- End Solver ---------------------------------*/	



}


