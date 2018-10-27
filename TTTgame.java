import java.util.Arrays;
import java.util.stream.IntStream;

class TTTgame {
	private int moves = 0, moves_left = 9;
	private int[] board = {0,1,2,3,4,5,6,7,8}, history = {-1,-1,-1,-1,-1,-1,-1,-1,-1};

	//Returns true if successful
	boolean makeMove(int pos) {
		boolean is_human = moves_left % 2 == 1;
		//position alread played
		if(pos > 8 || pos < 0) {println("value entered must be in range [0,9]"); return false; }
		if(board[pos] > 8) { println("position "+pos+" already played"); return false; }
		if(board[pos] < 9){ board[pos] = (is_human) ? 10 : 11; history[moves] = pos; moves++; moves_left--;}
		return true;
	}

	int status() {
		//board value 10 = X(human),  11 = O(ai)
		int[] wins = {0,1,2, 3,4,5, 6,7,8, 0,3,6, 1,4,7, 2,5,8, 0,4,8,  2,4,6};
		//If a winner exists, Return -1 if 'X'(human) won. Return 1 if 'O'(ai) won.
		for(int i = 0; i < 24; i+=3) {
			int a = board[wins[i]], b = board[wins[i+1]], c = board[wins[i+2]];
			if(a == b && a == c)
				return (a == 10) ? -1 : 1;
		}
		return 0;	//Return 0 if tie
	}

	int getLeft() { return moves_left;}
	int getMoves() { return moves;}
	int[] getHistory() { return Arrays.copyOf(history, moves); }

	boolean isFull() {
		for(int i = 0; i < 9; i++)
			if(board[i] < 9)
				return false;
		return true;
	}

	int[] openSpaces() {
		int[] spaces = new int[moves_left];
		int k = 0;
		for(int i = 0; i < board.length; i++)
			if(board[i] < 9)
				spaces[k++] = board[i];
		return spaces;
	}

	void showCurrentBoard() {
		String[] lines = new String[6];
		if(moves_left < 9)
			lines[0] = ((moves_left+1) % 2 == 1 ? "p1: " : "ai: ") + history[moves -1] + "         ";
		else
			lines[0] = "              ";
		System.arraycopy(getOutputStrings(board), 0, lines, 1, 5);
		for(int i = 0; i < lines.length; i++)
			println(lines[i]);
		println();
	}

	void showAllMoves() {
		String[] lines = new String[6]; Arrays.fill(lines, "");
		int[] copy = {0,1,2,3,4,5,6,7,8};
		boolean is_human = true;
		for(int i = 0; i < moves; i++) {
			int pos = history[i];
			lines[0] += ((is_human) ? "p1: " : "ai: ") + pos + "         ";
			copy[pos] = (is_human)? 10: 11;
			String[] tmp = getOutputStrings(copy);
			for(int k = 0; k < tmp.length; k++)
				lines[k+1] += tmp[k];
			is_human = !is_human;
		}
		if(moves == 0) {
			lines = new String[] {" 0 | 1 | 2    ", "---+---+---   ", " 3 | 4 | 5    ",
					"---+---+---   ", " 6 | 7 | 8    "};
		}
		println();
		Arrays.stream(lines).forEach(x->println(x));
	}

	String[] getOutputStrings(int[] a_board) {
		String[] s = IntStream.range(0, 9).mapToObj(x->st(a_board[x])).toArray(String[]::new);
		if(moves_left == 9)
			return new String[] {" 0 | 1 | 2    ", "---+---+---   ", " 3 | 4 | 5    ",
					"---+---+---   ", " 6 | 7 | 8    "};
		return new String[] {s[0]+"|"+s[1]+"|"+s[2]+"   ", "---+---+---   ", s[3]+"|"+s[4]+"|"+s[5]+"   ",
				"---+---+---   ", s[6] + "|" + s[7] + "|" + s[8] + "   "};
	}

	String st(int i) {
		if(moves_left == 9)
			return (i == 10) ? " X " : (i == 11) ? " O " :  " " + Integer.toString(i) + " ";
		return (i == 10) ? " X " : (i == 11) ? " O " :  "   ";
	}

	@SuppressWarnings("unchecked")
	<T> void println(T... line) { System.out.println((line.length == 0) ? "" : line[0]); }
	<T> void print(T data) { System.out.print(data); }

	TTTgame() {}
	TTTgame(TTTgame that) {
		this.moves = that.moves;
		this.moves_left = that.moves_left;
		this.board = Arrays.copyOf(that.board, that.board.length);
		this.history = Arrays.copyOf(that.history, that.history.length);
	}
}