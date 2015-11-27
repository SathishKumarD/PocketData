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
import net.sf.jsqlparser.expression.TimeValue;

/**
 *
 * @author san
 */
public class TimeValueSerializer extends Serializer<TimeValue>{

    @Override
    public void write(Kryo _kryo, Output _output, TimeValue _t) {
        _output.writeString(_t.getValue().toString());
    }

    @Override
    public TimeValue read(Kryo _kryo, Input _input, Class<TimeValue> _type) {
        return new TimeValue(_input.readString());
    }
    
}
