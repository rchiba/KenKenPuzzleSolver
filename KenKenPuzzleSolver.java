import java.io.*;
class KenKenPuzzleSolver 
{

    public class Square{
        int[] cage;
        int target;
        String operator;
        int value;

        public Square(int target, String operator, int[] cage){
            target = target;
            operator = operator;
            cage = cage;
            value = 0;
        }
    }

    public Square[] board; // array of squares
    public int size; // size of board

    public static void main(String args[])
    {
            KenKenPuzzleSolver solver = new KenKenPuzzleSolver();
            solver.start(args);
    }

    public void start(String args[]){
        try{
            // load board
            String inputFile = args[0];
            String outputFile = args[0];
            FileInputStream fstream = new FileInputStream(inputFile);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;
            board = new Square[1000];
            while ((line = br.readLine()) != null)   {
                // store each square
                System.out.println (line);
                String delims = "[ ]+";
                String[] tokens = line.split(delims);
                int target = Integer.parseInt(tokens[0]);
                String operator = tokens[1];
                int[] cage = new int[tokens.length-2];
                for(int i = 0; i < tokens.length-2; i++){
                    cage[i] = Integer.parseInt(tokens[i+2]);
                }
                Square sq = new Square(target,operator,cage);
                board[index] = sq;
                index++;
            }
            size = Math.sqrt(index);
            in.close();

            // start recursing


        } catch (Exception e){//Catch exception if any
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void printBoard(){
        for(int i = 0; i < board.length; i++){
            System.out.println(board[i].value);
        }
    }

    public boolean assignmentIsValid(int index, int value){
        // row check

        // col check

        // operator check
    }

    public void solve(Square[] board, int index) throws Exception{
        if(index === size^2){
            printBoard(board);
            return;
        }

        for(int i = 1; i < 10; i++){
            if(assignmentIsValid(index,i)){
                board[index].value = i;
                solve(board, ++i);
            } else {
                // backtrack
                // do not do anything, but return
                return;
            }
        }
    }
}