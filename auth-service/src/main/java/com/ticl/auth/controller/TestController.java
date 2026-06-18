package com.ticl.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/access")
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String hasAdminRights() {
        return "Yes, Logged In User is having a ADMIN Rights";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public String hasManagerRights() {
        return "Yes, Logged In User is having a MANAGER Rights";
    }

    @GetMapping("/viewer")
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER','VIEWER')")
    public String hasViewerRights() {
        return "Yes, Logged In User is having a Viewer Rights";
    }
}
