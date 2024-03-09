import java.util.Scanner;


class Ship {
    int length;
    int [][] coordinates;
}


public class Main {

    static int[] convertStringToCoordinate(String coordinate) {
        int column = 0;
        int row = 0;
        int[] tmp = new int[2];
        try {
            if (coordinate.length() == 3 && coordinate.charAt(1) == '1' && coordinate.charAt(2) == '0') {
                column = 10;
            } else if (coordinate.length() == 3) {
                column = coordinate.charAt(1) + 11 - 48;
            } else {
                column = coordinate.charAt(1) - 48;
            }
            row = coordinate.charAt(0) - 64;
            tmp[0] = row;
            tmp[1] = column;
        }catch (Exception e) {
        }
        return tmp;
    }
    //---------------------interaction with grid-------------------------------
    static String[][] createGrid() {
        String[][] tmp = new String[12][12];
        char row = 64; //64 is '@' in ASCII, we start with this, bcs we skip first row and incrementing this value every row
        for (int i = 0; i < tmp.length; i++) {
            for(int j = 0; j < tmp[0].length; j++) {
                if(i == 0) {
                    if(j == 0) {
                        tmp[i][j] = " ";
                    }else {
                        tmp[i][j] = Integer.toString(j);
                    }
                }else {
                    if(j == 0) {
                        tmp[i][j] = String.valueOf(row);
                    }else {
                        tmp[i][j] = "~";
                    }
                }
            }
            row++;
        }
        return tmp;
    }

    static boolean updateGrid(String[][] grid, String start, String end, int length, Ship ship) {
        try {
            ship.length = length;
            int[][] tmp = new int[length][2];
            int startColumn = 0;
            int startRow = start.charAt(0) - 64;
            int endColumn = 0;
            int endRow = end.charAt(0) - 64;
            if (start.length() == 3 && start.charAt(1) == '1' && start.charAt(2) == '0') {
                startColumn = 10;
            } else {
                startColumn = start.charAt(1) - 48;
            }
            if(end.length() == 3 && end.charAt(1) == '1' && end.charAt(2) == '0') {
                endColumn = 10;
            }else {
                endColumn = end.charAt(1) - 48;
            }
            if (checkInput(grid,start, end, length)) {
                if (start.charAt(0) == end.charAt(0)) {//row is the same
                    if(startColumn < endColumn) {
                        for (int i = 0; i < length; i++) {
                            grid[startRow][startColumn] = "o";
                            tmp[i][0] = startRow;
                            tmp[i][1] = startColumn;
                            startColumn++;
                        }
                    }else {
                        for (int i = 0; i < length; i++) {
                            grid[startRow][endColumn] = "o";
                            tmp[i][0] = startRow;
                            tmp[i][1] = startColumn;
                            endColumn++;
                        }
                    }
                    ship.coordinates = tmp;
                    return true;
                } else if (start.charAt(1) == end.charAt(1)) { //column is the same;
                    if(startRow < endRow) {
                        for (int i = 0; i < length; i++) {
                            grid[startRow][startColumn] = "o";
                            tmp[i][0] = startRow;
                            tmp[i][1] = startColumn;
                            startRow++;
                        }
                    }else {
                        for (int i = 0; i < length; i++) {
                            grid[endRow][startColumn] = "o";
                            tmp[i][0] = startRow;
                            tmp[i][1] = startColumn;
                            endRow++;
                        }
                    }
                }
                ship.coordinates = tmp;
                return true;
            }
        }catch (Exception e) {
            System.out.println("Error!");
            return false;
        }
        return false;
    }

    static void placeShip(String[][] grid, int length, String name, Ship ship) {
        System.out.println();
        System.out.println("Enter the coordinates of the " + name + " ("+ length + " cells)");
        Scanner scanner = new Scanner(System.in);
        boolean good = false;
        while(!good) {
            System.out.println();
            String start = scanner.next();
            String end = scanner.next();
            good = updateGrid(grid,start,end,length,ship);
        }
        System.out.println();
        show(grid);
    }
    //---------------------interaction with grid-------------------------------



    //--------------------check input errors----------------------------------------
    static boolean checkInput(String[][] grid,String start, String end, int length) {
        int startRow;
        int endRow;
        int cLength = 0;
        int startColumn;
        int endColumn;
        int[] startCoordinate = convertStringToCoordinate(start);
        int[] endCoordinate = convertStringToCoordinate(end);
        startRow = startCoordinate[0];
        startColumn = startCoordinate[1];
        endRow = endCoordinate[0];
        endColumn = endCoordinate[1];
        if(startColumn == endColumn) {
            cLength = Math.abs(startRow - endRow);
            cLength++;
        }else if(startRow == endRow) {
            cLength = Math.abs(startColumn - endColumn);
            cLength++;
        }
        //incorrect parameters
        if(startRow < 1  || startRow > 10 || endRow < 1 || endRow > 10) {
            System.out.println("\nError! Wrong row");
            return false;
        }else if(startColumn < 1 || startColumn > 10 || endColumn < 1 || endColumn > 10) {
            System.out.println("\nError! Wrong Column");
            return false;
        }
        //incorrect length
        if(cLength != length) {
            System.out.println("\nError! Wrong length of ship! Try again:");
            return false;
        }
        //check position (close to another ship or not)
        if(grid[startRow][startColumn + 1].equals("o") || grid[startRow][startColumn - 1].equals("o") || grid[startRow + 1][startColumn].equals("o") || grid[startRow -1][startColumn].equals("o")) {
            System.out.println("\nError! You placed it too close to another. Try again:");
            return false;
        }else if(grid[endRow][endColumn + 1].equals("o") || grid[endRow][endColumn - 1].equals("o") || grid[endRow + 1][endColumn].equals("o") || grid[endRow - 1][endColumn].equals("o")) {
            System.out.println("\nError! You placed it too close to another. Try again:");
            return false;
        }
        return true;
    }
    //--------------------check input errors----------------------------------------



    //--------------------------show-------------------------------------------------
    static void show (String[][] grid) {
        for(int i = 0; i < grid.length - 1;i++) {
            for(int j = 0; j < grid[0].length - 1; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    static void showWithFog (String[][] grid) {
        for(int i = 0; i < grid.length - 1;i++) {
            for(int j = 0; j < grid[0].length - 1; j++) {
                if(grid[i][j].equals("o")) {
                    System.out.print("~ ");
                }else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    //--------------------------show-------------------------------------------------

    static void shot(String[][] grid, Ship[] ships) {
        Scanner scanner = new Scanner(System.in);
        boolean good = false;
        System.out.println();
        System.out.println("Take a shot!");
        String shot= "";
        while(!good) {
            System.out.println();
            String coordinate = scanner.next();
            int[] tmp = convertStringToCoordinate(coordinate);
            if(tmp[0] < 1  || tmp[0] > 10 ) {
                System.out.println("\nError! You entered wrong coordinates! Try again:");
                continue;
            }else if(tmp[1] < 1 || tmp[1] > 10) {
                System.out.println("\nError! You entered wrong coordinates! Try again:");
                continue;
            }
            if(grid[tmp[0]][tmp[1]].equals("o")) {
                shot = "You hit a ship!";
                grid[tmp[0]][tmp[1]] = "X";
                good = true;
                //update ship coordinates
                for(Ship ship : ships) {
                    for(int i = 0; i < ship.coordinates.length; i++) {
                        if(ship.coordinates[i][0] == tmp[0] && ship.coordinates[i][1] == tmp[1]) {
                            ship.length -= 1;
                            if(ship.length == 0) {
                                shot = "You sank a ship! Specify a new target:";
                            }
                        }
                    }
                }
            }else if(grid[tmp[0]][tmp[1]].equals("X")){
                good = true;
                shot = "You hit a ship!";

            }else {
                shot = "You missed";
                grid[tmp[0]][tmp[1]] = "M";
                good = true;
            }
        }
        System.out.println();
        showWithFog(grid);
        System.out.println();
        System.out.println(shot + "\n");;
    }

    static boolean end(Ship [] ships) {
        int tmp = 0;
        for(Ship ship : ships) {
            tmp += ship.length;
        }
        return tmp == 0;
    }

    static void game(String[][] grid) {
        show(grid);
        Ship s1 = new Ship();
        Ship s2 = new Ship();
        Ship s3 = new Ship();
        Ship s4 = new Ship();
        Ship s5 = new Ship();
        Ship [] ships = {s1,s2,s3,s4,s5};
        placeShip(grid, 5, "Aircraft Carrier", s1 );
        placeShip(grid, 4, "Battleship", s2 );
        placeShip(grid, 3, "Submarine", s3 );
        placeShip(grid, 3, "Cruiser", s4 );
        placeShip(grid, 2, "Destroyer", s5 );
        System.out.println("\nThe game starts!\n");
        showWithFog(grid);
        while(!end(ships)) {
            shot(grid, ships);
            //showWithFog(grid);
        }
        System.out.println("\nYou sank the last ship. You won. Congratulations!");
    }

    public static void main(String[] args) {
        String[][] grid = createGrid();
        game(grid);

    }
}