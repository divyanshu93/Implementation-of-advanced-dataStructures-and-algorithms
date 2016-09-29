import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/** Driver program for LP2, level 1 :PRIM 2 method
 *  @author Shweta Nair  
 */
public class Prim2 extends MST {
	public static void main(String[] args) throws FileNotFoundException {
		
		// input file from console
		Scanner scan=new Scanner(System.in);
		System.out.println("Enter the input file fullpath with filename:");
		Scanner in = new Scanner(new File(scan.next()));
		System.out.println("Prims is reading the input graph...");
		Graph g = Graph.readGraph(in, false);
		System.out.println("MST weight using indexed heap: Prim2 :");
		System.out.println(PrimMSTIndexed(g));
	}
}