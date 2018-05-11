package com.jesseoberstein.alert.suite;

import com.jesseoberstein.alert.data.dao.DirectionDaoTest;
import com.jesseoberstein.alert.data.dao.EndpointDaoTest;
import com.jesseoberstein.alert.data.dao.RouteDaoTest;
import com.jesseoberstein.alert.data.dao.StopDaoTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    RouteDaoTest.class,
    StopDaoTest.class,
    DirectionDaoTest.class,
    EndpointDaoTest.class
})
public class DaoTestSuite {}