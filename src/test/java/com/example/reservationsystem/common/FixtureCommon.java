package com.example.reservationsystem.common;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

import java.util.Arrays;

public class FixtureCommon {

    public static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(new FailoverIntrospector(
                    Arrays.asList(
                            FieldReflectionArbitraryIntrospector.INSTANCE,
                            BeanArbitraryIntrospector.INSTANCE,
                            BuilderArbitraryIntrospector.INSTANCE
                    )
            ))
            .defaultNotNull(true)
            .build();

}
