/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;


import edu.ub.tbd.beans.ColumnBean;
import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.exceptions.IncompleteLogicError;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



import java.util.Set;

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
import edu.ub.tbd.constraint.EitherConstraint;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.regexp.internal.recompile;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
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
    private List<EitherConstraint> constraints = new ArrayList<>();
    private List<TableBean> knowledgeData = new ArrayList<>();
    
    public SchemaGen(LogData _ld) {
        this.stmt = _ld.getStmt();
    }

    public HashMap<String, TableBean> generate() throws ParseException, Exception {
        HashMap<String, TableBean> extractedSchemaFromSQL = null;
        if (stmt != null) {
            if (stmt instanceof Select) {
                Select select = (Select) stmt;
                //SelectUnionParser su_parser = new SelectUnionParser(stmt.toString());
                SelectUnionParser su_parser = new SelectUnionParser();
                select.getSelectBody().accept(su_parser);
                extractedSchemaFromSQL = su_parser.extractSchema();
                knowledgeData = su_parser.getKnowledgeData();
                constraints = su_parser.getConstraints();
            } else if (stmt instanceof Insert) {
                Insert insert = (Insert) stmt;
                SchemaParser insert_parser = new InsertParser(insert);
                insert_parser.init();
                extractedSchemaFromSQL = insert_parser.extractSchema();
                knowledgeData = insert_parser.getKnowledgeData();
                constraints = insert_parser.getConstraints();
            } else if (stmt instanceof Delete) {
                //TODO: <Sankar> Implement this
            } else if (stmt instanceof Update) {
                //TODO: <Sankar> Implement this
            } else {
                throw new IncompleteLogicError("Handle other statement types");
            }
        } else {
            throw new Exception("Null Statement");
        }

        return extractedSchemaFromSQL;
    }
    
    public List<EitherConstraint> getConstraints() {
		return constraints;
	}

	public List<TableBean> getKnowledgeData() {
		return knowledgeData;
	}

	abstract class SchemaParser {
        protected final HashMap<String, TableBean> EXTRACTED_SCHEMA = new HashMap<>();
        protected Set<String> EXTRACTED_ALIAS = new HashSet<>();
        protected List<EitherConstraint> CONSTRAINTS = new ArrayList<>();
        
        private String curr_sql; //Remove this after debugging and code is finalized.
        
        public SchemaParser(String _curr_sql) {
            this.curr_sql = _curr_sql;
        }
        
        public SchemaParser() {
        }
        
        public void addAllExtractedAlias(Set<String> alias) {
        	EXTRACTED_ALIAS.addAll(alias);
        }
        
        abstract public void init();
        
        public HashMap<String, TableBean> extractSchema() {
            return EXTRACTED_SCHEMA;
        }
        
        public void buildConstraints() {
        	
        }
        
        public List<TableBean> getKnowledgeData() {
        	if(EXTRACTED_SCHEMA != null) {
        		List<TableBean> list = new ArrayList<>();
        		for(TableBean bean : EXTRACTED_SCHEMA.values()) {
        			if(bean.getColumns() != null) {
        				TableBean newTBLBean = new TableBean(bean.getTbl_name());
        				for(ColumnBean column : bean.getColumns().values()) {
        					if(column.isConfirmed()) {
        						newTBLBean.addColumn(column);
        					}
        				}
        				list.add(newTBLBean);
        			}
        		}
        		return list;
        	}
        	return null;
        }
        
        public List<EitherConstraint> getConstraints() {
        	return CONSTRAINTS;
        }
        
        public Set<String> getExtractedAlias() {
        	return EXTRACTED_ALIAS;
        }
        
        protected void updateExtractedSchemaAndAlias(final SchemaParser parser) {
        	HashMap<String,TableBean> _extractedSchema = parser.extractSchema();
        	parser.buildConstraints();
        	addAllExtractedAlias(parser.getExtractedAlias());
            for(TableBean tbl : _extractedSchema.values()){
                updateExtractedSchema(tbl);
            }
        }

        protected void updateExtractedSchema(final TableBean _extractedTbl) {
            TableBean baseTbl = EXTRACTED_SCHEMA.get(_extractedTbl.getTbl_name());
            if (baseTbl != null) {
                baseTbl.addAllColumns(_extractedTbl.getColumns().values());
            } else {
                EXTRACTED_SCHEMA.put(_extractedTbl.getTbl_name(), _extractedTbl);
            }
        }
        
        protected void mergeColumnsToExtractedSchema(List<ColumnBean> extractedColumns) {
        	Iterator<ColumnBean> iterator = extractedColumns.iterator();
        	
        	while(iterator.hasNext()) {
        		ColumnBean colBean = iterator.next();
        		if(!EXTRACTED_ALIAS.contains(colBean.getCol_name())) {
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
        }
    }

    class InsertParser extends SchemaParser{
        
        private final Insert stmt;
        
        public InsertParser(Insert _stmt){
            super();
            this.stmt = (Insert) _stmt;
        }
        
        public void init(){
            String tblName = stmt.getTable().getName();
            
            List<ColumnBean> columnBeans = new ArrayList<>(stmt.getColumns().size());
            for(Column _column: stmt.getColumns()){
                ColumnBean columnBean = new ColumnBean(_column.getColumnName());
                columnBean.setTable_name(tblName);
                columnBean.setConfirmed(true);
                columnBeans.add(columnBean);
            }
            
            TableBean tableBean = new TableBean(tblName);
            tableBean.addAllColumns(columnBeans);
            
            updateExtractedSchema(tableBean);
        }
    }
    
    class SelectUnionParser extends SchemaParser implements SelectVisitor, FromItemVisitor {

        public SelectUnionParser(String _curr_sql) {
            super(_curr_sql);
        }

        public SelectUnionParser() {
        }

        public void init(){
            //No need to init anything
        }
        
        @Override
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
        
        @Override
        public void buildConstraints() {
        	HashMap<String,TableBean> extractSchema = extractSchema();
        	if(extractSchema != null) {
        		Map<String, List<String>> map = new HashMap<>();
        		for(TableBean tblBean : extractSchema.values()) {
        			if(tblBean.getColumns() != null) {
        				for(ColumnBean bean : tblBean.getColumns().values()) {
        					if(!bean.isConfirmed()) {
        						List<String> list = map.get(bean.getCol_name());
        						if(list != null) {
        							list.add(tblBean.getTbl_name());
        						} else {
        							list = new ArrayList<>();
        							list.add(tblBean.getTbl_name());
        							map.put(bean.getCol_name(), list);
        						}
        					}
        				}
        			}
            	}
        		
        		CONSTRAINTS = EitherConstraint.createEitherConstraints(map);
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
            	
            	addAllExtractedAlias(exprVisitorImpl.getExprAliasSet());
            	mergeColumnsToExtractedSchema(exprVisitorImpl.getColumns());
            }

            //Parse Projections
            List<SelectItem> selectItems = _ps.getSelectItems();
            for (SelectItem selectItem : selectItems) {
                ExpressionVisitorImpl exprVisitorImpl = new ExpressionVisitorImpl(this);
                selectItem.accept(exprVisitorImpl);
                addAllExtractedAlias(exprVisitorImpl.getExprAliasSet());
                mergeColumnsToExtractedSchema(exprVisitorImpl.getColumns());
            }
        }

        @Override
        public void visit(Union _union) {
            List<PlainSelect> plainSelects = _union.getPlainSelects();
            for (PlainSelect ps : plainSelects) {
                SelectUnionParser child_su_parser = new SelectUnionParser(ps.toString());
                ps.accept(child_su_parser);
                updateExtractedSchemaAndAlias(child_su_parser);
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
            updateExtractedSchemaAndAlias(child_su_parser);
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
