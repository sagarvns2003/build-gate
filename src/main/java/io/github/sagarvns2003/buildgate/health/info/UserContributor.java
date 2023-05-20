package io.github.sagarvns2003.buildgate.health.info;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class UserContributor implements InfoContributor {

	// @Autowired
	// UserRepository userRepository;

	@Override
	public void contribute(Builder builder) {
		Map<String, Integer> userDetails = new HashMap<>();
		userDetails.put("active", 2 /* userRepository.countByStatus(1) */);
		userDetails.put("inactive", 0 /* userRepository.countByStatus(0) */);

		builder.withDetail("users", userDetails);
	}

}
