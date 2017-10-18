package org.jqassistant.contrib.plugin.c4_java.set.component2.service;

import org.jqassistant.contrib.plugin.c4_java.set.component1.service.Service1;

public class Service2Impl implements Service2 {

    private Service1 service1;

    public Service2Impl(Service1 service1) {
        this.service1 = service1;
    }
}
