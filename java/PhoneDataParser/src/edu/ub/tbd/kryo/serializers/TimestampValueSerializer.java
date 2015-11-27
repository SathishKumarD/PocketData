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
import net.sf.jsqlparser.expression.TimestampValue;

/**
 *
 * @author san
 */
public class TimestampValueSerializer extends Serializer<TimestampValue>{

    @Override
    public void write(Kryo _kryo, Output _output, TimestampValue _t) {
        _output.writeString(_t.getValue().toString());
    }

    @Override
    public TimestampValue read(Kryo _kryo, Input _input, Class<TimestampValue> _type) {
        return new TimestampValue(_input.readString());
    }
    
}
