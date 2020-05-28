package com.malaxg.hardwork.hystrix;

import com.malaxg.hardwork.web.common.RestResult;

public interface CommonHystrixFallback {
	default RestResult hystrixFallback() {
		return RestResult.fail("sorry the service is stop");
	}
}
