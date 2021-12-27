/*
COURSE: COSC455
Assignment: Program 1
Name: Alemseged, Boaz
Name: Giorgis, Gideon
*/




package COSC455.ParserExample_Java11;
//  ************** NOTE: REQUIRES JAVA 11 OR ABOVE! ******************


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.print.attribute.standard.MediaSize.Other;

/*
 * GRAMMAR FOR PROCESSING SIMPLE SENTENCES:
 *
 * <SENTENCE> ::= <NP> <VP> <NP> <PP> <SENTENCE_TAIL> 
 * <SENTENCE_TAIL> ::= <CONJ> <SENTENCE> | <EOS>
 * <SENTENCE> <EOS> | <EOS>
 *
 * <NP> ::= <ART> <ADJ_LIST> <NOUN> 
 * <ADJ_LIST> ::= <ADJ> <ADJ_TAIL> | <<EMPTY>>
 * <ADJ_TAIL> ::= <COMMA> <ADJ> <ADJ_TAIL> | <<EMPTY>>
 *
 * <VP> ::= <ADV> <VERB> | <VERB> 
 * <PP> ::= <PREP> <NP> | <<EMPTY>>
 *
 * // *** Terminal Productions (Actual terminals omitted, but they are just the
 * valid words in the language). *** 
 * 
 * <COMMA> ::= ','
 * <EOS> ::= '.' | '!'
 * 
 * <ADJ> ::= ...adjective list... 
 * <ADV> ::= ...adverb list... 
 * <ART> ::= ...article list... 
 * <CONJ> ::= ...conjunction list... 
 * <NOUN> ::= ...noun list... 
 * <PREP> ::= ...preposition list... 
 * <VERB> ::= ...verb list....
 */


/**
 * The Syntax Analyzer.
 * 
 * ************** NOTE: REQUIRES JAVA 11 OR ABOVE! ******************
 */
class Parser {

// The lexer which will provide the tokens
    private final LexicalAnalyzer lexer;

    // the actual "code generator"
    private final CodeGenerator codeGenerator;

    /**
     * The constructor initializes the terminal literals in their vectors.
     *
     * @param lexer The Lexer Object
     */
    public Parser(LexicalAnalyzer lexer, CodeGenerator codeGenerator) {
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
    }

    /**
     * Since the "Compiler" portion of the code knows nothing about the start rule, the "analyze" method
     * must invoke the start rule.
     *
     * Begin analyzing...
     */
    public void analyze() {
        try {
            // Generate header for our output
            var startNode = codeGenerator.writeHeader("PARSE TREE");

            // THIS IS OUR START RULE
            // TODO: Change if necessary!
            //changed this
            program(startNode);

            // generate footer for our output
            codeGenerator.writeFooter();

        } catch (ParseException ex) {
            final String msg = String.format("%s\n", ex.getMessage());
            Logger.getAnonymousLogger().severe(msg);
        }
    }

    /*
    // <SENTENCE> ::= <NP> <VP> <NP> <PP> <SENTENCE_TAIL> 
    protected void SENTENCE(final ParseNode fromNode) throws ParseException {
        final var nodeName = codeGenerator.addNonTerminalToTree(fromNode);

        NP(nodeName);
        VP(nodeName);
        NP(nodeName);
        PP(nodeName);
        Sentence_Tail(nodeName);
    }

    // <SENTENCE_TAIL> ::= <CONJ> <SENTENCE> | <EOS>
    void Sentence_Tail(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if (lexer.isCurrentToken(TOKEN.CONJUNCTION)) {
            CONJ(treeNode);
            SENTENCE(treeNode);
        } else {
            EOS(treeNode);
        }
    }

    // <NP> ::= <ART> <ADJ_LIST> <NOUN>
    void NP(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        ART(treeNode);
        ADJ_LIST(treeNode);
        NOUN(treeNode);
    }

    // <ADJ_LIST> ::= <ADJ> <ADJ_TAIL> | <<EMPTY>>
    void ADJ_LIST(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if (lexer.isCurrentToken(TOKEN.ADJECTIVE)) {
            ADJ(treeNode);
            ADJ_TAIL(treeNode);
        } else {
            EMPTY(treeNode);
        }
    }

    // <ADJ_TAIL> ::= <COMMA> <ADJ> <ADJ_TAIL> | <<EMPTY>>
    void ADJ_TAIL(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if (lexer.isCurrentToken(TOKEN.ADJ_SEP)) {
            ADJ_SEP(treeNode);
            ADJ(treeNode);
            ADJ_TAIL(treeNode);
        } else {
            EMPTY(treeNode);
        }
    }

    // <VP> ::= <ADV> <VERB> | <VERB>
    void VP(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if (lexer.isCurrentToken(TOKEN.ADVERB)) {
            ADV(treeNode);
        }

        VERB(treeNode);
    }

    // <PP> ::= <PREP> <NP> | <<EMPTY>>
    void PP(final ParseNode fromNode) throws ParseException {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if (lexer.isCurrentToken(TOKEN.PREPOSITION)) {
            PREP(treeNode);
            NP(treeNode);
        } else {
            EMPTY(treeNode);
        }
    }*/

    //mychanges
    void program(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);
        statementList(treeNode);
    }
    
    //ID
    void id(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.id))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("ID", fromNode);
        }
    }

    //NUMBER
    void NUMBER(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.NUMBER))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("NUMBER", fromNode);
        }
    }

     //Expression
     void EXPR(final ParseNode fromNode) throws ParseException{
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);
        term(treeNode);
        termEnd(treeNode);
    }

    //cond
    void cond(final ParseNode fromNode) throws ParseException{
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);
        EXPR(treeNode);
        condOP(treeNode);
        EXPR(treeNode);
    }

    //CONDITIONAL OPERATORS
    void condOP(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.condOP))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("conditional operator", fromNode);
        }
    }

    //multipy
    void multiply(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.multiply))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("multiply operator", fromNode);
        }
    }

    //add
    void add(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.ENDIF))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        if(lexer.isCurrentToken(TOKEN.add))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("add operator", fromNode);
        }
    }

    //openParentheses
    void openParen(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.openParen))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("Open Parentheses", fromNode);
        }
    }

    //closeParentheses
    void closeParen(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.closeParen))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("Close Parentheses", fromNode);
        }
    }


    //IF
    void IF(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.IF))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("if() statement", fromNode);
        }
    }

    //ELSE
    void ELSE(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.ELSE))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("else() statement", fromNode);
        }
    }

    //ENDIF
    void ENDIF(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.ENDIF))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("end if", fromNode);
        }
    }

    //Then
    void then(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.then))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("then", fromNode);
        }
    }

    

    //WHILE
    void WHILE(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.WHILE))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("while() statement", fromNode);
        }
    }

    //DO WHILE
    void postWhile(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.postWhile))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("do while(); statement", fromNode);
        }
    }

    //READ
    void read(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.read))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("read", fromNode);
        }
    }

    //EndWhile
    void write(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.write))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("write", fromNode);
        }
    }

    //EndLoop
    void od(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.od))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("OD", fromNode);
        }
    }

    //assignment
    void assignment(final ParseNode fromNode) throws ParseException
    {
        if(lexer.isCurrentToken(TOKEN.assignment))
        {
            addTerminalAndAdvanceToken(fromNode);
        }
        else
        {
            raiseException("assignment", fromNode);
        }
    }





    //factor
    void factor(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if(lexer.isCurrentToken(TOKEN.openParen))
        {
            openParen(treeNode);
            EXPR(treeNode);
            closeParen(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.OTHER))
        {
            Other(fromNode);
        }
        else
        {
            NUMBER(treeNode);
        }
    }

    //factor Ending
    void factorEnd(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if(lexer.isCurrentToken(TOKEN.multiply))
        {
            multiply(treeNode);
            factor(treeNode);
            factorEnd(treeNode);
        }
        else
        {
            EMPTY(treeNode);
        }
    }

    //term
    void term(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        factor(treeNode);
        factorEnd(treeNode);
    }

    //term Ending
    void termEnd(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if(lexer.isCurrentToken(TOKEN.add))
        {
            add(treeNode);
            term(treeNode);
            termEnd(treeNode);
        }
        else
        {
            EMPTY(treeNode);
        }
    }
    void Other(ParseNode fromNode) throws ParseException {
        if (!lexer.isCurrentToken(TOKEN.OTHER)) {
            raiseException("OTHER expected", fromNode);
        } else {
            addTerminalAndAdvanceToken(fromNode);
        }
    }
    void statement(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);

        if(lexer.isCurrentToken(TOKEN.id))
        {
            addTerminalAndAdvanceToken(treeNode);
            addTerminalAndAdvanceToken(treeNode);
            EXPR(treeNode);
        }

        else if(lexer.isCurrentToken(TOKEN.read))
        {
            read(treeNode);
            EXPR(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.write))
        {
            write(treeNode);
            EXPR(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.WHILE))
        {
            WHILE(treeNode);
            cond(treeNode);
            postWhile(treeNode);
            statementList(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.postWhile))
        {
            postWhile(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.IF))
        {
            addTerminalAndAdvanceToken(treeNode);
            cond(treeNode);
            addTerminalAndAdvanceToken(treeNode);
            statementList(treeNode);
            addTerminalAndAdvanceToken(treeNode);
        }
        else if(lexer.isCurrentToken(TOKEN.OTHER))
        {
            Other(treeNode);
            assignment(treeNode);
            EXPR(treeNode);
        }
        else
        {
            raiseException("Statement", fromNode);
            
        }
    }

    void statementList(final ParseNode fromNode) throws ParseException
    {
        final var treeNode = codeGenerator.addNonTerminalToTree(fromNode);
        if(lexer.isCurrentToken(TOKEN.id)||lexer.isCurrentToken(TOKEN.read) || lexer.isCurrentToken(TOKEN.write) || lexer.isCurrentToken(TOKEN.IF) || lexer.isCurrentToken(TOKEN.OTHER))
        {
            statement(treeNode);
            statementList(treeNode);
        }
        else
        {
            EMPTY(treeNode);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // For the sake of completeness, each terminal-token has its own method,
    // though they all do the same thing here.  In a "REAL" program, each terminal
    // would likely have unique code associated with it.
    /////////////////////////////////////////////////////////////////////////////////////
    void EMPTY(final ParseNode fromNode) throws ParseException {
        codeGenerator.addEmptyToTree(fromNode);
    }

    // <EOS>
    void EOS(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.EOS)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("an End of Sentence", fromNode);
        }
    }

    // <ADJ>
    void ADJ(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.ADJECTIVE)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("an Adjective", fromNode);
        }
    }

    // <ADV> 
    void ADV(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.ADVERB)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("an Adverb", fromNode);
        }
    }

    // <ART> 
    void ART(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.ARTICLE)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("an Article", fromNode);
        }
    }

    // <CONJ> 
    void CONJ(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.CONJUNCTION)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("a Conjunction", fromNode);
        }
    }

    // <NOUN>
    void NOUN(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.NOUN)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("a Noun", fromNode);
        }
    }

    // <PREP>
    void PREP(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.PREPOSITION)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("a Preposition", fromNode);
        }
    }

    // <VERB>
    void VERB(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.VERB)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("a Verb", fromNode);
        }
    }

    // <ADJ_SEP>
    void ADJ_SEP(final ParseNode fromNode) throws ParseException {
        if (lexer.isCurrentToken(TOKEN.ADJ_SEP)) {
            addTerminalAndAdvanceToken(fromNode);
        } else {
            raiseException("an Adjective List Separator", fromNode);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Terminal:
    // Test its type and continue if we really have a terminal node, syntax error if fails.
    void addTerminalAndAdvanceToken(final ParseNode fromNode) throws ParseException {
        final var currentTerminal = lexer.getCurrentToken();
        final var terminalNode = codeGenerator.addNonTerminalToTree(fromNode, String.format("<%s>", currentTerminal));

        codeGenerator.addTerminalToTree(terminalNode, lexer.getCurrentLexeme());
        lexer.advanceToken();
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // The code below this point is just a bunch of "helper functions" to keep 
    // the parser code (above) a bit cleaner.
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //
    // Handle all the errors in one place for cleaner parser code.
    private void raiseException(String expected, ParseNode fromNode) throws ParseException {
        final var template = "SYNTAX ERROR: '%s' was expected but '%s' was found.";
        final var err = String.format(template, expected, lexer.getCurrentLexeme());
        codeGenerator.syntaxError(err, fromNode);
    }
}

///Users/boazd_000/Downloads/NB-LexerParser_StandAloneExample_Java11 (1)/src/COSC455/ParserExample_Java11/Parser.java

/**
 * All the Tokens/Terminals Used by the parser. The purpose of the enum type
 * here is to eliminate the need for direct string comparisons which is generally
 * slow, as being difficult to maintain. (We want Java's "static type checking"
 * to do as much work for us as it can!)
 *
 * !!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!
 * -----------------------------------------------------------------------------
 * IN MOST REAL CASES, THERE WILL BE ONLY ONE LEXEME PER TOKEN. !!!
 * -----------------------------------------------------------------------------
 * !!!!!!!!!!!!!!!!!!!!! IMPORTANT !!!!!!!!!!!!!!!!!!!!!!
 *
 * The fact that several lexemes exist per token in this example is because this
 * is to parse simple English sentences where most of the token types have many
 * words (lexemes) that could fit. This is generally NOT the case in most
 * programming languages!!!
 */
enum TOKEN {
    ARTICLE("a", "the"),
    CONJUNCTION("and", "or"),
    NOUN("dog", "cat", "rat", "house", "tree"),
    VERB("loves", "hates", "eats", "chases", "stalks"),
    ADJECTIVE("fast", "slow", "furry", "sneaky", "lazy", "tall"),
    ADJ_SEP(","),
    ADVERB("quickly", "secretly", "silently"),
    PREPOSITION("of", "on", "around", "with", "up"),
    EOS(".", "!"),
    // THESE ARE NOT USED IN THE GRAMMAR, BUT MIGHT BE USEFUL...  :)
    EOF, // End of file
    OTHER, // Could be "ID" in a "real programming language"


    //MY CHANGES
    id, cond, NUMBER,
    condOP("<", ">","<=", ">=", "!=", "==") ,
    multiply("*", "/"), add("+", "-"),
    openParen("("), closeParen(")"), 
    assignment(":="),
    IF("if"), ELSE("else"), ENDIF("fi"),
    od("od"),
    then("then"),
    postWhile("do"),
    WHILE("while"),
    read("read"),
    write("write"); // A sequence of digits.

    /**
     * A list of all lexemes for each token.
     */
    private final List<String> lexemeList;

    TOKEN(final String... tokenStrings) {
        lexemeList = new ArrayList<>(tokenStrings.length);
        lexemeList.addAll(Arrays.asList(tokenStrings));
    }

    /**
     * Get a TOKEN object from the Lexeme string.
     *
     * @param string The String (lexeme) to convert to a TOKEN
     *
     * @return A TOKEN object based on the input String (lexeme)
     */
    public static TOKEN fromLexeme(final String string) {
        // Just to be safe...
        final var lexeme = string.trim();

        // An empty string should mean no more tokens to process.
        if (lexeme.isEmpty()) {
            return EOF;
        }

        // Regex for one or more digits optionally followed by . and more digits. 
        // (doesn't handle "-", "+" etc., only digits)       
        if (lexeme.matches("\\d+(?:\\.\\d+)?")) {
            return NUMBER;
        }

        // Search through ALL lexemes looking for a match with early bailout.
        for (var token : TOKEN.values()) {
            if (token.lexemeList.contains(lexeme)) {
                // early bailout from for loop.
                return token;
            }
        }

        // NOTE: Other could represent an ID, for example.
        return OTHER;
    }
}
