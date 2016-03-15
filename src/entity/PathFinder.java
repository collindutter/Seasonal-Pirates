package entity;
import game.Game;
import game.Utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tiles.Node;
import tiles.Tile;

public class PathFinder
{
	
	BufferedImage img;
	public PathFinder()
	{
		try
		{
			img = ImageIO.read(ClassLoader.getSystemResourceAsStream("Menus/dimmer.png"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private ArrayList<Node> path;

	private int nullNode = -1;//represent a null node
	
	private int[] openListID;//openListID holds the ID's of nodes in the openList. We keep it "sorted" using binary heap
	private int[] closedListID;//closedListID holds the ID's of nodes in the closedList
	private int[] parentID;//holds parents of a node. example: parents[ID] gets the parent ID of ID
	private int[] checkedX;//checkedX and checkedY hold the coords of every single node checked
	private int[] checkedY;
	private int[] fScores;
	private int[] gScores;
	
	private boolean[][] inOpenList;//indicates whether or not its in the openlist or closedlist
	private boolean[][] inClosedList;
	private int referenceCol = 0;//values that we subtract in order to make the search area relative to zero
	private int referenceRow = 0;
	
	private int nodesChecked = 0;
	private int openListSize = 0;
	private int closedListSize = 0;
	
	private int startCol;
	private int startRow;
	private int destinationCol; 
	private int destinationRow; 
	
	private int estimatedDistance;
	private int searchBuffer;
	private int absoluteWidth;
	private int absoluteHeight;
	private int widthBuffer;
	private int heightBuffer;
	private int searchWidth;
	private int searchHeight;
	private int oneDArraySize;
	
	private void defineSearchArea(Entity entity)
	{
		estimatedDistance = calcH((int)entity.getCol(), (int)entity.getRow());
		searchBuffer = (int) (30 + Math.sqrt(estimatedDistance) / 8);//an approximation
		absoluteWidth = (Math.abs(destinationCol - startCol));
		absoluteHeight = (Math.abs(destinationRow - startRow));
		widthBuffer = (int) (searchBuffer);
		heightBuffer = (int) (searchBuffer);
		searchWidth = absoluteWidth + widthBuffer * 2;
		searchHeight = absoluteHeight + heightBuffer * 2;
		oneDArraySize = (searchWidth * searchHeight);
		
		//finds out the reference point for the 2d arrays
		if(startCol < destinationCol && startRow < destinationRow)//top left quadrant
		{
			referenceCol = startCol - widthBuffer;
			referenceRow = startRow - heightBuffer;
		}
		else if(startCol > destinationCol && startRow < destinationRow)//top right
		{
			referenceCol = destinationCol - widthBuffer;
			referenceRow = startRow - heightBuffer;
		}
		else if(startCol < destinationCol && startRow > destinationRow)//bottom left
		{
			referenceCol = startCol - widthBuffer;
			referenceRow = destinationRow - heightBuffer;
		}
		else if(startCol > destinationCol && startRow > destinationRow)//bottom right
		{
			referenceCol = destinationCol - widthBuffer;
			referenceRow = destinationRow - heightBuffer;
		}
	
		if(referenceCol + searchWidth > Game.getWorld().getWorldWidth())
			referenceCol = Game.getWorld().getWorldWidth() - searchWidth;
		if(referenceRow + searchHeight > Game.getWorld().getWorldHeight())
			referenceRow = Game.getWorld().getWorldHeight() - searchHeight;
		if(referenceCol < 0)
			referenceCol = 0;
		if(referenceRow < 0)
			referenceRow = 0;
	}
	
	protected synchronized ArrayList<Node> calculatePath(Entity entity)
	{
		path = new ArrayList<Node>(estimatedDistance);//calcH returns an estimated distance
		if(!Game.getWorld().getTile(entity.getDestinationCol(), entity.getDestinationRow()).isWalkable())
			return path;
		
		destinationCol = entity.getDestinationCol();
		destinationRow = entity.getDestinationRow();
		startCol = (int)entity.getCol();
		startRow = (int)entity.getRow();

		//doesnt path find if you right clicked on the unit
		if(startCol == destinationCol && startRow == destinationRow)
			return null;
		
		defineSearchArea(entity);

		openListID = new int[oneDArraySize];
		closedListID = new int[oneDArraySize];
		parentID = new int[oneDArraySize];
		checkedX = new int[oneDArraySize];
		checkedY = new int[oneDArraySize];
		fScores = new int[oneDArraySize];
		gScores = new int[oneDArraySize];
		
		inOpenList = new boolean[searchWidth][searchHeight];
		inClosedList = new boolean[searchWidth][searchHeight];
		
		nodesChecked = 0;
		openListSize = 0;
		closedListSize = 0;
		
		int destinationID = nullNode;
		
		//startTile
		addToOpenList(startCol, startRow, nullNode);//nullNode, because startTile doesnt have a parent 
		
		for(int row = startRow - 1; row <= startRow + 1; row++)
		{
			for(int col = startCol - 1; col <= startCol + 1; col++)
			{
				if(col < 0 || col >= Game.getWorld().getWorldWidth())
					continue;
				if(row < 0 || row >= Game.getWorld().getWorldHeight())
					continue;
				//if adjNode is startTile, continue
				if(col == startCol && row == startRow)
					continue;
				
				if(col == destinationCol && row == destinationRow)//if the its the destination, return
				{
					path.add(0, new Node(col, row));
					return path;
				}
					
				if(Game.getWorld().getTileID(col, row) == Tile.NULL_TILE)
					continue;
			
				if(Game.getWorld().getTile(col, row).isWalkable())
					addToOpenList(col, row, 0);
			}
		}
		
		
		closedListID[closedListSize] = 0;
		closedListSize++;
		inClosedList[startCol - referenceCol][startRow - referenceRow] = true;
		removeFromOpenList();//removes start tile

		do
		{
		// check if destination is in closed list.
			if(nodesChecked >= oneDArraySize)
				return null;
			if (inClosedList[destinationCol - referenceCol][destinationRow - referenceRow])
			{
				int pathNode = destinationID;
				//build path
				while (true)
				{
					path.add(0, new Node(checkedX[pathNode], checkedY[pathNode]));
					pathNode = parentID[pathNode];
					if (pathNode == 0)
					{
						System.out.println("nodesChecked " + nodesChecked);
						return path;
					}
				}
			}
			// pick node from openList with lowest F for currentTile
			int currentID = openListID[0];
			int currentCol = checkedX[currentID];
			int currentRow = checkedY[currentID];
		
			//add the node with the lowest f score to the closed list, and remove it from the openlist
			closedListID[closedListSize] = currentID;
			closedListSize++;
			inClosedList[currentCol - referenceCol][currentRow - referenceRow] = true;
			removeFromOpenList();
			
			//decides what nodes should be placed in the openList
			for (int row = currentRow - 1; row <= currentRow + 1; row++) 
			{
				for(int col = currentCol - 1; col <= (int) currentCol + 1; col++)
				{
					//if node isn't on the map, ignore it
					if(col < 0 || col >= Game.getWorld().getWorldWidth())
						continue;
					if(row < 0 || row >= Game.getWorld().getWorldHeight())
						continue;
				
					//if node is not contained in the search area, ignore it
					if(col <= referenceCol || col >= referenceCol + searchWidth)
						continue;
					if(row <= referenceRow || row >= referenceRow + searchHeight)
						continue;
					
					// if node is on a null tile, ignore it
					if (Game.getWorld().getTileID(col, row) == Tile.NULL_TILE)
						continue;
//					
//					// if the adjacent square is in the closedList already, ignore it.
					if(inClosedList[col - referenceCol][row - referenceRow] == true)
						continue;
//					
//					// if the adjacent square is not walkable, ignore it.
					if(!Game.getWorld().getTile(col, row).isWalkable())
						continue ;
					
					//setting destinationID is how the path building knows which node to start tracing backwards from
					if(col == destinationCol && row == destinationRow)
						destinationID = currentID;
					
//					// if the adjacent square is not in the open list, add and set parent
					if (!inOpenList[col - referenceCol][row - referenceRow])
					{
						addToOpenList(col, row, currentID);
						continue;
					}
				}
			}
			try
			{
				Thread.sleep(0);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(nodesChecked == oneDArraySize)
			{
				return null;
			}
		} while (openListSize != 0);
		return null;
	}	
	
	private void addToOpenList(int col, int row, int pID)
	{
		inOpenList[col - referenceCol][row - referenceRow] = true;
		checkedX[nodesChecked] = col;
		checkedY[nodesChecked] = row;
		
		if(pID == nullNode)//this should only happen if we're adding the start tile, because start tile doesnt have a parent
			parentID[nodesChecked] = 0;
		else 
			parentID[nodesChecked] = pID;
		fScores[nodesChecked] = calcG(col, row, nodesChecked, pID) + calcH(col, row);
		
		openListID[openListSize] = nodesChecked;//sticks new ID on the end of the openList
		nodesChecked++;
		openListSize++;
		
		int index = openListSize - 1; //Index of the node in the heap
		
		while(fScores[openListID[index]] < fScores[openListID[index / 2]])
		{
			//If the f of the current index is less than its parent, switch so that this index is now a step up in the heap
			//Swap
			int tempID = openListID[index / 2];
			openListID[index / 2] = openListID[index];
			openListID[index] = tempID;
			index = index / 2;
		}
	}
	
	private void removeFromOpenList()
	{
		inOpenList[(checkedX[openListID[0]]) - referenceCol][(checkedY[openListID[0]]) - referenceRow] = false;

		openListSize--;
		openListID[0] = openListID[openListSize];//Take the last element of the open list and set it to the first
		
		int index = 0;//This is zero because the we start the process from the top of the heap
		int lowestChild;//Index of child that has the lower f score
		int leftChild = 1;//1 and 2 are always the indexes of the left and right children at the start
		int rightChild = 2;
		
		while(leftChild < openListSize)//Continue while this index still has a left child
		{
			//Find smaller child
			if(rightChild < openListSize)//If current index has a right child, figure out which child is smaller
				lowestChild = fScores[openListID[leftChild]] <= fScores[openListID[rightChild]] ? leftChild : rightChild;
			else//If it current index does not have a right child, assign smallest child to the left child. We know for sure it has a left child because of the loop condition
				lowestChild = leftChild;
			
			if(fScores[openListID[index]] < fScores[openListID[lowestChild]])
				return;//If this index's f is less than the lowest f of its children, it has found its position in the heap
			
			//Swap positions with child
			int tempID = openListID[index];
			openListID[index] = openListID[lowestChild];
			openListID[lowestChild] = tempID;
			//The index is now the position of the child with the smaller f score
			index = lowestChild;
			
			leftChild = index * 2;//Find children of the new index
			rightChild = (index * 2) + 1;
		}
	}
	
	public void render(Graphics g) 
    { 
		if(closedListSize != 0)
		{
			for(int i = 0; i <= openListSize; i++)
			{
//				double renderCol2 = checkedX[openListID[i]] - Game.getWorld().getCamera().getCol();
//				double renderRow2 = checkedY[openListID[i]] - Game.getWorld().getCamera().getRow();
//				g.drawImage(img, (int)(Utility.toPixels(renderCol2)), (int)(Utility.toPixels(renderRow2)), Tile.TILE_SIZE, Tile.TILE_SIZE, null);
//				g.setColor(new Color(0x58FF16)); 
//				g.fillRect((int) Utility.toPixels(renderCol2), (int)Utility.toPixels(renderRow2), Tile.TILE_SIZE, Tile.TILE_SIZE); 


//							g.setColor(Color.WHITE);
//							g.setFont(new Font("Ariel", Font.BOLD, 12));
//							g.drawString(String.valueOf(gScores[closedListID[i]]), (int) Utility.toPixels(renderCol2 + .4), (int)Utility.toPixels(renderRow2 + .4));
			}}
			if(path != null)
			{
				for(int i = 0; i < path.size(); i++)
				{
					double renderCol4 = path.get(i).getCol() - Game.getWorld().getCamera().getCol(); 
					double renderRow4 = path.get(i).getRow() - Game.getWorld().getCamera().getRow(); 
					g.setColor(Color.RED); 
					g.fillRect((int) Utility.toPixels(renderCol4), (int)Utility.toPixels(renderRow4), Tile.TILE_SIZE, Tile.TILE_SIZE); 
				}
			}
//		}
//		for(int i = 0; i <= closedListSize; i++)
//		{
//			double renderCol2 = checkedX[closedListID[i]] - Game.getWorld().getCamera().getCol(); 
//			double renderRow2 = checkedY[closedListID[i]] - Game.getWorld().getCamera().getRow(); 
//			g.setColor(Color.WHITE);
//			g.setFont(new Font("Ariel", Font.BOLD, Tile.TILE_SIZE / 3));
//			g.drawString(String.valueOf(gScores[closedListID[i]]), (int) Utility.toPixels(renderCol2 + .4), (int)Utility.toPixels(renderRow2 + .5));
//		}
    }
	
	private static int diagonalCost = 14; 
	private static int orthogonalCost = 10;
	
	public int calcH(int col, int row)
	{
		int xDistance = (int) Math.abs(col - destinationCol);
		int yDistance = (int) Math.abs(row - destinationRow);
		if (xDistance > yDistance)
			return diagonalCost * yDistance + orthogonalCost * (xDistance - yDistance);
		else
			return diagonalCost * xDistance + orthogonalCost * (yDistance - xDistance);
	}
	
	public int calcG(int col, int row, int ID, int pID)
	{
		int g = 0;
		
		if(pID != nullNode)//if its parent isnt null
		{
			int parentCol = checkedX[pID];
			int parentRow = checkedY[pID];
			if(((parentCol - col) & (parentRow - row)) == 0)
				g = gScores[pID] + orthogonalCost + Game.getWorld().getTile(col, row).getCost();
			else
				g = gScores[pID] + diagonalCost + Game.getWorld().getTile(col, row).getCost();
		}

		gScores[ID] = g;

		return g;
	}
}


//
//public synchronized ArrayList<Node> findStraightPath(Entity entity)
//{
//	double col = entity.getCol(); 
//	double row = entity.getRow();
//	double destinationCol = entity.getDestinationCol();
//	double destinationRow = entity.getDestinationRow();
//	
//	double angle = Math.atan2(destinationRow - row, destinationCol - col);
//	
//	float step = 1f;
//	float colStep = (float) (Math.cos(angle) * step); 
//	float rowStep = (float) (Math.sin(angle) * step);
//	
//	path = new ArrayList<Node>(100);
//	Node mostRecentNode = new Node((int)col, (int)row);
//	Node destinationNode = new Node((int)destinationCol, (int)destinationRow);
//	
//	double distanceSquared = Utility.distSquared(col, destinationCol, row, destinationRow);
//	
//	//if clicked far away, immediately assume something will block the path and resort to A*
//	//will have to be different for boats
//	if(distanceSquared > 40 * 40)
//		return null;
//	
//	for(double i = 0; i < distanceSquared; i += (step * step))
//	{
//		col += colStep;
//		row += rowStep;
//		
//		if(Game.getWorld().getTile(col, row) == null)
//			return null;
//		
//		if((Game.getWorld().isTileOccupied(col, row) || !Game.getWorld().getTile(col, row).isWalkable()))
//			return null;
//		
//		Node checkNode = new Node((int)col, (int)row);
//		
//		//if drastic change in tile score, let real pathfinding take over
//		
////		if(Game.getWorld().getTile(col, row).getCost() < Game.getWorld().getTile((int)checkNode.getCol(), (int)checkNode.getRow()).getCost() - 50 ||
////				Game.getWorld().getTile(col, row).getCost() > Game.getWorld().getTile((int)checkNode.getCol(), (int)checkNode.getRow()).getCost() + 50)
////		{
////			return null;
////		}
//		
//		if(checkNode.is(mostRecentNode))
//		{
//			continue;
//		}
//		else if(checkNode.is(destinationNode))
//		{
//			path.add(destinationNode);
//			System.out.println("DETECTED STRAIGHT PATH");
//			return path;
//		}
//		else 
//		{
//			mostRecentNode = checkNode;
//			path.add(mostRecentNode);
//		}
//	}
//	
//	return null;
//}

