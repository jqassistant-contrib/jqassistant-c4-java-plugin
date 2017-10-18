package org.jqassistant.contrib.plugin.c4_java.set.component1.service;

import org.jqassistant.contrib.plugin.c4_java.set.component3.service.Service3;

public class Service1Impl implements Service1 {

    // Undefined dependency
    private Service3 service3;

    public Service1Impl(Service3 service3) {
        this.service3 = service3;
    }
}
