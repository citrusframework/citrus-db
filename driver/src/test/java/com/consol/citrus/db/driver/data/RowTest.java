package com.consol.citrus.db.driver.data;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.apache.commons.beanutils.ConvertUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.testng.Assert.assertEquals;

@PrepareForTest(ConvertUtils.class)
public class RowTest extends PowerMockTestCase {

    private Row row ;
    private final String COLUMN_1 = "col1";
    private final String COLUMN_2 = "col2";
    private final String COLUMN_3 = "col3";

    private final Object VALUE_1 = 42;
    private final Object VALUE_2 = "84";

    @BeforeMethod
    public void setUp(){
        spy(ConvertUtils.class);
        row = generateRow();
    }

    @Test
    public void testGetColumns(){

        //WHEN
        final List<String> columns = row.getColumns();

        //THEN
        assertEquals(columns.get(0), COLUMN_1);
        assertEquals(columns.get(1), COLUMN_2);
        assertEquals(columns.get(2), COLUMN_3);
    }

    @Test
    public void testGetValueByColumnName(){

        //WHEN
        final Object value = row.getValue(COLUMN_1);

        //THEN
        assertEquals(value, VALUE_1);

    }

    @Test
    public void testGetValueByColumnNameWithConversion(){

        //WHEN
        final Object value = row.getValue(COLUMN_2, int.class);

        //THEN
        assertEquals(value, Integer.valueOf((String) VALUE_2));
        verifyConversion(VALUE_2, int.class);

    }

    @Test
    public void testGetValueByColumnIndex(){

        //WHEN
        final Object value = row.getValue(1);

        //THEN
        assertEquals(value, VALUE_2);

    }

    @Test
    public void testGetValueByColumnIndexWithConversion(){

        //WHEN
        final Object value = row.getValue(1, double.class);

        //THEN
        assertEquals(value, Double.valueOf((String) VALUE_2));
        verifyConversion(VALUE_2, double.class);

    }

    @Test
    public void getGetValues(){

        //WHEN
        final Map<String, Object> values = row.getValues();

        //THEN
        assertEquals(values, generateRowValues());

    }

    @Test
    public void testToString(){
        ToStringVerifier.forClass(Row.class).verify();
    }

    @Test
    public void equalsContract(){
        EqualsVerifier.forClass(Row.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    private Row generateRow() {
        final SortedMap<String, Object> values = generateRowValues();
        final Row row = new Row();
        row.setValues(values);
        return row;
    }

    private SortedMap<String, Object> generateRowValues() {
        final SortedMap<String, Object> values = new TreeMap<>();
        values.put(COLUMN_1, VALUE_1);
        values.put(COLUMN_2, VALUE_2);
        values.put(COLUMN_3, null);
        return values;
    }

    private void verifyConversion(final Object toBeConverted, final Class<?> clazz) {
        verifyStatic(ConvertUtils.class);
        ConvertUtils.convert(eq(toBeConverted), eq(clazz));
    }
}