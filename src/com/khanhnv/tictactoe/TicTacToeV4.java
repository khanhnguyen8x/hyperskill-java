package com.khanhnv.tictactoe;

import java.awt.*;
import java.util.*;
import java.util.List;

class TicTacToeV4 {

    char PLAYER_X = 'X';
    char PLAYER_O = 'O';
    char[][] matrix;
    String command;
    String player1;
    String player2;

    public TicTacToeV4() {
        initMatrix();
    }

    private boolean isEmpty(String value) {
        return null == value || value.length() == 0;
    }

    private boolean isUserOrAI(String player) {
        return player.equalsIgnoreCase("user") || player.equalsIgnoreCase("easy")
                || player.equalsIgnoreCase("medium");
    }

    private boolean isMedium(String player) {
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
                    if (this.matrix[i][j] == PLAYER_X) {
                        countX++;
                    } else if (this.matrix[i][j] == PLAYER_O) {
                        countO++;
                    }
                }
            }
            if (countX == countO) {
                this.matrix[x][y] = PLAYER_X;
            } else if (countX > countO) {
                this.matrix[x][y] = PLAYER_O;
            }

            display();
        } else {
            aiMoveEasy();
        }
    }

    private Set<Point> getCoors() {
        Set<Point> coors = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            if (this.matrix[i][0] == ' ' && this.matrix[i][1] == 'X' && this.matrix[i][2] == 'X') {
                coors.add(new Point(i, 0));
            } else if (this.matrix[i][0] == 'X' && this.matrix[i][1] == 'X' && this.matrix[i][2] == ' ') {
                coors.add(new Point(i, 2));
            } else if (this.matrix[i][0] == ' ' && this.matrix[i][1] == 'O' && this.matrix[i][2] == 'O') {
                coors.add(new Point(i, 0));
            } else if (this.matrix[i][0] == 'O' && this.matrix[i][1] == 'O' && this.matrix[i][2] == ' ') {
                coors.add(new Point(i, 2));
            } else if (this.matrix[0][i] == ' ' && this.matrix[1][i] == 'X' && this.matrix[2][i] == 'X') {
                coors.add(new Point(0, i));
            } else if (this.matrix[0][i] == 'X' && this.matrix[1][i] == 'X' && this.matrix[2][i] == ' ') {
                coors.add(new Point(2, i));
            } else if (this.matrix[0][i] == ' ' && this.matrix[1][i] == 'O' && this.matrix[2][i] == 'O') {
                coors.add(new Point(0, i));
            } else if (this.matrix[0][i] == 'O' && this.matrix[1][i] == 'O' && this.matrix[2][i] == ' ') {
                coors.add(new Point(2, i));
            }

            if (this.matrix[0][0] == ' ' && this.matrix[1][1] == 'X' && this.matrix[2][2] == 'X') {
                coors.add(new Point(0, 0));
            } else if (this.matrix[0][0] == 'X' && this.matrix[1][1] == 'X' && this.matrix[2][2] == ' ') {
                coors.add(new Point(2, 2));
            } else if (this.matrix[0][0] == ' ' && this.matrix[1][1] == 'O' && this.matrix[2][2] == 'O') {
                coors.add(new Point(0, 0));
            } else if (this.matrix[0][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[2][2] == ' ') {
                coors.add(new Point(2, 2));
            } else if (this.matrix[2][0] == ' ' && this.matrix[1][1] == 'X' && this.matrix[0][2] == 'X') {
                coors.add(new Point(2, 0));
            } else if (this.matrix[2][0] == 'X' && this.matrix[1][1] == 'X' && this.matrix[0][2] == ' ') {
                coors.add(new Point(0, 2));
            } else if (this.matrix[2][0] == ' ' && this.matrix[1][1] == 'O' && this.matrix[0][2] == 'O') {
                coors.add(new Point(2, 0));
            } else if (this.matrix[2][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[0][2] == ' ') {
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
            if (this.matrix[x - 1][y - 1] == ' ') {
                int countX = 0;
                int countO = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (this.matrix[i][j] == PLAYER_X) {
                            countX++;
                        } else if (this.matrix[i][j] == PLAYER_O) {
                            countO++;
                        }
                    }
                }
                if (countX == countO) {
                    this.matrix[x - 1][y - 1] = PLAYER_X;
                } else if (countX > countO) {
                    this.matrix[x - 1][y - 1] = PLAYER_O;
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
            if (this.matrix[i][0] == 'X' && this.matrix[i][1] == 'X' && this.matrix[i][2] == 'X') {
                System.out.println("X wins");
                isWin = true;
                break;
            } else if (this.matrix[i][0] == 'O' && this.matrix[i][1] == 'O' && this.matrix[i][2] == 'O') {
                System.out.println("O wins");
                isWin = true;
                break;
            } else if (this.matrix[0][i] == 'X' && this.matrix[1][i] == 'X' && this.matrix[2][i] == 'X') {
                System.out.println("X wins");
                isWin = true;
                break;
            } else if (this.matrix[0][i] == 'O' && this.matrix[1][i] == 'O' && this.matrix[2][i] == 'O') {
                System.out.println("O wins");
                isWin = true;
                break;
            }
        }
        if (!isWin) {
            if (this.matrix[0][0] == 'X' && this.matrix[1][1] == 'X' && this.matrix[2][2] == 'X') {
                System.out.println("X wins");
                return false;
            } else if (this.matrix[0][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[2][2] == 'O') {
                System.out.println("O wins");
                return false;
            } else if (this.matrix[2][0] == 'X' && this.matrix[1][1] == 'X' && this.matrix[0][2] == 'X') {
                System.out.println("X wins");
                return false;
            } else if (this.matrix[2][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[0][2] == 'O') {
                System.out.println("O wins");
                return false;
            } else {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (this.matrix[k][l] == ' ') {
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
        matrix = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matrix[i][j] = ' ';
            }
        }
    }

    private void setMatrix(String cells) {
        matrix = new char[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells.charAt(index) == '_') {
                    this.matrix[i][j] = ' ';
                } else {
                    this.matrix[i][j] = cells.charAt(index);
                }
                index++;
            }
        }
    }

    private void display() {
        System.out.println("---------");
        for (int i = 0; i < matrix.length; i++) {
            var builder = new StringBuilder();
            builder.append("| ");
            for (int j = 0; j < matrix[i].length; j++) {
                builder.append(matrix[i][j]).append(" ");
            }
            builder.append("|");
            System.out.println(builder.toString());
        }
        System.out.println("---------");
    }
}
