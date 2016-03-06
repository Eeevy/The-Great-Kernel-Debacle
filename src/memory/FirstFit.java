package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the first-fit method. 
 * 
 * @author "Johan Holmberg, Malmö university"
 * @since 1.0
 */
public class FirstFit extends Memory {

	// nextAvailablePosition helps us keep track of where to start looking in the memory-array, 
	// so that we do not have to go through it from the start again.
	private int nextAvailablePosition;
	// the start and end positions-map helps us when we want to release a pointer from the memory,
	// and also when we want to make sure our new memory-allocations fit in the available space
	private Map<Integer, Integer> startAndEndPositions;
	
	/**
	 * Initializes an instance of a first fit-based memory.
	 * 
	 * @param size The number of cells.
	 */
	public FirstFit(int size) {
		super(size);
		// TODO Implement this!
		this.nextAvailablePosition = 0;
		this.startAndEndPositions = new HashMap<Integer, Integer>();
	}

	/**
	 * Allocates a number of memory cells. 
	 * 
	 * @param size the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		// TODO Implement this!
		for (int i = this.nextAvailablePosition; i < this.cells.length; i++) {
			if (this.cells[i] == 0) {
				// we found an empty memory cell
				boolean isAvailable = true;
				for (int j = i; j <= size+i; j++){
					// checking to see that there is room enough to allocate something of @size (this could be improved)
					if (this.cells[j] != 0){
						// there wasn't room enough, now we start to look for the next empty memory cell available
						while(this.cells[j] != 0) {
							// we set j to the end position of the occupied space
							j = j+this.startAndEndPositions.get(j);
						}
						i = j-1; // set i to the next available memory cell 
						isAvailable = false;
						break;
					}
				}
				
				if (isAvailable) {
					this.nextAvailablePosition = i+size;
					Pointer p = new Pointer(i, this);
					this.startAndEndPositions.put(i, size);
					return p;
				}
				else{
					continue;
				}			
			}
		}
		return null;
	}
	
	/**
	 * Releases a number of data cells
	 * 
	 * @param p The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		// TODO Implement this!
		int startPosition = p.pointsAt();
		int length = this.startAndEndPositions.get(startPosition).intValue();
		for (int i = 0; i < length; i++){
			this.cells[startPosition+i] = 0;
		}
		
		this.startAndEndPositions.remove(startPosition);
		this.nextAvailablePosition = startPosition;
	}
	
	/**
	 * Prints a simple model of the memory. Example:
	 * 
	 * |    0 -  110 | Allocated
	 * |  111 -  150 | Free
	 * |  151 -  999 | Allocated
	 * | 1000 - 1024 | Free
	 */
	@Override
	public void printLayout() {
		// TODO Implement this!
		System.out.println(this.toString());
	}
	
	/**
	 * Compacts the memory space.
	 */
	public void compact() {
		// TODO Implement this!
	}
}