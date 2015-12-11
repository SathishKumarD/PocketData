/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.tbd.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author san
 */
public final class StringUtil {
    
    private final static String ENCODING = "SHA-256";
    private static MessageDigest MD_SHA256;
    
    static {
        try {
            MD_SHA256 = MessageDigest.getInstance(ENCODING);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }
    
    private StringUtil(){}
    
    public static String getSHAHash(final String _text) {
        byte[] hash = MD_SHA256.digest(_text.getBytes());
        String stringHash = String.format("%064x", new java.math.BigInteger(1, hash));
        MD_SHA256.reset();
        return stringHash;
    }
    
    public static boolean isSameHash(byte[] _hash1, byte[] _hash2){
        return MessageDigest.isEqual(_hash1, _hash2);
    }
    
    public static void main(String[] args) {
        String input1 = "SELECT _id FROM user WHERE class=?";
        System.out.println(StringUtil.getSHAHash(input1));
        
        String input2 = "SELECT pendingid FROM clients WHERE uri == ?";
        System.out.println(StringUtil.getSHAHash(input2));
        
        /*
        for(int i = 1; i < 56000000; i++){
            StringUtil.getSHAHash(input2 + i);
        }
        */
    }
}
