//package ai_trabalho1;

import java.util.*;


class NodeCostComparator implements Comparator<Node>{
    @Override
    public int compare(Node x, Node y){
    	
        if (x.cost < y.cost){
            return -1;
        }
        if (x.cost > y.cost){
            return 1;
        }
        return 0;
    }
}

class Node{
	int[][] board;
	int depth;
	int width;
	Node father;
	int cost;
	int operator;
	
	Node(){
		depth=cost=operator	=-1;
		father				=null;
	}
	Node(int w, Node f,int d,int c,int op){
		board 		= new int[w][w];
		father 		= f;
		depth		= d;
		cost 		= c;
		width 		= w;
		operator	=op;
	}
	
	Node(int w,int id,boolean isGoal) {
		father		=null;
		depth 		= 0;
		cost 		= 0;
		width 		= w;
		operator 	= -1;
		
				
		/*

		6 12 0 9
		14 2 5 11
		7 8 4 13
		3 10 1 15

		14 6 12 9
		7 2 5 11
		8 4 13 15
		3 10 1 0

		%%%%%%%%%%%%%%%%%%%%%depth=8*/


		switch(id) {
		case 0: // Depth = 12
			if(!isGoal) 
				board = new int[][]{{1, 2, 3, 4},
									{5, 6, 8, 12},
									{13, 9, 0, 7},
									{14, 11, 10, 15}};
			else
				board = new int[][] {{1, 2, 3, 4},
									{5, 6, 7, 8},
									{9, 10, 11, 12},
									{13, 14, 15, 0}};
			break;
		case 1:	//Depth = 4
			if(!isGoal) 
				board = new int[][]{{1, 2, 3, 4},
								{5, 0, 7, 8},
								{9, 6, 10, 12},
								{13, 14, 11, 15}};
			else
				board = new int[][] {{1, 2, 3, 4},
									{5, 6, 7, 8},
									{9, 10, 11, 12},
									{13, 14, 15, 0}};
			break;
		
		case 2: // Depth = 13
			if(!isGoal)
				board = new int[][] {{9, 12, 0, 7},
									{14, 5, 13, 2},
									{6, 1, 4, 8},
									{10, 15, 3, 11}};
			else
				board = new int[][] {{9, 5, 12, 7},
									{14, 13, 0, 8},
									{1, 3, 2, 4},
									{6, 10, 15, 11}};
			break;
		case 3:// Depth = 8
			if(!isGoal)
				board = new int[][] {{6, 12, 0, 9},
									{14, 2, 5, 11},
									{7, 8, 4, 13},
									{3, 10, 1, 15}};
			else
				board = new int[][] {{14, 6, 12, 9},
									{7, 2, 5, 11},
									{8, 4, 13, 15},
									{3, 10, 1, 0}};
			break;
					
		}	
		if ( id == -1) {
			Scanner in = new Scanner(System.in);
			if(!isGoal)
				System.out.print("Input for the stating config:");
			else
				System.out.print("Input for the goal config:");
			board = new int[w][w];
			for (int l=0 ; l< w ; l++) {
				for (int c=0 ; c< w ; c++) {
					board[l][c] = in.nextInt();
				}
			}
		}
	}
	Node(Node n) {
		board 		= new int[n.width][n.width];
		father		= n;
		depth 		= n.depth+1;
		cost 		= 0;
		width 		= n.width;
		
		
		for (int l=0 ; l<n.width ; l++) {
			for (int c=0 ; c<n.width ; c++) {
				board[l][c] = n.board[l][c];
			}
		}
	}
	
}
class FBoards{
	static int depth_LIMIT;
	static int heuristic;
	
	public static void mPrint(int[][] matrix,int w) {
		for (int l=0 ; l<w ; l++) {
			for (int c=0 ; c<w ; c++) {
				System.out.print(matrix[l][c] + "  |  ");
			}
			System.out.println();
		}
		System.out.println("_________________________________________");
	
	}
	
	public static boolean IsGoal(Node test, Node goal){
		
		int limit = test.width; 
		
		for (int l=0;l<limit;l++) {
			for (int c=0;c<limit;c++) {
				if(test.board[l][c] != goal.board[l][c]) return false;
			}
		}
		return true;
		
	}
	
	
	public static int getBlankRow(Node b){
		
		int limit = b.width;
		
		for (int l=0 ; l < limit ; l++) {
			for (int c=0 ; c < limit ; c++) {
				if(b.board[l][c] == 0) return limit - l;
				//if(b.board[l-1][c-1] == 0) return l;
			}
		}
		
		return -1;
	}
	
	
	public static int getInversions(Node b){
		int width = b.width;
		int sum=0;
		
		for(int l=0; l< width ; l++) {
			for(int c=0 ; c<width; c++ ) {
				
				for(int l1 = l; l1 < width ; l1++) {
					for(int c1 = 0 ; c1 < width ; c1++ ) {
						
						if( !(l1==l && c1<=c) && b.board[l1][c1] < b.board[l][c] && b.board[l1][c1] != 0) {
							sum++;
						}
					}
					
				}
			}
		}	
		
		return sum;
	}
	
	public static int IsStandard(Node b){
		int limit = b.width*b.width;
		int aux=1;
		
		for(int l=0;l<b.width; l++) {
			for(int c=0;c<b.width; c++) {
				if(aux != b.board[l][c] && aux != limit) return 0;
				
				aux++;
			}
		}
		return 1;
	}

	public static boolean Solvable(Node b,Node goal){
		int k = getBlankRow(b);
		int invB = getInversions(b);
		int stando= IsStandard(goal);
		
		if ( (  (invB%2 == 0) == (k%2 == 1) ) && stando == 1 ) return true;
		
		int invGoal = getInversions(goal);
		int k1 = getBlankRow(goal);
		
		
		if ((   (invB%2 == 0) == (k%2 == 1) ) == ((invGoal%2 == 0) == (k1%2 == 1)) && stando == 0) return true;
		
	
		
		
		return false;
	}
	
	public static Descendants getDescendants(Node n,int val,int width){
		int l=-1,c=-1;
		Descendants x= new Descendants(n);
		
		//GET ROW AND COLUMN OF THE BLANK SPACE ( 0 )
		for(l=0; l<n.width;l++) {
			for(c=0; c<n.width;c++) {
				
				if(n.board[l][c] == val) {
					
					if(l != 0) {
						x.descs[0].board[l][c] 		= x.descs[0].board[l-1][c];
						x.descs[0].board[l-1][c]	= val;
					}
					if(l < width-1) {
						x.descs[1].board[l][c] 		= x.descs[1].board[l+1][c];
						x.descs[1].board[l+1][c]	= val;
					}

					if(c < width-1) {
						x.descs[2].board[l][c] 		= x.descs[2].board[l][c+1];
						x.descs[2].board[l][c+1]	= val;
					}
					if(c != 0) {
						x.descs[3].board[l][c] 		= x.descs[3].board[l][c-1];
						x.descs[3].board[l][c-1]	= val;
					}
					return x;
					
				}
			}
			
		}
		return x;
		
	}
	
	public static boolean SearchInStack(Stack<Node> st,Node n) {
		
		for(Node obj : st)
		{
		   	if(Arrays.deepEquals(obj.board,n.board))
		   		return true;
		}
		
		return false;
	}
	
	public static boolean SearchInQueue(Queue<Node> st,Node n) {
		
		for(Node obj : st){
		   	if(Arrays.deepEquals(obj.board,n.board))
		   		return true;
		}
		
		return false;
	}
	
	public static Node SearchInPriQueue(Queue<Node> st,Node n) {
		
		for(Node obj : st){
		   	if(Arrays.deepEquals(obj.board,n.board))
		   		return obj;
		}
		
		return null;
	}
	
	public static void DepthFirstSearch(Node b,Node goal){
		if(!(Solvable(b,goal))) {
			System.out.println("=> It is impossible to reach a solution.");
			return;
		}
		
		Stack<Node> st = new Stack<Node>();
		Map<Node,Node> path = new HashMap<Node,Node>();
		
		st.push(b);
		Node n;
		Descendants d;
		while( !(st.empty())) {
			n = st.pop();
			if(IsGoal(n,goal)) {
				System.out.println("=> Solution found!!!!");
				PrintMap(path,n);
				return; //PATH TO SOLUTION ?
			}
			d = new Descendants (n);
			d= getDescendants(n,0,n.width);
			for(int c=0 ; c<n.width ; c++) {
		   		//mPrint(d.descs[c].board , 4);
				if ( d.descs[c] != null && !SearchInStack(st,d.descs[c]) && d.descs[c].depth <= depth_LIMIT ) {
					st.push(d.descs[c]);
					path.put(d.descs[c], n);
				}
				//------------------------------
				// TO LIMIT DEPTH - Add to the if statement: && d.desc[c].depth < k   | WHERE K IS THE DEPTH LIMIT
				//------------------------------	
				
			}
			
			
		}

		System.out.println("=> No solution found");
		
		return;
		
	}
	
	public static void IDFS(Node b,Node goal){
		if(!(Solvable(b,goal))) {
			System.out.println("=> It is impossible to reach a solution.");
			return;
		}
		int k=0;
		while(k != Integer.MAX_VALUE) {
			k++;
			Stack<Node> st = new Stack<Node>();
			Map<Node,Node> path = new HashMap<Node,Node>();
			
			st.push(b);
			Node n;
			Descendants d;
			
			while( !(st.empty())) {
				n = st.pop();
				if(IsGoal(n,goal)) {
					System.out.println("=> Solution found!!!!");
					PrintMap(path,n);
					return; //PATH TO SOLUTION ?
				}
				d = new Descendants (n);
				d= getDescendants(n,0,n.width);
				for(int c=0 ; c<n.width ; c++) {
			   		//mPrint(d.descs[c].board , 4);
					if ( d.descs[c] != null && !SearchInStack(st,d.descs[c]) && d.descs[c].depth <= k ) {
						st.push(d.descs[c]);
						path.put(d.descs[c], n);
					}
					//------------------------------
					// TO LIMIT DEPTH - Add to the if statement: && d.desc[c].depth < k   | WHERE K IS THE DEPTH LIMIT
					//------------------------------	
					
				}
				
				
			}
		}
		System.out.println("No solution found");
		return;
		
	}

	public static void BreathFirstSearch(Node b,Node goal){
		if(!(Solvable(b,goal))) {
			System.out.println("=> It is impossible to reach a solution.");
			return;
		}
		
		Queue<Node> q = new LinkedList<Node>();
		Map<Node,Node> path = new HashMap<Node,Node>();
		
		q.add(b);
		Node n;
		Descendants d;
		while( !(q.isEmpty())) {
			n = q.remove();
			if(IsGoal(n,goal)) {
				System.out.println("=> Solution found!!!!");
				PrintMap(path,n);
				return; //PATH TO SOLUTION ?
			}
			d = new Descendants (n);
			d= getDescendants(n,0,n.width);
			for(int c=0 ; c<n.width ; c++) {
		   		//mPrint(d.descs[c].board , 4);
				if ( d.descs[c] != null && !SearchInQueue(q,d.descs[c])) {
					q.add(d.descs[c]);
					path.put(d.descs[c], n);
				}
				//------------------------------
				// TO LIMIT DEPTH - Add to the if statement: && d.desc[c].depth < k   | WHERE K IS THE DEPTH LIMIT
				//------------------------------	
				
			}
			
			
		}

		System.out.println("=> No solution found");
		
		return;
		
	}

	public static void GreedySearch(Node b,Node goal){
		// Check if it is possible to reach the solution
		if(!(Solvable(b,goal))) {
			System.out.println("=> It is impossible to reach a solution.");
			return;
		}
		Comparator<Node> comparator = new NodeCostComparator();
		
		PriorityQueue<Node> q = new PriorityQueue<Node>(comparator);
		Map<Node,Node> path = new HashMap<Node,Node>();
		
		q.add(b);
		Node n;
		Descendants d;
		
		while( !(q.isEmpty())) {
			n = q.remove();
			if(IsGoal(n,goal)) {
				System.out.println("=> Solution found!!!!");
				PrintMap(path,n);
				return; 
			}
			
			d = new Descendants (n);
			d = getDescendants(n,0,n.width);
			Descendants.SetCost(d,goal,heuristic);
			Node auxNode;
			for(int c=0 ; c<n.width ; c++) {
				auxNode = SearchInPriQueue(q,d.descs[c]);
				if( auxNode == null) {
					if ( d.descs[c] != null && d.descs[c].depth <= depth_LIMIT) {
						q.add(d.descs[c]);
						path.put(d.descs[c], n);
					}
				}else if(auxNode.depth > d.descs[c].depth){
					q.remove(auxNode);
					q.add(d.descs[c]);
				}
			}
			
			
			
		}

		System.out.println("=> No solution found");
		
		return;
		
	}
	
	// A* SEARCH ALGORITHM
	public static void AStar(Node b,Node goal){
		// Check if it is possible to reach the solution
		if(!(Solvable(b,goal))) {
			System.out.println("=> It is impossible to reach a solution.");
			return;
		}
		Comparator<Node> comparator = new NodeCostComparator();
		
		PriorityQueue<Node> q = new PriorityQueue<Node>(comparator);
		Map<Node,Node> path = new HashMap<Node,Node>();
		
		q.add(b);
		Node n;
		Descendants d;

		Scanner in = new Scanner(System.in);
		while( !(q.isEmpty())) {
			n = q.remove();
			
			if(IsGoal(n,goal)) {
				System.out.println("=> Solution found!!!!");
				PrintMap(path,n);
				return; 
			}
			
			d = new Descendants (n);
			d = getDescendants(n,0,n.width);
			Descendants.SetCost(d,goal,heuristic);
			Node auxNode;
			for(int c=0 ; c<n.width ; c++) {
				auxNode = SearchInPriQueue(q,d.descs[c]);
				if( auxNode == null) {
					if ( d.descs[c] != null && d.descs[c].depth <= depth_LIMIT) {
						d.descs[c].cost += d.descs[c].depth;
						q.add(d.descs[c]);
						path.put(d.descs[c], n);
					}
				}else if(auxNode.depth > d.descs[c].depth){
					q.remove(auxNode);
					q.add(d.descs[c]);
				}
					
			}
			
			
			
		}

		System.out.println("=> No solution found");
		
		return;
		
	}
	
	// Print the solution by going through the whole map	
	public static void PrintMap(Map<Node,Node> st,Node start) {
		
		ArrayList<String> out = new ArrayList<String>();
		String aux="";
		Node curr = st.get(start);
		while (curr != null) {
			switch(curr.operator) {
	   		
	   		case 0: aux = " Up";
	   			break;
	   		case 1: aux = " Down";
   				break;
	   		case 2: aux = " Right";
   				break;
	   		case 3: aux = " Left";
   				break;
   			default: aux = " \nStarting config";
   				break;
	   		
	   		}
	   		aux += " - Depth = " + curr.depth;
			curr = st.get(curr);
			
			out.add(aux);

		}
		for(int c = out.size() - 1 ; c >= 0 ; c--){
			System.out.println(out.get(c));
		}
		switch(start.operator) {
   		
   		case 0: aux = " Up";
   			break;
   		case 1: aux = " Down";
				break;
   		case 2: aux = " Right";
				break;
   		case 3: aux = " Left";
				break;
		default: 
				break;
   		
   		}
		System.out.println(aux + " - Depth = " + start.depth);
		
	}

	
}


class Descendants{
	Node[] descs; //[ UP , DOWN , RIGHT , LEFT]
	
	Descendants(Node n){
		descs= new Node[4];
		descs[0]= new Node(n);
		descs[1]= new Node(n);
		descs[2]= new Node(n);
		descs[3]= new Node(n);
		
		descs[0].operator = 0;
		descs[1].operator = 1;
		descs[2].operator = 2;
		descs[3].operator = 3;
	}
	
	public static void SetCost(Descendants d,Node goal,int heuristic) {
		int w = d.descs[0].width;
		
		if(heuristic == 0) {    // Somatorio de peças fora do lugar
			for(int n = 0 ; n < 4 ; n++) {
				for (int l=0 ; l<w ; l++) {
					if( d.descs[n] == null) 
						break;
					for (int c=0 ; c<w ; c++) {
						if(d.descs[n].board[l][c] != goal.board[l][c])
							d.descs[n].cost++;
					}
				}
				
				
			}
			
			
		}else {					// Manhattan distance
			for(int n = 0 ; n < 4 ; n++) {
				//Go through Goal board
				for (int l=0 ; l<w ; l++) {
					for (int c=0 ; c<w ; c++) {
						outerLoop:
						for (int l1=0 ; l1<w ; l1++) {
							for (int c1=0 ; c1<w ; c1++) {
								if(d.descs[n].board[l][c] == goal.board[l1][c1]){
									d.descs[n].cost += (Math.abs(l - l1) + Math.abs(c - c1));
									break outerLoop;
								}
							}
						}
					}
				}
				
				
			}
		}
		
		
		
	}
	
	
	
}





public class trabalho {
	
	
	
	public static void main (String[] args) {
		
		Scanner in = new Scanner(System.in);
		System.out.print("Select config ID (select -1 for manual input ; see readme for more details): ");
		int id = in.nextInt();
		
		Node b = new Node(4,id,false);
		
		Node goal = new Node(4,id,true);
		System.out.println();
		System.out.println("Select search method:");
		System.out.println("	1) Depth first search");
		System.out.println("	2) Breath first search");
		System.out.println("	3) Iterative deepening depth first search");
		System.out.println("	4) Greedy search");
		System.out.println("	5) A* search");

		System.out.println();
		System.out.print("Option: ");
		int op = in.nextInt();
		
		if(op == 4 || op == 5) {
			System.out.println();
			System.out.println("Select heuristic:");
			System.out.println("	1) Sum of out of place tiles");
			System.out.println("	2) Manhattan distance");
			System.out.println();
			System.out.print("Option: ");
			int h = in.nextInt();
			if(h == 1 || h == 2) {
				FBoards.heuristic = op - 1;
			}else {
				System.out.println();
				System.out.println("Invalid option so we'll just use manhattan distance");
				FBoards.heuristic = 1;
			}
			
		}
		
		System.out.println();
		if(op != 3) {
			System.out.print("Depth limit (-1 for unlimited search): ");
			FBoards.depth_LIMIT= in.nextInt();
			if(FBoards.depth_LIMIT == -1)
				FBoards.depth_LIMIT = Integer.MAX_VALUE;
		}
		
		switch(op) {
		case 1:
			FBoards.DepthFirstSearch(b, goal);
			break;
		case 2:
			FBoards.BreathFirstSearch(b, goal);
			break;
		case 3:
			FBoards.IDFS(b, goal);
			break;
		case 4:
			FBoards.GreedySearch(b, goal);
			break;
		case 5:
			FBoards.AStar(b, goal);
			break;
		default:
			System.out.println("=> INVALID OPTION <=");
			break;
		
		
		}
			
		in.close();
	}
}