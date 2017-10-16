package org.jqassistant.contrib.plugin.c4_java;

import static com.buschmais.jqassistant.core.analysis.api.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.analysis.api.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jqassistant.contrib.plugin.c4_java.set.component1.service.Service1;
import org.jqassistant.contrib.plugin.c4_java.set.component3.service.Service3;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.analysis.api.AnalyzerConfiguration;
import com.buschmais.jqassistant.core.analysis.api.Result;
import com.buschmais.jqassistant.core.analysis.api.rule.Concept;
import com.buschmais.jqassistant.core.analysis.api.rule.Constraint;
import com.buschmais.jqassistant.core.analysis.impl.AnalyzerImpl;
import com.buschmais.jqassistant.core.report.api.ReportPlugin;
import com.buschmais.jqassistant.core.report.impl.CompositeReportPlugin;
import com.buschmais.jqassistant.plugin.java.api.model.PackageDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

public class ComponentTest extends AbstractJavaPluginIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentTest.class);

    @Test
    public void componentDependency() throws Exception {
        scanClassPathDirectory(getClassesDirectory(ComponentTest.class));
        Result<Concept> result = applyConcept("c4-java:ComponentDependency");
        assertThat(result.getStatus(), equalTo(SUCCESS));
    }

    @Test
    public void componentDependencyGraphml() throws Exception {
        Map<String, ReportPlugin> reportWriters = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        properties.put("graphml.report.directory", "target/graphml");
        reportWriters.putAll(getReportPlugins(properties));
        reportWriters.put("memory", reportWriter);
        CompositeReportPlugin compositeReportPlugin = new CompositeReportPlugin(reportWriters);
        analyzer = new AnalyzerImpl(new AnalyzerConfiguration(), this.store, compositeReportPlugin, LOGGER);
        scanClassPathDirectory(getClassesDirectory(ComponentTest.class));
        Result<Concept> result = applyConcept("c4-java:ComponentDependency.graphml");
        assertThat(result.getStatus(), equalTo(SUCCESS));

    }

    @Test
    public void undefinedComponentDependency() throws Exception {
        scanClassPathDirectory(getClassesDirectory(ComponentTest.class));
        Result<Constraint> result = validateConstraint("c4-java:UndefinedComponentDependency");
        assertThat(result.getStatus(), equalTo(FAILURE));
        List<Map<String, Object>> rows = result.getRows();
        assertThat(rows.size(), equalTo(1));
        store.beginTransaction();
        Map<String, Object> row = rows.get(0);
        PackageDescriptor dependent = (PackageDescriptor) row.get("Dependent");
        TypeDescriptor dependentType = (TypeDescriptor) row.get("DependentType");
        PackageDescriptor undefinedDependency = (PackageDescriptor) row.get("UndefinedDependency");
        TypeDescriptor undefinedTypeDependency = (TypeDescriptor) row.get("UndefinedTypeDependency");
        assertThat(dependent.getName(), equalTo("component1"));
        assertThat(dependentType, typeDescriptor(Service1.class));
        assertThat(undefinedDependency.getName(), equalTo("component3"));
        assertThat(undefinedTypeDependency, typeDescriptor(Service3.class));
        store.commitTransaction();
    }
}
