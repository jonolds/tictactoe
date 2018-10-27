import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TTTcontrol {
	Random r = new Random();
	Scanner sc = new Scanner(System.in);
	boolean show_all = true;
	int score[] = new int[3];
	public static void main(String[] args) {
		TTTcontrol process = new TTTcontrol();
		process.play();
	}

	void play() {
		TTTgame g = new TTTgame();
		while(g.status() == 0 && !g.isFull()) {
			g.showAllMoves();
			if(g.getLeft() % 2 == 1) humanMove(g); else aiMove(g);
		}
		results(g);
	}

	void aiMove(TTTgame orig) {
		int[] poss_moves = orig.openSpaces();
		int best_score = -99;
		int best_move = -99;
		for(int i = 0; i < poss_moves.length; i++) {
			TTTgame copy = new TTTgame(orig);
			copy.makeMove(poss_moves[i]);
			int score = minimax(copy, copy.getLeft(), false);
			if(score > best_score) {
				best_score = score;
				best_move = poss_moves[i];
			}
		}
		orig.makeMove(best_move);
	}

	int minimax(TTTgame g, int depth, boolean is_ai) {
		if(depth == 0 || g.status() != 0)
			return g.status();
		int[] poss_moves = g.openSpaces();
		if(is_ai) {
			int best_score = Integer.MIN_VALUE;
			for(int poss_idx = 0; poss_idx < poss_moves.length; poss_idx++) {
				TTTgame tmp_game = new TTTgame(g);
				tmp_game.makeMove(poss_moves[poss_idx]);
				int score = minimax(tmp_game, depth-1, false);
				if(score > best_score)
					best_score = score;
			}
			return best_score;
		}
		else {
			int best_score = Integer.MAX_VALUE;
			for(int poss_idx = 0; poss_idx < poss_moves.length; poss_idx++) {
				TTTgame tmp_game = new TTTgame(g);
				tmp_game.makeMove(poss_moves[poss_idx]);
				int score = minimax(tmp_game, depth-1, true);
				if(score < best_score)
					best_score = score;
			}
			return best_score;
		}
	}

	void humanRandom(TTTgame g) {
		int pos = r.nextInt(9);
		while(g.makeMove(pos) == false)
			pos = r.nextInt(9);
	}

	int humanMove(TTTgame g) {
		int pos = -1;
		boolean move_accepted = false;
		while(!move_accepted) {
			println("Available moves: " + intArrCommaStr(g.openSpaces()));
			print("Your move: ");
			char input = sc.next().charAt(0);
			if(Character.isDigit(input))					//CHECK that char(0) is a digit
				move_accepted = g.makeMove(Character.digit(input, 10));
			sc.reset();
		}
		return pos;
	}

	void results(TTTgame g) {
		int winner = g.status();

		score[winner]++;
		if(winner == 0 || show_all) {
			println("\n\n\n\n\n");
			g.showAllMoves();
			println((winner == 0) ? "TIE game" : (winner == -1) ? "HUMAN WINS" : "AI WINS");
			println("history: " + intArrCommaStr(g.getHistory()));
//			println("open: " + intArrCommaStr(g.openSpaces()));
		}
	}

	<T>String intArrCommaStr(int[] arr) {
		return IntStream.of(arr).mapToObj(String::valueOf).collect(Collectors.joining(","));
	}

	@SuppressWarnings("unchecked")
	<T> void println(T... line) { System.out.println((line.length == 0) ? "" : line[0]); }
	<T> void print(T data) { System.out.print(data); }
}
