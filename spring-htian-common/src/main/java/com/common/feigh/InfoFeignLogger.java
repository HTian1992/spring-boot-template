package com.common.feigh;

import feign.Logger;

/**
 * @author lizehao
 */
public class InfoFeignLogger extends Logger {
    private final org.slf4j.Logger logger;

    InfoFeignLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format(methodTag(configKey) + format, args));
        }
    }
}
