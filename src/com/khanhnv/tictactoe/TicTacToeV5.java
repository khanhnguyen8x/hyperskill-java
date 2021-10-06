package com.khanhnv.tictactoe;

import java.awt.*;
import java.util.List;
import java.util.*;

class TicTacToeV5 {

    char PLAYER_X = 'X';
    char PLAYER_O = 'O';
    char[][] board;
    String command;
    String player1;
    String player2;

    public TicTacToeV5() {
        initMatrix();
    }

    private boolean isEmpty(String value) {
        return null == value || value.length() == 0;
    }

    private boolean isUserOrAI(String player) {
        return isUser(player) || isAI(player);
    }

    private boolean isUser(String player) {
        return player.equalsIgnoreCase("user");
    }

    private boolean isAI(String player) {
        return isEasyAI(player) || isMediumAI(player) || isHardAI(player);
    }

    private boolean isEasyAI(String player) {
        return player.equalsIgnoreCase("easy");
    }

    private boolean isHardAI(String player) {
        return player.equalsIgnoreCase("hard");
    }

    private boolean isMediumAI(String player) {
        return player.equalsIgnoreCase("medium");
    }

    private boolean isStartGame() {
        return command.equalsIgnoreCase("start");
    }

    private void player1Move() {
        if (player1.equalsIgnoreCase("user")) {
            userMove();
        } else {
            aiMove(player1);
        }
    }

    private void player2Move() {
        if (player2.equalsIgnoreCase("user")) {
            userMove();
        } else {
            aiMove(player2);
        }
    }

    public void startGame() {
        var scanner = new Scanner(System.in);
        boolean reRun = true;
        do {
            try {
                System.out.print("Input command:");
                String line = scanner.nextLine().trim();
                String[] commands = line.split(" ");
                if (isEmpty(line)) {
                    System.out.println("Bad parameters!");
                    reRun = true;
                    continue;
                }
                if (commands[0].equalsIgnoreCase("exit")) {
                    return;
                }
                if (commands.length != 3) {
                    System.out.println("Bad parameters!");
                    reRun = true;
                    continue;
                }
                String command = commands[0];
                String player1 = commands[1];
                String player2 = commands[2];
                if (isEmpty(command) || isEmpty(player1) || isEmpty(player2) || !isUserOrAI(player1)
                        || !isUserOrAI(player2)) {
                    System.out.println("Bad parameters!");
                    reRun = true;
                } else {
                    this.player1 = player1;
                    this.player2 = player2;
                    this.command = command;
                    if (!isStartGame()) {
                        reRun = false;
                    } else {
                        play();
                        System.out.println("");
                    }
                }
            } catch (Exception e) {
                System.out.println("Bad parameters!");
                reRun = true;
            }
        } while (reRun);
    }

    private void aiMove(String level) {
        System.out.println("Making move level \"" + level + "\"");
        switch (level) {
            case "easy":
                aiMoveEasy();
                break;
            case "medium":
                aiMoveMedium();
                break;
            case "hard":
                aiMoveHard();
                break;
        }
    }

    private void aiMoveEasy() {
        var random = new Random();
        boolean running;
        int i, j;
        do {
            i = random.nextInt(3) + 1;
            j = random.nextInt(3) + 1;
            running = move(i, j, true);
        } while (running);

        display();
    }

    private void aiMoveMedium() {
        Set<Point> coors = getCoors();
        if (!coors.isEmpty()) {
            List<Point> coorsList = new ArrayList<>(coors);
            Random random = new Random();
            int position = random.nextInt(coors.size());
            int x = coorsList.get(position).x;
            int y = coorsList.get(position).y;

            int countX = 0;
            int countO = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (this.board[i][j] == PLAYER_X) {
                        countX++;
                    } else if (this.board[i][j] == PLAYER_O) {
                        countO++;
                    }
                }
            }
            if (countX == countO) {
                this.board[x][y] = PLAYER_X;
            } else if (countX > countO) {
                this.board[x][y] = PLAYER_O;
            }

            display();
        } else {
            aiMoveEasy();
        }
    }

    private void aiMoveHard() {
        int countX = 0;
        int countO = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.board[i][j] == PLAYER_X) {
                    countX++;
                } else if (this.board[i][j] == PLAYER_O) {
                    countO++;
                }
            }
        }
        if (countX == countO) {
            Point point = findBestMove(board, PLAYER_X);
            this.board[point.x][point.y] = PLAYER_X;
        } else if (countX > countO) {
            Point point = findBestMove(board, PLAYER_O);
            this.board[point.x][point.y] = PLAYER_O;
        }
        display();
    }


    private Set<Point> getCoors() {
        Set<Point> coors = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            if (this.board[i][0] == ' ' && this.board[i][1] == 'X' && this.board[i][2] == 'X') {
                coors.add(new Point(i, 0));
            } else if (this.board[i][0] == 'X' && this.board[i][1] == 'X' && this.board[i][2] == ' ') {
                coors.add(new Point(i, 2));
            } else if (this.board[i][0] == ' ' && this.board[i][1] == 'O' && this.board[i][2] == 'O') {
                coors.add(new Point(i, 0));
            } else if (this.board[i][0] == 'O' && this.board[i][1] == 'O' && this.board[i][2] == ' ') {
                coors.add(new Point(i, 2));
            } else if (this.board[0][i] == ' ' && this.board[1][i] == 'X' && this.board[2][i] == 'X') {
                coors.add(new Point(0, i));
            } else if (this.board[0][i] == 'X' && this.board[1][i] == 'X' && this.board[2][i] == ' ') {
                coors.add(new Point(2, i));
            } else if (this.board[0][i] == ' ' && this.board[1][i] == 'O' && this.board[2][i] == 'O') {
                coors.add(new Point(0, i));
            } else if (this.board[0][i] == 'O' && this.board[1][i] == 'O' && this.board[2][i] == ' ') {
                coors.add(new Point(2, i));
            }

            if (this.board[0][0] == ' ' && this.board[1][1] == 'X' && this.board[2][2] == 'X') {
                coors.add(new Point(0, 0));
            } else if (this.board[0][0] == 'X' && this.board[1][1] == 'X' && this.board[2][2] == ' ') {
                coors.add(new Point(2, 2));
            } else if (this.board[0][0] == ' ' && this.board[1][1] == 'O' && this.board[2][2] == 'O') {
                coors.add(new Point(0, 0));
            } else if (this.board[0][0] == 'O' && this.board[1][1] == 'O' && this.board[2][2] == ' ') {
                coors.add(new Point(2, 2));
            } else if (this.board[2][0] == ' ' && this.board[1][1] == 'X' && this.board[0][2] == 'X') {
                coors.add(new Point(2, 0));
            } else if (this.board[2][0] == 'X' && this.board[1][1] == 'X' && this.board[0][2] == ' ') {
                coors.add(new Point(0, 2));
            } else if (this.board[2][0] == ' ' && this.board[1][1] == 'O' && this.board[0][2] == 'O') {
                coors.add(new Point(2, 0));
            } else if (this.board[2][0] == 'O' && this.board[1][1] == 'O' && this.board[0][2] == ' ') {
                coors.add(new Point(0, 2));
            }
        }

        return coors;
    }

    private void userMove() {
        var scanner = new Scanner(System.in);
        boolean reRun;
        System.out.print("Enter the coordinates:");
        do {
            try {
                reRun = move(scanner.nextInt(), scanner.nextInt(), false);
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
                System.out.print("Enter the coordinates:");
                reRun = true;
                scanner.next();
            }
        } while (reRun);
        display();
    }

    private void play() {
        if (!isStartGame()) {
            return;
        }
        initMatrix();
        display();
        var running = true;
        while (running) {
            player1Move();
            running = checkState();
            if (!running) {
                break;
            }
            player2Move();
            running = checkState();
        }
    }

    private boolean move(int x, int y, boolean isAI) {
        if (x >= 1 && x <= 3 && y >= 1 && y <= 3) {
            if (this.board[x - 1][y - 1] == ' ') {
                int countX = 0;
                int countO = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (this.board[i][j] == PLAYER_X) {
                            countX++;
                        } else if (this.board[i][j] == PLAYER_O) {
                            countO++;
                        }
                    }
                }
                if (countX == countO) {
                    this.board[x - 1][y - 1] = PLAYER_X;
                } else if (countX > countO) {
                    this.board[x - 1][y - 1] = PLAYER_O;
                }
                return false;
            } else {
                if (!isAI) {
                    System.out.println("This cell is occupied! Choose another one!");
                }
                return true;
            }
        } else {
            System.out.println("Coordinates should be from 1 to 3!");
            return true;
        }
    }

    private boolean checkState() {
        boolean isComplete = true;
        boolean isWin = false;
        for (int i = 0; i < 3; i++) {
            if (this.board[i][0] == 'X' && this.board[i][1] == 'X' && this.board[i][2] == 'X') {
                System.out.println("X wins");
                isWin = true;
                break;
            } else if (this.board[i][0] == 'O' && this.board[i][1] == 'O' && this.board[i][2] == 'O') {
                System.out.println("O wins");
                isWin = true;
                break;
            } else if (this.board[0][i] == 'X' && this.board[1][i] == 'X' && this.board[2][i] == 'X') {
                System.out.println("X wins");
                isWin = true;
                break;
            } else if (this.board[0][i] == 'O' && this.board[1][i] == 'O' && this.board[2][i] == 'O') {
                System.out.println("O wins");
                isWin = true;
                break;
            }
        }
        if (!isWin) {
            if (this.board[0][0] == 'X' && this.board[1][1] == 'X' && this.board[2][2] == 'X') {
                System.out.println("X wins");
                return false;
            } else if (this.board[0][0] == 'O' && this.board[1][1] == 'O' && this.board[2][2] == 'O') {
                System.out.println("O wins");
                return false;
            } else if (this.board[2][0] == 'X' && this.board[1][1] == 'X' && this.board[0][2] == 'X') {
                System.out.println("X wins");
                return false;
            } else if (this.board[2][0] == 'O' && this.board[1][1] == 'O' && this.board[0][2] == 'O') {
                System.out.println("O wins");
                return false;
            } else {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (this.board[k][l] == ' ') {
                            isComplete = false;
                            break;
                        }
                    }
                }
                if (isComplete) {
                    System.out.println("Draw");
                    return false;
                } else {
                    //System.out.println("TicTacToe not finished");
                    return true;
                }
            }
        }

        return false;
    }

    private void initMatrix() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = ' ';
            }
        }
    }

    public void setMatrix(String cells) {
        board = new char[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells.charAt(index) == '_') {
                    this.board[i][j] = ' ';
                } else {
                    this.board[i][j] = cells.charAt(index);
                }
                index++;
            }
        }
    }

    void display() {
        System.out.println("---------");
        for (int i = 0; i < board.length; i++) {
            var builder = new StringBuilder();
            builder.append("| ");
            for (int j = 0; j < board[i].length; j++) {
                builder.append(board[i][j]).append(" ");
            }
            builder.append("|");
            System.out.println(builder.toString());
        }
        System.out.println("---------");
    }

    private List<Point> getAvailablePosition() {
        List<Point> emptyPosition = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == ' ') {
                    emptyPosition.add(new Point(i, j));
                }
            }
        }
        return emptyPosition;
    }

    private void minimax(List<Point> points, String player) {
        var availablePosition = getAvailablePosition();


    }

    public boolean isWinning(char player) {
        boolean isWin = false;
        for (int i = 0; i < 3; i++) {
            if ((this.board[i][0] == player && this.board[i][1] == player && this.board[i][2] == player)
                    || (this.board[0][i] == player && this.board[1][i] == player && this.board[2][i] == player)) {
                isWin = true;
                break;
            }
        }
        if (!isWin) {
            if ((this.board[0][0] == player && this.board[1][1] == player && this.board[2][2] == player)
                    || (this.board[2][0] == player && this.board[1][1] == player && this.board[0][2] == player)) {
                isWin = true;
            }
        }
        return isWin;
    }

    // This function returns true if there are moves
    // remaining on the board. It returns false if
    // there are no moves left to play.
    Boolean isMovesLeft(char board[][]) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ')
                    return true;
        return false;
    }

    int evaluate(char board[][], char player) {
        char opponent = player == PLAYER_X ? PLAYER_O : PLAYER_X;

        // Checking for Rows for X or O victory.
        for (var row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == player) {
                    return +10;
                } else if (board[row][0] == opponent) {
                    return -10;
                }
            }
        }

        // Checking for Columns for X or O victory.
        for (var col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == player) {
                    return +10;
                } else if (board[0][col] == opponent) {
                    return -10;
                }
            }
        }

        // Checking for Diagonals for X or O victory.
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == player)
                return +10;
            else if (board[0][0] == opponent)
                return -10;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == player)
                return +10;
            else if (board[0][2] == opponent)
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }

    int minimax(char board[][], int depth, boolean isMax, char player) {
        char opponent = player == PLAYER_X ? PLAYER_O : PLAYER_X;
        int score = evaluate(board, player);

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10) {
            return score;
        }

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == -10) {
            return score;
        }

        // If there are no more moves and
        // no winner then it is a tie
        if (!isMovesLeft(board))
            return 0;

        // If this maximizer's move
        if (isMax) {
            int best = -1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j] == ' ') {
                        // Make the move
                        board[i][j] = player;
                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board, depth + 1, !isMax, player));
                        // Undo the move
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            // If this minimizer's move
            int best = 1000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j] == ' ') {
                        // Make the move
                        board[i][j] = opponent;
                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.min(best, minimax(board, depth + 1, !isMax, player));
                        // Undo the move
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    // This will return the best possible
    // move for the player
    Point findBestMove(char board[][], char player) {
        boolean isMax = false;
        int bestVal = -1000;
        Point bestMove = new Point();
        bestMove.x = -1;
        bestMove.y = -1;

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 3; j++) {
                // Check if cell is empty
                if (board[i][j] == ' ') {
                    // Make the move
                    board[i][j] = player;

                    // compute evaluation function for this
                    // move.
                    int moveVal = minimax(board, 0, isMax, player);

                    board[i][j] = ' ';

                    if (moveVal > bestVal) {
                        bestMove.x = i;
                        bestMove.y = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        System.out.printf("The value of the best Move " +
                "is : %d\n\n", bestVal);

        return bestMove;
    }
}
