package com.malaxg.hardwork.fegin;

import java.util.Map;

import com.malaxg.hardwork.hystrix.CommonHystrixFallback;
import com.malaxg.hardwork.web.common.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "HARKWORK-GENERATOR", fallback = HardWorkGeneratorServiceHystrixFallBack.class)
public interface HardWorkGeneratorService extends CommonHystrixFallback {
	@PostMapping("/generateCodeTemplate")
	RestResult generateCodeTemplete(@RequestBody Map<String, String> generator);
}
