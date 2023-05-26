package deu.cse.spring_webmail.control;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        DiskSpaceDetails diskSpaceDetails = new DiskSpaceDetails(126792257536L, 29115895808L, 10485760L, true);
        Components components = new Components("정상", diskSpaceDetails);

        return Health.up()
                .withDetail("status", "정상")
                .withDetail("components", components)
                .build();
    }
}
