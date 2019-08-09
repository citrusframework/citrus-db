package com.consol.citrus.db.driver.dataset;

import com.consol.citrus.db.driver.data.Row;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class DataSetTest {

    private final DataSet dataSet = new DataSet();

    @Test
    public void testEmptyDataSetReturnsNullOnGetNextRow() {

        //WHEN
        final Row nextRow = dataSet.getNextRow();

        //THEN
        assertNull(nextRow);
    }

    @Test
    public void testCursorPositionIsPreservationInCaseOfNoData(){

        //WHEN
        dataSet.getNextRow();

        //THEN
        assertEquals(dataSet.getCursor(), 0);
    }

    @Test
    public void testCursorPositionMovesInCaseOfMoreData(){

        //GIVEN
        final Map<String, Object> testData = Collections.singletonMap("foo","bar");
        final List<Row> testDataRows = Lists.newArrayList(new Row(testData));
        final DataSet dataSet = new DataSet(testDataRows);

        //WHEN
        dataSet.getNextRow();

        //THEN
        assertEquals(dataSet.getCursor(), 1);
    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(DataSet.class).verify();
    }

    @Test
    public void testEqualsContract(){
        EqualsVerifier
                .forClass(DataSet.class)
                .withIgnoredFields("cursor")
                .verify();
    }
}