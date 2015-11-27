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
import net.sf.jsqlparser.expression.DoubleValue;

/**
 *
 * @author san
 */
public class DoubleValueSerializer extends Serializer<DoubleValue>{

    @Override
    public void write(Kryo _kryo, Output _output, DoubleValue _t) {
        _output.writeDouble(_t.getValue());
    }

    @Override
    public DoubleValue read(Kryo _kryo, Input _input, Class<DoubleValue> _type) {
        return new DoubleValue(_input.readDouble());
    }
    
}
