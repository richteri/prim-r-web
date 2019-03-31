package com.example.primrweb;

import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.VerificationCollector;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTest {

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Rule
    public final ExpectedException expectedException = none();

    @Rule
    public final VerificationCollector verificationCollector = MockitoJUnit.collector();

}
