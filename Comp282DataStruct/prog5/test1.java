import java.io.*; // for IOException



public class test1 {

	final static String GraphLocation = new String(
			"/home/evance/Documents/comp282/prog5/graphs/");

	public static void main(String[] args) throws IOException {
		Vertex_Node startVertex;
		Graph g;

		for (int i = 1; i <= 10; i++) {
			g = new Graph();
			startVertex = g.input(GraphLocation + "Graphs" + i + ".txt");
			System.out.println("Test #" + i + ":  BFS  -- " + Graph.myName());
			System.out.println("=======");
			g.output();
			System.out.println();
			g.output_bfs(startVertex);
			System.out.println();
			if (i == 3) {
				System.out.println("=======");
				g.output();
				System.out.println();
				g.output_bfs(startVertex);
				System.out.println();

			}
			if (Graph.implementedDFS()) {
				System.out.println("Test #" + i + ":  DFS  -- "
						+ Graph.myName());
				System.out.println("=======");
				g.output_dfs(startVertex);
				System.out.println();
				if (i == 3) {
					System.out.println("=======");
					g.output_dfs(startVertex);
					System.out.println();

				}
			}
		}
		System.out.println("Done with " + Graph.myName() + "'s test run.");
	}
}
