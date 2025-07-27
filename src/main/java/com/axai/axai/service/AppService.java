package com.axai.axai.service;

import com.axai.axai.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppService {
    private final AppRepository appRepository;
}
