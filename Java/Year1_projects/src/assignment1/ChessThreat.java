package assignment1;

public class ChessThreat {
    public static void main(String[] args) {
        System.out.println(CheckThreats(1, 0, 0, 2, 2));
        System.out.println(CheckThreats(2, 0, 0, 2, 2));
        System.out.println(CheckThreats(3, 0, 0, 1, 2));
    }

    // a boolean function that checks weather the wanted game piece is threatening on the given location
    // if the chosen type is incorrect - the function returns false
    public static boolean CheckThreats(int type, int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        if (type == 1) {
            return CheckBishop(x1, y1, x2, y2);
        }
        if (type == 2) {
            return CheckRook(x1, y1, x2, y2);
        }
        if (type == 3) {
            return CheckKnight(x1, y1, x2, y2);
        }
        return false;
    }

    // a boolean function that checks if the Bishop is threatening on the wanted location
    public static boolean CheckBishop(int x1, int y1, int x2, int y2) {
        return (x2 - x1 == y2 - y1) || (x2 - x1 == -1 * (y2 - y1));
    }

    // a boolean function that checks if the Rook is threatening on the wanted location
    public static boolean CheckRook(int x1, int y1, int x2, int y2) {
        return (x1 == x2 || y1 == y2);
    }

    // a boolean function that checks if the Knight is threatening on the wanted location
    public static boolean CheckKnight(int x1, int y1, int x2, int y2) {
        if ((x2 - x1 == 2 || x2 - x1 == -2) && (y2 - y1 == 1 || y2 - y1 == -1)) {
            return true;
        }
        if ((x2 - x1 == 1 || x2 - x1 == -1) && (y2 - y1 == 2 || y2 - y1 == -2)) {
            return true;
        }
        return false;
    }
}