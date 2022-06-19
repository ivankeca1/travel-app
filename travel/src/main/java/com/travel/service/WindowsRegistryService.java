package com.travel.service;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class WindowsRegistryService {

    public String read() throws InvocationTargetException, IllegalAccessException {
        return Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName");
    }

    public String write(final String key, final String value){
        // Create a key and write a string
        Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\IvanKeca");
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\IvanKeca", key, value);
        return Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\IvanKeca\\", key);
    }

}
