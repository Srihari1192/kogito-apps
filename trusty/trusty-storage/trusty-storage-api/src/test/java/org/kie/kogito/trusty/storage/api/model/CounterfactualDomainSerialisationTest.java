/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.trusty.storage.api.model;

import java.io.StringWriter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.tracing.typedvalue.TypedValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CounterfactualDomainSerialisationTest {

    private ObjectMapper mapper;
    private StringWriter writer;

    @BeforeEach
    public void setup() {
        this.mapper = new ObjectMapper();
        this.writer = new StringWriter();
    }

    @Test
    public void testCounterfactualSearchDomain_Range_RoundTrip() throws Exception {
        CounterfactualDomainRange domainRange = new CounterfactualDomainRange(new IntNode(18), new IntNode(65));
        CounterfactualSearchDomain searchDomain = new CounterfactualSearchDomain(TypedValue.Kind.UNIT,
                "age",
                "integer",
                null,
                Boolean.TRUE,
                domainRange);

        mapper.writeValue(writer, searchDomain);
        String searchDomainJson = writer.toString();
        assertNotNull(searchDomainJson);

        CounterfactualSearchDomain roundTrippedSearchDomain = mapper.readValue(searchDomainJson, CounterfactualSearchDomain.class);

        assertEquals(searchDomain.getKind(), roundTrippedSearchDomain.getKind());
        assertEquals(searchDomain.getName(), roundTrippedSearchDomain.getName());
        assertEquals(searchDomain.getTypeRef(), roundTrippedSearchDomain.getTypeRef());
        assertNull(roundTrippedSearchDomain.getComponents());
        assertEquals(searchDomain.isFixed(), roundTrippedSearchDomain.isFixed());
        assertTrue(roundTrippedSearchDomain.getDomain() instanceof CounterfactualDomainRange);

        CounterfactualDomainRange roundTrippedDomainRange = (CounterfactualDomainRange) roundTrippedSearchDomain.getDomain();
        assertEquals(domainRange.getLowerBound(), roundTrippedDomainRange.getLowerBound());
        assertEquals(domainRange.getUpperBound(), roundTrippedDomainRange.getUpperBound());
    }

    @Test
    public void testCounterfactualSearchDomain_Categorical_RoundTrip() throws Exception {
        CounterfactualDomainCategorical domainCategorical = new CounterfactualDomainCategorical(List.of(new TextNode("A"), new TextNode("B")));
        CounterfactualSearchDomain searchDomain = new CounterfactualSearchDomain(TypedValue.Kind.UNIT,
                "age",
                "integer",
                null,
                Boolean.TRUE,
                domainCategorical);

        mapper.writeValue(writer, searchDomain);
        String searchDomainJson = writer.toString();
        assertNotNull(searchDomainJson);

        CounterfactualSearchDomain roundTrippedSearchDomain = mapper.readValue(searchDomainJson, CounterfactualSearchDomain.class);

        assertEquals(searchDomain.getKind(), roundTrippedSearchDomain.getKind());
        assertEquals(searchDomain.getName(), roundTrippedSearchDomain.getName());
        assertEquals(searchDomain.getTypeRef(), roundTrippedSearchDomain.getTypeRef());
        assertNull(roundTrippedSearchDomain.getComponents());
        assertEquals(searchDomain.isFixed(), roundTrippedSearchDomain.isFixed());
        assertTrue(roundTrippedSearchDomain.getDomain() instanceof CounterfactualDomainCategorical);

        CounterfactualDomainCategorical roundTrippedDomainCategorical = (CounterfactualDomainCategorical) roundTrippedSearchDomain.getDomain();
        assertEquals(domainCategorical.getCategories().size(), roundTrippedDomainCategorical.getCategories().size());
        assertTrue(roundTrippedDomainCategorical.getCategories().containsAll(domainCategorical.getCategories()));
    }

}
