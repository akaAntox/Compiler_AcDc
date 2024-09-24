package parser;

import java.util.ArrayList;

import ast.*;
import exception.*;
import scanner.*;
import token.*;

public class Parser {
    private Scanner scanner;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Esegue il parsing del programma scansionato
     * 
     * @return NodeProgram che rappresenta l'intero programma parsato
     * @throws SyntaxException
     */
    public NodeProgram parse() throws SyntaxException {
        return parsePrg();
    }

    /**
     * Esegue il parsing di tutti i token scansionati costruendo l'AST
     * 
     * @return NodeProgram che rappresenta l'intero programma parsato
     * @throws SyntaxException
     */
    private NodeProgram parsePrg() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case TYINT:
            case TYFLOAT:
            case ID:
            case PRINT:
            case EOF:
                ArrayList<NodeDecSt> retNodeDecSt = parseDSs();
                match(TokenType.EOF);
                return new NodeProgram(retNodeDecSt);
            default:
                throw isNotProgramStart(token);
        }
    }

    /**
     * Esegue il parsing delle dichiarazioni e delle istruzioni
     * 
     * @return un {@code ArrayList<NodeDecSt>} contenente tutti i token
     * @throws SyntaxException
     */
    private ArrayList<NodeDecSt> parseDSs() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case TYINT:
            case TYFLOAT: // DSs -> Dcl DSs
                NodeDecl dec = parseDcl(); // type float/int?
                ArrayList<NodeDecSt> retList = parseDSs();  // prossima dichiarazione
                retList.add(0, dec);
                return retList;
            case ID:
            case PRINT: // DSs -> Stm DSs
                NodeStm stm = parseStm(); // istruzione print/assign
                ArrayList<NodeDecSt> retList1 = parseDSs();  // prossima dichiarazione
                retList1.add(0, stm);
                return retList1;
            case EOF:
                return new ArrayList<>();
            default:
                throw isNotProgramStart(token);
        }
    }

    /**
     * Esegue il parsing di una dichiarazione
     * 
     * @return Nodo AST che rappresenta la dichiarazione
     * @throws SyntaxException
     */
    private NodeDecl parseDcl() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case TYFLOAT:
                String name = match(TokenType.TYFLOAT).getValue();
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodeDecl(LangType.FLOAT, name);
            case TYINT:
                name = match(TokenType.TYINT).getValue();
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodeDecl(LangType.INT, name);
            default:
                throw unexpectedToken(token);
        }

    }

    /**
     * Esegue il parsing di un'istruzione
     * 
     * @return NodeStm che rappresenta l'istruzione visitata
     * @throws SyntaxException
     */
    private NodeStm parseStm() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case ID:
                match(TokenType.ID);
                match(TokenType.ASSIGN);
                NodeExpr expr = parseExp();
                match(TokenType.SEMI);
                return new NodeAssign(new NodeId(token.getValue()), expr);
            case PRINT:
                Token token2 = match(TokenType.PRINT);
                match(TokenType.ID);
                match(TokenType.SEMI);
                return new NodePrint(new NodeId(token2.getValue()));
            default:
                throw unexpectedToken(token);
        }
    }

    /**
     * Esegue il parsing di un'espressione per il non-terminale Exp
     * 
     * @return NodeExpr che rappresenta l'espressione visitata
     * @throws SyntaxException
     */
    private NodeExpr parseExp() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case INT:
            case FLOAT:
            case ID: // Exp -> Tr ExpP
                NodeExpr ter = parseTr();
                NodeExpr exp = parseExpP(ter);
                return exp;
            default:
                throw unexpectedToken(token);
        }
    }

    /**
     * Esegue il parsing dell'espressione (PLUS e MINUS)
     * 
     * @param leftOp Operazione a sinistra da tracciare
     * @return NodeExpr che rappresenta l'espressione corrente
     * @throws SyntaxException
     */
    private NodeExpr parseExpP(NodeExpr leftOp) throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case PLUS: // ExpP -> + Tr ExpP
                match(TokenType.PLUS);
                NodeExpr terP = parseTr();
                NodeExpr opP = new NodeBinOp(leftOp, terP, LangOper.PLUS);
                NodeExpr expP = parseExpP(opP);
                return expP;
            case MINUS: // ExpP -> - Tr ExpP
                match(TokenType.MINUS);
                NodeExpr terM = parseTr();
                NodeExpr opM = new NodeBinOp(leftOp, terM, LangOper.MINUS);
                NodeExpr expM = parseExpP(opM);
                return expM;
            case SEMI:
                return leftOp;
            default:
                throw unexpectedToken(token);
        }
    }

    /**
     * Parsing del non-terminale Tr che rappresenta le operazioni DIVIDE o TIMES (precedenza maggiore)
     * 
     * @return un oggetto che rappresenta l'albero delle espressioni completo
     * @throws SyntaxException
     */
    private NodeExpr parseTr() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case INT:
            case FLOAT:
            case ID:
                NodeExpr left = parseVal();
                NodeExpr expr = parseTrP(left);
                return expr;
            default:
                throw unexpectedToken(token);
        }
    }

    /**
     * Parsing del non-terminale TrP 
     * 
     * @param leftOp Operazione a sinistra da tracciare
     * @return NodeExpr che rappresenta un albero AST completo per l'espressione data
     * @throws SyntaxException
     */
    private NodeExpr parseTrP(NodeExpr leftOp) throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case TIMES:
                match(TokenType.TIMES);
                NodeExpr valT = parseVal();
                NodeExpr opT = new NodeBinOp(leftOp, valT, LangOper.TIMES);
                NodeExpr expT = parseTrP(opT);
                return expT;
            case DIV:
                match(TokenType.DIV);
                NodeExpr valD = parseVal();
                NodeExpr opD = new NodeBinOp(leftOp, valD, LangOper.DIV);
                NodeExpr expD = parseTrP(opD);
                return expD;
            case PLUS:
            case MINUS:
            case SEMI:
                return leftOp;
            default:
                throw unexpectedToken(token);
        }
    }

    /**
     * Parsing del Val 
     * 
     * @return valore costante o dereferenziato di variabile
     * @throws SyntaxException
     */
    private NodeExpr parseVal() throws SyntaxException {
        Token token = peekToken();
        switch (token.getTokenType()) {
            case INT:
                match(TokenType.INT);
                return new NodeConst(token.getValue(), LangType.INT);
            case FLOAT:
                match(TokenType.FLOAT);
                return new NodeConst(token.getValue(), LangType.FLOAT);
            case ID:
                match(TokenType.ID);
                return new NodeDeref(new NodeId(token.getValue()));
            default:
                throw unexpectedToken(token);
        }

    }

    /**
     * Metodo di matching con il catch dell'eccezione
     * 
     * @param type Tipo da confrontare
     * @return Il prossimo token scansionato
     * @throws SyntaxException
     */
    private Token match(TokenType type) throws SyntaxException {
        Token token = peekToken();
        if (type.equals(token.getTokenType()))
            try {
                return scanner.nextToken();
            } catch (Exception e) {
                throw new SyntaxException("Si è verificato un errore durante la scansione", e);
            }
        throw unexpectedToken(token);
    }

    /**
     * Metodo di peeking con il catch dell'eccezione
     * 
     * @return Il prossimo token scansionato
     * @throws SyntaxException
     */
    private Token peekToken() throws SyntaxException {
        try {
            return scanner.peekToken();
        } catch (Exception e) {
            throw new SyntaxException("Si è verificato un errore durante la scansione", e);
        }
    }

    /**
     * Eccezione lanciata quando il token atteso non è un inizio di programma
     * 
     * @param token Token che ha generato l'eccezione
     * @return Eccezione creata
     */
    private SyntaxException isNotProgramStart(Token token) {
        return new SyntaxException("Il token \'" + token.getTokenType() + "\' in riga " + token.getRow() + " non è un inizio di programma");
    }

    /**
     * Eccezione lanciata quando il token è inatteso
     * 
     * @param token Token che ha generato l'eccezione
     * @return Eccezione creata
     */
    private SyntaxException unexpectedToken(Token token) {
        return new SyntaxException("Token inatteso \'" + token.getTokenType() + "\' in riga " + token.getRow());
    }
}
