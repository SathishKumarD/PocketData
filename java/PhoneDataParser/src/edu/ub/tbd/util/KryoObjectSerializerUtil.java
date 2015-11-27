/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import edu.ub.tbd.beans.LogData;
import edu.ub.tbd.kryo.serializers.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author san
 */
public class KryoObjectSerializerUtil implements ObjectSerializerUtil{
    private final Kryo kryo;
    
    public KryoObjectSerializerUtil(){
        this.kryo = new Kryo();
        try {
            kryoSetUpDefaultSerializers(kryo);
            kryoRegistrationSetUp(kryo);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void kryoSetUpDefaultSerializers(Kryo _k) throws ClassNotFoundException{
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.StringValue.class, StringValueSerializer.class);
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.LongValue.class, LongValueSerializer.class);
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.DateValue.class, DateValueSerializer.class);
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.DoubleValue.class, DoubleValueSerializer.class);
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.TimeValue.class, TimeValueSerializer.class);
        _k.addDefaultSerializer(net.sf.jsqlparser.expression.TimestampValue.class, TimestampValueSerializer.class);
        _k.addDefaultSerializer(Class.forName("java.util.Arrays$ArrayList"), ArraysAsListSerializer.class);
    }
    
    private void kryoRegistrationSetUp(Kryo _k) throws ClassNotFoundException{
        _k.register(java.util.ArrayList.class);
        _k.register(LogData.class);
        _k.register(net.sf.jsqlparser.statement.Statement.class);
        _k.register(net.sf.jsqlparser.statement.select.Select.class);
        _k.register(net.sf.jsqlparser.statement.select.PlainSelect.class);
        _k.register(net.sf.jsqlparser.statement.select.FromItem.class);
        _k.register(net.sf.jsqlparser.expression.Expression.class);
        _k.register(net.sf.jsqlparser.schema.Table.class);
        _k.register(net.sf.jsqlparser.statement.select.Limit.class);
        _k.register(net.sf.jsqlparser.statement.select.AllColumns.class);
        _k.register(net.sf.jsqlparser.expression.BinaryExpression.class);
        _k.register(net.sf.jsqlparser.schema.Column.class);
        _k.register(net.sf.jsqlparser.expression.BooleanValue.class);
        _k.register(net.sf.jsqlparser.expression.DateValue.class);
        _k.register(net.sf.jsqlparser.expression.DoubleValue.class);
        _k.register(net.sf.jsqlparser.expression.LongValue.class);
        _k.register(net.sf.jsqlparser.expression.NullValue.class);
        _k.register(net.sf.jsqlparser.expression.StringValue.class);
        _k.register(net.sf.jsqlparser.expression.TimeValue.class);
        _k.register(net.sf.jsqlparser.expression.TimestampValue.class);
        _k.register(net.sf.jsqlparser.statement.select.OrderByElement.class);
        _k.register(net.sf.jsqlparser.expression.operators.conditional.AndExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.conditional.OrExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.Between.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.EqualsTo.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.ExistsExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.ExpressionList.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.GreaterThan.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.InExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.IsNullExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.ItemsList.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.LikeExpression.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.Matches.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.MinorThan.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.MinorThanEquals.class);
        _k.register(net.sf.jsqlparser.expression.operators.relational.NotEqualsTo.class);
        _k.register(Class.forName("java.util.Arrays$ArrayList"));
    }
    
    @Override
    public void serialize(String file, ArrayList<LogData> _lds){
        try {
            Output k_output = new Output(new FileOutputStream(file));
            kryo.writeObject(k_output, _lds);
            k_output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public ArrayList<LogData> read(String file){
        return read(new File(file));
    }
    
    @Override
    public ArrayList<LogData> read(File file){
        ArrayList<LogData> lds = null;
        try {
            Input k_input = new Input(new FileInputStream(file));
            lds = kryo.readObject(k_input, ArrayList.class);
            k_input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lds;
    }
}
