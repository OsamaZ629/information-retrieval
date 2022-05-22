package com.osama.infoRetrieval.matching.boolean_matchers;

import ai.djl.ndarray.NDManager;
import com.osama.infoRetrieval.document.Document;
import com.osama.infoRetrieval.document.Query;
import com.osama.infoRetrieval.matching.Matcher;
import com.osama.infoRetrieval.matching.word_matchers.TDMWordMatcher;
import com.osama.infoRetrieval.processing.storageDevice.TDMStorage;
import com.osama.infoRetrieval.processing.tokenization.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class TDMBooleanMatcher extends Matcher<TDMStorage> {
    private static final Token andToken = new Token("&&");
    private static final Token andNotToken = new Token("&&!");
    private static final Token orToken = new Token("||");

    public TDMBooleanMatcher(TDMStorage storage) {
        super(storage);
    }

    @Override
    public List<Document> match(Query q) {
        TDMWordMatcher wm = new TDMWordMatcher(storage);
        Stack<Token> rpn = buildRPN(q);
        return evalRPN(rpn, wm);
    }

    public Stack<Token> buildRPN(Query q){
        Stack<Token> output = new Stack<>();
        Stack<Token> ops = new Stack<>();
        List<Token> tokens = new ArrayList<>(q.getTokens());

        for (Token term: tokens){
            if (term.equals(andToken)){
                ops.push(term);
            } else if (term.equals(andNotToken)){
                if (ops.isEmpty()){
                    ops.push(term);
                    continue;
                }
                Token t = ops.peek();
                if (t != null && t.equals(andToken)){
                    t = ops.pop();
                    output.push(t);
                    ops.push(term);
                } else{
                    ops.push(term);
                }
            } else if (term.equals(orToken)){
                if (ops.isEmpty()){
                    ops.push(term);
                    continue;
                }
                Token t = ops.peek();
                if (t != null && (t.equals(andNotToken) || t.equals(andToken))){
                    t = ops.pop();
                    output.push(t);
                    ops.push(term);
                } else {
                    ops.push(term);
                }
            } else{
                output.push(term);
            }
        }
        output.addAll(ops);
        return output;
    }

    private List<Document> evalRPN(Stack<Token> expr, TDMWordMatcher wordMatcher){
        Stack<Token> stack = new Stack<>();
        List<Document> res = new ArrayList<>();
        boolean firstOpMade = false;

        for (Token token : expr){

            if (token.equals(andNotToken)) {
                Token firstOperand = stack.pop();
                if (!firstOpMade){
                    firstOpMade = true;
                    Token second = stack.pop();
                    res = resolveOperation(wordMatcher.match(buildQuery(second)), wordMatcher.match(buildQuery(firstOperand)), Operator.AND_NOT);
                } else {
                    res = resolveOperation(res, wordMatcher.match(buildQuery(firstOperand)), Operator.AND_NOT);
                }
            } else if (token.equals(andToken)) {
                Token firstOperand = stack.pop();
                if (!firstOpMade){
                    firstOpMade = true;
                    Token second = stack.pop();
                    res = resolveOperation(wordMatcher.match(buildQuery(second)), wordMatcher.match(buildQuery(firstOperand)), Operator.AND);
                } else {
                    res = resolveOperation(res, wordMatcher.match(buildQuery(firstOperand)), Operator.AND);
                }            } else if (token.equals(orToken)) {
                Token firstOperand = stack.pop();
                if (!firstOpMade){
                    firstOpMade = true;
                    Token second = stack.pop();
                    res = resolveOperation(wordMatcher.match(buildQuery(second)), wordMatcher.match(buildQuery(firstOperand)), Operator.OR);
                } else {
                    res = resolveOperation(res, wordMatcher.match(buildQuery(firstOperand)), Operator.OR);
                }            } else {
                stack.push(token);
            }
        }

        return res;
    }

    private List<Document> resolveOperation(List<Document> f, List<Document> s, Operator operator) {
        HashSet<Document> fSet = new HashSet<>(f);
        HashSet<Document> sSet = new HashSet<>(s);

        switch (operator){
            case OR:
                fSet.addAll(s);
                return new ArrayList<>(fSet);
            case AND:
                HashSet<Document> res = new HashSet<>();
                for(Document doc: fSet){
                    if (sSet.contains(doc))
                        res.add(doc);
                }
                return new ArrayList<>(res);
            case AND_NOT:
                HashSet<Document> res2 = new HashSet<>();
                for(Document doc: fSet){
                    if (!sSet.contains(doc))
                        res2.add(doc);
                }
                return new ArrayList<>(res2);
            default:
                return f;
        }
    }

    private Query buildQuery(Token c){
        List<Token> l = new ArrayList<>(1);
        l.add(c);
        return new Query(l);
    }
}
