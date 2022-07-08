package com.travel.service;

import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

@Service
public class IniFileService {

    @Value("${ini.configuration.location}")
    private String location;

    public String readApproved(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String warrantStatus = ini.get("constants", "warrant.status.approved", String.class);
            return warrantStatus;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readPending(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String warrantStatus = ini.get("constants", "warrant.status.pending", String.class);
            return warrantStatus;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readCanceled(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String warrantStatus = ini.get("constants", "warrant.status.canceled", String.class);
            return warrantStatus;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readXmllogLocation(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String location = ini.get("logs", "log.file.xml.location", String.class);
            return location;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readJsonlogLocation(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String location = ini.get("logs", "log.file.json.location", String.class);
            return location;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean writeJsonLogs(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String writeJsonLogs = ini.get("logs", "log.file.json", String.class);
            if(writeJsonLogs.toLowerCase(Locale.ROOT).equals("true")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean writeXmlLogs(){
        Wini ini = null;
        try {
            ini = new Wini(new File(location));
            String writeXmlLogs = ini.get("logs", "log.file.xml", String.class);
            if(writeXmlLogs.toLowerCase(Locale.ROOT).equals("true")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
