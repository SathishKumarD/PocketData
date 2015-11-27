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
import net.sf.jsqlparser.expression.StringValue;

/**
 *
 * @author san
 */
public class StringValueSerializer extends Serializer<StringValue>{

    @Override
    public void write(Kryo _kryo, Output _output, StringValue _t) {
        _output.writeString(_t.getValue());
    }

    @Override
    public StringValue read(Kryo _kryo, Input _input, Class<StringValue> _type) {
        return new StringValue(_input.readString());
    }
    
}
