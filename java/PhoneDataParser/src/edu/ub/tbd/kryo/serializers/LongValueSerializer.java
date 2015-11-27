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
import net.sf.jsqlparser.expression.LongValue;

/**
 *
 * @author san
 */
public class LongValueSerializer extends Serializer<LongValue>{

    @Override
    public void write(Kryo _kryo, Output _output, LongValue _t) {
        _output.writeLong(_t.getValue());
    }

    @Override
    public LongValue read(Kryo _kryo, Input _input, Class<LongValue> _type) {
        return new LongValue(_input.readLong());
    }
    
}
