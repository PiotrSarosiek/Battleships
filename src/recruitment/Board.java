package recruitment;

import java.util.*;

public class Board {
    static int BOARD_SIZE = 10;
    static char WATER = '~';
    static char HIT = 'x';
    static char MISS = 'o';
    char[][] board;
    List<Ship> ships;

    public Board(List<Ship> ships) {
        initBoard();
        this.ships = ships;
        initShips();
    }

    private void initBoard(){
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for(char[] row : board){
            Arrays.fill(row, WATER);
        }
        this.board = board;
    }

    private void initShips(){
        ships.forEach(this::placeShip);
    }

    public void printBoard(){
        System.out.print("   A  B  C  D  E  F  G  H  I  J\n".toCharArray());
        for(int i=0;i<BOARD_SIZE;i++){
            System.out.print(i);
            for(int j=0;j<BOARD_SIZE;j++){
                System.out.print("  " + board[i][j]);
            }
            System.out.print("\n");
        }
    }

    public boolean coordinatesValid(String coordinates){
        if(coordinates.length() > 2)
            return false;
        int x = coordinates.charAt(0);
        int y = coordinates.charAt(1);
        return x >= 65 && x <= 74 && y >= 48 && y <= 57;
    }

    public void checkHit(String coordinates){
        Coords coords = resolveCoordinate(coordinates);
        boolean hit = false;
        for (Ship ship : ships) {
            if(ship.isDestroyed())
                continue;
            if(ship.tryDestroySegment(coords)){
                hit = true;
                board[coords.getX()][coords.getY()] = HIT;
                if(ship.isDestroyed())
                    markFieldsAroundDestroyedShip(ship);
                break;
            }
        }
        if(!hit && board[coords.getX()][coords.getY()] != HIT) {
            board[coords.getX()][coords.getY()] = MISS;
        }
    }

    private void placeShip(Ship ship){
        boolean isPlaced = false;
        Random random = new Random();
        int randomX;
        int randomY;
        int randomDirection;

        while (!isPlaced){
            List<Coords> coords = new ArrayList<>();
            randomX = random.nextInt(BOARD_SIZE-1);
            randomY = random.nextInt(BOARD_SIZE-1);

            if(placeIsValid(randomX, randomY)){
                coords.add(new Coords(randomX, randomY));
                randomDirection = random.nextInt(3);

                while (coords.size()!=ship.getSize()){
                    boolean badShipCoordinates = false;
                    switch (randomDirection){
                        case 0:
                            if(!placeIsValid(++randomX, randomY))
                                badShipCoordinates = true;
                            break;
                        case 1:
                            if(!placeIsValid(randomX, ++randomY))
                                badShipCoordinates = true;
                            break;
                        case 2:
                            if(!placeIsValid(--randomX, randomY))
                                badShipCoordinates = true;
                            break;
                        case 3:
                            if(!placeIsValid(randomX, --randomY))
                                badShipCoordinates = true;
                            break;
                    }
                    if(badShipCoordinates)
                        break;
                    coords.add(new Coords(randomX, randomY));
                }
                if(coords.size()==ship.getSize()){
                    ship.setCoordinates(coords);
                    isPlaced = true;
                }
            }
        }
    }

    private boolean placeIsValid(int x, int y){
        if(x<0 || x>=BOARD_SIZE || y< 0|| y>=BOARD_SIZE)
            return false;

        int xStart = x == 0 ? x : x-1;
        int xStop = x == BOARD_SIZE-1 ? x : x+1;
        int yStart = y == 0 ? y : y-1;
        int yStop = y == BOARD_SIZE-1 ? y : y+1;

        for(int i=xStart;i<=xStop;i++)
            for(int j=yStart;j<=yStop;j++){
                if(isShip(i,j))
                    return false;
            }
        return true;
    }

    private Coords resolveCoordinate(String coordinates){
        int asciiValue = (coordinates.charAt(0)-17);
        int numericValue = Character.getNumericValue(asciiValue);
        return new Coords(Integer.parseInt(String.valueOf(coordinates.charAt(1))),numericValue);
    }

    private boolean isShip(int x, int y){
        return ships.stream().filter(ship -> ship.getCoordinates() != null)
                .anyMatch(ship -> ship.getCoordinates().stream().anyMatch(coords -> coords.getX() == x && coords.getY() == y));
    }

    private void markFieldsAroundDestroyedShip(Ship ship){
        ship.getCoordinates().forEach(coords -> {
            for(int i=coords.getX()-1;i<=coords.getX()+1;i++)
                for(int j=coords.getY()-1;j<=coords.getY()+1;j++){
                    if(!(i<0 || i>=BOARD_SIZE || j< 0 || j>=BOARD_SIZE) && board[i][j] != HIT)
                        board[i][j]=MISS;
                }
        });
    }

    public boolean allShipsDestroyed(){
        return ships.stream().allMatch(Ship::isDestroyed);
    }
}
