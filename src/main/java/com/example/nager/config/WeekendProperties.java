package com.example.nager.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.*;
@Configuration
@ConfigurationProperties(prefix = "holiday.weekend")
public class WeekendProperties {
    private List<String> defaults = List.of("SATURDAY","SUNDAY");
    private Map<String,List<String>> overrides = new HashMap<>();
    public List<String> getDefault() { return defaults; }
    public void setDefault(List<String> defaults) { this.defaults = defaults; }
    public Map<String, List<String>> getOverrides() { return overrides; }
    public void setOverrides(Map<String, List<String>> overrides) { this.overrides = overrides; }
}
