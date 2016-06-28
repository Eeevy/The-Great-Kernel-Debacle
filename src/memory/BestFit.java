package memory;

import java.util.HashMap;
import java.util.Map;

/**
 * This memory model allocates memory cells based on the best-fit method.
 * 
 * @author "Johan Holmberg, Malmö university"
 * Modified by Evelyn Gustavsson and Emma Shakespeare
 * @since 1.0
 */
public class BestFit extends Memory {
	private Map<Pointer, Integer> startPositionsAndSize;

	/**
	 * Initializes an instance of a best fit-based memory.
	 * 
	 * @param size
	 *            The number of cells.
	 */
	public BestFit(int size) {
		super(size);
		this.startPositionsAndSize = new HashMap<>();
	}
	
	/**
	 * Allocates a number of memory cells.
	 * 
	 * @param size
	 *            the number of cells to allocate.
	 * @return The address of the first cell.
	 */
	@Override
	public Pointer alloc(int size) {
		int minSize = Integer.MAX_VALUE;
		int minStartValue = Integer.MAX_VALUE;
		int y = 0;
		
		while (y < cells.length) {
			if (cells[y] == 0) { // If the position is available.
				int z = y;
				int count = 1; // Count represents number of available spaces in a row.
				while (cells[z] == 0 && z < (cells.length - 1)) {
					z++;
					count++;
				}
				if (count >= size && count <= minSize) {
					// A large enough free space has been found.
					minStartValue = y; // Where we want to start inserting our value.
					minSize = count;
					y = z;
				} else {
					y = z; // The next position to check for availability.
				}
			}
			y++;
		}
		
		Pointer pointer = new Pointer(this);
		pointer.pointAt(minStartValue);
		startPositionsAndSize.put(pointer, size);
		return pointer;
	}

	/**
	 * Releases a number of data cells.
	 * 
	 * @param p
	 *            The pointer to release.
	 */
	@Override
	public void release(Pointer p) {
		int count = p.pointsAt() + startPositionsAndSize.get(p);
		
		for (int i = p.pointsAt(); i < count; i++) {
			cells[i] = 0;
			startPositionsAndSize.remove(p);
		}
	}

	/**
	 * Prints a simple model of the memory.
	 * 
	 * | 	  0 - 110 	| Allocated 
	 * | 	111 - 150 	| Free 
	 * | 	151 - 999 	| Allocated 
	 * |   1000 - 1024	| Free
	 */
	@Override
	public void printLayout() {		
		boolean isFilled = false;
		
		for (int i = 0; i < cells.length; i = i) {
			if (cells[i] == 0) {
				isFilled = false;
				for (int x = i; isFilled == false; x++) {
					try {
						if (cells[x] == 0) {
							isFilled = false;
						} else {
							isFilled = true;
							System.out.println("| " + i + " - " + (x - 1) + " | Free");
							i = x;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						isFilled = true;
						System.out.println("| " + i + " - " + (x - 1) + " | Free");
						i = x;
					}
				}
			} else {
				isFilled = true;
				for (int x = i; isFilled == true; x++) {
					try {
						if (cells[x] > 0) {
							isFilled = true;
						} else {
							isFilled = false;
							System.out.println("| " + i + " - " + (x - 1) + " | Allocated");
							i = x;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						isFilled = false;
						System.out.println("| " + i + " - " + (x - 1) + " | Allocated");
						i = x;
					}
				}
			}
		}	
	}
}