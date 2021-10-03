package com.khanhnv.tictactoe;

import java.util.Random;
import java.util.Scanner;

public class TicTacToeV2 {

    char PLAYER_X = 'X';
    char PLAYER_O = 'O';
    char[][] matrix;

    public void aiPlay() {
        System.out.println("Making move level \"easy\"");
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

    public void userPlay() {
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

    public void play() {
        initMatrix();
        display();
        var running = true;
        while (running) {
            userPlay();
            running = checkState();
            if (!running) {
                break;
            }
            aiPlay();
            running = checkState();
        }
    }

    public boolean move(int x, int y, boolean isAI) {
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

    public boolean checkState() {
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
//          System.out.println("TicTacToe not finished");
                    return true;
                }
            }
        }

        return false;
    }

    public void initMatrix() {
        matrix = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.matrix[i][j] = ' ';
            }
        }
    }

    public void setMatrix(String cells) {
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

    public void display() {
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
