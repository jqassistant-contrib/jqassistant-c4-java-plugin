package org.jqassistant.contrib.plugin.c4_java.set.component3.service;

import org.jqassistant.contrib.plugin.c4_java.set.component1.service.Service1;
import org.jqassistant.contrib.plugin.c4_java.set.component2.service.Service2;

public class Service3Impl implements Service3 {

    private Service1 service1;

    private Service2 service2;

    public Service3Impl(Service1 service1, Service2 service2) {
        this.service1 = service1;
        this.service2 = service2;
    }
}
