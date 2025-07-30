package com.axai.axai.service;

import com.axai.axai.entities.App;
import com.axai.axai.entities.SubMenu;
import com.axai.axai.entities.User;
import com.axai.axai.repository.AppRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppService {
    private final AppRepository appRepository;

    public App addApp(String name,String iconName,User user){
        if(appRepository.existsByNameAndUser(name,user)){
            throw new RuntimeException("App already exists!");
        }
        App app = new App();
        app.setName(name);
        app.setIconName(iconName);
        app.setUser(user);

        return appRepository.save(app);
    }

    @Transactional
    public void deleteApp(String appName, User user){
        App app = appRepository.findByNameAndUser(appName,user)
                .orElseThrow(() -> new RuntimeException("App not found!"));
        if(!appRepository.existsById(app.getId())){
            throw new RuntimeException("App not exists!");
        }

        for (SubMenu subMenu : app.getSubMenuList()) {
            subMenu.getApps().remove(app);
        }

        app.getSubMenuList().clear();

        appRepository.deleteById(app.getId());
    }

    public void runApp(String appName, User user){
        App app =  appRepository.findByNameAndUser(appName,user).orElseThrow(()->new RuntimeException("App not exists!"));

        System.out.println(app.getName()+" app is running...");
    }

    public App updateIcon(String appName,String iconName,User user){
        App app = appRepository.findByNameAndUser(appName,user)
                .orElseThrow(() -> new RuntimeException("App not found!"));
        app.setIconName(iconName);
        return appRepository.save(app);
    }

    public App deleteIcon(String appName,User user){
        App app =  appRepository.findByNameAndUser(appName,user).orElseThrow(()->new RuntimeException("App not found!"));
        app.setIconName(null);
        return appRepository.save(app);
    }
}
