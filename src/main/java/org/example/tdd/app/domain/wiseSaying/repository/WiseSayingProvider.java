package org.example.tdd.app.domain.wiseSaying.repository;

import org.example.tdd.app.domain.wiseSaying.service.WiseSayingRepository;
import org.example.tdd.app.global.AppConfig;

public class WiseSayingProvider {
    public static WiseSayingRepository provide(){
        switch (AppConfig.getDbMode()){
            case FILE: return new WiseSayingFileRepository();
            case MEM: return WiseSayingRepositoryImpl.getInstance();
            default: throw new IllegalArgumentException("Unsupported db mode: " + AppConfig.getDbMode());
        }
    }
}
