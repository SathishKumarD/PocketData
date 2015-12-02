/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;


import edu.ub.tbd.beans.ColumnBean;
import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.exceptions.IncompleteLogicError;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.Union;
import net.sf.jsqlparser.statement.update.Update;
import edu.ub.tbd.beans.TableBean;

import java.util.HashMap;
import net.sf.jsqlparser.expression.Expression;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;


/**
 *
 * @author sathish
 */
public class SchemaGen {

    private final Statement stmt;

    public SchemaGen(LogData _ld) {
        this.stmt = _ld.getStmt();
    }

    public HashMap<String, TableBean> generate() throws ParseException, Exception {
        HashMap<String, TableBean> extractedSchemaFromSQL = null;
        if (stmt != null) {
            if (stmt instanceof Select) {
                Select select = (Select) stmt;
                SelectUnionParser su_parser = new SelectUnionParser(stmt.toString());
                select.getSelectBody().accept(su_parser);
                extractedSchemaFromSQL = su_parser.extractSchema();
            } else if (stmt instanceof Delete) {
                //TODO: <Sankar> Implement this
            } else if (stmt instanceof Update) {
                //TODO: <Sankar> Implement this
            } else if (stmt instanceof Insert) {
                //TODO: <Sankar> Implement this
            } else {
                throw new IncompleteLogicError("Handle other statement types");
            }
        } else {
            throw new Exception("Null Statement");
        }

        return extractedSchemaFromSQL;
    }

    class SelectUnionParser implements SelectVisitor, FromItemVisitor {

        private final HashMap<String, TableBean> EXTRACTED_SCHEMA = new HashMap<>();
        private String curr_sql; //Remove this after debugging and code is finalized.

        public SelectUnionParser(String _curr_sql) {
            this.curr_sql = _curr_sql;
        }

        public HashMap<String, TableBean> extractSchema() {
            if(EXTRACTED_SCHEMA.size() == 1){
                for(TableBean tbl : EXTRACTED_SCHEMA.values()){
                    for(ColumnBean col : tbl.getColumns().values()){
                        col.setConfirmed(true);
                    }
                }
            }
            return EXTRACTED_SCHEMA;
        }
        
        private void updateExtractedSchema(final HashMap<String, TableBean> _extractedSchema) {
            for(TableBean tbl : _extractedSchema.values()){
                updateExtractedSchema(tbl);
            }
        }

        private void updateExtractedSchema(final TableBean _extractedTbl) {
            TableBean baseTbl = EXTRACTED_SCHEMA.get(_extractedTbl.getTbl_name());
            if (baseTbl != null) {
                baseTbl.addAllColumns(_extractedTbl.getColumns().values());
            } else {
                EXTRACTED_SCHEMA.put(_extractedTbl.getTbl_name(), _extractedTbl);
            }
        }

        @Override
        public void visit(PlainSelect _ps) {
            //Parse From statements
            FromItem fromItem = _ps.getFromItem();
            fromItem.accept(this);
            
            List<Join> joins = _ps.getJoins();
            if(joins != null && joins.size() > 0) {
            	for(int i = 0; i < joins.size(); i++) {
            		Join join = joins.get(i);
            		FromItem rightItem = join.getRightItem();
            		rightItem.accept(this);
            	}
            }
            
            //Parse whereclauses
            if (_ps.getWhere() != null) {
            	Expression expression = _ps.getWhere();
            	ExpressionVisitorImpl exprVisitorImpl = new ExpressionVisitorImpl(this);
            	expression.accept(exprVisitorImpl);
            	
            	mergeColumnsToExtractedSchema(exprVisitorImpl.getColumns());
            }

            //Parse Projections
            List<SelectItem> selectItems = _ps.getSelectItems();
            for (SelectItem selectItem : selectItems) {
                ExpressionVisitorImpl exprVisitorImpl = new ExpressionVisitorImpl(this);
                selectItem.accept(exprVisitorImpl);
                mergeColumnsToExtractedSchema(exprVisitorImpl.getColumns());
            }
        }
        
        public void mergeColumnsToExtractedSchema(List<ColumnBean> extractedColumns) {
        	Iterator<ColumnBean> iterator = extractedColumns.iterator();
        	
        	while(iterator.hasNext()) {
        		ColumnBean colBean = iterator.next();
        		Iterator<Entry<String, TableBean>> schemaIterator = EXTRACTED_SCHEMA.entrySet().iterator();
        		while(schemaIterator.hasNext()) {
        			Entry<String, TableBean> next = schemaIterator.next();
        			TableBean tableBean = next.getValue();
        			
        			if(colBean.getTable_name() == null) {
        				tableBean.addColumn(colBean);
        			} else if(colBean.getTable_name().equals(next.getKey()) || colBean.getTable_name().equals(next.getValue().getTbl_alias())) {
        				// Checking for table Name or Table Alias
        				colBean.setConfirmed(true);
        				tableBean.addColumn(colBean);
        				break;
        			}
        		}
        	}
        }

        @Override
        public void visit(Union _union) {
            List<PlainSelect> plainSelects = _union.getPlainSelects();
            for (PlainSelect ps : plainSelects) {
                SelectUnionParser child_su_parser = new SelectUnionParser(ps.toString());
                ps.accept(child_su_parser);
                updateExtractedSchema(child_su_parser.extractSchema());
            }

        }

        @Override
        public void visit(Table _table) {
            TableBean tbl_bean = new TableBean(_table.getName(), _table.getAlias());
            tbl_bean.setSchemaName(_table.getSchemaName());
            tbl_bean.setWholeTblName(_table.getWholeTableName());
            updateExtractedSchema(tbl_bean);
        }

        @Override
        public void visit(SubSelect _ss) {
            SelectUnionParser child_su_parser = new SelectUnionParser(_ss.toString());
            _ss.getSelectBody().accept(child_su_parser);
            updateExtractedSchema(child_su_parser.extractSchema());
        }

        @Override
        public void visit(SubJoin _sj) {
        	_sj.getLeft().accept(this);
        	Join join = _sj.getJoin();
        	FromItem rightItem = join.getRightItem();
        	rightItem.accept(this);
        }

    }

}
