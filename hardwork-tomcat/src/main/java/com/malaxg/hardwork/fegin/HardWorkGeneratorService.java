package com.malaxg.hardwork.fegin;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("HARKWORK-GENERATOR")
public interface HardWorkGeneratorService {
	@PostMapping("/generateCodeTemplate")
	void generateCodeTemplete(@RequestBody Map<String, String> generator);
}
