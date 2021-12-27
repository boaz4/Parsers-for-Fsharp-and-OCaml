//grammar
//program       : stmt_list $$
//stmt_list     : stmt stmt_list | ε 
//stmt          : id := expr | read id | write expr
//expr          : term term_tail
//term_tail     : add_op term term_tail | ε 
//term          : factor factor_tail
//factor_tail   : mult_op factor factor_tail | ε 
//factor        : ( expr ) | id | number
//add_op        : + | -
//mult_op       : * | /

//Tokens
type Token =
    | ID of string
    | READ
    | ADD_OPERATION 
    | MULT_OPERATION 
    | OPEN_P 
    | CLOSING_P 
    | ASSIGN
    | WRITE
    | NUM

//function to get a token from a lexeme (String)
let tokenFromLexeme str =
    match str with
    | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" -> NUM 
    | "read" -> READ 
    | "write" -> WRITE
    | "+" | "-" -> ADD_OPERATION
    | "*" | "/" -> MULT_OPERATION
    | "(" -> OPEN_P
    | ")" -> CLOSING_P
    | ":=" -> ASSIGN
    | x -> ID x


let rec parse theList = program theList

//program       : stmt_list $$
and program lst =
    lst |> stmtL 

//statement_list     : stmt stmt_list | ε 
and stmtL lst = 
    match lst with 
    | ID x :: ASSIGN :: _ -> lst |> stmt
    | READ :: _ -> lst |> stmt
    | WRITE :: _ -> lst |> stmt
              // <empty> means the rule is empty (not the list), if there is no adjective, then…
    | xs -> xs // just resolve to what was passed (instead of failing)

//statement          : id := expr | read id | write expr
and stmt = function
    | ID x :: ASSIGN :: xs -> xs |> exp
    | READ :: xs -> xs |> id
    | WRITE :: xs -> xs |> exp
    | x :: xs -> failwithf "Expected ID, READ, or WRITE, but found: %A" x
    | [] -> failwith "ID should not be empty"
    
//expression          : term term_tail
and exp = function
    | term :: xs -> xs |> termTail
    | [] -> failwith "term should not be empty"

//Factor        : ( expr ) | id | number
and factor = function
    | OPEN_P :: xs -> xs |> exp |> CLOSE_P 
    | ID x :: xs -> xs
    | NUM :: xs -> xs
    | x :: xs -> failwithf "Expected an ID or Number but found: %A" x
    | [] -> failwith "unexpected end of input while processing an expression"

//term          : factor factor_tail
and term = function
    | factor :: xs -> xs |> factorTail
    | [] -> failwith "factor should not be empty"

//Factor Tail   : mult_op factor factor_tail | ε 
and factorTail = function 
    | multOP :: xs -> xs |> factor |> factorTail
    | xs -> xs 
    //| x :: xs -> failwithf "Expected multOP but found: %A" x

//Term Tail     : add_op term term_tail | ε 
and termTail = function
    | addOP :: xs -> xs |> term |> termTail
    | xs -> xs

//CP :: )
and CLOSE_P = function
    | CLOSING_P :: xs -> xs
    | x -> failwithf "Expected CLOSE_P, but found: %A" x

//Addition operations       : + | -
and addOP = function       
    | ADD_OPERATION :: xs -> xs
    | x :: xs -> failwithf "Expected add operator but found: %A" x
    | [] -> failwith "add operator should not be empty"

//Multiplication Operation       : * | /
and multOP = function 
    | MULT_OPERATION :: xs -> xs
    | x :: xs -> failwithf "Expected multiplacatioon operator but found: %A" x
    | [] -> failwith "multiplication operator should not be empty"


open System

(* Begin Parsing Process *)
let startParsing (str:string) =
    let tokenList =
        str.Split ' ' |>
        Array.toList |>
        List.map tokenFromLexeme

    printfn "The initial String: %s" str
    printfn "The initial List: %A\n" tokenList


    try
        let parsedList = program tokenList
        in printfn "The Final List:\n\t%A" parsedList
        printfn "The sentence %s follows the grammar" str
    with
        Failure msg -> printfn "Error: %s" msg

let promptAndGo () =
    //TEST DATA 
    // let userInput = "test ( a  + -- /, b  ) )"
    printf "Enter String: ";
    let userInput =
    
       System.Console.ReadLine ()

    in startParsing userInput

promptAndGo ()
