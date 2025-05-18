package org.deltacore.delta.controller.activity;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class ActivityGeneralSection {
    @GetMapping
    public String getActivitiesByFilters() {
        return "This is the Activity General Section";
    }
}
