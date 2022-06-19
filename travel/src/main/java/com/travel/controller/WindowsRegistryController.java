package com.travel.controller;

import com.travel.service.WindowsRegistryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("api/v1/windows-registry")
public class WindowsRegistryController {

    private final WindowsRegistryService windowsRegistryService;

    public WindowsRegistryController(WindowsRegistryService windowsRegistryService) {
        this.windowsRegistryService = windowsRegistryService;
    }

    @GetMapping("/read")
    public ResponseEntity readWindowsRegistry(){
        try {
            return ResponseEntity.ok(this.windowsRegistryService.read());
        } catch (InvocationTargetException | IllegalAccessException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/write")
    public ResponseEntity writeToWindowsRegistry(@RequestParam final String key, @RequestParam final String value){
        return ResponseEntity.ok(this.windowsRegistryService.write(key, value));
    }
}
