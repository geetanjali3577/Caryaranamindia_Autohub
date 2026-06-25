package com.autohub;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CaryanamAutoHubApplication {

    private static final Logger log = LoggerFactory.getLogger(CaryanamAutoHubApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CaryanamAutoHubApplication.class, args);
        System.out.println("CaryanamAutoHub Application started");
        System.out.println("\n\n");
        System.err.println("PORT : localhost8080");
        System.err.println("  *****    *******  *******       *****   *******    *****    ******   *******" );
        System.err.println(" *     *   *      *    *         *           *      *     *   *     *     *   " );
        System.err.println("*       *  *      *    *         *           *     *       *  *     *     *   " );
        System.err.println("*       *  *******     *          *****      *     *       *  ******      *   " );
        System.err.println("*********  *           *               *     *     *********  *   *       *   " );
        System.err.println("*       *  *           *               *     *     *       *  *    *      *   " );
        System.err.println("*       *  *        *******       *****      *     *       *  *     *     *   " );
        log.info("Application Run !!");
    }

    @PostConstruct
    public void init() {
        System.out.println("🔥 ChatController Loaded");
    }

}
