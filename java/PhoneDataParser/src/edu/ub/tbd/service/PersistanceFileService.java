/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.service;

import edu.ub.tbd.constants.AppConstants;
import edu.ub.tbd.entity.AbstractEntity;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import edu.ub.tbd.entity.Column;

/**
 *
 * @author san
 */
public class PersistanceFileService extends PersistanceService{

    private final String outputFolderPath;
    private final String valueSeperator;
    private final String fileName;
    private final Class classOfEntity;
    private final BufferedWriter writer;
    private final Field[] fieldsToPersist;
    
    public PersistanceFileService(String _outputFolderPath, String _valueSeperator, Class _classOfEntity) throws IOException{
        this.outputFolderPath = _outputFolderPath;
        this.valueSeperator = _valueSeperator;
        this.classOfEntity = _classOfEntity;
        this.fileName = this.classOfEntity.getSimpleName().toLowerCase() + AppConstants.OUTPUT_FILE_EXT;
        writer = new BufferedWriter(new FileWriter(this.outputFolderPath + File.separator + this.fileName, true));
        
        this.fieldsToPersist = getPersistableFields();
        if(AppConstants.OUTPUT_FILE_WRITE_HEADER) {
            writer.append(getFileHeader());
            writer.newLine();
        }
    }
    
    private StringBuilder getFileHeader(){
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < fieldsToPersist.length; i++){
            out.append(fieldsToPersist[i].getName());
            if(i != fieldsToPersist.length - 1){
                out.append(this.valueSeperator);
            }
        }
        return out;
    }
    
    private Field[] getPersistableFields(){
        ArrayList<Field> filteredFields = new ArrayList<>();
        
        Field[] allPublicFields = classOfEntity.getFields();
        for(Field f : allPublicFields){
            if(f.isAnnotationPresent(Column.class)){
                filteredFields.add(f);
            }
        }
        
        Field [] out = new Field[filteredFields.size()];
        return filteredFields.toArray(out);
    }
    
    @Override
    public void write(AbstractEntity _entity) throws Exception{
        StringBuilder line = new StringBuilder();
        for(int i = 0; i < fieldsToPersist.length; i++){
            String cellValue = fieldsToPersist[i].get(_entity).toString();
            line.append(cellValue);
            if(i != fieldsToPersist.length - 1){
                line.append(this.valueSeperator);
            }
        }
        writer.append(line);
        writer.newLine();
    }

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }
    
    
}
