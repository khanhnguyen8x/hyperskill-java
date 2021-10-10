package com.khanhnv.tictactoe;

import java.util.Scanner;

public class TicTacToe {

    char PLAYER_X = 'X';
    char PLAYER_O = 'O';
    char[][] matrix;

    public void play() {
        var scanner = new Scanner(System.in);
        System.out.print("Enter the cells:");
        String cells = scanner.nextLine();
        setMatrix(cells);
        display();
        System.out.print("Enter the coordinates:");
        boolean reRun;
        do {
            try {
                reRun = move(scanner.nextInt(), scanner.nextInt());
            } catch (Exception e) {
                System.out.println("You should enter numbers!");
                System.out.print("Enter the coordinates:");
                reRun = true;
                scanner.next();
            }
        } while (reRun);

        display();
        checkState();
    }

    public boolean move(int x, int y) {
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
                System.out.println("This cell is occupied! Choose another one!");
                return true;
            }
        } else {
            System.out.println("Coordinates should be from 1 to 3!");
            return true;
        }
    }

    public void checkState() {
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
            } else if (this.matrix[0][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[2][2] == 'O') {
                System.out.println("O wins");
            } else if (this.matrix[2][0] == 'X' && this.matrix[1][1] == 'X' && this.matrix[0][2] == 'X') {
                System.out.println("X wins");
            } else if (this.matrix[2][0] == 'O' && this.matrix[1][1] == 'O' && this.matrix[0][2] == 'O') {
                System.out.println("O wins");
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
                } else {
                    System.out.println("Game not finished");
                }
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