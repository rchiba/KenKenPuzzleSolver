import java.io.*;

class Square{
    public int[] cage;
    public int target;
    public String operator;
    public int value;

    public Square(int target, String operator, int[] cage){
        this.target = target;
        this.operator = operator;
        this.cage = cage;
        this.value = 0; // all squares begin with 0
    }

    public String toString(){
        return "{'target':'"+this.target+"','operator':'"+this.operator+"','value':'"+this.value+"'}";
    }
}

class KenKenPuzzleSolver 
{
    public int size; // size of board

    public static void main(String args[])
    {
            KenKenPuzzleSolver solver = new KenKenPuzzleSolver();
            solver.start(args);
    }

    public void start(String args[]){
        try{
            if(args.length == 0){
                System.out.println("ex: KenKenPuzzleSolver inputFile outputFile");
                return;
            }
            // load board
            String inputFile = args[0];
            String outputFile = null;
            if( args.length > 1 ){
                outputFile = args[1];
            }
            if(outputFile != null){
                PrintStream out = new PrintStream(new FileOutputStream(outputFile));
                System.setOut(out);
            }

            //String outputFile = args[1];
            FileInputStream fstream = new FileInputStream(inputFile);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;
            Square[] board = new Square[1000];
            size = 0;
            while ((line = br.readLine()) != null)   {
                // store each square
                //System.out.println (line);
                String delims = "[ ]+";
                String[] tokens = line.split(delims);
                int target = Integer.parseInt(tokens[0]);     // target
                String operator = tokens[1];                  // operator
                int[] cage = new int[tokens.length-2];        // cage
                for(int i = 0; i < tokens.length-2; i++){
                    cage[i] = Integer.parseInt(tokens[i+2]);
                    size++;
                }

                // construct a square from each of the tokens after the second one
                for(int i = 0; i < cage.length; i++){
                    Square sq = new Square(target,operator,cage); // square
                    // assign the value to the square if operator is .
                    /*
                    if( operator.equals('.') ){
                        sq.value = target;
                    }
                    */
                    board[cage[i]] = sq;
                }

                index++;
            }
            size = (int)Math.sqrt(size);
            //System.out.println("Size: "+size);
            in.close();

            // start recursing
            solve(board, -1);


        } catch (Exception e){//Catch exception if any
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void printBoard(Square[] board){
        //System.out.println("PRINTING BOARD");
        for(int i = 0; i < Math.pow(size, 2); i++){
            if(i%size == 0 && i != 0){
                System.out.print("\n");
            }
            System.out.print(board[i].value);
        }
        System.out.print("\n\n");
    }

    public boolean assignmentIsValid(int index, int value, Square[] board){
    
        //System.out.println("assignmentIsValid index:"+index+" value:"+value+" operator: "+board[index].operator);

    	if(index == 11){
    		//System.out.println("RyoChiba");
    	}
    	
        // single square only has one valid possibility
        if( board[index].operator.equals('.') ){
            if( board[index].value == value ){
                return true;
            } else {
                return false;
            }
        }
    
        // row check
        int row = (int)Math.floor(index / size);
        for(int r = row*size; r < row*size + size; r++){
            if(board[r].value == value){
                return false;
            }
        }
        
        // col check
        int col = index % size;
        for(int c = 0; c < size; c++){
            if( board[c*size+col].value == value ){
                return false;
            }
        }

        // temporarily assign the value to the square
        // while we check the next few conditions
        board[index].value = value;

        
        if(board[index].operator.equals(".")){
        	//System.out.println("operator is period");
        	if(board[index].value == board[index].target){
        		return true;
        	} else {
        		return false;
        	}
        }
        
        // operator check
        if(board[index].operator.equals("*")){
            // multiplication is invalid if the value is
            // not a divisor of the target
            // or if the product of the cage is greater than the target
            if( board[index].target % board[index].value != 0 ){
                return false;
            }
            
            int product = board[board[index].cage[0]].value;
            for(int i = 1; i < board[index].cage.length; i++){
                product *= board[board[index].cage[i]].value;
            }
            if(product > board[index].target){
                return false;
            }
            
            // or if all of the cage is filled and the product is not equal to the target
            boolean isNotFilled = false;
            for(int i = 0; i < board[index].cage.length; i++){
                if( board[board[index].cage[i]].value == 0 ){
                	isNotFilled = true;
                }
            }
            if(!isNotFilled && product != board[index].target){
            	return false;
            }
            
            
        } else if(board[index].operator.equals("+")){
            // addition is invalid if the 
            // sum of the cage is greater than the target
            int sum = 0;
            for(int i = 0; i < board[index].cage.length; i++){
                sum += board[board[index].cage[i]].value;
            }

            if(sum > board[index].target){
                return false;
            }
            
            // or if all of the cage is filled and the sum is not equal to the target
            boolean isNotFilled = false;
            for(int i = 0; i < board[index].cage.length; i++){
                if( board[board[index].cage[i]].value == 0 ){
                	isNotFilled = true;
                }
            }
            if(!isNotFilled && sum != board[index].target){
            	return false;
            }
        
        } else if(board[index].operator.equals("/")){
            // division is invalid if 
            // both values in the cage are not zero
            // and the quotient of the cage does not equal the target
            // or if the value is not a divisor of the target
            int a = board[board[index].cage[0]].value;
            int b = board[board[index].cage[1]].value;

            if(board[index].target > value){
            	if(board[index].target % value != 0){
                    return false;
                }
            } else {
            	if( value % board[index].target != 0){
                    return false;
                }
            }

            int quotient = 0;
            if(a > b && a != 0 && b != 0){
                quotient = (int)a/b;
                if(quotient != board[index].target){
                    return false;
                }
            } else if(a < b && a != 0 && b != 0) {
                quotient = (int)b/a;
                if(quotient != board[index].target){
                    return false;
                }
            }

        
        } else if(board[index].operator.equals("-")){
            // subtraction is invalid if
            // both operators are not zero
            // and the difference of the cage does not equal the target
            int a = board[board[index].cage[0]].value;
            int b = board[board[index].cage[1]].value;

            if(a > b && a != 0 && b != 0){
                if(a - b != board[index].target){
                    return false;
                }
            } else if( a < b && a !=0 && b != 0){
                if(b - a != board[index].target){
                    return false;
                }
            }
            
            

        }

        return true;
    }

    public Square[] solve(Square[] board, int index) throws Exception{
        //System.out.println("solve called with "+index);
        //System.out.println(board[index]);
        //printBoard(board);

        if(index == Math.pow(size,2)-1){
            printBoard(board);
            return board;
        }
        
        // for the next space, try every possible value for this specific index
        index++;
        for(int i = 1; i < 10; i++){
            if(assignmentIsValid(index, i, board)){
                //System.out.println("Assignment Valid");
                board[index].value = i;
                solve(board, index);
            } else {
                //System.out.println("Assignment Invalid");
                // backtrack
                // do not do anything, but return
                board[index].value = 0;
                //return null;
            }
        }
        return null;
    }
}