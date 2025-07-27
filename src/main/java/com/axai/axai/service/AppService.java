package com.axai.axai.service;

import com.axai.axai.entities.App;
import com.axai.axai.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AppService {
    private final AppRepository appRepository;

    public App addApp(String name){
        if(appRepository.existsByName(name)){
            throw new RuntimeException("App already exists!");
        }
        App app = new App();
        app.setName(name);

        return appRepository.save(app);
    }

    public void deleteApp(UUID appId){
        if(!appRepository.existsById(appId)){
            throw new RuntimeException("App not exists!");
        }
        appRepository.deleteById(appId);
    }

    public void runApp(UUID appId){
        App app =  appRepository.findById(appId).orElseThrow(()->new RuntimeException("App not exists!"));

        System.out.println(app.getName()+" app is running...");
    }
}
