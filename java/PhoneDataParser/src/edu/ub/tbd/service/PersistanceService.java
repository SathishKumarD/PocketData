/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.service;

import edu.ub.tbd.entity.AbstractEntity;

/**
 *
 * @author san
 */
public abstract class PersistanceService {
    public abstract void write(AbstractEntity _entity) throws Exception;
    public abstract void close() throws Exception;
}
