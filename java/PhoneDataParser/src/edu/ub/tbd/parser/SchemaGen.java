/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.parser;


import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.entity.Analytics;
import edu.ub.tbd.exceptions.IncompleteLogicError;
import edu.ub.tbd.util.ParserUtil;

import java.util.ArrayList;
import java.util.List;


import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.Union;
import net.sf.jsqlparser.statement.update.Update;
import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.beans.TableBean;

import java.util.HashMap;

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

    private final LogData ld;
    private final Statement stmt;
    private final Analytics parent_analytics;
    private final ArrayList<Analytics> list_analytics = new ArrayList<>();

    public SchemaGen(LogData _ld) {
        this.ld = _ld;
        this.stmt = _ld.getStmt();
        this.parent_analytics = initParentAnalyticsEntity();
        list_analytics.add(parent_analytics);
    }

    public HashMap<String, TableBean> generate() throws ParseException, Exception {
        /*
         if(ParserUtil.isPRAGMA_Query(parent_analytics.getParent_sql())){
         parent_analytics.setQuery_type("PRAGMA");
         }
         */
        HashMap<String, TableBean> extractedSchemaFromSQL = null;

        if (stmt != null) {
            if (stmt instanceof Select) {
                Select select = (Select) stmt;
                SelectUnionParser su_parser = new SelectUnionParser(stmt.toString());
                select.getSelectBody().accept(su_parser);
                extractedSchemaFromSQL = su_parser.getExtractedSchema();
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

    private Analytics initParentAnalyticsEntity() {
        Analytics out = new Analytics();

        out.setTicks(ld.getTicks());
        out.setTicks_ms(ld.getTicks_ms());
        out.setDate_time(ld.getTicks());
        out.setTime_taken(ld.getTime_taken());
        out.setCounter(ld.getCounter());
        out.setRows_returned(ld.getRows_returned());
        out.setUser_id(ld.getUser_id());
        out.setApp_id(ld.getApp_id());
        out.setSql_log_id(ld.getSql_log_id());
        out.setParent_analytics_id(-1);
        out.setSql_height(0);
        out.setQuery_type(ld.getAction());
        out.setParent_sql(ld.getSql());

        return out;
    }

    private Analytics initChildAnalyticsEntity(Analytics _parentAnalytics) {
        Analytics out = new Analytics();

        //out.setArguments((String) JSON_Obj.get("Arguments")); //TODO: <Sankar> Maybe this needs to be fixed
        out.setUser_id(_parentAnalytics.getUser_id());
        out.setApp_id(_parentAnalytics.getApp_id());
        out.setSql_log_id(_parentAnalytics.getSql_log_id());
        out.setParent_analytics_id(_parentAnalytics.getAnalytics_id());
        out.setSql_height(_parentAnalytics.getSql_height() + 1);

        return out;
    }

    public void cumulateAnalyticsFromChildToParent(Analytics _child, Analytics _parent) {
        if (AppConstants.CUMULATE_ANALYTICS_TO_PARENT && _parent != null) {
            _parent.setOuterjoin_count(_parent.getOuterjoin_count() + _child.getOuterjoin_count());
            _parent.setDistinct_count(_parent.getDistinct_count() + _child.getDistinct_count());
            _parent.setLimit_count(_parent.getLimit_count() + _child.getLimit_count());
            _parent.setOrderby_count(_parent.getOrderby_count() + _child.getOrderby_count());
            _parent.setAggregate_count(_parent.getAggregate_count() + _child.getAggregate_count());
            _parent.setGroupby_count(_parent.getGroupby_count() + _child.getGroupby_count());
            _parent.setUnion_count(_parent.getUnion_count() + _child.getUnion_count());
            _parent.setJoin_width(_parent.getJoin_width() + _child.getJoin_width());
            _parent.setWhere_count(_parent.getWhere_count() + _child.getWhere_count());
            _parent.setProject_col_count(_parent.getProject_col_count() + _child.getProject_col_count());
            _parent.setNoOfRelations(_parent.getNoOfRelations() + _child.getNoOfRelations());
            _parent.setLeftOuterJoin_count(_parent.getLeftOuterJoin_count() + _child.getLeftOuterJoin_count());
            _parent.setRightOuterJoint_count(_parent.getRightOuterJoint_count() + _child.getRightOuterJoint_count());
            _parent.setInnerJoin_count(_parent.getInnerJoin_count() + _child.getInnerJoin_count());
            _parent.setSimpleJoin_count(_parent.getSimpleJoin_count() + _child.getSimpleJoin_count());
            _parent.setCrossJoin_count(_parent.getCrossJoin_count() + _child.getCrossJoin_count());
            _parent.setNaturalJoin_count(_parent.getNaturalJoin_count() + _child.getNaturalJoin_count());
            _parent.setSelectItems_count(_parent.getSelectItems_count() + _child.getSelectItems_count());
            _parent.setMaxCount(_parent.getMaxCount() + _child.getMaxCount());
            _parent.setMinCount(_parent.getMinCount() + _child.getMinCount());
            _parent.setSumCount(_parent.getSumCount() + _child.getSumCount());
            _parent.setCount(_parent.getCount() + _child.getCount());
            _parent.setAvgCount(_parent.getAvgCount() + _child.getAvgCount());
            _parent.setGroupConcatCount(_parent.getGroupConcatCount() + _child.getGroupConcatCount());
            _parent.setLengthCount(_parent.getLengthCount() + _child.getLengthCount());
            _parent.setSubstrCount(_parent.getSubstrCount() + _child.getSubstrCount());
            _parent.setCastCount(_parent.getCastCount() + _child.getCastCount());
            _parent.setUpperCount(_parent.getUpperCount() + _child.getUpperCount());
            _parent.setLowerCount(_parent.getLowerCount() + _child.getLowerCount());
            _parent.setCoalesceCount(_parent.getCoalesceCount() + _child.getCoalesceCount());
            _parent.setPhoneNoEqualCount(_parent.getPhoneNoEqualCount() + _child.getPhoneNoEqualCount());
            _parent.setIfNullCount(_parent.getIfNullCount() + _child.getIfNullCount());
            _parent.setJulianDayCount(_parent.getJulianDayCount() + _child.getJulianDayCount());
            _parent.setDateCount(_parent.getDateCount() + _child.getDateCount());
            _parent.setStrfTimeCount(_parent.getStrfTimeCount() + _child.getStrfTimeCount());
            _parent.setTotalWhereClauses(_parent.getTotalWhereClauses() + _child.getTotalWhereClauses());
        }
    }

    class SelectUnionParser implements SelectVisitor, SelectItemVisitor, FromItemVisitor {

        private final HashMap<String, TableBean> EXTRACTED_SCHEMA = new HashMap<>();
        private String curr_sql; //Remove this after debugging and code is finalized.

        public SelectUnionParser(String _curr_sql) {
            this.curr_sql = _curr_sql;
        }

        public HashMap<String, TableBean> getExtractedSchema() {
            return EXTRACTED_SCHEMA;
        }

        private Analytics setUpChildAnalyticsEntity(Analytics _parentAnalytics) {
            Analytics out = initChildAnalyticsEntity(_parentAnalytics);
            list_analytics.add(out);
            return out;
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
            int totalWhereClauses = 0;
            if (_ps.getWhere() != null) {
                totalWhereClauses = ParserUtil.getAndClauses(_ps.getWhere()).size();
            }

            //Parse Projections
            List<SelectItem> selectItems = _ps.getSelectItems();
            for (SelectItem selectItem : selectItems) {
                selectItem.accept(this);
            }
        }

        @Override
        public void visit(AllColumns _ac) {
            //No need to implement in SchemaGen
        }

        @Override
        public void visit(AllTableColumns _atc) {
            //No need to implement in SchemaGen
        }

        @Override
        public void visit(SelectExpressionItem _sei) {
            //TODO: <Sankar>
            //currAnalytics.setProject_col_count(currAnalytics.getProject_col_count() + 1);
        }

        @Override
        public void visit(Union _union) {
            List<PlainSelect> plainSelects = _union.getPlainSelects();
            for (PlainSelect ps : plainSelects) {
                SelectUnionParser child_su_parser = new SelectUnionParser(ps.toString());
                ps.accept(child_su_parser);
                updateExtractedSchema(child_su_parser.getExtractedSchema());
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
            updateExtractedSchema(child_su_parser.getExtractedSchema());
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
