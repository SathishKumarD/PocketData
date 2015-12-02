/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;

import edu.ub.tbd.beans.ColumnBean;
import edu.ub.tbd.parser.AnalyticsGen.SelectUnionParser;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BooleanValue;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 *
 * @author san
 */
public class ExpressionVisitorImpl implements ExpressionVisitor {

    private List<ColumnBean> columns = new ArrayList<>();
    private SelectUnionParser caller_su_parser;
    
    public ExpressionVisitorImpl(SelectUnionParser _caller_su_parser){
        this.caller_su_parser = _caller_su_parser;
    }

    public List<ColumnBean> getColumns() {
        return columns;
    }

    @Override
    public void visit(CaseExpression ce) {
        Expression switchExpr = ce.getSwitchExpression();
        if (switchExpr != null) {
            switchExpr.accept(this);
        }

        Expression elseExpr = ce.getElseExpression();
        if (elseExpr != null) {
            elseExpr.accept(this);
        }

        List<WhenClause> whenClauses = ce.getWhenClauses();
        if (whenClauses != null) {
            for (WhenClause whenClause : whenClauses) {
                whenClause.accept(this);
            }
        }
    }

    @Override
    public void visit(WhenClause e) {
        e.getWhenExpression().accept(this);
        e.getThenExpression().accept(this);
    }

    @Override
    public void visit(Between e) {
        e.getLeftExpression().accept(this);
        e.getBetweenExpressionStart().accept(this);
        e.getBetweenExpressionEnd().accept(this);
    }

    @Override
    public void visit(InverseExpression e) {
        e.getExpression().accept(this);
    }

    @Override
    public void visit(InExpression e) {
        e.getLeftExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression e) {
        e.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Addition e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(AndExpression e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(BitwiseAnd e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(BitwiseOr e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(BitwiseXor e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(Concat e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(Division e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThan e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(GreaterThanEquals e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(LikeExpression e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(Matches e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThan e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(MinorThanEquals e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(Multiplication e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(NotEqualsTo e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(OrExpression e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(Subtraction e) {
        e.getLeftExpression().accept(this);
        e.getRightExpression().accept(this);
    }

    @Override
    public void visit(AllComparisonExpression ace) {
        throw new UnsupportedOperationException("AllComparisonExpression in SQL Not supported yet.");
    }

    @Override
    public void visit(AnyComparisonExpression ace) {
        throw new UnsupportedOperationException("AnyComparisonExpression in SQL Not supported yet.");
    }

    @Override
    public void visit(StringValue sv) {
        //Ignore this
    }

    @Override
    public void visit(DoubleValue dv) {
        //Ignore this
    }

    @Override
    public void visit(LongValue lv) {
        //Ignore this
    }

    @Override
    public void visit(DateValue dv) {
        //Ignore this
    }

    @Override
    public void visit(TimeValue tv) {
        //Ignore this
    }

    @Override
    public void visit(TimestampValue tv) {
        //Ignore this
    }

    @Override
    public void visit(NullValue nv) {
        //Ignore this
    }

    @Override
    public void visit(ExistsExpression ee) {
        throw new UnsupportedOperationException("ExistsExpression in SQL Not supported yet.");
    }

    @Override
    public void visit(JdbcParameter jp) {
        throw new UnsupportedOperationException("JdbcParameter in SQL Not supported yet.");
    }

    @Override
    public void visit(Function e) {
        ExpressionList params = e.getParameters();
        if (params != null) {
//    	            params.accept(this);
        }
    }

    @Override
    public void visit(BooleanValue bv) {
        // TODO Auto-generated method stub
    }

    @Override
    public void visit(Column _column) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void visit(SubSelect _ss) {
        caller_su_parser.visit(_ss);
    }

}
