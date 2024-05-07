import java.util.Arrays;

//still not fully working??? 

public class TicTacToeGame {
    private String[][] board = new String[3][3];
    private String currentPlayer;
    private String player1, player2;
    private boolean playing = true;

    public TicTacToeGame(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        for (String[] row : board) {
            Arrays.fill(row, " ");
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isValidMove(String move) {
        try {
            int position = Integer.parseInt(move);
            int row = (position - 1) / 3;
            int col = (position - 1) % 3;
            return position >= 1 && position <= 9 && board[row][col].equals(" ");
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public String processMove(String player, String move) {
        if (!player.equals(currentPlayer)) {
            return null;
        }

        try {
            int position = Integer.parseInt(move);
            int row = (position - 1) / 3;
            int col = (position - 1) % 3;

            if (position < 1 || position > 9 || !board[row][col].equals(" ")) {
                return null;
            }

            board[row][col] = currentPlayer.equals(player1) ? "X" : "O";
            if (checkWin()) {
                playing = false;
                return "Game over! " + currentPlayer + " wins!\n" + getBoard();
            } else if (isBoardFull()) {
                playing = false;
                return "Game over! It's a draw!\n" + getBoard();
            } else {
                currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
                return getBoard();
            }
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public String getBoard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                builder.append(" ").append(board[i][j]);
                if (j < 2) {
                    builder.append(" |");
                }
            }
            builder.append("\n");
            if (i < 2) {
                builder.append("---+---+---\n");
            }
        }
        return builder.toString();
    }

    private boolean checkWin() {
        //checks rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals(" ")) {
                return true;
            }
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && !board[0][i].equals(" ")) {
                return true;
            }
        }

        //checks diagonals
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && !board[0][0].equals(" ")) {
            return true;
        }
        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && !board[0][2].equals(" ")) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col].equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }
}