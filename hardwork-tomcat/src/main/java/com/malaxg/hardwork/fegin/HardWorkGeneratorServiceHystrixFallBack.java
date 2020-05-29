package com.malaxg.hardwork.fegin;

import java.util.Map;

import com.malaxg.hardwork.web.common.RestResult;
import org.springframework.stereotype.Component;

@Component
public class HardWorkGeneratorServiceHystrixFallBack implements HardWorkGeneratorService {
	@Override
	public RestResult generateCodeTemplete(Map<String, String> generator) {
		return hystrixFallback();
	}
}
