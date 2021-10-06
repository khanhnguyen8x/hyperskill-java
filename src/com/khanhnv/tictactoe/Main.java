package com.khanhnv.tictactoe;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //var game = new TicTacToe();
        //var game = new TicTacToeV2();
        //var game = new TicTacToeV3();
        //var game = new TicTacToeV4();
        var game = new TicTacToeV5();
        game.startGame();

        /*
        game.setMatrix("XOXOOX___");
        game.display();
        int value = game.evaluate(game.board, 'O');
        System.out.printf("The value of this board is %d\n", value);

        var bestMove = game.findBestMove(game.board, 'X');
        System.out.printf("The Optimal Move is :\n");
        System.out.printf("ROW: %d COL: %d\n\n",
                bestMove.x, bestMove.y);
         */
    }
}