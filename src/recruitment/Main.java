package recruitment;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

	    Board board = new Board(Arrays.asList(new Ship(5), new Ship(4), new Ship(4)));
        Scanner scanner = new Scanner(System.in);

        while (!board.allShipsDestroyed()){
            board.printBoard();
            System.out.print("Enter the position: ");
            String coordinates = scanner.nextLine();
            if(!board.coordinatesValid(coordinates))
                System.out.println("Invalid position");
            else
                board.checkHit(coordinates);
        }
        board.printBoard();
        System.out.println("You won!");
    }
}
