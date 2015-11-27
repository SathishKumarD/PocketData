/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import net.sf.jsqlparser.expression.DateValue;

/**
 *
 * @author san
 */
public class DateValueSerializer extends Serializer<DateValue>{

    @Override
    public void write(Kryo _kryo, Output _output, DateValue _t) {
        _output.writeString(_t.toString());
    }

    @Override
    public DateValue read(Kryo _kryo, Input _input, Class<DateValue> _type) {
        return new DateValue(_input.readString());
    }
    
}
